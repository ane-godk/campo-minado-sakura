
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Dificuldade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaDificuldade extends JDialog {

    private final JComboBox<Dificuldade> seletor;
    private Dificuldade dificuldadeConfirmada;

    public TelaDificuldade(
            JFrame janelaPai,
            Dificuldade dificuldadeAtual
    ) {
        super(janelaPai, "Escolher dificuldade", true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(
                new BorderLayout(0, 20)
        );

        principal.setBackground(TemaSakura.FUNDO);
        principal.setBorder(
                new EmptyBorder(25, 28, 25, 28)
        );

        JLabel titulo = new JLabel(
                "✿ ESCOLHER DIFICULDADE ✿",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );
        titulo.setForeground(TemaSakura.ROSA_ESCURO);

        JLabel subtitulo = new JLabel(
                "Selecione o desafio da sua partida",
                SwingConstants.CENTER
        );

        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );
        subtitulo.setForeground(TemaSakura.TEXTO);

        JPanel cabecalho = new JPanel(
                new GridLayout(2, 1, 0, 7)
        );
        cabecalho.setOpaque(false);
        cabecalho.add(titulo);
        cabecalho.add(subtitulo);

        principal.add(cabecalho, BorderLayout.NORTH);

        seletor = new JComboBox<>(
                new Dificuldade[]{
                        Dificuldade.FACIL,
                        Dificuldade.MEDIO,
                        Dificuldade.DIFICIL,
                        Dificuldade.INFERNO
                }
        );

        seletor.setSelectedItem(
                dificuldadeAtual == null
                        ? Dificuldade.FACIL
                        : dificuldadeAtual
        );

        TemaSakura.estilizarSeletor(seletor);

        seletor.setPreferredSize(
                new Dimension(320, 44)
        );

        JLabel detalhes = new JLabel(
                "",
                SwingConstants.CENTER
        );

        detalhes.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );
        detalhes.setForeground(TemaSakura.ROSA_ESCURO);

        seletor.addActionListener(
                evento -> atualizarDetalhes(detalhes)
        );

        JPanel centro = new JPanel(
                new BorderLayout(0, 16)
        );
        centro.setOpaque(false);

        centro.add(seletor, BorderLayout.NORTH);
        centro.add(detalhes, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        atualizarDetalhes(detalhes);

        JButton cancelar =
                TemaSakura.criarBotao("Cancelar");

        JButton confirmar =
                TemaSakura.criarBotao("Confirmar");

        cancelar.setPreferredSize(
                new Dimension(140, 42)
        );

        confirmar.setPreferredSize(
                new Dimension(140, 42)
        );

        cancelar.addActionListener(
                evento -> dispose()
        );

        confirmar.addActionListener(evento -> {
            dificuldadeConfirmada =
                    (Dificuldade) seletor.getSelectedItem();

            dispose();
        });

        JPanel rodape = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 12, 0
                )
        );

        rodape.setOpaque(false);
        rodape.add(cancelar);
        rodape.add(confirmar);

        principal.add(rodape, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(confirmar);

        setContentPane(principal);
        pack();

        setSize(
                Math.max(430, getWidth()),
                Math.max(290, getHeight())
        );

        setLocationRelativeTo(janelaPai);
    }

    private void atualizarDetalhes(JLabel label) {
        Dificuldade dificuldade =
                (Dificuldade) seletor.getSelectedItem();

        if (dificuldade == null) {
            label.setText(" ");
            return;
        }

        label.setText(
                dificuldade.getLinhas()
                        + " × "
                        + dificuldade.getColunas()
                        + " células  |  "
                        + dificuldade.getMinas()
                        + " minas"
        );
    }

    public Dificuldade mostrar() {
        setVisible(true);
        return dificuldadeConfirmada;
    }
}
