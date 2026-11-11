/**
 * Universidade Centro Universitário Jorge Amado / Disciplina: Programação Orientada a Objetos
 * Trabalho Prático: AV3 - Urna Eletrônica Simplificada
 *
 * Descrição: Ponto de entrada (Entry Point) da aplicação, responsável por
 * inicializar os serviços do Firebase e lançar a interface gráfica na EDT.
 *
 * Padrão Arquitetural: MVC
 * Tecnologia: Java Swing & Firebase Firestore
 *
 * @author Luan Gabriel, Cauã dos Santos
 * @version 1.0
 */
import model.Urna;
import service.FirebaseService;
import view.TelaVotacao;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        FirebaseService.inicializarFirebase();

        EventQueue.invokeLater(() -> {
            Urna urna = new Urna();
            new TelaVotacao(urna).setVisible(true);
        });
    }
}