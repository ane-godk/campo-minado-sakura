package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Tabuleiro;
import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        // Define o layout em grelha com base nas linhas e colunas do tabuleiro
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        setBackground(new Color(255, 228, 225)); // Tom rosa claro do tema Sakura

        // Percorre as linhas e colunas para criar um botão para cada célula
        for (int l = 0; l < tabuleiro.getLinhas(); l++) {
            for (int c = 0; c < tabuleiro.getColunas(); c++) {
                JButton botao = new JButton();
            
                // Opcional: ajustar o estilo visual base dos botões
                botao.setFocusPainted(false);
                botao.setBackground(new Color(255, 240, 245)); // Tom sakura mais claro para os botões
                
                add(botao);
            }
        }
    }
}