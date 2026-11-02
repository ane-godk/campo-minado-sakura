
package br.com.sakura.campominado.solver;

import br.com.sakura.campominado.domain.Celula;
import br.com.sakura.campominado.domain.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

public class TestadorPartida {

    private final Tabuleiro tabuleiro;

    private final List<String> erros = new ArrayList<>();

    private int jogadasExecutadas;

    public TestadorPartida(Tabuleiro tabuleiro) {
        if (tabuleiro == null) {
            throw new IllegalArgumentException(
                    "Tabuleiro não pode ser nulo."
            );
        }

        this.tabuleiro = tabuleiro;
    }

    public boolean executarProximoPasso() {

        if (tabuleiro.isJogoEncerrado()) {
            return false;
        }

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

                Celula celula = tabuleiro.getCelula(
                        linha, coluna
                );

                if (celula.isRevelada()) {
                    continue;
                }

                if (celula.isMinada()) {

                    if (!celula.isMarcada()) {
                        if (tabuleiro.alternarBandeira(
                                linha, coluna)) {
                            jogadasExecutadas++;
                            return true;
                        }
                    }

                } else {

                    if (celula.isMarcada()) {
                        tabuleiro.alternarBandeira(
                                linha, coluna
                        );
                    }

                    if (tabuleiro.revelar(linha, coluna)) {
                        jogadasExecutadas++;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public ResultadoTeste executarTesteCompleto() {

        while (!tabuleiro.isJogoEncerrado()) {

            boolean executou = executarProximoPasso();

            if (!executou) {
                erros.add(
                        "Nenhuma jogada disponível antes do fim."
                );
                break;
            }
        }

        validarResultado();

        return new ResultadoTeste(
                tabuleiro.isVenceu(),
                tabuleiro.isJogoEncerrado(),
                jogadasExecutadas,
                List.copyOf(erros)
        );
    }

    private void validarResultado() {

        int segurasPendentes = 0;

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

                Celula celula = tabuleiro.getCelula(
                        linha, coluna
                );

                if (!celula.isMinada()
                        && !celula.isRevelada()) {

                    segurasPendentes++;
                }
            }
        }

        if (segurasPendentes == 0
                && !tabuleiro.isVenceu()) {

            erros.add(
                    "Todas as células seguras foram reveladas, "
                            + "mas a vitória não foi reconhecida."
            );
        }

        if (tabuleiro.isVenceu()
                && segurasPendentes > 0) {

            erros.add(
                    "Vitória reconhecida com células seguras pendentes."
            );
        }

        if (tabuleiro.isVenceu()
                && !tabuleiro.isJogoEncerrado()) {

            erros.add(
                    "Vitória sem encerramento da partida."
            );
        }
    }

    public record ResultadoTeste(
            boolean venceu,
            boolean encerrado,
            int jogadas,
            List<String> erros
    ) {
        public boolean passou() {
            return venceu && encerrado && erros.isEmpty();
        }
    }
}
