
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.ConfiguracaoJogo;
import br.com.sakura.campominado.domain.Dificuldade;
import br.com.sakura.campominado.domain.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private Tabuleiro tabuleiro;
    private PainelTabuleiro painelTabuleiro;

    private final JPanel painelPrincipal;
    private final JPanel areaTabuleiro;

    private final JLabel labelVidas;
    private final JLabel labelMinas;

    private final JComboBox<Dificuldade> seletorDificuldade;

    public TelaPrincipal() {

        setTitle("Campo Minado - Sakura");
        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(new Dimension(450, 450));

        painelPrincipal = new JPanel(
                new BorderLayout(8, 8)
        );

        painelPrincipal.setBackground(
                new Color(255, 253, 240)
        );

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        JPanel painelSuperior = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        5
                )
        );

        painelSuperior.setOpaque(false);

        seletorDificuldade = new JComboBox<>(
                new Dificuldade[]{
                        Dificuldade.FACIL,
                        Dificuldade.MEDIO,
                        Dificuldade.DIFICIL,
                        Dificuldade.INFERNO
                }
        );

        JButton botaoReiniciar =
                new JButton("Reiniciar");

        labelVidas = new JLabel();
        labelMinas = new JLabel();

        painelSuperior.add(
                new JLabel("Dificuldade:")
        );

        painelSuperior.add(seletorDificuldade);
        painelSuperior.add(botaoReiniciar);

        JPanel painelInformacoes = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        20,
                        5
                )
        );

        painelInformacoes.setOpaque(false);
        painelInformacoes.add(labelVidas);
        painelInformacoes.add(labelMinas);

        JPanel cabecalho = new JPanel(
                new BorderLayout()
        );

        cabecalho.setOpaque(false);
        cabecalho.add(
                painelSuperior,
                BorderLayout.NORTH
        );

        cabecalho.add(
                painelInformacoes,
                BorderLayout.SOUTH
        );

        areaTabuleiro = new JPanel(
                new BorderLayout()
        );

        painelPrincipal.add(
                cabecalho,
                BorderLayout.NORTH
        );

        painelPrincipal.add(
                areaTabuleiro,
                BorderLayout.CENTER
        );

        setContentPane(painelPrincipal);

        botaoReiniciar.addActionListener(
                evento -> iniciarNovaPartida()
        );

        seletorDificuldade.addActionListener(
                evento -> iniciarNovaPartida()
        );

        iniciarNovaPartida();
        setLocationRelativeTo(null);
    }

    private void iniciarNovaPartida() {

        Dificuldade dificuldade =
                (Dificuldade) seletorDificuldade
                        .getSelectedItem();

        if (dificuldade == null) {
            return;
        }

        ConfiguracaoJogo configuracao =
                new ConfiguracaoJogo(
                        dificuldade.getLinhas(),
                        dificuldade.getColunas(),
                        dificuldade.getMinas(),
                        3,
                        0,
                        dificuldade,
                        "Jogador"
                );

        tabuleiro = new Tabuleiro(configuracao);

        painelTabuleiro = new PainelTabuleiro(
                tabuleiro,
                this::atualizarInformacoes
        );

        areaTabuleiro.removeAll();

        areaTabuleiro.add(
                painelTabuleiro,
                BorderLayout.CENTER
        );

        atualizarInformacoes();

        int largura = Math.max(
                450,
                dificuldade.getColunas() * 30 + 40
        );

        int altura = Math.max(
                450,
                dificuldade.getLinhas() * 30 + 130
        );

        setSize(largura, altura);
        setLocationRelativeTo(null);

        areaTabuleiro.revalidate();
        areaTabuleiro.repaint();
    }

    private void atualizarInformacoes() {

        labelVidas.setText(
                "Vidas: " + tabuleiro.getVidas()
        );

        labelMinas.setText(
                "Minas restantes: "
                        + tabuleiro.getMinasRestantes()
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new TelaPrincipal()
                        .setVisible(true)
        );
    }
}
