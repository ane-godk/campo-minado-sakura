
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Dificuldade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;

public class TelaInicial extends JFrame {

    private static final Color VINHO =
            new Color(158, 63, 105);

    private static final Color ROSA =
            new Color(216, 107, 153);

    private static final Color CLARO =
            new Color(255, 239, 247);

    private final JComboBox<Dificuldade> seletorDificuldade =
            new JComboBox<>(new Dificuldade[]{
                    Dificuldade.FACIL,
                    Dificuldade.MEDIO,
                    Dificuldade.DIFICIL,
                    Dificuldade.INFERNO
            });

    private final JPanel conteudo = new JPanel();

    private final JLabel titulo = new JLabel(
            "<html><div style='text-align:center'>"
                    + "Campo<br>Minado"
                    + "</div></html>",
            SwingConstants.CENTER
    );

    private final JLabel flores = new JLabel(
            "✿  ✿  ✿",
            SwingConstants.CENTER
    );

    private final JLabel subtitulo = new JLabel(
            "✿ SAKURA ✿",
            SwingConstants.CENTER
    );

    private final PainelMenu menu = new PainelMenu();

    private final BotaoSakura jogar =
            new BotaoSakura("▶  Jogar", true);

    private final BotaoSakura dificuldade =
            new BotaoSakura("⚙  Dificuldade", false);

    private final BotaoSakura comoJogar =
            new BotaoSakura("?  Como Jogar", false);

