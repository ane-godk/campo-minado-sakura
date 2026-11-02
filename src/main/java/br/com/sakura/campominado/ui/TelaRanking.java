
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.RegistroPartida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaRanking extends JDialog {

    public TelaRanking(
            JFrame janelaPai,
            List<RegistroPartida> registros
    ) {
        super(janelaPai, "Ranking Sakura", true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(480, 400));

        JPanel principal = new JPanel(
                new BorderLayout(0, 16)
        );

        principal.setBackground(TemaSakura.FUNDO);
        principal.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel(
                "✿ RANKING SAKURA ✿",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font(
                "SansSerif", Font.BOLD, 22
        ));
        titulo.setForeground(TemaSakura.ROSA_ESCURO);

        JLabel subtitulo = new JLabel(
                "As 10 melhores partidas",
                SwingConstants.CENTER
        );

        subtitulo.setFont(new Font(
                "SansSerif", Font.PLAIN, 13
        ));
        subtitulo.setForeground(TemaSakura.TEXTO);

        JPanel cabecalho = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );
        cabecalho.setOpaque(false);
        cabecalho.add(titulo);
        cabecalho.add(subtitulo);

        principal.add(cabecalho, BorderLayout.NORTH);

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(
                lista, BoxLayout.Y_AXIS
        ));

        if (registros == null || registros.isEmpty()) {
            JLabel vazio = new JLabel(
                    "Nenhuma vitória registrada ainda.",
                    SwingConstants.CENTER
            );

            vazio.setFont(TemaSakura.FONTE);
            vazio.setForeground(TemaSakura.TEXTO);
            vazio.setAlignmentX(Component.CENTER_ALIGNMENT);

            lista.add(Box.createVerticalStrut(65));
            lista.add(vazio);
        } else {
            int limite = Math.min(10, registros.size());

            for (int i = 0; i < limite; i++) {
                lista.add(criarCartao(
                        i + 1, registros.get(i)
                ));

                lista.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane rolagem = new JScrollPane(lista);
        rolagem.setBorder(null);
        rolagem.setOpaque(false);
        rolagem.getViewport().setOpaque(false);
        rolagem.getVerticalScrollBar()
                .setUnitIncrement(18);

        principal.add(rolagem, BorderLayout.CENTER);

        JButton fechar = TemaSakura.criarBotao(
                "Fechar"
        );

        fechar.setPreferredSize(
                new Dimension(160, 42)
        );

        fechar.addActionListener(
                evento -> dispose()
        );

        JPanel rodape = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );
        rodape.setOpaque(false);
        rodape.add(fechar);

        principal.add(rodape, BorderLayout.SOUTH);

        setContentPane(principal);
        setSize(540, 570);
        setLocationRelativeTo(janelaPai);
    }

    private JPanel criarCartao(
            int posicao,
            RegistroPartida registro
    ) {
        JPanel cartao = new JPanel(
                new BorderLayout(12, 5)
        );

        cartao.setBackground(TemaSakura.BRANCO);

        cartao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        JLabel numero = new JLabel(
                String.format("%02d", posicao),
                SwingConstants.CENTER
        );

        numero.setFont(new Font(
                "SansSerif", Font.BOLD, 20
        ));
        numero.setForeground(TemaSakura.ROSA_ESCURO);
        numero.setPreferredSize(new Dimension(42, 45));

        JPanel dados = new JPanel(
                new GridLayout(2, 1, 0, 4)
        );
        dados.setOpaque(false);

        JLabel jogador = new JLabel(
                registro.jogador()
        );
        jogador.setFont(new Font(
                "SansSerif", Font.BOLD, 14
        ));
        jogador.setForeground(TemaSakura.ROSA_ESCURO);

        JLabel detalhes = new JLabel(
                registro.dificuldade()
                        + "  |  "
                        + registro.tempoSegundos()
                        + "s"
        );

        detalhes.setFont(new Font(
                "SansSerif", Font.PLAIN, 12
        ));
        detalhes.setForeground(TemaSakura.TEXTO);

        dados.add(jogador);
        dados.add(detalhes);

        JLabel pontos = new JLabel(
                registro.pontuacao() + " pts"
        );
        pontos.setFont(new Font(
                "SansSerif", Font.BOLD, 14
        ));
        pontos.setForeground(TemaSakura.ROSA_ESCURO);

        cartao.add(numero, BorderLayout.WEST);
        cartao.add(dados, BorderLayout.CENTER);
        cartao.add(pontos, BorderLayout.EAST);

        cartao.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 75
        ));

        return cartao;
    }

    public void mostrar() {
        setVisible(true);
    }
}
