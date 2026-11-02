
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Celula;
import br.com.sakura.campominado.domain.Tabuleiro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class PainelTabuleiro extends JPanel {

    private final Tabuleiro tabuleiro;
    private final JButton[][] botoes;

    private final Runnable aoAtualizar;
    private final Runnable aoPrimeiroClique;
    private final Runnable aoEncerrar;

    private boolean primeiroCliqueRealizado;
    private boolean encerramentoNotificado;

    public PainelTabuleiro(
            Tabuleiro tabuleiro,
            Runnable aoAtualizar,
            Runnable aoPrimeiroClique,
            Runnable aoEncerrar
    ) {
        this.tabuleiro = tabuleiro;
        this.aoAtualizar = aoAtualizar;
        this.aoPrimeiroClique = aoPrimeiroClique;
        this.aoEncerrar = aoEncerrar;

        setLayout(new GridLayout(
                tabuleiro.getLinhas(),
                tabuleiro.getColunas(),
                3, 3
        ));

        setBackground(TemaSakura.BORDA);
        setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        botoes = new JButton[
                tabuleiro.getLinhas()
        ][tabuleiro.getColunas()];

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

                final int l = linha;
                final int c = coluna;

                JButton botao = new CelulaSakura();

                botao.setMargin(
                        new Insets(0, 0, 0, 0)
                );

                botao.setFont(TemaSakura.FONTE_NUMERO);
                botao.setFocusPainted(false);
                botao.setFocusable(false);

                botao.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent evento) {

                        if (SwingUtilities.isRightMouseButton(evento)) {
                            alternarBandeira(l, c);

                        } else if (SwingUtilities.isLeftMouseButton(evento)) {
                            revelarCelula(l, c);
                        }
                    }
                });

                botoes[linha][coluna] = botao;
                add(botao);
            }
        }

        atualizarTabuleiro();
    }

    public PainelTabuleiro(
            Tabuleiro tabuleiro,
            Runnable aoAtualizar
    ) {
        this(tabuleiro, aoAtualizar, null, null);
    }

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        this(tabuleiro, null, null, null);
    }

    private void revelarCelula(int linha, int coluna) {

        if (tabuleiro.isJogoEncerrado()) {
            return;
        }

        boolean alterou = tabuleiro.revelar(
                linha, coluna
        );

        if (alterou && !primeiroCliqueRealizado) {

            primeiroCliqueRealizado = true;

            if (aoPrimeiroClique != null) {
                aoPrimeiroClique.run();
            }
        }

        if (alterou) {
            atualizarAposJogada();
        }
    }

    private void alternarBandeira(int linha, int coluna) {

        if (tabuleiro.isJogoEncerrado()) {
            return;
        }

        if (tabuleiro.alternarBandeira(linha, coluna)) {
            atualizarAposJogada();
        }
    }

    private void atualizarAposJogada() {

        atualizarTabuleiro();

        if (aoAtualizar != null) {
            aoAtualizar.run();
        }

        if (tabuleiro.isJogoEncerrado()
                && !encerramentoNotificado) {

            encerramentoNotificado = true;

            if (aoEncerrar != null) {
                aoEncerrar.run();
            }
        }
    }

    public void atualizarTabuleiro() {

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

                Celula celula =
                        tabuleiro.getCelula(linha, coluna);

                JButton botao = botoes[linha][coluna];

                String texto = "";
                Color fundo = TemaSakura.ROSA_CLARO;
                Color corTexto = TemaSakura.ROSA_ESCURO;

                if (celula.isRevelada()) {

                    fundo = TemaSakura.REVELADA;

                    if (celula.isMinada()) {

                        texto = "✿";
                        fundo = TemaSakura.MINA;
                        corTexto = new Color(111, 45, 76);

                    } else if (celula.getMinasVizinhas() > 0) {

                        int numero = celula.getMinasVizinhas();

                        texto = String.valueOf(numero);
                        corTexto = corDoNumero(numero);
                    }

                } else if (celula.isMarcada()) {

                    if (tabuleiro.isJogoEncerrado()
                            && !celula.isMinada()) {

                        texto = "×";
                        fundo = TemaSakura.MINA;

                    } else {

                        texto = "⚑";
                        fundo = TemaSakura.BANDEIRA;
                        corTexto = TemaSakura.ROSA_ESCURO;
                    }

                } else if (tabuleiro.isJogoEncerrado()
                        && celula.isMinada()) {

                    texto = "✿";
                    fundo = TemaSakura.MINA;
                    corTexto = new Color(111, 45, 76);
                }

                botao.setText(texto);
                botao.setForeground(corTexto);
                botao.setBackground(fundo);

                if (botao instanceof CelulaSakura celulaVisual) {

                    celulaVisual.setFlorDecorativa(
                            !celula.isRevelada()
                                    && !celula.isMarcada()
                                    && !tabuleiro.isJogoEncerrado()
                    );
                }
            }
        }

        revalidate();
        repaint();
    }

    private Color corDoNumero(int numero) {

        return switch (numero) {
            case 1 -> new Color(171, 81, 125);
            case 2 -> new Color(78, 139, 104);
            case 3 -> new Color(193, 76, 95);
            case 4 -> new Color(110, 83, 155);
            case 5 -> new Color(160, 91, 71);
            case 6 -> new Color(63, 139, 144);
            default -> new Color(105, 71, 94);
        };
    }

    private static class CelulaSakura extends JButton {

        private boolean florDecorativa;

        CelulaSakura() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        void setFlorDecorativa(boolean valor) {
            florDecorativa = valor;
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int largura = getWidth();
            int altura = getHeight();

            g2.setColor(new Color(165, 86, 121, 65));

            g2.fill(new RoundRectangle2D.Float(
                    1, 3,
                    Math.max(0, largura - 2),
                    Math.max(0, altura - 3),
                    12, 12
            ));

            g2.setColor(getBackground());

            g2.fill(new RoundRectangle2D.Float(
                    1, 1,
                    Math.max(0, largura - 2),
                    Math.max(0, altura - 4),
                    12, 12
            ));

            g2.setColor(TemaSakura.BORDA);
            g2.setStroke(new BasicStroke(1.1f));

            g2.draw(new RoundRectangle2D.Float(
                    1, 1,
                    Math.max(0, largura - 3),
                    Math.max(0, altura - 5),
                    12, 12
            ));

            if (florDecorativa
                    && largura >= 22
                    && altura >= 22) {

                desenharFlor(
                        g2,
                        largura / 2,
                        altura / 2
                );
            }

            g2.dispose();
            super.paintComponent(g);
        }

        private void desenharFlor(
                Graphics2D g2,
                int x,
                int y
        ) {

            int raio = Math.max(
                    3,
                    Math.min(
                            getWidth(),
                            getHeight()
                    ) / 10
            );

            g2.setColor(
                    new Color(232, 130, 165, 85)
            );

            for (int i = 0; i < 5; i++) {

                double angulo =
                        Math.toRadians(i * 72 - 90);

                int px = x + (int) (
                        Math.cos(angulo) * raio * 1.5
                );

                int py = y + (int) (
                        Math.sin(angulo) * raio * 1.5
                );

                g2.fillOval(
                        px - raio,
                        py - raio,
                        raio * 2,
                        raio * 2
                );
            }

            g2.setColor(
                    new Color(246, 200, 120, 130)
            );

            g2.fillOval(
                    x - 2,
                    y - 2,
                    4,
                    4
            );
        }
    }
}
