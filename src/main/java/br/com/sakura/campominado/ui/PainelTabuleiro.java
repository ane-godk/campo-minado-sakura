
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Celula;
import br.com.sakura.campominado.domain.Tabuleiro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PainelTabuleiro extends JPanel {

    private final Tabuleiro tabuleiro;
    private final JButton[][] botoes;
    private final Runnable aoAtualizar;
    private boolean resultadoExibido;

    public PainelTabuleiro(
            Tabuleiro tabuleiro,
            Runnable aoAtualizar) {

        this.tabuleiro = tabuleiro;
        this.aoAtualizar = aoAtualizar;

        botoes = new JButton[
                tabuleiro.getLinhas()
        ][tabuleiro.getColunas()];

        setLayout(new GridLayout(
                tabuleiro.getLinhas(),
                tabuleiro.getColunas(),
                1,
                1
        ));

        setBackground(new Color(220, 190, 200));

        for (int linha = 0;
             linha < tabuleiro.getLinhas();
             linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas();
                 coluna++) {

                final int l = linha;
                final int c = coluna;

                JButton botao = new JButton();

                botao.setFocusPainted(false);
                botao.setMargin(new Insets(0, 0, 0, 0));
                botao.setFont(new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                ));

                botao.setBackground(
                        new Color(255, 240, 245)
                );

                botao.addMouseListener(
                        new MouseAdapter() {

                            @Override
                            public void mousePressed(
                                    MouseEvent evento) {

                                processarClique(evento, l, c);
                            }
                        }
                );

                botoes[linha][coluna] = botao;
                add(botao);
            }
        }

        atualizarTabuleiro();
    }

    // Mantém compatibilidade com o construtor anterior.
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        this(tabuleiro, () -> {});
    }

    private void processarClique(
            MouseEvent evento,
            int linha,
            int coluna) {

        if (tabuleiro.isJogoEncerrado()) {
            return;
        }

        boolean alterou = false;

        if (SwingUtilities.isLeftMouseButton(evento)) {

            alterou = tabuleiro.revelar(
                    linha,
                    coluna
            );

        } else if (
                SwingUtilities.isRightMouseButton(evento)) {

            alterou = tabuleiro.alternarBandeira(
                    linha,
                    coluna
            );
        }

        if (!alterou) {
            return;
        }

        atualizarTabuleiro();
        aoAtualizar.run();

        if (tabuleiro.isJogoEncerrado()
                && !resultadoExibido) {

            resultadoExibido = true;

            String mensagem = tabuleiro.isVenceu()
                    ? "Parabéns! Você venceu!"
                    : "Fim de jogo! Suas vidas acabaram.";

            JOptionPane.showMessageDialog(
                    this,
                    mensagem,
                    "Campo Minado Sakura",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void atualizarTabuleiro() {

        for (int linha = 0;
             linha < tabuleiro.getLinhas();
             linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas();
                 coluna++) {

                Celula celula = tabuleiro.getCelula(
                        linha,
                        coluna
                );

                JButton botao = botoes[linha][coluna];

                botao.setText("");
                botao.setForeground(Color.BLACK);
                botao.setBackground(
                        new Color(255, 240, 245)
                );

                if (celula.isRevelada()) {

                    botao.setBackground(
                            new Color(245, 235, 235)
                    );

                    if (celula.isMinada()) {

                        botao.setText("*");
                        botao.setForeground(
                                new Color(190, 40, 60)
                        );

                    } else if (
                            celula.getMinasVizinhas() > 0) {

                        int numero =
                                celula.getMinasVizinhas();

                        botao.setText(
                                String.valueOf(numero)
                        );

                        botao.setForeground(
                                corDoNumero(numero)
                        );
                    }

                } else if (celula.isMarcada()) {

                    botao.setText("F");
                    botao.setForeground(
                            new Color(180, 40, 90)
                    );
                }

                if (tabuleiro.isJogoEncerrado()) {

                    if (celula.isMinada()
                            && !celula.isRevelada()
                            && !celula.isMarcada()) {

                        botao.setText("*");
                        botao.setForeground(Color.RED);
                    }

                    if (celula.isMarcada()
                            && !celula.isMinada()) {

                        botao.setText("X");
                        botao.setForeground(Color.RED);
                    }
                }
            }
        }

        revalidate();
        repaint();
    }

    private Color corDoNumero(int numero) {

        return switch (numero) {
            case 1 -> new Color(40, 90, 200);
            case 2 -> new Color(30, 140, 70);
            case 3 -> new Color(200, 50, 50);
            case 4 -> new Color(100, 50, 160);
            case 5 -> new Color(150, 60, 40);
            case 6 -> new Color(30, 140, 140);
            case 7 -> Color.BLACK;
            default -> Color.DARK_GRAY;
        };
    }
}
