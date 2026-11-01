
package br.com.sakura.campominado.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TabuleiroTest {

    @Test
    void deveCriarTabuleiroComDimensoesConfiguradas() {
        Tabuleiro tabuleiro = new Tabuleiro(9, 9, 10);

        assertEquals(9, tabuleiro.getLinhas());
        assertEquals(9, tabuleiro.getColunas());
        assertEquals(10, tabuleiro.getMinas());
    }

    @Test
    void deveRejeitarQuantidadeInvalidaDeMinas() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Tabuleiro(3, 3, 9)
        );
    }

    @Test
    void deveRejeitarPosicaoForaDoTabuleiro() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 1);

        assertThrows(
            IndexOutOfBoundsException.class,
            () -> tabuleiro.getCelula(-1, 0)
        );
    }

    @Test
    void todasAsCelulasDevemTerContagemEntreZeroEOito() {
        Tabuleiro tabuleiro = new Tabuleiro(9, 9, 10);

        for (int linha = 0; linha < tabuleiro.getLinhas(); linha++) {
            for (int coluna = 0; coluna < tabuleiro.getColunas(); coluna++) {
                int quantidade = tabuleiro.getCelula(linha, coluna)
                    .getMinasVizinhas();

                assertTrue(quantidade >= 0 && quantidade <= 8);
            }
        }
    }
}
