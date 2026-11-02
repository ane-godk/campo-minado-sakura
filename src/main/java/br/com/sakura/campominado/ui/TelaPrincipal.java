
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.ConfiguracaoJogo;
import br.com.sakura.campominado.domain.Dificuldade;
import br.com.sakura.campominado.domain.RegistroPartida;
import br.com.sakura.campominado.domain.Tabuleiro;
import br.com.sakura.campominado.service.RankingService;
import br.com.sakura.campominado.solver.Solver;
import br.com.sakura.campominado.solver.TestadorPartida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private Tabuleiro tabuleiro;
    private PainelTabuleiro painelTabuleiro;
    private TestadorPartida testador;
    private Solver solver;

    private final JPanel areaTabuleiro =
            new JPanel(new BorderLayout());

    private final JScrollPane rolagemTabuleiro =
            new JScrollPane();

    private final JLabel labelVidas = new JLabel();
    private final JLabel labelMinas = new JLabel();
    private final JLabel labelTempo = new JLabel();
    private final JLabel labelPontos = new JLabel();
    private final JLabel labelDificuldade = new JLabel();

    private final JButton botaoDica =
            TemaSakura.criarBotao("Dica (3/3)");

    private final JButton botaoPausar =
            TemaSakura.criarBotao("Pausar");

    private final RankingService rankingService =
            new RankingService();

    private final Timer cronometro;
    private Timer timerAutojogo;

    private Dificuldade dificuldadeAtual =
            Dificuldade.FACIL;

    private int segundos;
    private int dicasRestantes = 3;

    private boolean partidaIniciada;
    private boolean resultadoProcessado;
    private boolean pausado;
    private boolean autosolveAtivo;

    private String nomeJogador = "Jogador";

    public TelaPrincipal() {

        setTitle("Campo Minado - Sakura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel principal = new JPanel(
                new BorderLayout(8, 8)
        );

        principal.setBackground(TemaSakura.FUNDO);

        principal.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        JPanel cabecalho = criarCabecalho();

        areaTabuleiro.setBackground(TemaSakura.PAINEL);

        areaTabuleiro.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA,
                                2,
                                true
                        ),
                        new EmptyBorder(5, 5, 5, 5)
                )
        );

        rolagemTabuleiro.setBorder(null);

        rolagemTabuleiro.getViewport().setBackground(
                TemaSakura.PAINEL
        );

        rolagemTabuleiro.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        rolagemTabuleiro.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        rolagemTabuleiro.getVerticalScrollBar()
                .setUnitIncrement(32);

        rolagemTabuleiro.getHorizontalScrollBar()
                .setUnitIncrement(32);

        areaTabuleiro.add(
                rolagemTabuleiro,
                BorderLayout.CENTER
        );

        principal.add(
                cabecalho,
                BorderLayout.NORTH
        );

        principal.add(
                areaTabuleiro,
                BorderLayout.CENTER
        );

        setContentPane(principal);

        cronometro = new Timer(1000, evento -> {

            if (!pausado
                    && !resultadoProcessado
                    && tabuleiro != null
                    && !tabuleiro.isJogoEncerrado()) {

                segundos++;
                atualizarInformacoes();
            }
        });

        iniciarNovaPartida();
    }

    public void selecionarDificuldade(
            Dificuldade dificuldade
    ) {

        if (dificuldade == null) {
            return;
        }

        dificuldadeAtual = dificuldade;
        iniciarNovaPartida();
    }

    private JPanel criarCabecalho() {

        JPanel cabecalho = new JPanel(
                new BorderLayout(0, 10)
        );

        cabecalho.setBackground(TemaSakura.PAINEL);

        cabecalho.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA,
                                2,
                                true
                        ),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        JLabel titulo = new JLabel(
                "✿ CAMPO MINADO SAKURA ✿",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        titulo.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        JPanel linhaSuperior = new JPanel(
                new BorderLayout(8, 0)
        );

        linhaSuperior.setOpaque(false);

        labelDificuldade.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        labelDificuldade.setForeground(
                TemaSakura.ROSA_ESCURO
        );

        linhaSuperior.add(
                labelDificuldade,
                BorderLayout.CENTER
        );

        botaoPausar.setPreferredSize(
                new Dimension(115, 38)
        );

        linhaSuperior.add(
                botaoPausar,
                BorderLayout.EAST
        );

        JPanel informacoes = new JPanel(
                new GridLayout(1, 4, 6, 0)
        );

        informacoes.setOpaque(false);

        configurarIndicador(labelVidas);
        configurarIndicador(labelMinas);
        configurarIndicador(labelTempo);
        configurarIndicador(labelPontos);

        informacoes.add(criarCartao(labelVidas));
        informacoes.add(criarCartao(labelMinas));
        informacoes.add(criarCartao(labelTempo));
        informacoes.add(criarCartao(labelPontos));

        JPanel linhaDica = new JPanel(
                new BorderLayout()
        );

        linhaDica.setOpaque(false);

        JLabel explicacao = new JLabel(
                "Use uma dica para encontrar uma jogada segura."
        );

        explicacao.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        explicacao.setForeground(TemaSakura.TEXTO);

        linhaDica.add(
                explicacao,
                BorderLayout.CENTER
        );

        botaoDica.setPreferredSize(
                new Dimension(145, 38)
        );

        linhaDica.add(
                botaoDica,
                BorderLayout.EAST
        );

        JPanel centro = new JPanel(
                new BorderLayout(0, 9)
        );

        centro.setOpaque(false);

        centro.add(
                linhaSuperior,
                BorderLayout.NORTH
        );

        centro.add(
                informacoes,
                BorderLayout.CENTER
        );

        centro.add(
                linhaDica,
                BorderLayout.SOUTH
        );

        cabecalho.add(
                titulo,
                BorderLayout.NORTH
        );

        cabecalho.add(
                centro,
                BorderLayout.CENTER
        );

        botaoPausar.addActionListener(
                evento -> abrirMenuPausa()
        );

        botaoDica.addActionListener(
                evento -> solicitarDica()
        );

        return cabecalho;
    }

    private void configurarIndicador(JLabel label) {

        label.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        label.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        label.setForeground(
                TemaSakura.ROSA_ESCURO
        );
    }

    private JPanel criarCartao(JLabel label) {

        JPanel cartao = new JPanel(
                new BorderLayout()
        );

        cartao.setBackground(TemaSakura.BRANCO);

        cartao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaSakura.BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(9, 3, 9, 3)
                )
        );

        cartao.add(
                label,
                BorderLayout.CENTER
        );

        return cartao;
    }

    private void iniciarNovaPartida() {

        pararAutojogo();

        if (cronometro != null) {
            cronometro.stop();
        }

        segundos = 0;
        dicasRestantes = 3;

        partidaIniciada = false;
        resultadoProcessado = false;
        pausado = false;
        autosolveAtivo = false;

        ConfiguracaoJogo configuracao =
                new ConfiguracaoJogo(
                        dificuldadeAtual.getLinhas(),
                        dificuldadeAtual.getColunas(),
                        dificuldadeAtual.getMinas(),
                        3,
                        0,
                        dificuldadeAtual,
                        nomeJogador
                );

        tabuleiro = new Tabuleiro(configuracao);
        testador = new TestadorPartida(tabuleiro);
        solver = new Solver(tabuleiro);

        painelTabuleiro = new PainelTabuleiro(
                tabuleiro,
                this::atualizarInformacoes,
                this::iniciarCronometro,
                this::encerrarPartida
        );

        rolagemTabuleiro.setViewportView(
                painelTabuleiro
        );

        atualizarInformacoes();
        ajustarTamanhoJanela();

        SwingUtilities.invokeLater(() ->
                rolagemTabuleiro.getViewport()
                        .setViewPosition(new Point(0, 0))
        );
    }

    private void ajustarTamanhoJanela() {

        Rectangle areaUtil = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        Dimension tamanhoGrade =
                painelTabuleiro.getTamanhoGrade();

        Component cabecalho =
                getContentPane().getComponent(0);

        Dimension tamanhoCabecalho =
                cabecalho.getPreferredSize();

        Insets margens =
                ((JComponent) getContentPane())
                        .getInsets();

        Insets margensTabuleiro =
                areaTabuleiro.getInsets();

        Insets decoracao = getInsets();

        int larguraExtra =
                margens.left
                        + margens.right
                        + margensTabuleiro.left
                        + margensTabuleiro.right
                        + decoracao.left
                        + decoracao.right;

        int alturaExtra =
                margens.top
                        + margens.bottom
                        + margensTabuleiro.top
                        + margensTabuleiro.bottom
                        + decoracao.top
                        + decoracao.bottom
                        + tamanhoCabecalho.height
                        + 8;

        int larguraMaxima = Math.max(
                350,
                areaUtil.width - 40
        );

        int alturaMaxima = Math.max(
                350,
                areaUtil.height - 40
        );

        int larguraUtil = Math.max(
                200,
                larguraMaxima - larguraExtra
        );

        int alturaUtil = Math.max(
                200,
                alturaMaxima - alturaExtra
        );

        int larguraDesejada = Math.max(
                tamanhoGrade.width,
                tamanhoCabecalho.width
                        - margensTabuleiro.left
                        - margensTabuleiro.right
        );

        int alturaDesejada = Math.max(
                tamanhoGrade.height,
                Math.min(larguraDesejada, alturaUtil)
        );

        int larguraViewport = Math.min(
                larguraUtil,
                larguraDesejada
        );

        int alturaViewport = Math.min(
                alturaUtil,
                alturaDesejada
        );

        rolagemTabuleiro.setPreferredSize(
                new Dimension(
                        larguraViewport,
                        alturaViewport
                )
        );

        pack();

        setSize(
                Math.min(getWidth(), larguraMaxima),
                Math.min(getHeight(), alturaMaxima)
        );

        setLocationRelativeTo(null);

        revalidate();
        repaint();
    }

    private void iniciarCronometro() {

        if (pausado
                || resultadoProcessado
                || tabuleiro.isJogoEncerrado()) {
            return;
        }

        partidaIniciada = true;

        if (!cronometro.isRunning()) {
            cronometro.start();
        }
    }

    private void solicitarDica() {

        if (pausado
                || resultadoProcessado
                || tabuleiro.isJogoEncerrado()
                || autojogoEmAndamento()) {
            return;
        }

        if (dicasRestantes <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Você já utilizou as três dicas desta partida.",
                    "Dicas esgotadas",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        Solver.Jogada jogada =
                solver.encontrarJogada();

        if (jogada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ainda não existe uma dedução lógica segura.\n"
                            + "Revele mais células e tente novamente.\n\n"
                            + "Nenhuma dica foi consumida.",
                    "Dica Sakura",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        dicasRestantes--;
        atualizarInformacoes();

        String acao =
                jogada.tipo() == Solver.TipoJogada.REVELAR
                        ? "Revelar"
                        : "Marcar como mina";

        JOptionPane.showMessageDialog(
                this,
                acao
                        + " a célula da linha "
                        + (jogada.linha() + 1)
                        + ", coluna "
                        + (jogada.coluna() + 1)
                        + ".\n\n"
                        + jogada.explicacao()
                        + "\n\nDicas restantes: "
                        + dicasRestantes,
                "Dica Sakura",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void abrirMenuPausa() {

        if (resultadoProcessado
                || tabuleiro.isJogoEncerrado()) {
            return;
        }

        boolean autosolveEstavaAtivo =
                autojogoEmAndamento();

        if (autosolveEstavaAtivo) {
            pararAutojogo();
        }

        pausado = true;
        cronometro.stop();

        boolean menuAberto = true;

        while (menuAberto) {

            TelaPausa telaPausa = new TelaPausa(
                    this,
                    nomeJogador,
                    dificuldadeAtual.getNome(),
                    segundos,
                    autosolveEstavaAtivo
            );

            TelaPausa.Acao acao =
                    telaPausa.mostrar();

            switch (acao) {

                case CONTINUAR -> {

                    menuAberto = false;
                    retomarPartida();

                    if (autosolveEstavaAtivo) {
                        iniciarAutojogo();
                    }
                }

                case AUTOSOLVE -> {

                    menuAberto = false;

                    if (autosolveEstavaAtivo) {
                        pararAutojogo();
                        retomarPartida();
                    } else {
                        iniciarAutojogo();
                    }
                }

                case ALTERAR_JOGADOR ->
                        alterarJogador();

                case RANKING ->
                        mostrarRanking();

                case REINICIAR -> {

                    int confirmar =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Deseja reiniciar a partida atual?\n"
                                            + "O progresso será perdido.",
                                    "Reiniciar partida",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );

                    if (confirmar == JOptionPane.YES_OPTION) {
                        menuAberto = false;
                        iniciarNovaPartida();
                    }
                }

                case VOLTAR_MENU -> {

                    int confirmar =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Deseja voltar ao menu inicial?\n"
                                            + "A partida atual será perdida.",
                                    "Voltar ao menu",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );

                    if (confirmar == JOptionPane.YES_OPTION) {
                        menuAberto = false;
                        voltarAoMenuInicial();
                    }
                }
            }
        }
    }

    private void retomarPartida() {

        pausado = false;

        if (partidaIniciada
                && !resultadoProcessado
                && !tabuleiro.isJogoEncerrado()) {
            cronometro.start();
        }
    }

    private void iniciarAutojogo() {

        if (resultadoProcessado
                || tabuleiro.isJogoEncerrado()) {
            return;
        }

        pararAutojogo();

        pausado = false;
        autosolveAtivo = true;

        iniciarCronometro();

        timerAutojogo = new Timer(60, evento -> {

            if (pausado || resultadoProcessado) {
                pararAutojogo();
                return;
            }

            if (!tabuleiro.isJogoEncerrado()) {

                boolean executou =
                        testador.executarProximoPasso();

                if (!executou) {

                    pararAutojogo();

                    JOptionPane.showMessageDialog(
                            this,
                            "O Autosolve não encontrou mais jogadas.",
                            "Autosolve interrompido",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            atualizarAposJogadaAutomatica();

            if (tabuleiro.isJogoEncerrado()) {
                pararAutojogo();
            }
        });

        timerAutojogo.start();
    }

    private boolean autojogoEmAndamento() {

        return timerAutojogo != null
                && timerAutojogo.isRunning();
    }

    private void pararAutojogo() {

        if (timerAutojogo != null) {
            timerAutojogo.stop();
            timerAutojogo = null;
        }

        autosolveAtivo = false;
    }

    private void atualizarAposJogadaAutomatica() {

        painelTabuleiro.atualizarTabuleiro();
        atualizarInformacoes();

        if (tabuleiro.isJogoEncerrado()) {
            encerrarPartida();
        }
    }

    private void atualizarInformacoes() {

        if (tabuleiro == null) {
            return;
        }

        labelVidas.setText(
                "♥ Vidas: " + tabuleiro.getVidas()
        );

        labelMinas.setText(
                "✿ Minas: " + tabuleiro.getMinasRestantes()
        );

        labelTempo.setText(
                "◷ Tempo: " + segundos + "s"
        );

        labelPontos.setText(
                "★ Pontos: " + calcularPontuacao()
        );

        labelDificuldade.setText(
                "Dificuldade: " + dificuldadeAtual.getNome()
        );

        botaoDica.setText(
                "Dica (" + dicasRestantes + "/3)"
        );

        botaoDica.setEnabled(
                dicasRestantes > 0
                        && !resultadoProcessado
                        && !tabuleiro.isJogoEncerrado()
                        && !pausado
                        && !autojogoEmAndamento()
        );

        botaoPausar.setEnabled(
                !resultadoProcessado
                        && !tabuleiro.isJogoEncerrado()
        );

        // Detecta o fim mesmo quando o tabuleiro
        // apenas notifica uma atualização visual.
        if (tabuleiro.isJogoEncerrado()
                && !resultadoProcessado) {
            encerrarPartida();
        }
    }

    private int calcularPontuacao() {

        if (tabuleiro == null) {
            return 0;
        }

        int reveladas = 0;

        for (int linha = 0;
             linha < tabuleiro.getLinhas();
             linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas();
                 coluna++) {

                if (tabuleiro.getCelula(
                        linha, coluna
                ).isRevelada()
                        && !tabuleiro.getCelula(
                                linha, coluna
                        ).isMinada()) {
                    reveladas++;
                }
            }
        }

        double multiplicador = tabuleiro
                .getConfiguracao()
                .getDificuldade()
                .getMultiplicador();

        int base =
                reveladas * 10
                        + tabuleiro.getMinasEncontradas() * 20;

        int bonusVidas =
                tabuleiro.getVidas() * 50;

        return Math.max(
                0,
                (int) Math.round(
                        (base + bonusVidas) * multiplicador
                ) - segundos
        );
    }

    private void encerrarPartida() {

        if (resultadoProcessado
                || tabuleiro == null
                || !tabuleiro.isJogoEncerrado()) {
            return;
        }

        // Impede abertura duplicada da janela.
        resultadoProcessado = true;

        pararAutojogo();
        cronometro.stop();

        final boolean venceu = tabuleiro.isVenceu();
        final int pontos = calcularPontuacao();
        final int tempoFinal = segundos;
        final String jogadorFinal = nomeJogador;
        final Dificuldade dificuldadeFinal =
                dificuldadeAtual;

        boolean salvouRanking = false;

        if (venceu) {
            try {

                rankingService.salvar(
                        new RegistroPartida(
                                jogadorFinal,
                                dificuldadeFinal.getNome(),
                                pontos,
                                tempoFinal,
                                LocalDateTime.now()
                        )
                );

                salvouRanking = true;

            } catch (IllegalStateException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível salvar o ranking:\n"
                                + e.getMessage(),
                        "Ranking",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }

        final boolean rankingSalvo = salvouRanking;

        botaoPausar.setEnabled(false);
        botaoDica.setEnabled(false);

        // Abre depois que a jogada atual terminar
        // de ser processada pelo Swing.
        SwingUtilities.invokeLater(() -> {

            if (!isDisplayable()) {
                return;
            }

            TelaFimPartida telaFim = new TelaFimPartida(
                    this,
                    venceu,
                    jogadorFinal,
                    dificuldadeFinal,
                    tempoFinal,
                    pontos,
                    rankingSalvo
            );

            TelaFimPartida.Acao acao =
                    telaFim.mostrar();

            switch (acao) {

                case JOGAR_NOVAMENTE ->
                        iniciarNovaPartida();

                case MUDAR_DIFICULDADE -> {
                    Dificuldade nova =
                            telaFim.getDificuldadeSelecionada();

                    if (nova != null) {
                        dificuldadeAtual = nova;
                        iniciarNovaPartida();
                    }
                }

                case VOLTAR_MENU ->
                        voltarAoMenuInicial();
            }
        });
    }

    private void voltarAoMenuInicial() {

        pararAutojogo();
        cronometro.stop();

        SwingUtilities.invokeLater(() -> {

            TelaInicial inicial = new TelaInicial();
            inicial.setVisible(true);

            dispose();
        });
    }

    private void alterarJogador() {

        String nome = JOptionPane.showInputDialog(
                this,
                "Nome do jogador:",
                nomeJogador
        );

        if (nome != null && !nome.isBlank()) {

            nomeJogador = nome.trim();

            setTitle(
                    "Campo Minado Sakura - " + nomeJogador
            );
        }
    }

    private void mostrarRanking() {

        List<RegistroPartida> registros;

        try {

            registros = rankingService.listar();

        } catch (IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro no ranking",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (registros.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhuma vitória registrada.",
                    "Ranking",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        StringBuilder texto = new StringBuilder();

        for (int i = 0;
             i < Math.min(10, registros.size());
             i++) {

            RegistroPartida registro =
                    registros.get(i);

            texto.append(i + 1)
                    .append(". ")
                    .append(registro.jogador())
                    .append(" | ")
                    .append(registro.dificuldade())
                    .append(" | ")
                    .append(registro.pontuacao())
                    .append(" pontos | ")
                    .append(registro.tempoSegundos())
                    .append("s\n");
        }

        JTextArea area = new JTextArea(
                texto.toString(),
                12,
                40
        );

        area.setEditable(false);

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        area.setBackground(TemaSakura.BRANCO);
        area.setForeground(TemaSakura.TEXTO);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "✿ Top 10 - Ranking Sakura ✿",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new TelaPrincipal().setVisible(true)
        );
    }
}
