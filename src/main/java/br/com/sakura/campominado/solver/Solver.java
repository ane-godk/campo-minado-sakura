
package br.com.sakura.campominado.solver;

import br.com.sakura.campominado.domain.Celula;
import br.com.sakura.campominado.domain.Tabuleiro;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Solver {

    public enum TipoJogada {
        REVELAR,
        MARCAR_MINA
    }

    public record Jogada(
            int linha,
            int coluna,
            TipoJogada tipo,
            String explicacao
    ) {}

    private record Restricao(
            BitSet celulas,
            int minasRestantes
    ) {}

    private final Tabuleiro tabuleiro;

    public Solver(Tabuleiro tabuleiro) {
        if (tabuleiro == null) {
            throw new IllegalArgumentException(
                    "Tabuleiro não pode ser nulo."
            );
        }

        this.tabuleiro = tabuleiro;
    }

    public Jogada encontrarJogada() {

        if (tabuleiro.isJogoEncerrado()) {
            return null;
        }

        List<Restricao> restricoes =
                construirRestricoes();

        if (restricoes == null) {
            // Informações contraditórias:
            // não é seguro sugerir uma jogada.
            return null;
        }

        // Primeiro, procura deduções diretas.
        for (Restricao restricao : restricoes) {

            Jogada jogada = extrairJogada(
                    restricao,
                    "Dedução direta a partir dos números "
                            + "e das bandeiras visíveis."
            );

            if (jogada != null) {
                return jogada;
            }
        }

        // Depois, compara conjuntos de células
        // desconhecidas de restrições diferentes.
        for (int i = 0; i < restricoes.size(); i++) {

            for (int j = 0;
                 j < restricoes.size(); j++) {

                if (i == j) {
                    continue;
                }

                Restricao menor = restricoes.get(i);
                Restricao maior = restricoes.get(j);

                if (!ehSubconjunto(
                        menor.celulas(),
                        maior.celulas())) {
                    continue;
                }

                BitSet diferenca =
                        (BitSet) maior.celulas().clone();

                diferenca.andNot(menor.celulas());

                if (diferenca.isEmpty()) {
                    continue;
                }

                int minasDiferenca =
                        maior.minasRestantes()
                                - menor.minasRestantes();

                if (minasDiferenca < 0
                        || minasDiferenca
                        > diferenca.cardinality()) {
                    continue;
                }

                Restricao deduzida = new Restricao(
                        diferenca,
                        minasDiferenca
                );

                Jogada jogada = extrairJogada(
                        deduzida,
                        "Dedução por subconjuntos: "
                                + "a comparação de dois grupos "
                                + "de células permite determinar "
                                + "esta jogada."
                );

                if (jogada != null) {
                    return jogada;
                }
            }
        }

        return null;
    }

    private List<Restricao> construirRestricoes() {

        List<Restricao> resultado =
                new ArrayList<>();

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

                Celula atual =
                        tabuleiro.getCelula(linha, coluna);

                if (!atual.isRevelada()
                        || atual.isMinada()) {
                    continue;
                }

                BitSet desconhecidas = new BitSet();
                int bandeiras = 0;

                for (int dl = -1; dl <= 1; dl++) {

                    for (int dc = -1; dc <= 1; dc++) {

                        if (dl == 0 && dc == 0) {
                            continue;
                        }

                        int nl = linha + dl;
                        int nc = coluna + dc;

                        if (nl < 0
                                || nl >= tabuleiro.getLinhas()
                                || nc < 0
                                || nc >= tabuleiro.getColunas()) {
                            continue;
                        }

                        Celula vizinha =
                                tabuleiro.getCelula(nl, nc);

                        if (vizinha.isMarcada()) {
                            bandeiras++;

                        } else if (!vizinha.isRevelada()) {

                            desconhecidas.set(
                                    indice(nl, nc)
                            );
                        }
                    }
                }

                int restantes =
                        atual.getMinasVizinhas()
                                - bandeiras;

                if (restantes < 0
                        || restantes
                        > desconhecidas.cardinality()) {

                    return null;
                }

                if (!desconhecidas.isEmpty()) {

                    resultado.add(new Restricao(
                            desconhecidas,
                            restantes
                    ));
                }
            }
        }

        // Detecta contradições entre restrições
        // com o mesmo conjunto de células.
        for (int i = 0; i < resultado.size(); i++) {

            for (int j = i + 1;
                 j < resultado.size(); j++) {

                Restricao a = resultado.get(i);
                Restricao b = resultado.get(j);

                if (a.celulas().equals(b.celulas())
                        && a.minasRestantes()
                        != b.minasRestantes()) {

                    return null;
                }
            }
        }

        return resultado;
    }

    private Jogada extrairJogada(
            Restricao restricao,
            String explicacao
    ) {

        int quantidade =
                restricao.celulas().cardinality();

        if (quantidade == 0) {
            return null;
        }

        TipoJogada tipo;

        if (restricao.minasRestantes() == 0) {
            tipo = TipoJogada.REVELAR;

        } else if (restricao.minasRestantes()
                == quantidade) {

            tipo = TipoJogada.MARCAR_MINA;

        } else {
            return null;
        }

        int indice =
                restricao.celulas().nextSetBit(0);

        int linha = indice / tabuleiro.getColunas();
        int coluna = indice % tabuleiro.getColunas();

        String descricao = tipo == TipoJogada.REVELAR
                ? "Revelar célula."
                : "Marcar célula como mina.";

        return new Jogada(
                linha,
                coluna,
                tipo,
                explicacao + " " + descricao
        );
    }

    private boolean ehSubconjunto(
            BitSet menor,
            BitSet maior
    ) {

        BitSet copia = (BitSet) menor.clone();
        copia.andNot(maior);

        return copia.isEmpty();
    }

    private int indice(int linha, int coluna) {
        return linha * tabuleiro.getColunas() + coluna;
    }

    public boolean executarJogada(Jogada jogada) {

        if (jogada == null
                || tabuleiro.isJogoEncerrado()) {
            return false;
        }

        // Recalcula a dedução para evitar executar
        // uma dica antiga após mudanças no tabuleiro.
        Jogada atual = encontrarJogada();

        if (atual == null
                || atual.linha() != jogada.linha()
                || atual.coluna() != jogada.coluna()
                || atual.tipo() != jogada.tipo()) {
            return false;
        }

        Celula celula = tabuleiro.getCelula(
                jogada.linha(),
                jogada.coluna()
        );

        if (celula.isRevelada()
                || celula.isMarcada()) {
            return false;
        }

        if (jogada.tipo() == TipoJogada.REVELAR) {

            return tabuleiro.revelar(
                    jogada.linha(),
                    jogada.coluna()
            );
        }

        return tabuleiro.alternarBandeira(
                jogada.linha(),
                jogada.coluna()
        );
    }
}
