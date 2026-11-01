package br.com.sakura.campominado.ui;

import br.com.sakura.campominado.domain.Tabuleiro;
import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        // Define o layout em grelha com base nas dimensões do tabuleiro
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        setBackground(new Color(255, 228, 225)); // Tom rosa claro do tema Sakura

        // Aqui vamos adicionar os botões de cada célula a seguir
    }
}