package br.com.sakura.campominado.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class AudioManager {

    private static boolean somAtivado = true;

    public static void tocarSom(String nomeArquivo) {
        if (!somAtivado) return;

        try {
            // Procura o ficheiro dentro da pasta resources/sounds/
            URL url = AudioManager.class.getResource("/sounds/" + nomeArquivo);
            if (url == null) {
                System.err.println("Aviso: Ficheiro de som não encontrado: " + nomeArquivo);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            System.err.println("Erro ao reproduzir o som: " + e.getMessage());
        }
    }

    public static void alternarSom() {
        somAtivado = !somAtivado;
    }

    public static boolean isSomAtivado() {
        return somAtivado;
    }
}