
package br.com.sakura.campominado.domain;

public class Celula {

    private boolean minada;
    private boolean revelada;
    private boolean marcada;
    private int minasVizinhas;

    public boolean isMinada() {
        return minada;
    }

    public boolean isRevelada() {
        return revelada;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public int getMinasVizinhas() {
        return minasVizinhas;
    }

    public void definirMinada(boolean minada) {
        this.minada = minada;
    }

    public void definirMinasVizinhas(int quantidade) {
        if (quantidade < 0 || quantidade > 8) {
            throw new IllegalArgumentException(
                "A quantidade deve estar entre 0 e 8."
            );
        }

        this.minasVizinhas = quantidade;
    }

    public boolean alternarBandeira() {
        if (revelada) {
            return false;
        }

        marcada = !marcada;
        return true;
    }

    public boolean revelar() {
        if (revelada || marcada) {
            return false;
        }

        revelada = true;
        return true;
    }
}
