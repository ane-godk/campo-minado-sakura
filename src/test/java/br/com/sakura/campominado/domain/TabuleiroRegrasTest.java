
package br.com.sakura.campominado.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TabuleiroRegrasTest {

    @Test
    void deveContarMinasVizinhasCorretamente() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false, false},
                        {false, false, false},
                        {false, false, false}
                }
        );

        assertEquals(
                1,
                tabuleiro.getCelula(0, 1).getMinasVizinhas()
        );

        assertEquals(
                1,
                tabuleiro.getCelula(1, 1).getMinasVizinhas()
        );

        assertEquals(
                0,
                tabuleiro.getCelula(2, 2).getMinasVizinhas()
        );
    }

    @Test
    void deveVencerAoRevelarTodasAsCelulasSeguras() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        tabuleiro.revelar(0, 1);
        tabuleiro.revelar(1, 0);
        tabuleiro.revelar(1, 1);

        assertTrue(tabuleiro.isVenceu());
        assertTrue(tabuleiro.isJogoEncerrado());
    }

    @Test
    void deveColocarERemoverBandeira() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        assertTrue(tabuleiro.alternarBandeira(0, 0));
        assertTrue(tabuleiro.getCelula(0, 0).isMarcada());

        assertTrue(tabuleiro.alternarBandeira(0, 0));
        assertFalse(tabuleiro.getCelula(0, 0).isMarcada());
    }

    @Test
    void naoDeveRevelarCelulaComBandeira() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        tabuleiro.alternarBandeira(0, 0);

        assertFalse(tabuleiro.revelar(0, 0));
        assertFalse(tabuleiro.getCelula(0, 0).isRevelada());
    }

    @Test
    void devePerderAoEsgotarUmaVida() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        assertEquals(1, tabuleiro.getVidas());

        tabuleiro.revelar(0, 0);

        assertEquals(0, tabuleiro.getVidas());
        assertTrue(tabuleiro.isJogoEncerrado());
        assertFalse(tabuleiro.isVenceu());
    }

    @Test
    void deveIniciarComTresVidasConfiguradas() {

        ConfiguracaoJogo configuracao =
                new ConfiguracaoJogo(
                        9,
                        9,
                        10,
                        3,
                        0,
                        Dificuldade.FACIL,
                        "Jogador Teste"
                );

        Tabuleiro tabuleiro = new Tabuleiro(configuracao);

        assertEquals(3, tabuleiro.getVidas());
        assertFalse(tabuleiro.isJogoEncerrado());
    }
}
