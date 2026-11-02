
package br.com.sakura.campominado.domain;

public enum Dificuldade {

    FACIL("Fácil", 9, 9, 10, 1.0),
    MEDIO("Médio", 16, 16, 40, 1.5),
    DIFICIL("Difícil", 16, 30, 99, 2.0),
    INFERNO("Inferno", 24, 30, 180, 3.0),
    PUZZLE("Puzzle", 0, 0, 0, 1.10),
    ALEATORIO("Aleatório", 0, 0, 0, 1.20);

    private final String nome;
    private final int linhas;
    private final int colunas;
    private final int minas;
    private final double multiplicador;

    Dificuldade(String nome, int linhas, int colunas,
                int minas, double multiplicador) {

        this.nome = nome;
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        this.multiplicador = multiplicador;
    }

    public String getNome() {
        return nome;
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

    public double getMultiplicador() {
        return multiplicador;
    }

    @Override
    public String toString() {
        return nome;
    }
}
