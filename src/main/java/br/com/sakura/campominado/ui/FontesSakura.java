
package br.com.sakura.campominado.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public final class FontesSakura {

    private static final Font LILITA = carregarFonte(
            "/fonts/LilitaOne-Regular.ttf",
            new Font("Serif", Font.BOLD, 72)
    );

    private static final Font FREDOKA = carregarFonte(
            "/fonts/Fredoka-Bold.ttf",
            new Font("SansSerif", Font.BOLD, 18)
    );

    private FontesSakura() {
    }

    private static Font carregarFonte(
            String caminho,
            Font alternativa
    ) {
        try (InputStream arquivo =
                     FontesSakura.class.getResourceAsStream(caminho)) {

            if (arquivo == null) {
                System.err.println(
                        "Fonte não encontrada: " + caminho
                );
                return alternativa;
            }

            Font fonte = Font.createFont(
                    Font.TRUETYPE_FONT,
                    arquivo
            );

            GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .registerFont(fonte);

            return fonte;

        } catch (Exception e) {
            System.err.println(
                    "Não foi possível carregar a fonte: "
                            + caminho
            );

            return alternativa;
        }
    }

    public static Font titulo(float tamanho) {
        return LILITA.deriveFont(tamanho);
    }

    public static Font botao(float tamanho) {
        return FREDOKA.deriveFont(tamanho);
    }

    public static Font texto(float tamanho) {
        return FREDOKA.deriveFont(tamanho);
    }
}
