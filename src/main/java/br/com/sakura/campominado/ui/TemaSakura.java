
package br.com.sakura.campominado.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public final class TemaSakura {

    public static final Color FUNDO = new Color(255, 236, 243);
    public static final Color PAINEL = new Color(255, 218, 232);
    public static final Color ROSA_CLARO = new Color(255, 207, 224);
    public static final Color ROSA = new Color(232, 128, 164);
    public static final Color ROSA_ESCURO = new Color(167, 73, 111);
    public static final Color BORDA = new Color(219, 145, 173);
    public static final Color TEXTO = new Color(126, 62, 88);
    public static final Color BRANCO = new Color(255, 250, 252);
    public static final Color REVELADA = new Color(255, 243, 247);
    public static final Color MINA = new Color(246, 153, 175);
    public static final Color BANDEIRA = new Color(250, 188, 211);

    public static final Font FONTE =
            new Font("SansSerif", Font.BOLD, 14);

    public static final Font FONTE_NUMERO =
            new Font("SansSerif", Font.BOLD, 22);

    private TemaSakura() {
    }

    public static void aplicarJanela(JFrame janela) {
        janela.getContentPane().setBackground(FUNDO);
    }

    public static void estilizarTexto(JLabel label) {
        label.setForeground(TEXTO);
        label.setFont(FONTE);
    }

    public static JButton criarBotao(String texto) {
        return new BotaoSakura(texto);
    }

    public static void estilizarBotao(JButton botao) {
        botao.setFont(FONTE);
        botao.setForeground(ROSA_ESCURO);
        botao.setBackground(ROSA_CLARO);
        botao.setFocusPainted(false);
        botao.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        botao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDA, 1),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );
    }

    public static void estilizarSeletor(JComboBox<?> seletor) {
        seletor.setFont(FONTE);
        seletor.setForeground(TEXTO);
        seletor.setBackground(BRANCO);
    }

    private static class BotaoSakura extends JButton {

        BotaoSakura(String texto) {
            super(texto);
            setFont(FONTE);
            setForeground(ROSA_ESCURO);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );
            setMargin(new Insets(8, 16, 8, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int largura = getWidth() - 2;
            int altura = getHeight() - 3;

            g2.setColor(new Color(198, 108, 142, 100));
            g2.fill(new RoundRectangle2D.Float(
                    1, 4, largura, altura - 1, 18, 18
            ));

            Color cor = getModel().isPressed()
                    ? ROSA
                    : getModel().isRollover()
                    ? BRANCO
                    : ROSA_CLARO;

            g2.setColor(cor);
            g2.fill(new RoundRectangle2D.Float(
                    1, 1, largura, altura - 2, 18, 18
            ));

            g2.setColor(BORDA);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(
                    1, 1, largura, altura - 2, 18, 18
            ));

            g2.setColor(new Color(255, 255, 255, 160));
            g2.drawRoundRect(
                    5, 4,
                    Math.max(0, largura - 8),
                    Math.max(0, altura - 10),
                    14, 14
            );

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
