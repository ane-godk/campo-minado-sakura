
package br.com.sakura.campominado.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tabuleiro {

    private final Celula[][] grade;
    private final int linhas;
    private final int colunas;
    private final int minas;

    private final ConfiguracaoJogo configuracao;

    private int vidas;
    private int bandeirasColocadas;
    private int minasEncontradas;

    private boolean jogoEncerrado;
    private boolean venceu;

    // Mantém compatibilidade com o Sakura original.
    public Tabuleiro(int linhas, int colunas, int minas) {
        this(linhas, colunas, minas, null);
    }

    // Nova forma de criar uma partida com configurações.
    public Tabuleiro(ConfiguracaoJogo configuracao) {
        this(
                validarConfiguracao(configuracao).getLinhas(),
                configuracao.getColunas(),
                configuracao.getMinas(),
                configuracao
        );
    }

    private static ConfiguracaoJogo validarConfiguracao(
            ConfiguracaoJogo configuracao) {

        if (configuracao == null) {
            throw new IllegalArgumentException(
                    "Configuração não pode ser nula.");
        }

        return configuracao;
    }

    private Tabuleiro(
            int linhas,
            int colunas,
            int minas,
            ConfiguracaoJogo configuracao) {

        validarDimensoes(linhas, colunas, minas);

        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        this.configuracao = configuracao;
        this.vidas = configuracao == null
                ? 1
                : configuracao.getVidas();

        this.grade = new Celula[linhas][colunas];

        criarCelulas();
        distribuirMinas();
        calcularMinasVizinhas();
    }

    // Construtor usado em testes com minas predeterminadas.
    public Tabuleiro(boolean[][] disposicaoMinas) {

        if (disposicaoMinas == null
                || disposicaoMinas.length == 0
                || disposicaoMinas[0] == null
                || disposicaoMinas[0].length == 0) {

            throw new IllegalArgumentException(
                    "Disposição de minas inválida.");
        }

        this.linhas = disposicaoMinas.length;
        this.colunas = disposicaoMinas[0].length;
        this.configuracao = null;
        this.vidas = 1;

        for (boolean[] linha : disposicaoMinas) {
            if (linha == null || linha.length != colunas) {
                throw new IllegalArgumentException(
                        "Todas as linhas devem ter o mesmo tamanho.");
            }
        }

        this.grade = new Celula[linhas][colunas];
        criarCelulas();

        int totalMinas = 0;

        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {

                if (disposicaoMinas[linha][coluna]) {
                    grade[linha][coluna].definirMinada(true);
                    totalMinas++;
                }
            }
        }

        this.minas = totalMinas;
        calcularMinasVizinhas();
    }

    private void validarDimensoes(
            int linhas, int colunas, int minas) {

        if (linhas <= 0 || colunas <= 0) {
            throw new IllegalArgumentException(
                    "Dimensões inválidas.");
        }

        if (minas < 0
                || minas >= (long) linhas * colunas) {

            throw new IllegalArgumentException(
                    "Quantidade de minas inválida.");
        }
    }

    private void criarCelulas() {

        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                grade[linha][coluna] = new Celula();
            }
        }
    }

    private void distribuirMinas() {

        List<Integer> posicoes = new ArrayList<>();

        for (int i = 0; i < linhas * colunas; i++) {
            posicoes.add(i);
        }

        Collections.shuffle(posicoes, new Random());

        for (int i = 0; i < minas; i++) {

            int posicao = posicoes.get(i);
            int linha = posicao / colunas;
            int coluna = posicao % colunas;

            grade[linha][coluna].definirMinada(true);
        }
    }

    private void calcularMinasVizinhas() {

        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {

                grade[linha][coluna].definirMinasVizinhas(
                        contarMinasVizinhas(linha, coluna)
                );
            }
        }
    }

    private int contarMinasVizinhas(
            int linha, int coluna) {

        int quantidade = 0;

        for (int dl = -1; dl <= 1; dl++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dl == 0 && dc == 0) {
                    continue;
                }

                int novaLinha = linha + dl;
                int novaColuna = coluna + dc;

                if (dentroDoTabuleiro(novaLinha, novaColuna)
                        && grade[novaLinha][novaColuna].isMinada()) {

                    quantidade++;
                }
            }
        }

        return quantidade;
    }

    private boolean dentroDoTabuleiro(
            int linha, int coluna) {

        return linha >= 0
                && linha < linhas
                && coluna >= 0
                && coluna < colunas;
    }

    public Celula getCelula(int linha, int coluna) {

        validarPosicao(linha, coluna);
        return grade[linha][coluna];
    }

    private void validarPosicao(
            int linha, int coluna) {

        if (!dentroDoTabuleiro(linha, coluna)) {
            throw new IndexOutOfBoundsException(
                    "Posição fora do tabuleiro.");
        }
    }

    // Clique esquerdo: revela uma célula.
    public boolean revelar(int linha, int coluna) {

        if (jogoEncerrado) {
            return false;
        }

        validarPosicao(linha, coluna);

        Celula inicial = grade[linha][coluna];

        if (inicial.isRevelada() || inicial.isMarcada()) {
            return false;
        }

        if (inicial.isMinada()) {

            inicial.revelar();
            vidas--;

            if (vidas <= 0) {
                vidas = 0;
                jogoEncerrado = true;
                venceu = false;
            } else {
                verificarVitoria();
            }

            return true;
        }

        revelarArea(linha, coluna);
        verificarVitoria();

        return true;
    }

    private void revelarArea(
            int linha, int coluna) {

        ArrayList<Posicao> pendentes =
                new ArrayList<>();

        boolean[][] visitadas =
                new boolean[linhas][colunas];

        pendentes.add(new Posicao(linha, coluna));

        while (!pendentes.isEmpty()) {

            Posicao posicao = pendentes.remove(
                    pendentes.size() - 1
            );

            int l = posicao.linha();
            int c = posicao.coluna();

            if (!dentroDoTabuleiro(l, c)
                    || visitadas[l][c]) {
                continue;
            }

            visitadas[l][c] = true;

            Celula celula = grade[l][c];

            if (celula.isRevelada()
                    || celula.isMarcada()
                    || celula.isMinada()) {
                continue;
            }

            celula.revelar();

            if (celula.getMinasVizinhas() == 0) {

                for (int dl = -1; dl <= 1; dl++) {
                    for (int dc = -1; dc <= 1; dc++) {

                        if (dl == 0 && dc == 0) {
                            continue;
                        }

                        int nl = l + dl;
                        int nc = c + dc;

                        if (dentroDoTabuleiro(nl, nc)
                                && !visitadas[nl][nc]) {

                            pendentes.add(
                                    new Posicao(nl, nc)
                            );
                        }
                    }
                }
            }
        }
    }

    // Clique direito: coloca ou remove uma bandeira.
    public boolean alternarBandeira(
            int linha, int coluna) {

        if (jogoEncerrado) {
            return false;
        }

        validarPosicao(linha, coluna);

        Celula celula = grade[linha][coluna];

        if (celula.isRevelada()) {
            return false;
        }

        boolean estavaMarcada = celula.isMarcada();

        if (!celula.alternarBandeira()) {
            return false;
        }

        if (estavaMarcada) {
            bandeirasColocadas--;

            if (celula.isMinada()) {
                minasEncontradas--;
            }
        } else {
            bandeirasColocadas++;

            if (celula.isMinada()) {
                minasEncontradas++;
            }
        }

        return true;
    }

    private void verificarVitoria() {

        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {

                Celula celula = grade[linha][coluna];

                if (!celula.isMinada()
                        && !celula.isRevelada()) {
                    return;
                }
            }
        }

        venceu = true;
        jogoEncerrado = true;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getMinas() {
        return minas;
    }

    public int getVidas() {
        return vidas;
    }

    public int getBandeirasColocadas() {
        return bandeirasColocadas;
    }

    public int getMinasRestantes() {
        return minas - bandeirasColocadas;
    }

    public int getMinasEncontradas() {
        return minasEncontradas;
    }

    public ConfiguracaoJogo getConfiguracao() {
        return configuracao;
    }

    public boolean isJogoEncerrado() {
        return jogoEncerrado;
    }

    public boolean isVenceu() {
        return venceu;
    }
}
