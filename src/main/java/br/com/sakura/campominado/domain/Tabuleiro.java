
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

    private boolean jogoEncerrado;
    private boolean venceu;

    public Tabuleiro(int linhas, int colunas, int minas) {
        if (linhas <= 0 || colunas <= 0) {
            throw new IllegalArgumentException(
                    "Linhas e colunas devem ser positivas.");
        }

        if (minas < 0 || minas >= (long) linhas * colunas) {
            throw new IllegalArgumentException(
                    "Quantidade de minas inválida.");
        }

        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        this.grade = new Celula[linhas][colunas];

        criarCelulas();
        distribuirMinas();
        calcularMinasVizinhas();
    }

    // Construtor auxiliar de testes
    public Tabuleiro(boolean[][] disposicaoMinas) {
        this.linhas = disposicaoMinas.length;
        this.colunas = disposicaoMinas[0].length;
        this.grade = new Celula[linhas][colunas];

        int totalMinas = 0;

        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                this.grade[l][c] = new Celula();
                if (disposicaoMinas[l][c]) {
                    this.grade[l][c].definirMinada(true);
                    totalMinas++;
                }
            }
        }

        this.minas = totalMinas;
        calcularMinasVizinhas();
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
                int quantidade = contarMinasVizinhas(linha, coluna);
                grade[linha][coluna].definirMinasVizinhas(quantidade);
            }
        }
    }

    private int contarMinasVizinhas(int linha, int coluna) {
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

    private boolean dentroDoTabuleiro(int linha, int coluna) {
        return linha >= 0 && linha < linhas
                && coluna >= 0 && coluna < colunas;
    }

    public Celula getCelula(int linha, int coluna) {
        if (!dentroDoTabuleiro(linha, coluna)) {
            throw new IndexOutOfBoundsException(
                    "Posição fora do tabuleiro.");
        }

        return grade[linha][coluna];
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

    public boolean revelar(int linha, int coluna) {
        if (jogoEncerrado) {
            return false;
        }

        if (!dentroDoTabuleiro(linha, coluna)) {
            throw new IndexOutOfBoundsException(
                    "Posição fora do tabuleiro.");
        }

        Celula inicial = grade[linha][coluna];

        if (inicial.isRevelada() || inicial.isMarcada()) {
            return false;
        }

        if (inicial.isMinada()) {
            inicial.revelar();
            jogoEncerrado = true;
            venceu = false;
            return true;
        }

        ArrayList<Posicao> pendentes = new ArrayList<>();
        boolean[][] visitadas = new boolean[linhas][colunas];

        pendentes.add(new Posicao(linha, coluna));

        while (!pendentes.isEmpty()) {
            Posicao posicao = pendentes.remove(pendentes.size() - 1);

            int l = posicao.linha();
            int c = posicao.coluna();

            if (!dentroDoTabuleiro(l, c) || visitadas[l][c]) {
                continue;
            }

            visitadas[l][c] = true;

            Celula celula = grade[l][c];

            if (celula.isRevelada() || celula.isMarcada()
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
                            pendentes.add(new Posicao(nl, nc));
                        }
                    }
                }
            }
        }

        verificarVitoria();
        return true;
    }

    private void verificarVitoria() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                Celula celula = grade[linha][coluna];

                if (!celula.isMinada() && !celula.isRevelada()) {
                    return;
                }
            }
        }

        venceu = true;
        jogoEncerrado = true;
    }

    public boolean isJogoEncerrado() {
        return jogoEncerrado;
    }

    public boolean isVenceu() {
        return venceu;
    }

}
