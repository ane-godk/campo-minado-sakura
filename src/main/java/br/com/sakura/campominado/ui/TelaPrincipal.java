
package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.ConfiguracaoJogo;
import br.com.sakura.campominado.domain.Dificuldade;
import br.com.sakura.campominado.domain.RegistroPartida;
import br.com.sakura.campominado.domain.Tabuleiro;
import br.com.sakura.campominado.service.RankingService;
import br.com.sakura.campominado.solver.Solver;
import br.com.sakura.campominado.solver.TestadorPartida;

import javax.swing.*;
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

    private final JLabel labelVidas = new JLabel();
    private final JLabel labelMinas = new JLabel();
    private final JLabel labelTempo = new JLabel();
    private final JLabel labelPontos = new JLabel();

    private final JComboBox<Dificuldade> seletorDificuldade =
            new JComboBox<>(new Dificuldade[]{
                    Dificuldade.FACIL,
                    Dificuldade.MEDIO,
                    Dificuldade.DIFICIL,
                    Dificuldade.INFERNO
            });

    private final RankingService rankingService =
            new RankingService();

    private final Timer cronometro;
    private Timer timerAutojogo;

    private int segundos;
    private boolean partidaIniciada;
    private boolean resultadoProcessado;
    private String nomeJogador = "Jogador";

    public TelaPrincipal() {

        setTitle("Campo Minado - Sakura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 520));

        JPanel principal =
                new JPanel(new BorderLayout(8, 8));

        principal.setBackground(
                new Color(255, 253, 240)
        );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        // Três linhas para acomodar todos os controles.
        JPanel controles = new JPanel(
                new GridLayout(3, 3, 8, 8)
        );
        controles.setOpaque(false);

        JButton reiniciar = new JButton("Reiniciar");
        JButton ranking = new JButton("Ranking");
        JButton jogador = new JButton("Jogador");
        JButton passo = new JButton("Próximo passo");
        JButton autojogar =
                new JButton("Autojogar / Testar");
        JButton dica = new JButton("Dica lógica");
        JButton passoLogico = new JButton("Passo lógico");

        controles.add(new JLabel("Dificuldade:"));
        controles.add(seletorDificuldade);
        controles.add(reiniciar);
        controles.add(jogador);
        controles.add(ranking);
        controles.add(passo);
        controles.add(autojogar);
        controles.add(dica);
        controles.add(passoLogico);

        JPanel informacoes = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 15, 5
                )
        );
        informacoes.setOpaque(false);

        informacoes.add(labelVidas);
        informacoes.add(labelMinas);
        informacoes.add(labelTempo);
        informacoes.add(labelPontos);

        JPanel cabecalho =
                new JPanel(new BorderLayout(0, 8));

        cabecalho.setOpaque(false);
        cabecalho.add(controles, BorderLayout.NORTH);
        cabecalho.add(informacoes, BorderLayout.SOUTH);

        principal.add(cabecalho, BorderLayout.NORTH);
        principal.add(areaTabuleiro, BorderLayout.CENTER);

        setContentPane(principal);

        cronometro = new Timer(1000, evento -> {
            segundos++;
            atualizarInformacoes();
        });

        reiniciar.addActionListener(
                evento -> iniciarNovaPartida()
        );

        seletorDificuldade.addActionListener(
                evento -> iniciarNovaPartida()
        );

        ranking.addActionListener(
                evento -> mostrarRanking()
        );

        jogador.addActionListener(
                evento -> alterarJogador()
        );

        passo.addActionListener(
                evento -> executarPasso()
        );

        autojogar.addActionListener(
                evento -> executarAutojogo()
        );

        dica.addActionListener(
                evento -> mostrarDicaLogica()
        );

        passoLogico.addActionListener(
                evento -> executarPassoLogico()
        );

        iniciarNovaPartida();
    }

    private void iniciarNovaPartida() {

        pararAutojogo();
        cronometro.stop();

        segundos = 0;
        partidaIniciada = false;
        resultadoProcessado = false;

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

        areaTabuleiro.removeAll();
        areaTabuleiro.add(
                painelTabuleiro,
                BorderLayout.CENTER
        );

        atualizarInformacoes();

        int largura = Math.max(
                650,
                dificuldade.getColunas() * 30 + 60
        );

        int altura = Math.max(
                540,
                dificuldade.getLinhas() * 30 + 250
        );

        setSize(largura, altura);
        setLocationRelativeTo(null);

        areaTabuleiro.revalidate();
        areaTabuleiro.repaint();
    }

    private void iniciarCronometro() {

        if (!partidaIniciada) {
            partidaIniciada = true;

            if (!tabuleiro.isJogoEncerrado()) {
                cronometro.start();
            }
        }
    }

    private void executarPasso() {

        if (tabuleiro.isJogoEncerrado()
                || autojogoEmAndamento()) {
            return;
        }

        iniciarCronometro();

        if (testador.executarProximoPasso()) {
            atualizarAposJogadaAutomatica();
        }
    }

    private void executarAutojogo() {

        if (tabuleiro.isJogoEncerrado()) {
            return;
        }

        if (autojogoEmAndamento()) {
            pararAutojogo();
            return;
        }

        iniciarCronometro();

        timerAutojogo = new Timer(60, evento -> {

            if (!tabuleiro.isJogoEncerrado()) {

                boolean executou =
                        testador.executarProximoPasso();

                if (!executou) {

                    pararAutojogo();

                    JOptionPane.showMessageDialog(
                            this,
                            "O testador não encontrou "
                                    + "mais jogadas.",
                            "Teste interrompido",
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
    }

    private void mostrarDicaLogica() {

        if (autojogoEmAndamento()) {
            return;
        }

        Solver.Jogada jogada = solver.encontrarJogada();

        if (jogada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhuma jogada lógica disponível.\n"
                            + "Revele mais células "
                            + "ou continue manualmente.",
                    "Dica lógica",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

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
                        + jogada.explicacao(),
                "Dica lógica",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void executarPassoLogico() {

        if (tabuleiro.isJogoEncerrado()
                || autojogoEmAndamento()) {
            return;
        }

        Solver.Jogada jogada = solver.encontrarJogada();

        if (jogada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhuma jogada lógica disponível.\n"
                            + "O solver não fará "
                            + "uma jogada aleatória.",
                    "Solver lógico",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        iniciarCronometro();

        if (solver.executarJogada(jogada)) {
            atualizarAposJogadaAutomatica();
        }
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
                "Vidas: " + tabuleiro.getVidas()
        );

        labelMinas.setText(
                "Minas: " + tabuleiro.getMinasRestantes()
        );

        labelTempo.setText(
                "Tempo: " + segundos + "s"
        );

        labelPontos.setText(
                "Pontos: " + calcularPontuacao()
        );
    }

    private int calcularPontuacao() {

        if (tabuleiro == null) {
            return 0;
        }

        int reveladas = 0;

        for (int linha = 0;
             linha < tabuleiro.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < tabuleiro.getColunas(); coluna++) {

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

        int base = reveladas * 10
                + tabuleiro.getMinasEncontradas() * 20;

        int bonusVidas = tabuleiro.getVidas() * 50;

        return Math.max(
                0,
                (int) Math.round(
                        (base + bonusVidas) * multiplicador
                ) - segundos
        );
    }

    private void encerrarPartida() {

        if (resultadoProcessado) {
            return;
        }

        resultadoProcessado = true;

        pararAutojogo();
        cronometro.stop();
        atualizarInformacoes();

        if (tabuleiro.isVenceu()) {

            int pontos = calcularPontuacao();

            try {

                rankingService.salvar(
                        new RegistroPartida(
                                nomeJogador,
                                tabuleiro.getConfiguracao()
                                        .getDificuldade()
                                        .getNome(),
                                pontos,
                                segundos,
                                LocalDateTime.now()
                        )
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Vitória!\n"
                                + "Jogador: " + nomeJogador
                                + "\nPontuação: " + pontos
                                + "\nTempo: " + segundos + "s"
                                + "\nRanking atualizado.",
                        "Campo Minado Sakura",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IllegalStateException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vitória!\n"
                                + "Não foi possível salvar "
                                + "o ranking:\n"
                                + e.getMessage(),
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Fim de jogo!\n"
                            + "Pontuação: "
                            + calcularPontuacao()
                            + "\nTempo: " + segundos + "s",
                    "Derrota",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void alterarJogador() {

        String nome = JOptionPane.showInputDialog(
                this,
                "Nome do jogador:",
                nomeJogador
        );

        if (nome != null && !nome.isBlank()) {
            nomeJogador = nome.trim();
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
             i < Math.min(10, registros.size()); i++) {

            RegistroPartida registro = registros.get(i);

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
                texto.toString(), 12, 40
        );

        area.setEditable(false);
        area.setFont(new Font(
                "Monospaced", Font.PLAIN, 13
        ));

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Top 10 - Ranking Sakura",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new TelaPrincipal().setVisible(true)
        );
    }
}
