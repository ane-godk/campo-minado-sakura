package br.com.sakura.campominado.ui;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        // 1. Configurar o título da janela
        setTitle("Campo Minado - Sakura");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela no ecrã

        // 2. Criar o painel principal com fundo creme
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(new Color(255, 253, 240)); // Tom creme suave
        painelPrincipal.setLayout(new BorderLayout());

        // 3. Criar o painel secundário rosa claro (onde depois ficará o tabuleiro)
        JPanel painelTabuleiroOuCentro = new JPanel();
        painelTabuleiroOuCentro.setBackground(new Color(255, 228, 225)); // Tom rosa claro
        painelPrincipal.add(painelTabuleiroOuCentro, BorderLayout.CENTER);

        // 4. Criar o botão para reiniciar
        JButton botaoReiniciar = new JButton("Reiniciar");
        botaoReiniciar.addActionListener(e -> {
            // Ação provisória para testar se o botão responde ao clique
            JOptionPane.showMessageDialog(this, "Botão Reiniciar clicado!");
        });

        // Adicionar o botão num painel superior ou inferior
        JPanel painelSuperior = new JPanel();
        painelSuperior.setOpaque(false); // Para acompanhar o fundo creme
        painelSuperior.add(botaoReiniciar);
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

        // Adicionar o painel principal à janela
        add(painelPrincipal);
    }

    public static void main(String[] args) {
        // Executar a janela de forma segura
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}