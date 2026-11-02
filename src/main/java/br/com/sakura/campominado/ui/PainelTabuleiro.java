
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Celula;
import br.com.sakura.campominado.domain.Tabuleiro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class PainelTabuleiro extends JPanel implements Scrollable {

    private static final int CELULA_IDEAL = 46;
    private static final int CELULA_MINIMA = 30;
    private static final int ESPACO = 3;
    private static final int MARGEM = 12;

    private static final Color FUNDO =
            new Color(255, 224, 236);

    private static final Color MOLDURA =
            new Color(226, 151, 181);

    private static final Color FECHADA =
            new Color(255, 207, 224);

    private static final Color REVELADA =
            new Color(255, 245, 249);

    private static final Color SOMBRA =
            new Color(191, 100, 139, 95);

    private static final Color DOURADO =
            new Color(218, 151, 43);

    private static final Color DOURADO_CLARO =
            new Color(255, 222, 116);

    private final Tabuleiro tabuleiro;
    private final Runnable aoAtualizar;
    private final Runnable aoPrimeiroClique;
    private final Runnable aoEncerrar;

    private boolean primeiroCliqueRealizado;
    private boolean encerramentoNotificado;

    private int dicaLinha = -1;
    private int dicaColuna = -1;
    private boolean dicaMarcarMina;
    private boolean pulsoClaro = true;

    private final Timer animacaoDica;

    public PainelTabuleiro(
            Tabuleiro tabuleiro,
            Runnable aoAtualizar,
            Runnable aoPrimeiroClique,
            Runnable aoEncerrar
    ) {
        if (tabuleiro == null) {
            throw new IllegalArgumentException(
                    "O tabuleiro não pode ser nulo."
            );
        }

        this.tabuleiro = tabuleiro;
        this.aoAtualizar = aoAtualizar;
        this.aoPrimeiroClique = aoPrimeiroClique;
        this.aoEncerrar = aoEncerrar;

        setBackground(FUNDO);
        setOpaque(true);

        setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
        ));

        // Correção: utiliza o Timer recebido pelo evento.
        // Assim não acessamos animacaoDica antes da
        // inicialização do atributo final.
        animacaoDica = new Timer(420, evento -> {
            if (dicaLinha < 0) {
                ((Timer) evento.getSource()).stop();
                return;
            }

            pulsoClaro = !pulsoClaro;
            repaint();
        });

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evento) {
                tratarClique(evento);
            }
        };

        addMouseListener(mouse);
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

    public void destacarDica(
            int linha,
            int coluna,
            boolean marcarMina
    ) {
        if (tabuleiro.isJogoEncerrado()) {
            return;
        }

        if (linha < 0
                || linha >= tabuleiro.getLinhas()
                || coluna < 0
                || coluna >= tabuleiro.getColunas()) {
            return;
        }

        dicaLinha = linha;
        dicaColuna = coluna;
        dicaMarcarMina = marcarMina;
        pulsoClaro = true;

        if (!animacaoDica.isRunning()) {
            animacaoDica.start();
        }

        repaint();
    }

    public void limparDica() {
        dicaLinha = -1;
        dicaColuna = -1;
        dicaMarcarMina = false;

        animacaoDica.stop();
        repaint();
    }

    public boolean temDicaAtiva() {
        return dicaLinha >= 0 && dicaColuna >= 0;
    }

    public void encerrarAnimacoes() {
        limparDica();
    }

    public Rectangle getLimitesCelula(
            int linha,
            int coluna
    ) {
        if (linha < 0
                || linha >= tabuleiro.getLinhas()
                || coluna < 0
                || coluna >= tabuleiro.getColunas()) {
            return new Rectangle();
        }

        Rectangle grade = limitesGrade();
        int tamanho = tamanhoCelula();
        int passo = tamanho + ESPACO;

        return new Rectangle(
                grade.x + coluna * passo,
                grade.y + linha * passo,
                tamanho,
                tamanho
        );
    }

    public Dimension getTamanhoGrade() {
        return dimensaoParaCelula(CELULA_IDEAL);
    }

    private Dimension dimensaoParaCelula(int tamanho) {
        int largura = MARGEM * 2
                + tabuleiro.getColunas() * tamanho
                + (tabuleiro.getColunas() - 1) * ESPACO;

        int altura = MARGEM * 2
                + tabuleiro.getLinhas() * tamanho
                + (tabuleiro.getLinhas() - 1) * ESPACO;

        return new Dimension(largura, altura);
    }

    @Override
    public Dimension getPreferredSize() {
        return getTamanhoGrade();
    }

    @Override
    public Dimension getMinimumSize() {
        return dimensaoParaCelula(CELULA_MINIMA);
    }

    private int tamanhoCelula() {
        int larguraDisponivel =
                getWidth() - MARGEM * 2;

        int alturaDisponivel =
                getHeight() - MARGEM * 2;

        int larguraPorCelula =
                (larguraDisponivel
                        - (tabuleiro.getColunas() - 1) * ESPACO)
                        / tabuleiro.getColunas();

        int alturaPorCelula =
                (alturaDisponivel
                        - (tabuleiro.getLinhas() - 1) * ESPACO)
                        / tabuleiro.getLinhas();

        return Math.max(
                CELULA_MINIMA,
                Math.min(larguraPorCelula, alturaPorCelula)
        );
    }

    private Rectangle limitesGrade() {
        int tamanho = tamanhoCelula();

        int largura =
                tabuleiro.getColunas() * tamanho
                + (tabuleiro.getColunas() - 1) * ESPACO;

        int altura =
                tabuleiro.getLinhas() * tamanho
                + (tabuleiro.getLinhas() - 1) * ESPACO;

        int x = Math.max(
                MARGEM,
                (getWidth() - largura) / 2
        );

        int y = Math.max(
                MARGEM,
                (getHeight() - altura) / 2
        );

        return new Rectangle(x, y, largura, altura);
    }

    private void tratarClique(MouseEvent evento) {
        if (tabuleiro.isJogoEncerrado()) {
            return;
        }

        Rectangle grade = limitesGrade();

        if (!grade.contains(evento.getPoint())) {
            return;
        }

        int passo = tamanhoCelula() + ESPACO;

        int coluna =
                (evento.getX() - grade.x) / passo;

        int linha =
                (evento.getY() - grade.y) / passo;

        if (linha < 0
                || linha >= tabuleiro.getLinhas()
                || coluna < 0
                || coluna >= tabuleiro.getColunas()) {
            return;
        }

        int xDentro =
                (evento.getX() - grade.x) % passo;

        int yDentro =
                (evento.getY() - grade.y) % passo;

        if (xDentro >= tamanhoCelula()
                || yDentro >= tamanhoCelula()) {
            return;
        }

        boolean alterou = false;

        if (SwingUtilities.isRightMouseButton(evento)) {
            alterou = tabuleiro.alternarBandeira(
                    linha, coluna
            );

        } else if (SwingUtilities.isLeftMouseButton(evento)) {
            alterou = tabuleiro.revelar(
                    linha, coluna
            );

            if (alterou && !primeiroCliqueRealizado) {
                primeiroCliqueRealizado = true;

                if (aoPrimeiroClique != null) {
                    aoPrimeiroClique.run();
                }
            }
        }

        if (alterou) {
            limparDica();
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
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        Rectangle grade = limitesGrade();
        int tamanho = tamanhoCelula();
        int passo = tamanho + ESPACO;

        g2.setColor(MOLDURA);

        g2.fill(new RoundRectangle2D.Double(
                grade.x - 7,
                grade.y - 7,
                grade.width + 14,
                grade.height + 14,
                16,
                16
        ));

        for (int linha = 0;
             linha < tabuleiro.getLinhas();
             linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas();
                 coluna++) {

                int x = grade.x + coluna * passo;
                int y = grade.y + linha * passo;

                desenharCelula(
                        g2,
                        tabuleiro.getCelula(linha, coluna),
                        x,
                        y,
                        tamanho
                );

                if (linha == dicaLinha
                        && coluna == dicaColuna
                        && !tabuleiro.isJogoEncerrado()) {
                    desenharDestaqueDica(
                            g2, x, y, tamanho
                    );
                }
            }
        }

        g2.dispose();
    }

    private void desenharCelula(
            Graphics2D g2,
            Celula celula,
            int x,
            int y,
            int tamanho
    ) {
        boolean encerrado =
                tabuleiro.isJogoEncerrado();

        Color fundo = FECHADA;

        if (celula.isRevelada()) {
            fundo = celula.isMinada()
                    ? new Color(250, 155, 181)
                    : REVELADA;

        } else if (celula.isMarcada()) {
            fundo = new Color(249, 190, 211);

        } else if (encerrado && celula.isMinada()) {
            fundo = new Color(250, 155, 181);
        }

        g2.setColor(SOMBRA);

        g2.fill(new RoundRectangle2D.Double(
                x + 1,
                y + 3,
                tamanho - 2,
                tamanho - 3,
                10,
                10
        ));

        g2.setColor(fundo);

        g2.fill(new RoundRectangle2D.Double(
                x + 1,
                y,
                tamanho - 2,
                tamanho - 4,
                10,
                10
        ));

        g2.setColor(new Color(212, 135, 167));
        g2.setStroke(new BasicStroke(1.1f));

        g2.draw(new RoundRectangle2D.Double(
                x + 1,
                y,
                tamanho - 3,
                tamanho - 5,
                10,
                10
        ));

        if (celula.isRevelada() && celula.isMinada()) {
            desenharMina(g2, x, y, tamanho);

        } else if (celula.isRevelada()) {
            if (celula.getMinasVizinhas() > 0) {
                desenharNumero(
                        g2,
                        celula.getMinasVizinhas(),
                        x,
                        y,
                        tamanho
                );
            }

        } else if (celula.isMarcada()) {
            desenharBandeira(g2, x, y, tamanho);

            if (encerrado && !celula.isMinada()) {
                desenharErro(g2, x, y, tamanho);
            }

        } else if (encerrado && celula.isMinada()) {
            desenharMina(g2, x, y, tamanho);

        } else {
            desenharFlor(
                    g2,
                    x + tamanho / 2,
                    y + tamanho / 2 - 2,
                    Math.max(3, tamanho / 7),
                    new Color(232, 123, 166)
            );
        }
    }

    private void desenharDestaqueDica(
            Graphics2D g2,
            int x,
            int y,
            int tamanho
    ) {
        Graphics2D destaque =
                (Graphics2D) g2.create();

        Color corBorda = pulsoClaro
                ? DOURADO_CLARO
                : DOURADO;

        destaque.setColor(new Color(
                255, 222, 116,
                pulsoClaro ? 85 : 42
        ));

        destaque.fill(new RoundRectangle2D.Double(
                x + 2,
                y + 1,
                tamanho - 4,
                tamanho - 6,
                10,
                10
        ));

        destaque.setColor(corBorda);

        destaque.setStroke(new BasicStroke(
                pulsoClaro ? 3.5f : 2.5f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        destaque.draw(new RoundRectangle2D.Double(
                x + 2,
                y + 1,
                tamanho - 5,
                tamanho - 6,
                10,
                10
        ));

        String simbolo =
                dicaMarcarMina ? "!" : "?";

        int tamanhoFonte =
                Math.max(16, tamanho / 2);

        destaque.setFont(new Font(
                "SansSerif",
                Font.BOLD,
                tamanhoFonte
        ));

        FontMetrics fm =
                destaque.getFontMetrics();

        int larguraSelo =
                Math.max(15, tamanho / 3);

        int sx = x + tamanho
                - larguraSelo - 3;

        int sy = y + 3;

        destaque.setColor(
                new Color(255, 248, 218)
        );

        destaque.fillOval(
                sx, sy,
                larguraSelo,
                larguraSelo
        );

        destaque.setColor(DOURADO);
        destaque.setStroke(
                new BasicStroke(1.5f)
        );

        destaque.drawOval(
                sx, sy,
                larguraSelo,
                larguraSelo
        );

        int textoX = sx
                + (larguraSelo
                - fm.stringWidth(simbolo)) / 2;

        int textoY = sy
                + (larguraSelo
                - fm.getHeight()) / 2
                + fm.getAscent();

        destaque.setColor(
                new Color(139, 87, 26)
        );

        destaque.drawString(
                simbolo, textoX, textoY
        );

        destaque.dispose();
    }

    private void desenharNumero(
            Graphics2D g2,
            int numero,
            int x,
            int y,
            int tamanho
    ) {
        Color cor = switch (numero) {
            case 1 -> new Color(173, 68, 119);
            case 2 -> new Color(61, 139, 94);
            case 3 -> new Color(203, 66, 94);
            case 4 -> new Color(106, 81, 160);
            case 5 -> new Color(156, 83, 71);
            case 6 -> new Color(52, 132, 139);
            default -> new Color(91, 57, 89);
        };

        g2.setColor(cor);

        int fonte =
                Math.max(14, tamanho / 2 + 3);

        g2.setFont(new Font(
                "SansSerif",
                Font.BOLD,
                fonte
        ));

        String texto =
                String.valueOf(numero);

        FontMetrics fm =
                g2.getFontMetrics();

        int tx = x
                + (tamanho
                - fm.stringWidth(texto)) / 2;

        int ty = y
                + (tamanho
                - fm.getHeight()) / 2
                + fm.getAscent() - 2;

        g2.drawString(texto, tx, ty);
    }

    private void desenharFlor(
            Graphics2D g2,
            int cx,
            int cy,
            int raio,
            Color cor
    ) {
        g2.setColor(cor);

        for (int i = 0; i < 5; i++) {
            double angulo =
                    Math.toRadians(i * 72 - 90);

            double px = cx
                    + Math.cos(angulo)
                    * raio * 0.85;

            double py = cy
                    + Math.sin(angulo)
                    * raio * 0.85;

            g2.fill(new Ellipse2D.Double(
                    px - raio * 0.65,
                    py - raio * 0.65,
                    raio * 1.3,
                    raio * 1.3
            ));
        }

        g2.setColor(
                new Color(255, 237, 165)
        );

        int centro =
                Math.max(3, raio / 2);

        g2.fillOval(
                cx - centro / 2,
                cy - centro / 2,
                centro,
                centro
        );
    }

    private void desenharBandeira(
            Graphics2D g2,
            int x,
            int y,
            int tamanho
    ) {
        Graphics2D simbolo =
                (Graphics2D) g2.create();

        simbolo.translate(
                x + tamanho / 2.0,
                y + tamanho / 2.0 - 2
        );

        simbolo.scale(
                tamanho / 44.0,
                tamanho / 44.0
        );

        simbolo.setColor(
                new Color(133, 73, 105)
        );

        simbolo.setStroke(
                new BasicStroke(2.5f)
        );

        simbolo.drawLine(
                -5, -14, -5, 14
        );

        Path2D bandeira =
                new Path2D.Double();

        bandeira.moveTo(-4, -14);
        bandeira.lineTo(14, -8);
        bandeira.lineTo(-4, 2);
        bandeira.closePath();

        simbolo.setColor(
                new Color(224, 83, 140)
        );
        simbolo.fill(bandeira);

        simbolo.setColor(
                new Color(151, 64, 108)
        );
        simbolo.draw(bandeira);

        simbolo.fillRoundRect(
                -12, 13, 16, 4, 4, 4
        );

        simbolo.dispose();
    }

    private void desenharMina(
            Graphics2D g2,
            int x,
            int y,
            int tamanho
    ) {
        Graphics2D simbolo =
                (Graphics2D) g2.create();

        simbolo.translate(
                x + tamanho / 2.0,
                y + tamanho / 2.0 - 2
        );

        simbolo.scale(
                tamanho / 44.0,
                tamanho / 44.0
        );

        simbolo.setColor(
                new Color(95, 47, 82)
        );

        simbolo.setStroke(
                new BasicStroke(3)
        );

        for (int i = 0; i < 8; i++) {
            double angulo =
                    Math.toRadians(i * 45);

            int x1 = (int) (
                    Math.cos(angulo) * 9
            );

            int y1 = (int) (
                    Math.sin(angulo) * 9
            );

            int x2 = (int) (
                    Math.cos(angulo) * 15
            );

            int y2 = (int) (
                    Math.sin(angulo) * 15
            );

            simbolo.drawLine(
                    x1, y1, x2, y2
            );
        }

        simbolo.setColor(
                new Color(127, 66, 102)
        );

        simbolo.fillOval(
                -11, -11, 22, 22
        );

        desenharFlor(
                simbolo,
                0,
                0,
                5,
                new Color(255, 188, 214)
        );

        simbolo.dispose();
    }

    private void desenharErro(
            Graphics2D g2,
            int x,
            int y,
            int tamanho
    ) {
        g2.setColor(
                new Color(179, 43, 76)
        );

        g2.setStroke(new BasicStroke(
                Math.max(2f, tamanho / 15f),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        int margem = tamanho / 5;

        g2.drawLine(
                x + margem,
                y + margem,
                x + tamanho - margem,
                y + tamanho - margem
        );

        g2.drawLine(
                x + tamanho - margem,
                y + margem,
                x + margem,
                y + tamanho - margem
        );
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(
            Rectangle visibleRect,
            int orientation,
            int direction
    ) {
        return CELULA_MINIMA + ESPACO;
    }

    @Override
    public int getScrollableBlockIncrement(
            Rectangle visibleRect,
            int orientation,
            int direction
    ) {
        return orientation == SwingConstants.VERTICAL
                ? Math.max(30, visibleRect.height - 40)
                : Math.max(30, visibleRect.width - 40);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (!(getParent()
                instanceof JViewport viewport)) {
            return false;
        }

        return viewport.getWidth()
                >= getMinimumSize().width;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        if (!(getParent()
                instanceof JViewport viewport)) {
            return false;
        }

        return viewport.getHeight()
                >= getMinimumSize().height;
    }
}
