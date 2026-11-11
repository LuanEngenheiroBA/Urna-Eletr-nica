/**
 * Universidade Centro Universitário Jorge Amado / Disciplina: Programação Orientada a Objetos
 * Trabalho Prático: AV3 - Urna Eletrônica Simplificada
 *
 * Descrição: Serviço responsável pela inicialização do Firebase Admin SDK
 * e persistência remota dos votos em coleção NoSQL (Firestore).
 *
 * Padrão Arquitetural: MVC
 * Tecnologia: Java Swing & Firebase Firestore
 *
 * @author Luan Gabriel, cauã do Santos , eyel
 * @version 1.0
 */
package service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseService {
    private static Firestore baseDados;

    public static void inicializarFirebase() {
        try {
            File arquivoChave = new File("serviceAccountKey.json");
            if (!arquivoChave.exists()) {
                System.err.println("Aviso: 'serviceAccountKey.json' não encontrado na raiz.");
                return;
            }

            FileInputStream credenciais = new FileInputStream(arquivoChave);
            FirebaseOptions opcoes = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credenciais))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(opcoes);
            }

            baseDados = FirestoreClient.getFirestore();
            System.out.println("Firebase Firestore conectado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro na inicialização do Firebase: " + e.getMessage());
        }
    }

    public static void registarVoto(String cargo, String tipoVoto, String numeroCandidato) {
        if (baseDados == null) {
            return;
        }

        new Thread(() -> {
            try {
                Map<String, Object> registo = new HashMap<>();
                registo.put("cargo", cargo);
                registo.put("tipoVoto", tipoVoto);
                registo.put("numeroCandidato", numeroCandidato);
                registo.put("timestamp", System.currentTimeMillis());

                baseDados.collection("votos").add(registo);
            } catch (Exception e) {
                System.err.println("Falha ao registar voto no Firebase: " + e.getMessage());
            }
        }).start();
    }
}