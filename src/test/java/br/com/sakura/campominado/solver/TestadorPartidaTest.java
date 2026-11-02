
package br.com.sakura.campominado.solver;

import br.com.sakura.campominado.domain.Tabuleiro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestadorPartidaTest {

    @Test
    void deveConcluirPartidaDeTesteComVitoria() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        TestadorPartida testador =
                new TestadorPartida(tabuleiro);

        TestadorPartida.ResultadoTeste resultado =
                testador.executarTesteCompleto();

        assertTrue(resultado.passou());
        assertTrue(resultado.venceu());
        assertTrue(resultado.encerrado());
        assertTrue(resultado.erros().isEmpty());
        assertTrue(resultado.jogadas() > 0);
    }

    @Test
    void deveMarcarMinaDuranteTesteAutomatico() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        TestadorPartida testador =
                new TestadorPartida(tabuleiro);

        assertTrue(testador.executarProximoPasso());

        assertTrue(
                tabuleiro.getCelula(0, 0).isMarcada()
        );
    }

    @Test
    void naoDeveContinuarAposFimDaPartida() {

        Tabuleiro tabuleiro = new Tabuleiro(
                new boolean[][]{
                        {true, false},
                        {false, false}
                }
        );

        TestadorPartida testador =
                new TestadorPartida(tabuleiro);

        testador.executarTesteCompleto();

        assertTrue(tabuleiro.isJogoEncerrado());

        assertFalse(
                testador.executarProximoPasso()
        );
    }
}