    public TelaInicial() {

        setTitle("Campo Minado Sakura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(new FundoSakura());
        getContentPane().setLayout(new GridBagLayout());

        setMinimumSize(new Dimension(380, 540));

        montarMenu();

        Rectangle tela = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        setSize(
                Math.min(760, tela.width),
                Math.min(850, tela.height)
        );

        setLocationRelativeTo(null);
    }

    private void montarMenu() {

        conteudo.setOpaque(false);

        conteudo.setLayout(
                new BoxLayout(conteudo, BoxLayout.Y_AXIS)
        );

        flores.setFont(
                new Font("Serif", Font.BOLD, 33)
        );

        flores.setForeground(ROSA);
        flores.setAlignmentX(Component.CENTER_ALIGNMENT);

        titulo.setFont(
                new Font("Serif", Font.BOLD, 74)
        );

        titulo.setForeground(VINHO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitulo.setFont(
                new Font("SansSerif", Font.BOLD, 19)
        );

        subtitulo.setForeground(VINHO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.setLayout(
                new GridLayout(3, 1, 0, 13)
        );

        menu.setBorder(
                new EmptyBorder(27, 31, 27, 31)
        );

        menu.add(jogar);
        menu.add(dificuldade);
        menu.add(comoJogar);

        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        conteudo.add(flores);
        conteudo.add(Box.createVerticalStrut(7));
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(5));
        conteudo.add(subtitulo);
        conteudo.add(Box.createVerticalStrut(28));
        conteudo.add(menu);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        getContentPane().add(conteudo, gbc);

        jogar.addActionListener(
                e -> iniciarJogo()
        );

        dificuldade.addActionListener(
                e -> escolherDificuldade()
        );

        comoJogar.addActionListener(
                e -> mostrarComoJogar()
        );

        getContentPane().addComponentListener(
                new ComponentAdapter() {

                    @Override
                    public void componentResized(
                            ComponentEvent e
                    ) {
                        adaptar();
                    }
                }
        );

        SwingUtilities.invokeLater(this::adaptar);
    }

    private void adaptar() {

        int w = getContentPane().getWidth();
        int h = getContentPane().getHeight();

        if (w <= 0 || h <= 0) {
            return;
        }

        int largura = Math.min(
                520,
                Math.max(315, w - 34)
        );

        boolean compacto = h < 670;

        titulo.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        compacto ? 53 : 74
                )
        );

        flores.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        compacto ? 25 : 33
                )
        );

        menu.setPreferredSize(
                new Dimension(
                        largura,
                        compacto ? 258 : 303
                )
        );

        conteudo.revalidate();
        conteudo.repaint();
    }

    private void iniciarJogo() {

        Dificuldade selecionada =
                (Dificuldade) seletorDificuldade
                        .getSelectedItem();

        TelaPrincipal jogo = new TelaPrincipal();

        if (selecionada != null) {
            jogo.selecionarDificuldade(selecionada);
        }

        jogo.setVisible(true);
        dispose();
    }

    private void escolherDificuldade() {

        Dificuldade anterior =
                (Dificuldade) seletorDificuldade
                        .getSelectedItem();

        int resposta = JOptionPane.showConfirmDialog(
                this,
                seletorDificuldade,
                "Escolha a dificuldade",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            seletorDificuldade.setSelectedItem(anterior);
        }
    }

    private void mostrarComoJogar() {

        String instrucoes = """
                CAMPO MINADO SAKURA

                OBJETIVO
                Revele todas as células que não possuem minas.

                CONTROLES
                • Clique esquerdo: revelar célula.
                • Clique direito: colocar ou remover bandeira.

                NÚMEROS
                Indicam quantas minas existem nas oito
                células vizinhas.

                VIDAS
                Você começa com três vidas.
                Revelar uma mina custa uma vida.

                FERRAMENTAS
                • Próximo passo: jogada do testador.
                • Autojogar / Testar: executa a partida
                  automaticamente.
                • Dica lógica: mostra uma dedução.
                • Passo lógico: executa a dedução.

                PONTUAÇÃO
                Revelar células concede pontos.
                O tempo e a dificuldade influenciam
                o resultado.
                """;

        JTextArea texto = new JTextArea(
                instrucoes,
                19,
                35
        );

        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);

        texto.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        texto.setBackground(CLARO);
        texto.setForeground(VINHO);

        texto.setBorder(
                new EmptyBorder(14, 14, 14, 14)
        );

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(texto),
                "Como Jogar",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static class PainelMenu extends JPanel {

        PainelMenu() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D p = (Graphics2D) g.create();

            p.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            p.setColor(
                    new Color(153, 57, 100, 45)
            );

            p.fill(new RoundRectangle2D.Double(
                    5, 8, w - 10, h - 10, 52, 52
            ));

            p.setColor(
                    new Color(255, 226, 238, 237)
            );

            p.fill(new RoundRectangle2D.Double(
                    2, 2, w - 5, h - 9, 50, 50
            ));

            p.setColor(
                    new Color(224, 127, 164)
            );

            p.setStroke(new BasicStroke(3));

            p.draw(new RoundRectangle2D.Double(
                    3, 3, w - 7, h - 12, 50, 50
            ));

            p.dispose();
            super.paintComponent(g);
        }
    }

    private static class BotaoSakura extends JButton {

        private final boolean destaque;
        private boolean hover;

        BotaoSakura(String nome, boolean destaque) {

            super(nome);

            this.destaque = destaque;

            setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            destaque ? 24 : 20
                    )
            );

            setForeground(
                    destaque ? Color.WHITE : VINHO
            );

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D p = (Graphics2D) g.create();

            p.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            p.setColor(
                    new Color(153, 63, 103, 75)
            );

            p.fill(new RoundRectangle2D.Double(
                    3, 6, w - 6, h - 8, 32, 32
            ));

            Color cima = destaque
                    ? new Color(239, 146, 180)
                    : new Color(255, 242, 248);

            Color baixo = destaque
                    ? new Color(193, 73, 125)
                    : new Color(247, 178, 204);

            if (hover) {
                cima = cima.brighter();
                baixo = baixo.brighter();
            }

            p.setPaint(
                    new GradientPaint(
                            0, 0, cima,
                            0, h, baixo
                    )
            );

            p.fill(new RoundRectangle2D.Double(
                    3, 2, w - 6, h - 10, 32, 32
            ));

            p.setColor(
                    new Color(255, 255, 255, 180)
            );

            p.setStroke(new BasicStroke(2));

            p.draw(new RoundRectangle2D.Double(
                    8, 7, w - 16, h - 20, 25, 25
            ));

            p.setColor(VINHO);

            p.draw(new RoundRectangle2D.Double(
                    3, 2, w - 6, h - 10, 32, 32
            ));

            p.dispose();

            super.paintComponent(g);
        }
    }

    private static class FundoSakura extends JPanel {

        FundoSakura() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D p = (Graphics2D) g.create();

            p.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            p.setPaint(
                    new GradientPaint(
                            0, 0,
                            new Color(255, 185, 210),
                            0, h,
                            new Color(255, 235, 244)
                    )
            );

            p.fillRect(0, 0, w, h);

            desenharSol(p, w, h);
            desenharMontanhas(p, w, h);
            desenharLago(p, w, h);
            desenharPagode(p, w, h);
            desenharPonte(p, w, h);
            desenharCerejeiras(p, w, h);
            desenharPetalas(p, w, h);

            p.dispose();
        }

        private void desenharSol(
                Graphics2D p,
                int w,
                int h
        ) {

            p.setColor(
                    new Color(255, 246, 249, 115)
            );

            int r = Math.max(55, w / 9);

            p.fillOval(
                    w - r * 2,
                    h / 10,
                    r,
                    r
            );

            p.setColor(
                    new Color(255, 255, 255, 70)
            );

            p.fillOval(
                    w - r * 2 - 18,
                    h / 10 - 18,
                    r + 36,
                    r + 36
            );
        }

        private void desenharMontanhas(
                Graphics2D p,
                int w,
                int h
        ) {

            Path2D fundo = new Path2D.Double();

            fundo.moveTo(0, h * .72);

            fundo.curveTo(
                    w * .18, h * .58,
                    w * .32, h * .76,
                    w * .53, h * .64
            );

            fundo.curveTo(
                    w * .75, h * .52,
                    w * .87, h * .70,
                    w, h * .59
            );

            fundo.lineTo(w, h);
            fundo.lineTo(0, h);
            fundo.closePath();

            p.setColor(
                    new Color(211, 133, 165, 70)
            );

            p.fill(fundo);
        }

        private void desenharLago(
                Graphics2D p,
                int w,
                int h
        ) {

            p.setColor(
                    new Color(255, 245, 249, 125)
            );

            p.fillRect(
                    0,
                    (int) (h * .84),
                    w,
                    h
            );

            p.setStroke(new BasicStroke(2));

            Random r = new Random(72);

            for (int i = 0; i < 30; i++) {

                int y = (int) (h * .84)
                        + r.nextInt(
                                Math.max(
                                        1,
                                        (int) (h * .16)
                                )
                        );

                int x = r.nextInt(
                        Math.max(1, w)
                );

                p.setColor(
                        new Color(195, 106, 149, 45)
                );

                p.drawLine(
                        x,
                        y,
                        x + 12 + r.nextInt(35),
                        y
                );
            }
        }

        private void desenharPagode(
                Graphics2D p,
                int w,
                int h
        ) {

            Graphics2D a = (Graphics2D) p.create();

            a.translate(
                    w * .87,
                    h * .77
            );

            a.setColor(
                    new Color(143, 74, 109, 125)
            );

            for (int i = 0; i < 4; i++) {

                int y = -i * 32;
                int largura = 70 - i * 12;

                a.fillRoundRect(
                        -largura / 2 + 5,
                        y - 25,
                        largura - 10,
                        29,
                        3,
                        3
                );

                Path2D teto = new Path2D.Double();

                teto.moveTo(
                        -largura / 2 - 8,
                        y - 24
                );

                teto.quadTo(
                        0,
                        y - 49,
                        largura / 2 + 8,
                        y - 24
                );

                teto.closePath();
                a.fill(teto);
            }

            a.setStroke(new BasicStroke(3));
            a.drawLine(0, -146, 0, -162);

            a.dispose();
        }

        private void desenharPonte(
                Graphics2D p,
                int w,
                int h
        ) {

            Graphics2D a = (Graphics2D) p.create();

            a.translate(
                    w * .12,
                    h * .87
            );

            a.setColor(
                    new Color(166, 83, 120, 125)
            );

            a.setStroke(
                    new BasicStroke(
                            9,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            a.drawArc(
                    -100, -48, 200, 96, 0, 180
            );

            a.setStroke(new BasicStroke(3));

            a.drawArc(
                    -100, -60, 200, 96, 0, 180
            );

            for (int x = -80; x <= 80; x += 25) {
                a.drawLine(x, -32, x, 5);
            }

            a.dispose();
        }

        private void desenharCerejeiras(
                Graphics2D p,
                int w,
                int h
        ) {

            p.setColor(
                    new Color(139, 78, 102, 140)
            );

            p.setStroke(
                    new BasicStroke(
                            9,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            Path2D galho = new Path2D.Double();

            galho.moveTo(-20, 0);

            galho.curveTo(
                    w * .12, h * .06,
                    w * .23, h * .04,
                    w * .49, h * .12
            );

            p.draw(galho);

            Random r = new Random(42);

            for (int i = 0; i < 39; i++) {

                int x = r.nextInt(
                        Math.max(
                                1,
                                (int) (w * .58)
                        )
                );

                int y = r.nextInt(
                        Math.max(
                                1,
                                (int) (h * .29)
                        )
                );

                flor(
                        p,
                        x,
                        y,
                        12 + r.nextInt(17)
                );
            }

            for (int i = 0; i < 16; i++) {

                int x = r.nextBoolean()
                        ? r.nextInt(
                                Math.max(1, w / 5)
                        )
                        : w - r.nextInt(
                                Math.max(1, w / 5)
                        );

                int y = (int) (h * .77)
                        + r.nextInt(
                                Math.max(
                                        1,
                                        (int) (h * .22)
                                )
                        );

                flor(
                        p,
                        x,
                        y,
                        9 + r.nextInt(13)
                );
            }
        }

        private void desenharPetalas(
                Graphics2D p,
                int w,
                int h
        ) {

            Random r = new Random(99);

            p.setColor(
                    new Color(219, 101, 151, 100)
            );

            for (int i = 0; i < 75; i++) {

                int x = r.nextInt(
                        Math.max(1, w)
                );

                int y = r.nextInt(
                        Math.max(1, h)
                );

                int t = 4 + r.nextInt(8);

                p.fill(new Ellipse2D.Double(
                        x,
                        y,
                        t,
                        t * .55
                ));
            }
        }

        private void flor(
                Graphics2D p,
                int x,
                int y,
                int raio
        ) {

            p.setColor(
                    new Color(255, 222, 236, 220)
            );

            for (int i = 0; i < 5; i++) {

                double ang =
                        i * Math.PI * 2 / 5;

                double px =
                        x + Math.cos(ang) * raio * .65;

                double py =
                        y + Math.sin(ang) * raio * .65;

                p.fill(new Ellipse2D.Double(
                        px - raio * .5,
                        py - raio * .5,
                        raio,
                        raio
                ));
            }

            p.setColor(
                    new Color(216, 107, 153, 220)
            );

            p.fill(new Ellipse2D.Double(
                    x - raio * .17,
                    y - raio * .17,
                    raio * .34,
                    raio * .34
            ));
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new TelaInicial().setVisible(true)
        );
    }
}
