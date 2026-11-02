
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Dificuldade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaFimPartida extends JDialog {

    public enum Acao {
        JOGAR_NOVAMENTE,
        MUDAR_DIFICULDADE,
        VOLTAR_MENU
    }

    private Acao acaoSelecionada;
    private Dificuldade dificuldadeSelecionada;

    private final JComboBox<Dificuldade> seletorDificuldade =
            new JComboBox<>(new Dificuldade[]{
                    Dificuldade.FACIL,
                    Dificuldade.MEDIO,
                    Dificuldade.DIFICIL,
                    Dificuldade.INFERNO
            });

    public TelaFimPartida(
            JFrame janelaPai,
            boolean venceu,
            String jogador,
            Dificuldade dificuldade,
            int segundos,
            int pontos,
            boolean rankingSalvo
    ) {
        super(janelaPai, "Resultado Sakura", true);

        this.dificuldadeSelecionada = dificuldade;

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel fundo = new JPanel(new BorderLayout(0, 18));
        fundo.setBackground(new Color(255, 231, 241));
        fundo.setBorder(new EmptyBorder(24, 26, 24, 26));
        setContentPane(fundo);

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(
                new BoxLayout(cabecalho, BoxLayout.Y_AXIS)
        );

        JLabel flores = criarTexto(
                "✿  ✿  ✿",
                27,
                Font.BOLD
        );

        JLabel titulo = criarTexto(
                venceu ? "Vitória Sakura!" : "Fim de Partida",
                33,
                Font.BOLD
        );

        JLabel subtitulo = criarTexto(
                venceu
                        ? "Todas as células seguras foram reveladas!"
                        : "As vidas acabaram. Tente novamente!",
                13,
                Font.PLAIN
        );

        cabecalho.add(flores);
        cabecalho.add(Box.createVerticalStrut(9));
        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(8));
        cabecalho.add(subtitulo);
        cabecalho.add(Box.createVerticalStrut(22));

        JPanel informacoes = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );
        informacoes.setOpaque(false);

        informacoes.add(criarCartao(
                "Jogador", jogador
        ));
        informacoes.add(criarCartao(
                "Dificuldade", dificuldade.getNome()
        ));
        informacoes.add(criarCartao(
                "Tempo", segundos + "s"
        ));
        informacoes.add(criarCartao(
                "Pontuação", String.valueOf(pontos)
        ));

        cabecalho.add(informacoes);
        fundo.add(cabecalho, BorderLayout.NORTH);

        JPanel acoes = new JPanel();
        acoes.setOpaque(false);
        acoes.setLayout(
                new BoxLayout(acoes, BoxLayout.Y_AXIS)
        );

        if (venceu) {
            JLabel ranking = criarTexto(
                    rankingSalvo
                            ? "✿ Resultado salvo no ranking!"
                            : "Resultado concluído.",
                    12,
                    Font.BOLD
            );

            acoes.add(ranking);
            acoes.add(Box.createVerticalStrut(12));
        }

        JButton jogarNovamente =
                criarBotao("Jogar novamente");

        JButton mudarDificuldade =
                criarBotao("Mudar dificuldade");

        JButton voltarMenu =
                criarBotao("Voltar ao menu inicial");

        adicionarBotao(acoes, jogarNovamente);
        adicionarBotao(acoes, mudarDificuldade);
        adicionarBotao(acoes, voltarMenu);

        jogarNovamente.addActionListener(e ->
                selecionar(Acao.JOGAR_NOVAMENTE)
        );

        mudarDificuldade.addActionListener(e ->
                escolherDificuldade()
        );

        voltarMenu.addActionListener(e ->
                selecionar(Acao.VOLTAR_MENU)
        );

        fundo.add(acoes, BorderLayout.CENTER);

        pack();

        setMinimumSize(new Dimension(420, 490));
        setLocationRelativeTo(janelaPai);
    }

    private JLabel criarTexto(
            String texto,
            int tamanho,
            int estilo
    ) {
        JLabel label = new JLabel(
                texto,
                SwingConstants.CENTER
        );

        label.setFont(new Font(
                "Serif", estilo, tamanho
        ));

        label.setForeground(TemaSakura.ROSA_ESCURO);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private JPanel criarCartao(
            String titulo,
            String valor
    ) {
        JPanel cartao = new JPanel();
        cartao.setLayout(
                new BoxLayout(cartao, BoxLayout.Y_AXIS)
        );

        cartao.setBackground(TemaSakura.BRANCO);

        cartao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA, 1, true
                        ),
                        new EmptyBorder(12, 8, 12, 8)
                )
        );

        JLabel nome = new JLabel(
                titulo,
                SwingConstants.CENTER
        );
        nome.setFont(new Font(
                "SansSerif", Font.PLAIN, 12
        ));
        nome.setForeground(TemaSakura.TEXTO);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel conteudo = new JLabel(
                valor,
                SwingConstants.CENTER
        );
        conteudo.setFont(new Font(
                "SansSerif", Font.BOLD, 14
        ));
        conteudo.setForeground(
                TemaSakura.ROSA_ESCURO
        );
        conteudo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cartao.add(nome);
        cartao.add(Box.createVerticalStrut(6));
        cartao.add(conteudo);

        return cartao;
    }

    private JButton criarBotao(String texto) {
        JButton botao = TemaSakura.criarBotao(texto);

        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        botao.setPreferredSize(
                new Dimension(340, 48)
        );

        botao.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
        );

        return botao;
    }

    private void adicionarBotao(
            JPanel painel,
            JButton botao
    ) {
        painel.add(botao);
        painel.add(Box.createVerticalStrut(13));
    }

    private void escolherDificuldade() {
        seletorDificuldade.setSelectedItem(
                dificuldadeSelecionada
        );

        TemaSakura.estilizarSeletor(
                seletorDificuldade
        );

        int resposta = JOptionPane.showConfirmDialog(
                this,
                seletorDificuldade,
                "Dificuldade da próxima partida",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        Dificuldade escolhida =
                (Dificuldade) seletorDificuldade
                        .getSelectedItem();

        if (escolhida != null) {
            dificuldadeSelecionada = escolhida;
            selecionar(Acao.MUDAR_DIFICULDADE);
        }
    }

    private void selecionar(Acao acao) {
        acaoSelecionada = acao;
        dispose();
    }

    public Acao mostrar() {
        setVisible(true);
        return acaoSelecionada;
    }

    public Dificuldade getDificuldadeSelecionada() {
        return dificuldadeSelecionada;
    }
}
