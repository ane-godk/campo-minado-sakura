
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Dificuldade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class TelaInicial extends JFrame {

    private static final Color ROSA_ESCURO =
            new Color(165, 65, 108);

    private static final Color ROSA_MEDIO =
            new Color(220, 116, 156);

    private static final Color ROSA_CLARO =
            new Color(255, 221, 235);

    private static final Color ROSA_BOTAO =
            new Color(247, 181, 207);

    private static final Color BRANCO =
            new Color(255, 247, 251);

    private final JComboBox<Dificuldade> seletorDificuldade =
            new JComboBox<>(new Dificuldade[]{
                    Dificuldade.FACIL,
                    Dificuldade.MEDIO,
                    Dificuldade.DIFICIL,
                    Dificuldade.INFERNO
            });

    private final JPanel painelConteudo = new JPanel();

    public TelaInicial() {

        setTitle("Campo Minado Sakura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(380, 520));

        setContentPane(new FundoSakura());

        configurarConteudo();

        Dimension tela = Toolkit.getDefaultToolkit()
                .getScreenSize();

        int largura = Math.min(720, tela.width - 100);
        int altura = Math.min(820, tela.height - 100);

        setSize(largura, altura);
        setLocationRelativeTo(null);
    }

    private void configurarConteudo() {

        JPanel raiz = (JPanel) getContentPane();

        raiz.setLayout(new GridBagLayout());

        painelConteudo.setOpaque(false);

        painelConteudo.setLayout(
                new BoxLayout(
                        painelConteudo,
                        BoxLayout.Y_AXIS
                )
        );

        painelConteudo.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JLabel florSuperior = new JLabel(
                "✿  ✿  ✿",
                SwingConstants.CENTER
        );

        florSuperior.setFont(
                new Font("Serif", Font.PLAIN, 38)
        );

        florSuperior.setForeground(ROSA_MEDIO);
        florSuperior.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel(
                "<html><div style='text-align:center'>"
                        + "Campo<br>Minado"
                        + "</div></html>",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Serif", Font.BOLD, 78)
        );

        titulo.setForeground(ROSA_ESCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "✿ SAKURA ✿",
                SwingConstants.CENTER
        );

        subtitulo.setFont(
                new Font("SansSerif", Font.BOLD, 19)
        );

        subtitulo.setForeground(ROSA_ESCURO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel menu = criarPainelMenu();

        painelConteudo.add(florSuperior);
        painelConteudo.add(Box.createVerticalStrut(6));
        painelConteudo.add(titulo);
        painelConteudo.add(Box.createVerticalStrut(5));
        painelConteudo.add(subtitulo);
        painelConteudo.add(Box.createVerticalStrut(36));
        painelConteudo.add(menu);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        raiz.add(painelConteudo, gbc);

        raiz.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                atualizarResponsividade();
            }
        });

        SwingUtilities.invokeLater(
                this::atualizarResponsividade
        );
    }

    private JPanel criarPainelMenu() {

        JPanel menu = new JPanel();

        menu.setLayout(
                new BoxLayout(menu, BoxLayout.Y_AXIS)
        );

        menu.setBackground(
                new Color(255, 231, 240, 235)
        );

        menu.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                ROSA_MEDIO,
                                3,
                                true
                        ),
                        new EmptyBorder(26, 30, 26, 30)
                )
        );

        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jogar = criarBotao(
                "▶  Jogar",
                true
        );

        JButton dificuldade = criarBotao(
                "⚙  Dificuldade",
                false
        );

        JButton comoJogar = criarBotao(
                "?  Como Jogar",
                false
        );

        menu.add(jogar);
        menu.add(Box.createVerticalStrut(15));
        menu.add(dificuldade);
        menu.add(Box.createVerticalStrut(15));
        menu.add(comoJogar);

        jogar.addActionListener(
                evento -> iniciarJogo()
        );

        dificuldade.addActionListener(
                evento -> escolherDificuldade()
        );

        comoJogar.addActionListener(
                evento -> mostrarComoJogar()
        );

        return menu;
    }

    private JButton criarBotao(
            String texto,
            boolean destaque
    ) {

        JButton botao = new JButton(texto);

        botao.setAlignmentX(Component.CENTER_ALIGNMENT);

        botao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        destaque ? 25 : 21
                )
        );

        botao.setForeground(
                destaque ? Color.WHITE : ROSA_ESCURO
        );

        botao.setBackground(
                destaque ? ROSA_MEDIO : ROSA_BOTAO
        );

        botao.setFocusPainted(false);
        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        botao.setMaximumSize(
                new Dimension(420, destaque ? 82 : 68)
        );

        botao.setPreferredSize(
                new Dimension(420, destaque ? 82 : 68)
        );

        botao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                ROSA_ESCURO,
                                2,
                                true
                        ),
                        new EmptyBorder(12, 24, 12, 24)
                )
        );

        return botao;
    }

    private void iniciarJogo() {

        Dificuldade dificuldade =
                (Dificuldade) seletorDificuldade
                        .getSelectedItem();

        TelaPrincipal jogo = new TelaPrincipal();

        // A dificuldade escolhida na tela inicial
        // será aplicada na tela de jogo.
        if (dificuldade != null) {
            jogo.selecionarDificuldade(dificuldade);
        }

        jogo.setVisible(true);
        dispose();
    }

    private void escolherDificuldade() {

        int resposta = JOptionPane.showConfirmDialog(
                this,
                seletorDificuldade,
                "Escolha a dificuldade",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta == JOptionPane.OK_OPTION) {

            Dificuldade selecionada =
                    (Dificuldade) seletorDificuldade
                            .getSelectedItem();

            if (selecionada != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Dificuldade selecionada: "
                                + selecionada.getNome(),
                        "Campo Minado Sakura",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    private void mostrarComoJogar() {

        String instrucoes =
                """
                CAMPO MINADO SAKURA

                OBJETIVO
                Revele todas as células sem minas.

                CONTROLES
                • Clique esquerdo: revelar uma célula.
                • Clique direito: colocar ou retirar bandeira.

                NÚMEROS
                Cada número indica quantas minas existem
                nas oito células ao redor.

                VIDAS
                Você começa com três vidas.
                Revelar uma mina faz perder uma vida.

                FERRAMENTAS
                • Próximo passo: jogada do testador.
                • Autojogar / Testar: executa uma partida.
                • Dica lógica: mostra uma dedução segura.
                • Passo lógico: executa a dedução.

                PONTUAÇÃO
                Você ganha pontos ao revelar células.
                Tempo e dificuldade afetam o resultado.

                Boa partida!
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

        texto.setBackground(BRANCO);
        texto.setForeground(ROSA_ESCURO);

        texto.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(texto),
                "Como Jogar",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void atualizarResponsividade() {

        int largura = getContentPane().getWidth();
        int altura = getContentPane().getHeight();

        if (largura <= 0 || altura <= 0) {
            return;
        }

        int larguraConteudo = Math.min(
                540,
                Math.max(320, largura - 40)
        );

        painelConteudo.setPreferredSize(
                new Dimension(
                        larguraConteudo,
                        Math.min(650, altura - 20)
                )
        );

        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private static class FundoSakura extends JPanel {

        private final Random random = new Random(42);

        FundoSakura() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            GradientPaint gradiente = new GradientPaint(
                    0,
                    0,
                    new Color(255, 190, 214),
                    0,
                    h,
                    new Color(255, 235, 243)
            );

            g2.setPaint(gradiente);
            g2.fillRect(0, 0, w, h);

            desenharGalhos(g2, w, h);
            desenharPetalas(g2, w, h);

            g2.dispose();
        }

        private void desenharGalhos(
                Graphics2D g2,
                int w,
                int h
        ) {

            g2.setColor(new Color(169, 101, 120, 130));

            g2.setStroke(
                    new BasicStroke(
                            7,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            Path2D galho = new Path2D.Double();

            galho.moveTo(0, 0);
            galho.curveTo(
                    w * 0.15,
                    h * 0.05,
                    w * 0.20,
                    h * 0.15,
                    w * 0.40,
                    h * 0.07
            );

            g2.draw(galho);

            random.setSeed(42);

            for (int i = 0; i < 24; i++) {

                int x = random.nextInt(
                        Math.max(1, w / 2)
                );

                int y = random.nextInt(
                        Math.max(1, h / 4)
                );

                desenharFlor(
                        g2,
                        x,
                        y,
                        14 + random.nextInt(18)
                );
            }

            for (int i = 0; i < 10; i++) {

                int x = random.nextBoolean()
                        ? random.nextInt(Math.max(1, w / 6))
                        : w - random.nextInt(
                                Math.max(1, w / 6)
                        );

                int y = h - random.nextInt(
                        Math.max(1, h / 4)
                );

                desenharFlor(
                        g2,
                        x,
                        y,
                        12 + random.nextInt(15)
                );
            }
        }

        private void desenharPetalas(
                Graphics2D g2,
                int w,
                int h
        ) {

            random.setSeed(99);

            g2.setColor(
                    new Color(234, 111, 160, 100)
            );

            for (int i = 0; i < 55; i++) {

                int x = random.nextInt(
                        Math.max(1, w)
                );

                int y = random.nextInt(
                        Math.max(1, h)
                );

                int tamanho = 4 + random.nextInt(8);

                g2.fill(new Ellipse2D.Double(
                        x,
                        y,
                        tamanho,
                        tamanho / 2.0
                ));
            }
        }

        private void desenharFlor(
                Graphics2D g2,
                int x,
                int y,
                int raio
        ) {

            g2.setColor(
                    new Color(255, 216, 230, 220)
            );

            for (int i = 0; i < 5; i++) {

                double angulo =
                        Math.toRadians(i * 72);

                double px =
                        x + Math.cos(angulo) * raio * 0.65;

                double py =
                        y + Math.sin(angulo) * raio * 0.65;

                g2.fill(new Ellipse2D.Double(
                        px - raio * 0.55,
                        py - raio * 0.55,
                        raio * 1.1,
                        raio * 1.1
                ));
            }

            g2.setColor(ROSA_MEDIO);

            g2.fillOval(
                    x - raio / 5,
                    y - raio / 5,
                    raio * 2 / 5,
                    raio * 2 / 5
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new TelaInicial().setVisible(true)
        );
    }
}
