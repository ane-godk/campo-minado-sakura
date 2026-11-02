
package br.com.sakura.campominado;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.com.sakura.campominado.ui.TelaInicial;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.err.println(
                    "Não foi possível configurar o visual do sistema: "
                    + e.getMessage()
                );
            }

            TelaInicial telaInicial = new TelaInicial();
            telaInicial.setVisible(true);
        });
    }
}
