
package br.com.sakura.campominado.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TelaPausa extends JDialog {

    public enum Acao {
        CONTINUAR,
        AUTOSOLVE,
        ALTERAR_JOGADOR,
        RANKING,
        REINICIAR,
        VOLTAR_MENU
    }

    private Acao acaoSelecionada = Acao.CONTINUAR;

    public TelaPausa(
            JFrame janelaPai,
            String jogador,
            String dificuldade,
            int segundos,
            boolean autosolveAtivo
    ) {

        super(janelaPai, "Pausa Sakura", true);

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        setResizable(false);

        JPanel fundo = new JPanel(
                new BorderLayout(0, 18)
        );

        fundo.setBackground(
                new Color(255, 231, 241)
        );

        fundo.setBorder(
                new EmptyBorder(22, 26, 22, 26)
        );

        setContentPane(fundo);

        criarCabecalho(
                fundo,
                jogador,
                dificuldade,
                segundos
        );

        criarBotoes(
                fundo,
                autosolveAtivo
        );

        pack();

        setMinimumSize(
                new Dimension(420, 500)
        );

        setLocationRelativeTo(janelaPai);
    }

    private void criarCabecalho(
            JPanel fundo,
            String jogador,
            String dificuldade,
            int segundos
    ) {

        JPanel cabecalho = new JPanel();

        cabecalho.setOpaque(false);

        cabecalho.setLayout(
                new BoxLayout(
                        cabecalho,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel flores = new JLabel(
                "✿  ✿  ✿",
                SwingConstants.CENTER
        );

        flores.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        26
                )
        );

        flores.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        flores.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel titulo = new JLabel(
                "Jogo Pausado",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        34
                )
        );

        titulo.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitulo = new JLabel(
                "CAMPO MINADO SAKURA",
                SwingConstants.CENTER
        );

        subtitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        subtitulo.setForeground(
                TemaSakura.TEXTO
        );

        subtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cabecalho.add(flores);
        cabecalho.add(
                Box.createVerticalStrut(5)
        );
        cabecalho.add(titulo);
        cabecalho.add(
                Box.createVerticalStrut(5)
        );
        cabecalho.add(subtitulo);
        cabecalho.add(
                Box.createVerticalStrut(20)
        );

        JPanel informacoes = new JPanel(
                new GridLayout(1, 3, 9, 0)
        );

        informacoes.setOpaque(false);

        informacoes.add(
                criarCartao(
                        "Jogador",
                        jogador
                )
        );

        informacoes.add(
                criarCartao(
                        "Dificuldade",
                        dificuldade
                )
        );

        informacoes.add(
                criarCartao(
                        "Tempo",
                        segundos + "s"
                )
        );

        cabecalho.add(informacoes);

        fundo.add(
                cabecalho,
                BorderLayout.NORTH
        );
    }

    private JPanel criarCartao(
            String titulo,
            String valor
    ) {

        JPanel cartao = new JPanel();

        cartao.setLayout(
                new BoxLayout(
                        cartao,
                        BoxLayout.Y_AXIS
                )
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
                                12,
                                6,
                                12,
                                6
                        )
                )
        );

        JLabel nome = new JLabel(
                titulo,
                SwingConstants.CENTER
        );

        nome.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        nome.setForeground(
                TemaSakura.TEXTO
        );

        nome.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel conteudo = new JLabel(
                valor,
                SwingConstants.CENTER
        );

        conteudo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        conteudo.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        conteudo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cartao.add(nome);
        cartao.add(
                Box.createVerticalStrut(5)
        );
        cartao.add(conteudo);

        return cartao;
    }

    private void criarBotoes(
            JPanel fundo,
            boolean autosolveAtivo
    ) {

        JPanel botoes = new JPanel();

        botoes.setOpaque(false);

        botoes.setLayout(
                new BoxLayout(
                        botoes,
                        BoxLayout.Y_AXIS
                )
        );

        JButton continuar =
                criarBotao(
                        "Continuar partida"
                );

        JButton autosolve =
                criarBotao(
                        autosolveAtivo
                                ? "Parar Autosolve"
                                : "Autosolve"
                );

        JButton jogador =
                criarBotao(
                        "Alterar jogador"
                );

        JButton ranking =
                criarBotao(
                        "Ranking"
                );

        JButton reiniciar =
                criarBotao(
                        "Reiniciar partida"
                );

        JButton voltar =
                criarBotao(
                        "Voltar ao menu inicial"
                );

        adicionarBotao(
                botoes,
                continuar
        );

        adicionarBotao(
                botoes,
                autosolve
        );

        adicionarBotao(
                botoes,
                jogador
        );

        adicionarBotao(
                botoes,
                ranking
        );

        adicionarBotao(
                botoes,
                reiniciar
        );

        adicionarBotao(
                botoes,
                voltar
        );

        continuar.addActionListener(
                e -> selecionar(
                        Acao.CONTINUAR
                )
        );

        autosolve.addActionListener(
                e -> selecionar(
                        Acao.AUTOSOLVE
                )
        );

        jogador.addActionListener(
                e -> selecionar(
                        Acao.ALTERAR_JOGADOR
                )
        );

        ranking.addActionListener(
                e -> selecionar(
                        Acao.RANKING
                )
        );

        reiniciar.addActionListener(
                e -> selecionar(
                        Acao.REINICIAR
                )
        );

        voltar.addActionListener(
                e -> selecionar(
                        Acao.VOLTAR_MENU
                )
        );

        fundo.add(
                botoes,
                BorderLayout.CENTER
        );
    }

    private JButton criarBotao(
            String texto
    ) {

        JButton botao =
                TemaSakura.criarBotao(texto);

        botao.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        botao.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        47
                )
        );

        botao.setPreferredSize(
                new Dimension(
                        340,
                        47
                )
        );

        return botao;
    }

    private void adicionarBotao(
            JPanel painel,
            JButton botao
    ) {

        painel.add(botao);

        painel.add(
                Box.createVerticalStrut(10)
        );
    }

    private void selecionar(
            Acao acao
    ) {

        acaoSelecionada = acao;
        dispose();
    }

    public Acao mostrar() {

        setVisible(true);

        return acaoSelecionada;
    }
}
