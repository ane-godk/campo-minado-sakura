
package br.com.sakura.campominado.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CelulaTest {

    @Test
    void deveMarcarEDesmarcarBandeira() {
        Celula celula = new Celula();

        assertTrue(celula.alternarBandeira());
        assertTrue(celula.isMarcada());

        assertTrue(celula.alternarBandeira());
        assertFalse(celula.isMarcada());
    }

    @Test
    void naoDeveRevelarCelulaMarcada() {
        Celula celula = new Celula();

        celula.alternarBandeira();

        assertFalse(celula.revelar());
        assertFalse(celula.isRevelada());
    }

    @Test
    void naoDevePermitirMaisDeOitoMinasVizinhas() {
        Celula celula = new Celula();

        assertThrows(
            IllegalArgumentException.class,
            () -> celula.definirMinasVizinhas(9)
        );
    }
}
