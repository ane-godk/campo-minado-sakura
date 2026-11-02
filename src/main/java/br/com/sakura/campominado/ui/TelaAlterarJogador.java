
package br.com.sakura.campominado.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaAlterarJogador extends JDialog {

    private final JTextField campoNome;
    private String nomeConfirmado;

    public TelaAlterarJogador(
            JFrame janelaPai,
            String nomeAtual
    ) {
        super(janelaPai, "Alterar jogador", true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(
                new BorderLayout(0, 18)
        );

        principal.setBackground(TemaSakura.FUNDO);
        principal.setBorder(
                new EmptyBorder(22, 24, 22, 24)
        );

        JLabel titulo = new JLabel(
                "✿ ALTERAR JOGADOR ✿",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font(
                "SansSerif", Font.BOLD, 20
        ));
        titulo.setForeground(TemaSakura.ROSA_ESCURO);

        principal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(
                new BorderLayout(0, 8)
        );
        formulario.setOpaque(false);

        JLabel instrucao = new JLabel(
                "Como deseja ser chamado?"
        );
        instrucao.setFont(TemaSakura.FONTE);
        instrucao.setForeground(TemaSakura.TEXTO);

        campoNome = new JTextField(
                nomeAtual == null ? "" : nomeAtual
        );

        campoNome.setFont(new Font(
                "SansSerif", Font.PLAIN, 15
        ));
        campoNome.setForeground(TemaSakura.TEXTO);
        campoNome.setBackground(TemaSakura.BRANCO);
        campoNome.setCaretColor(TemaSakura.ROSA_ESCURO);

        campoNome.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA,
                                2,
                                true
                        ),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        JLabel aviso = new JLabel(" ");
        aviso.setFont(new Font(
                "SansSerif", Font.PLAIN, 12
        ));
        aviso.setForeground(new Color(180, 60, 85));

        formulario.add(
                instrucao, BorderLayout.NORTH
        );
        formulario.add(
                campoNome, BorderLayout.CENTER
        );
        formulario.add(
                aviso, BorderLayout.SOUTH
        );

        principal.add(
                formulario, BorderLayout.CENTER
        );

        JButton cancelar = TemaSakura.criarBotao(
                "Cancelar"
        );
        JButton confirmar = TemaSakura.criarBotao(
                "Confirmar"
        );

        cancelar.setPreferredSize(
                new Dimension(135, 42)
        );
        confirmar.setPreferredSize(
                new Dimension(135, 42)
        );

        cancelar.addActionListener(
                evento -> dispose()
        );

        confirmar.addActionListener(evento -> {
            String nome = campoNome.getText().trim();

            if (nome.isBlank()) {
                aviso.setText(
                        "Digite um nome para continuar."
                );
                campoNome.requestFocusInWindow();
                return;
            }

            if (nome.length() > 30) {
                aviso.setText(
                        "Use no máximo 30 caracteres."
                );
                campoNome.requestFocusInWindow();
                return;
            }

            nomeConfirmado = nome;
            dispose();
        });

        JPanel botoes = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 12, 0
                )
        );
        botoes.setOpaque(false);
        botoes.add(cancelar);
        botoes.add(confirmar);

        principal.add(
                botoes, BorderLayout.SOUTH
        );

        getRootPane().setDefaultButton(confirmar);

        setContentPane(principal);
        setSize(410, 275);
        setLocationRelativeTo(janelaPai);
    }

    public String mostrar() {
        SwingUtilities.invokeLater(
                campoNome::requestFocusInWindow
        );

        setVisible(true);
        return nomeConfirmado;
    }
}
