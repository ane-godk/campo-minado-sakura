
package br.com.sakura.campominado.domain;

import java.util.Objects;

/**
 * Configuração imutável de uma partida.
 */
public final class ConfiguracaoJogo {

    private final int linhas;
    private final int colunas;
    private final int minas;
    private final int vidas;
    private final int limiteSegundos;
    private final Dificuldade dificuldade;
    private final String nomeJogador;

    public ConfiguracaoJogo(
            int linhas,
            int colunas,
            int minas,
            int vidas,
            int limiteSegundos,
            Dificuldade dificuldade,
            String nomeJogador) {

        if (linhas <= 0 || colunas <= 0) {
            throw new IllegalArgumentException(
                    "Dimensões inválidas.");
        }

        if (minas <= 0 || minas >= (long) linhas * colunas) {
            throw new IllegalArgumentException(
                    "Quantidade de minas inválida.");
        }

        if (vidas < 1 || vidas > 3) {
            throw new IllegalArgumentException(
                    "Vidas devem estar entre 1 e 3.");
        }

        if (limiteSegundos < 0) {
            throw new IllegalArgumentException(
                    "Limite de tempo inválido.");
        }

        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        this.vidas = vidas;
        this.limiteSegundos = limiteSegundos;
        this.dificuldade = Objects.requireNonNull(
                dificuldade, "Dificuldade obrigatória.");

        this.nomeJogador =
                nomeJogador == null || nomeJogador.isBlank()
                        ? "Jogador"
                        : nomeJogador.trim();
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

    public int getLimiteSegundos() {
        return limiteSegundos;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }
}
