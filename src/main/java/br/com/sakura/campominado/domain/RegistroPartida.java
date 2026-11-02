
package br.com.sakura.campominado.domain;

import java.time.LocalDateTime;

public record RegistroPartida(
        String jogador,
        String dificuldade,
        int pontuacao,
        int tempoSegundos,
        LocalDateTime data
) {
}
