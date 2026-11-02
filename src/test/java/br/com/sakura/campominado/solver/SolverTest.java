
package br.com.sakura.campominado.solver;

import br.com.sakura.campominado.domain.Tabuleiro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    @Test
    void naoDeveAdivinharSemInformacoes() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false, false}
                }
        );

        Solver solver = new Solver(tabuleiro);

        assertNull(solver.encontrarJogada());
    }

    @Test
    void deveEncontrarCelulaSeguraComMinaMarcada() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false, false}
                }
        );

        tabuleiro.alternarBandeira(0, 0);
        tabuleiro.revelar(0, 1);

        Solver solver = new Solver(tabuleiro);
        Solver.Jogada jogada = solver.encontrarJogada();

        assertNotNull(jogada);
        assertEquals(
                Solver.TipoJogada.REVELAR,
                jogada.tipo()
        );
        assertEquals(0, jogada.linha());
        assertEquals(2, jogada.coluna());
    }

    @Test
    void deveIdentificarMinaPorDeducao() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {false, true, false}
                }
        );

        tabuleiro.revelar(0, 0);

        Solver solver = new Solver(tabuleiro);
        Solver.Jogada jogada = solver.encontrarJogada();

        assertNotNull(jogada);
        assertEquals(
                Solver.TipoJogada.MARCAR_MINA,
                jogada.tipo()
        );
        assertEquals(0, jogada.linha());
        assertEquals(1, jogada.coluna());
    }

    @Test
    void deveExecutarMarcacaoDeMina() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {false, true, false}
                }
        );

        tabuleiro.revelar(0, 0);

        Solver solver = new Solver(tabuleiro);
        Solver.Jogada jogada = solver.encontrarJogada();

        assertNotNull(jogada);
        assertTrue(solver.executarJogada(jogada));

        assertTrue(
                tabuleiro.getCelula(0, 1).isMarcada()
        );
    }

    @Test
    void naoDeveExecutarJogadaNula() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false}
                }
        );

        Solver solver = new Solver(tabuleiro);

        assertFalse(solver.executarJogada(null));
    }

    @Test
    void naoDeveDeduzirComBandeirasContraditorias() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false, false, false}
                }
        );

        tabuleiro.revelar(0, 1);

        // O número exibido é 1, mas duas células
        // vizinhas estão marcadas como minas.
        tabuleiro.alternarBandeira(0, 0);
        tabuleiro.alternarBandeira(0, 2);

        Solver solver = new Solver(tabuleiro);

        assertNull(solver.encontrarJogada());
    }

    @Test
    void naoDeveExecutarDicaDesatualizada() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false, false}
                }
        );

        tabuleiro.alternarBandeira(0, 0);
        tabuleiro.revelar(0, 1);

        Solver solver = new Solver(tabuleiro);

        Solver.Jogada dicaAntiga =
                solver.encontrarJogada();

        assertNotNull(dicaAntiga);

        // O jogador modifica o tabuleiro
        // depois de receber a dica.
        tabuleiro.alternarBandeira(0, 0);

        assertFalse(
                solver.executarJogada(dicaAntiga)
        );
    }
}
