
package br.com.sakura.campominado.solver;

import br.com.sakura.campominado.domain.Celula;
import br.com.sakura.campominado.domain.Tabuleiro;

import java.util.ArrayList;
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

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

                Celula atual = tabuleiro.getCelula(
                        linha, coluna
                );

                if (!atual.isRevelada()
                        || atual.isMinada()) {
                    continue;
                }

                // Nome correto do método em Celula.java
                int numero = atual.getMinasVizinhas();

                List<int[]> fechadas = new ArrayList<>();
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
                            fechadas.add(
                                    new int[]{nl, nc}
                            );
                        }
                    }
                }

                if (fechadas.isEmpty()) {
                    continue;
                }

                // Regra 1:
                // Se todas as minas vizinhas já estão
                // marcadas, as demais células são seguras.
                if (bandeiras == numero) {

                    int[] posicao = fechadas.get(0);

                    return new Jogada(
                            posicao[0],
                            posicao[1],
                            TipoJogada.REVELAR,
                            "As " + numero
                                    + " minas vizinhas já estão "
                                    + "marcadas. Esta célula pode "
                                    + "ser revelada, desde que as "
                                    + "bandeiras estejam corretas."
                    );
                }

                // Regra 2:
                // Se o número de minas restantes coincide
                // com o número de células fechadas,
                // todas essas células são minas.
                if (numero - bandeiras
                        == fechadas.size()) {

                    int[] posicao = fechadas.get(0);

                    return new Jogada(
                            posicao[0],
                            posicao[1],
                            TipoJogada.MARCAR_MINA,
                            "O número " + numero
                                    + " indica que todas as "
                                    + fechadas.size()
                                    + " células fechadas restantes "
                                    + "contêm minas."
                    );
                }
            }
        }

        return null;
    }

    public boolean executarJogada(Jogada jogada) {

        if (jogada == null
                || tabuleiro.isJogoEncerrado()) {
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
