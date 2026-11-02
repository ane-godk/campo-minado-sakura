
package br.com.sakura.campominado.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaTutorial extends JDialog {

    public TelaTutorial(JFrame janelaPai) {
        super(janelaPai, "Tutorial - Campo Minado Sakura", true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(
                new BorderLayout(0, 16)
        );

        principal.setBackground(TemaSakura.FUNDO);
        principal.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel(
                "✿ COMO JOGAR ✿",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 22)
        );

        titulo.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        principal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel conteudo = new JPanel();

        conteudo.setOpaque(false);

        conteudo.setLayout(
                new BoxLayout(
                        conteudo,
                        BoxLayout.Y_AXIS
                )
        );

        adicionarSecao(
                conteudo,
                "OBJETIVO",
                "Revele todas as células que não possuem minas."
        );

        adicionarSecao(
                conteudo,
                "CONTROLES",
                "Clique esquerdo: revelar uma célula.\n"
                        + "Clique direito: colocar ou retirar bandeira."
        );

        adicionarSecao(
                conteudo,
                "NÚMEROS",
                "Os números indicam quantas minas existem "
                        + "nas oito células ao redor."
        );

        adicionarSecao(
                conteudo,
                "VIDAS",
                "Você começa com três vidas.\n"
                        + "Revelar uma mina faz perder uma vida."
        );

        adicionarSecao(
                conteudo,
                "DICAS",
                "Você possui três dicas por partida.\n"
                        + "Uma dica destaca uma jogada lógica no tabuleiro.\n"
                        + "Se não houver dedução segura, nenhuma dica é consumida."
        );

        adicionarSecao(
                conteudo,
                "AUTOSOLVE",
                "No menu de pausa, você pode iniciar "
                        + "a resolução automática da partida.\n"
                        + "O sistema realiza jogadas até terminar "
                        + "ou ser interrompido."
        );

        adicionarSecao(
                conteudo,
                "PONTUAÇÃO",
                "Você ganha pontos ao revelar células "
                        + "e identificar minas.\n"
                        + "Tempo, vidas restantes e dificuldade "
                        + "influenciam o resultado."
        );

        adicionarSecao(
                conteudo,
                "RANKING",
                "As vitórias podem ser registradas no Ranking Sakura.\n"
                        + "Consulte os melhores resultados pelo menu de pausa."
        );

        JScrollPane rolagem = new JScrollPane(
                conteudo
        );

        rolagem.setBorder(null);
        rolagem.setOpaque(false);

        rolagem.getViewport().setOpaque(false);

        rolagem.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        rolagem.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        rolagem.getVerticalScrollBar()
                .setUnitIncrement(18);

        principal.add(
                rolagem,
                BorderLayout.CENTER
        );

        JButton fechar =
                TemaSakura.criarBotao("Entendi");

        fechar.setPreferredSize(
                new Dimension(160, 44)
        );

        fechar.addActionListener(
                evento -> dispose()
        );

        JPanel rodape = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER
                )
        );

        rodape.setOpaque(false);
        rodape.add(fechar);

        principal.add(
                rodape,
                BorderLayout.SOUTH
        );

        getRootPane().setDefaultButton(fechar);

        setContentPane(principal);

        setSize(570, 620);
        setMinimumSize(new Dimension(400, 350));

        setLocationRelativeTo(janelaPai);
    }

    private void adicionarSecao(
            JPanel conteudo,
            String titulo,
            String descricao
    ) {
        JPanel cartao = new JPanel(
                new BorderLayout(0, 9)
        );

        cartao.setBackground(
                TemaSakura.BRANCO
        );

        cartao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                14,
                                16,
                                14,
                                16
                        )
                )
        );

        JLabel labelTitulo = new JLabel(
                "✿ " + titulo
        );

        labelTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        labelTitulo.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        JTextArea texto = new JTextArea(
                descricao
        );

        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setOpaque(false);

        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);

        texto.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        texto.setForeground(
                TemaSakura.TEXTO
        );

        texto.setColumns(35);

        cartao.add(
                labelTitulo,
                BorderLayout.NORTH
        );

        cartao.add(
                texto,
                BorderLayout.CENTER
        );

        cartao.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        cartao.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        160
                )
        );

        conteudo.add(cartao);

        conteudo.add(
                Box.createVerticalStrut(10)
        );
    }

    public void mostrar() {
        setVisible(true);
    }
}
