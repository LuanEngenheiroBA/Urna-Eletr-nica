/**
 * Universidade Centro Universitário Jorge Amado / Disciplina: Programação Orientada a Objetos
 * Trabalho Prático: AV3 - Urna Eletrônica Simplificada
 *
 * Descrição: Utilitário para síntese e reprodução assíncrona dos efeitos sonoros
 * da urna eletrônica em formato 16-bit PCM (teclas, branco, corrige, confirma e fim).
 *
 * Padrão Arquitetural: MVC
 * Tecnologia: Java Swing & Firebase Firestore
 *
 * @author Luan Gabriel, Cauâ do Santos
 * @version 1.0
 */
package util;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

public class SomUtil {

    private static final float TAXA_AMOSTRAGEM = 44100f;

    public static void tocarTeclaNumero() {
        tocarTomSintetizado(1050, 70, 0.7);
    }

    public static void tocarBranco() {
        tocarTomSintetizado(820, 90, 0.7);
    }

    public static void tocarCorrige() {
        new Thread(() -> {
            tocarTomSintetizado(750, 60, 0.7);
            try { Thread.sleep(30); } catch (InterruptedException ignored) {}
            tocarTomSintetizado(520, 70, 0.7);
        }).start();
    }

    public static void tocarConfirmaIntermediario() {
        new Thread(() -> {
            tocarTomSintetizado(880, 80, 0.75);
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            tocarTomSintetizado(1320, 120, 0.75);
        }).start();
    }

    public static void tocarFimUrna() {
        new Thread(() -> {
            int[] frequencias = {880, 1046, 1318, 1760};
            int[] duracoes = {80, 80, 80, 500};

            for (int i = 0; i < frequencias.length; i++) {
                tocarTomSintetizado(frequencias[i], duracoes[i], 0.8);
                try {
                    Thread.sleep(duracoes[i] + 15);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private static void tocarTomSintetizado(int freqHz, int duracaoMs, double volume) {
        new Thread(() -> {
            try {
                int totalAmostras = (int) (TAXA_AMOSTRAGEM * duracaoMs / 1000);
                byte[] buffer = new byte[totalAmostras * 2];
                int bufferIdx = 0;

                for (int i = 0; i < totalAmostras; i++) {
                    double angulo = 2.0 * Math.PI * i / (TAXA_AMOSTRAGEM / freqHz);
                    short amostra = (short) (Math.sin(angulo) * 32767.0 * volume);

                    buffer[bufferIdx++] = (byte) (amostra & 0xFF);
                    buffer[bufferIdx++] = (byte) ((amostra >> 8) & 0xFF);
                }

                AudioFormat formato = new AudioFormat(TAXA_AMOSTRAGEM, 16, 1, true, false);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                AudioInputStream ais = new AudioInputStream(bais, formato, totalAmostras);

                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();

                clip.addLineListener(evento -> {
                    if (evento.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

            } catch (Exception ignored) {}
        }).start();
    }
}