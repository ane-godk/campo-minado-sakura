
package br.com.sakura.campominado.service;

import br.com.sakura.campominado.domain.RegistroPartida;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingService {

    private static final Path ARQUIVO = Path.of(
            System.getProperty("user.home"),
            ".campo-minado-sakura",
            "ranking.csv"
    );

    public void salvar(RegistroPartida registro) {

        try {
            Files.createDirectories(ARQUIVO.getParent());

            String jogador = limpar(registro.jogador());
            String dificuldade = limpar(registro.dificuldade());

            String linha = jogador + ";"
                    + dificuldade + ";"
                    + registro.pontuacao() + ";"
                    + registro.tempoSegundos() + ";"
                    + registro.data() + System.lineSeparator();

            Files.writeString(
                    ARQUIVO,
                    linha,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível salvar o ranking.", e
            );
        }
    }

    public List<RegistroPartida> listar() {

        List<RegistroPartida> registros = new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return registros;
        }

        try {
            for (String linha : Files.readAllLines(
                    ARQUIVO, StandardCharsets.UTF_8)) {

                String[] partes = linha.split(";", -1);

                if (partes.length != 5) {
                    continue;
                }

                try {
                    registros.add(new RegistroPartida(
                            partes[0],
                            partes[1],
                            Integer.parseInt(partes[2]),
                            Integer.parseInt(partes[3]),
                            LocalDateTime.parse(partes[4])
                    ));
                } catch (IllegalArgumentException e) {
                    // Ignora registros inválidos.
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível ler o ranking.", e
            );
        }

        registros.sort(
                Comparator.comparingInt(
                        RegistroPartida::pontuacao
                ).reversed()
        );

        return registros;
    }

    private String limpar(String valor) {
        return valor == null
                ? ""
                : valor.replace(";", " ")
                       .replace("\n", " ")
                       .replace("\r", " ");
    }
}
