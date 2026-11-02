
package br.com.sakura.campominado.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public final class TemaSakura {

    public static final Color FUNDO =
            new Color(255, 236, 243);

    public static final Color PAINEL =
            new Color(255, 218, 232);

    public static final Color ROSA_CLARO =
            new Color(255, 207, 224);

    public static final Color ROSA =
            new Color(232, 128, 164);

    public static final Color ROSA_ESCURO =
            new Color(167, 73, 111);

    public static final Color BORDA =
            new Color(219, 145, 173);

    public static final Color TEXTO =
            new Color(126, 62, 88);

    public static final Color BRANCO =
            new Color(255, 250, 252);

    public static final Color REVELADA =
            new Color(255, 243, 247);

    public static final Color MINA =
            new Color(246, 153, 175);

    public static final Color BANDEIRA =
            new Color(250, 188, 211);

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
    JButton botao = new BotaoSakura(texto);
    botao.setFont(FontesSakura.botao(15f));
    return botao;
}

    public static void estilizarBotao(JButton botao) {

        botao.setFont(FONTE);
        botao.setForeground(ROSA_ESCURO);

        botao.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        botao.setHorizontalTextPosition(
                SwingConstants.CENTER
        );

        botao.setFocusPainted(false);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        botao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );
    }

    public static void estilizarSeletor(
            JComboBox<?> seletor
    ) {

        seletor.setFont(FONTE);
        seletor.setForeground(TEXTO);
        seletor.setBackground(BRANCO);

        seletor.setFocusable(false);

        seletor.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(4, 7, 4, 7)
                )
        );

        seletor.setRenderer(
                new BasicComboBoxRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> lista,
                            Object valor,
                            int indice,
                            boolean selecionado,
                            boolean temFoco
                    ) {

                        super.getListCellRendererComponent(
                                lista,
                                valor,
                                indice,
                                selecionado,
                                temFoco
                        );

                        setFont(FONTE);

                        setBorder(
                                new EmptyBorder(
                                        5,
                                        9,
                                        5,
                                        9
                                )
                        );

                        if (selecionado) {

                            setBackground(ROSA);
                            setForeground(Color.WHITE);

                        } else {

                            setBackground(BRANCO);
                            setForeground(TEXTO);
                        }

                        return this;
                    }
                }
        );
    }

    private static class BotaoSakura extends JButton {

        BotaoSakura(String texto) {

            super(texto);

            setFont(FONTE);
            setForeground(ROSA_ESCURO);

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setHorizontalTextPosition(
                    SwingConstants.CENTER
            );

            setVerticalAlignment(
                    SwingConstants.CENTER
            );

            setVerticalTextPosition(
                    SwingConstants.CENTER
            );

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);

            setRolloverEnabled(true);

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setBorder(
                    new EmptyBorder(
                            8,
                            12,
                            10,
                            12
                    )
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int largura = getWidth();
            int altura = getHeight();

            if (largura < 8 || altura < 8) {

                g2.dispose();
                super.paintComponent(g);
                return;
            }

            ButtonModel modelo = getModel();

            boolean pressionado =
                    modelo.isPressed()
                            && modelo.isArmed();

            boolean hover =
                    modelo.isRollover()
                            && isEnabled();

            int deslocamento =
                    pressionado ? 2 : 0;

            int x = 2;
            int y = 2 + deslocamento;

            int w = largura - 4;
            int h = Math.max(
                    4,
                    altura - 8
            );

            // Sombra.
            g2.setColor(
                    new Color(
                            143,
                            62,
                            100,
                            isEnabled() ? 75 : 30
                    )
            );

            g2.fill(
                    new RoundRectangle2D.Double(
                            x + 1,
                            6,
                            w,
                            h,
                            22,
                            22
                    )
            );

            Color topo;
            Color base;

            if (!isEnabled()) {

                topo = new Color(241, 228, 234);
                base = new Color(219, 207, 213);

            } else if (pressionado) {

                topo = new Color(226, 151, 181);
                base = new Color(236, 183, 204);

            } else if (hover) {

                topo = new Color(255, 251, 253);
                base = new Color(247, 170, 201);

            } else {

                topo = new Color(255, 239, 247);
                base = new Color(246, 190, 213);
            }

            g2.setPaint(
                    new GradientPaint(
                            0,
                            y,
                            topo,
                            0,
                            y + h,
                            base
                    )
            );

            g2.fill(
                    new RoundRectangle2D.Double(
                            x,
                            y,
                            w,
                            h,
                            22,
                            22
                    )
            );

            // Borda.
            g2.setColor(
                    isEnabled()
                            ? ROSA_ESCURO
                            : BORDA
            );

            g2.setStroke(
                    new BasicStroke(1.5f)
            );

            g2.draw(
                    new RoundRectangle2D.Double(
                            x,
                            y,
                            w,
                            h,
                            22,
                            22
                    )
            );

            // Brilho interno.
            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            hover ? 200 : 135
                    )
            );

            g2.setStroke(
                    new BasicStroke(1.2f)
            );

            g2.draw(
                    new RoundRectangle2D.Double(
                            x + 4,
                            y + 4,
                            Math.max(1, w - 8),
                            Math.max(1, h - 9),
                            17,
                            17
                    )
            );

            g2.dispose();

            // Desenha o texto centralizado
            // usando o próprio Swing.
            super.paintComponent(g);
        }
    }
}
