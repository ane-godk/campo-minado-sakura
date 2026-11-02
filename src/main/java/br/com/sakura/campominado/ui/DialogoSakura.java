
package br.com.sakura.campominado.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogoSakura extends JDialog {

    public enum Tipo {
        INFORMACAO,
        AVISO,
        ERRO,
        CONFIRMACAO
    }

    private boolean confirmado;

    private DialogoSakura(
            Window janelaPai,
            String titulo,
            String mensagem,
            Tipo tipo
    ) {
        super(janelaPai, titulo, ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel principal = new JPanel(
                new BorderLayout(0, 18)
        );

        principal.setBackground(TemaSakura.FUNDO);
        principal.setBorder(
                new EmptyBorder(24, 28, 22, 28)
        );

        JLabel labelTitulo = new JLabel(
                "✿ " + titulo + " ✿",
                SwingConstants.CENTER
        );

        labelTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        labelTitulo.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        principal.add(
                labelTitulo,
                BorderLayout.NORTH
        );

        JPanel conteudo = new JPanel(
                new BorderLayout(0, 12)
        );

        conteudo.setOpaque(false);

        JLabel icone = new JLabel(
                obterSimbolo(tipo),
                SwingConstants.CENTER
        );

        icone.setFont(
                new Font("SansSerif", Font.BOLD, 32)
        );

        icone.setForeground(
                obterCor(tipo)
        );

        conteudo.add(
                icone,
                BorderLayout.NORTH
        );

        JTextArea texto = new JTextArea(mensagem);

        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setOpaque(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);

        texto.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        texto.setForeground(TemaSakura.TEXTO);
        texto.setBorder(null);

        texto.setColumns(30);

        conteudo.add(
                texto,
                BorderLayout.CENTER
        );

        principal.add(
                conteudo,
                BorderLayout.CENTER
        );

        JPanel botoes = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        12,
                        0
                )
        );

        botoes.setOpaque(false);

        if (tipo == Tipo.CONFIRMACAO) {
            JButton cancelar =
                    TemaSakura.criarBotao("Cancelar");

            JButton confirmar =
                    TemaSakura.criarBotao("Confirmar");

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
                confirmado = true;
                dispose();
            });

            botoes.add(cancelar);
            botoes.add(confirmar);

            getRootPane().setDefaultButton(
                    cancelar
            );

        } else {
            JButton fechar =
                    TemaSakura.criarBotao("Entendi");

            fechar.setPreferredSize(
                    new Dimension(150, 42)
            );

            fechar.addActionListener(
                    evento -> dispose()
            );

            botoes.add(fechar);

            getRootPane().setDefaultButton(
                    fechar
            );
        }

        principal.add(
                botoes,
                BorderLayout.SOUTH
        );

        setContentPane(principal);

        pack();

        int largura = Math.max(
                390,
                getWidth()
        );

        int altura = Math.max(
                270,
                getHeight()
        );

        setSize(
                Math.min(largura, 520),
                Math.min(altura, 400)
        );

        setLocationRelativeTo(janelaPai);
    }

    private String obterSimbolo(Tipo tipo) {
        return switch (tipo) {
            case INFORMACAO -> "✿";
            case AVISO -> "!";
            case ERRO -> "×";
            case CONFIRMACAO -> "?";
        };
    }

    private Color obterCor(Tipo tipo) {
        return switch (tipo) {
            case INFORMACAO -> TemaSakura.ROSA;
            case AVISO -> new Color(194, 131, 58);
            case ERRO -> new Color(191, 77, 100);
            case CONFIRMACAO -> TemaSakura.ROSA_ESCURO;
        };
    }

    public static void informar(
            Window janelaPai,
            String titulo,
            String mensagem
    ) {
        DialogoSakura dialogo = new DialogoSakura(
                janelaPai,
                titulo,
                mensagem,
                Tipo.INFORMACAO
        );

        dialogo.setVisible(true);
    }

    public static void avisar(
            Window janelaPai,
            String titulo,
            String mensagem
    ) {
        DialogoSakura dialogo = new DialogoSakura(
                janelaPai,
                titulo,
                mensagem,
                Tipo.AVISO
        );

        dialogo.setVisible(true);
    }

    public static void erro(
            Window janelaPai,
            String titulo,
            String mensagem
    ) {
        DialogoSakura dialogo = new DialogoSakura(
                janelaPai,
                titulo,
                mensagem,
                Tipo.ERRO
        );

        dialogo.setVisible(true);
    }

    public static boolean confirmar(
            Window janelaPai,
            String titulo,
            String mensagem
    ) {
        DialogoSakura dialogo = new DialogoSakura(
                janelaPai,
                titulo,
                mensagem,
                Tipo.CONFIRMACAO
        );

        dialogo.setVisible(true);

        return dialogo.confirmado;
    }
}
