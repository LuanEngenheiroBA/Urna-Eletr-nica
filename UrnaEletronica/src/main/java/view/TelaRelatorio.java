/**
 * Universidade Centro Universitário Jorge Amado / Disciplina: Programação Orientada a Objetos
 * Trabalho Prático: AV3 - Urna Eletrônica Simplificada
 *
 * Descrição: Janela de relatório final (Boletim de Urna) que exibe a apuração
 * percentual e quantitativa dos votos computados por cargo eleitoral.
 *
 * Padrão Arquitetural: MVC
 * Tecnologia: Java Swing & Firebase Firestore
 *
 * @author Luan Gabriel, Cauã dos Santos
 * @version 1.0
 */
package view;

import model.Candidato;
import model.Urna;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaRelatorio extends JFrame {

    public TelaRelatorio(Urna urna) {
        setTitle("Relatório Final da Eleição");
        setSize(550, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("RELATÓRIO DA ELEIÇÃO (POR CARGO)");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelConteudo.add(titulo);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 15)));

        List<Candidato> candidatos = urna.obterListaCandidatos();

        painelConteudo.add(criarSecaoTitulo("--- CARGO: GOVERNADOR ---"));
        int totalGov = urna.obterVotosBrancosGovernador() + urna.obterVotosNulosGovernador();
        for (Candidato c : candidatos) {
            if ("Governador".equalsIgnoreCase(c.obterCargo())) {
                totalGov += c.obterQuantidadeVotos();
            }
        }
        for (Candidato c : candidatos) {
            if ("Governador".equalsIgnoreCase(c.obterCargo())) {
                double pct = totalGov > 0 ? ((double) c.obterQuantidadeVotos() / totalGov) * 100 : 0;
                painelConteudo.add(new JLabel(String.format("%s - %s (%s): %.0f%% (%d votos)", c.obterNome(), c.obterEspecialidade(), c.obterNumero(), pct, c.obterQuantidadeVotos())));
            }
        }
        double pctBrancoGov = totalGov > 0 ? ((double) urna.obterVotosBrancosGovernador() / totalGov) * 100 : 0;
        double pctNuloGov = totalGov > 0 ? ((double) urna.obterVotosNulosGovernador() / totalGov) * 100 : 0;
        painelConteudo.add(new JLabel(String.format("Votos Brancos: %.0f%% (%d votos)", pctBrancoGov, urna.obterVotosBrancosGovernador())));
        painelConteudo.add(new JLabel(String.format("Votos Nulos: %.0f%% (%d votos)", pctNuloGov, urna.obterVotosNulosGovernador())));

        painelConteudo.add(Box.createRigidArea(new Dimension(0, 15)));

        painelConteudo.add(criarSecaoTitulo("--- CARGO: PRESIDENTE ---"));
        int totalPres = urna.obterVotosBrancosPresidente() + urna.obterVotosNulosPresidente();
        for (Candidato c : candidatos) {
            if ("Presidente".equalsIgnoreCase(c.obterCargo())) {
                totalPres += c.obterQuantidadeVotos();
            }
        }
        for (Candidato c : candidatos) {
            if ("Presidente".equalsIgnoreCase(c.obterCargo())) {
                double pct = totalPres > 0 ? ((double) c.obterQuantidadeVotos() / totalPres) * 100 : 0;
                painelConteudo.add(new JLabel(String.format("%s - %s (%s): %.0f%% (%d votos)", c.obterNome(), c.obterEspecialidade(), c.obterNumero(), pct, c.obterQuantidadeVotos())));
            }
        }
        double pctBrancoPres = totalPres > 0 ? ((double) urna.obterVotosBrancosPresidente() / totalPres) * 100 : 0;
        double pctNuloPres = totalPres > 0 ? ((double) urna.obterVotosNulosPresidente() / totalPres) * 100 : 0;
        painelConteudo.add(new JLabel(String.format("Votos Brancos: %.0f%% (%d votos)", pctBrancoPres, urna.obterVotosBrancosPresidente())));
        painelConteudo.add(new JLabel(String.format("Votos Nulos: %.0f%% (%d votos)", pctNuloPres, urna.obterVotosNulosPresidente())));

        add(new JScrollPane(painelConteudo), BorderLayout.CENTER);

        JButton botaoFechar = new JButton("Encerrar Aplicação");
        botaoFechar.addActionListener(e -> System.exit(0));
        JPanel painelBotao = new JPanel();
        painelBotao.add(botaoFechar);
        add(painelBotao, BorderLayout.SOUTH);
    }

    private JLabel criarSecaoTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(30, 80, 160));
        return label;
    }
}