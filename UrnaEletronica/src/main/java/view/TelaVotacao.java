package view;

import model.Candidato;
import model.Urna;
import service.FirebaseService;
import util.SomUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TelaVotacao extends JFrame {
    private Urna urna;
    private StringBuilder digitosDigitados;
    private String cargoAtual;

    private JLabel labelCargoCandidato;
    private JLabel labelNomeCandidato;
    private JLabel labelEspecialidadeCandidato;
    private JLabel labelPartidoCandidato;
    private JLabel labelFotoCandidato;
    private JLabel[] caixasDigitos;

    public TelaVotacao(Urna urna) {
        this.urna = urna;
        this.digitosDigitados = new StringBuilder();
        this.cargoAtual = "Governador";

        setTitle("Urna Eletrônica Simplificada");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        solicitarSenhaInicial();
        inicializarInterface();
    }

    private void solicitarSenhaInicial() {
        JPasswordField campoSenha = new JPasswordField(10);
        JPanel painel = new JPanel(new GridLayout(2, 1));
        painel.add(new JLabel("Digite a senha de inicialização (99999):"));
        painel.add(campoSenha);

        int resultado = JOptionPane.showConfirmDialog(this, painel, "Autenticação", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION && "99999".equals(new String(campoSenha.getPassword()))) {
            urna.setVotacaoAtiva(true);
            JOptionPane.showMessageDialog(this, "Urna liberada para votação. Relatório inicial zerado.");
        } else {
            JOptionPane.showMessageDialog(this, "Senha incorreta. O sistema será fechado.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void inicializarInterface() {
        JPanel painelEcran = new JPanel(null);
        painelEcran.setBackground(Color.WHITE);
        painelEcran.setPreferredSize(new Dimension(500, 450));
        painelEcran.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel tituloCargo = new JLabel("SEU VOTO PARA");
        tituloCargo.setBounds(20, 20, 200, 20);
        tituloCargo.setFont(new Font("Arial", Font.PLAIN, 12));
        painelEcran.add(tituloCargo);

        labelCargoCandidato = new JLabel(cargoAtual.toUpperCase());
        labelCargoCandidato.setBounds(20, 45, 250, 30);
        labelCargoCandidato.setFont(new Font("Arial", Font.BOLD, 20));
        painelEcran.add(labelCargoCandidato);

        JLabel labelNumeroTexto = new JLabel("Número:");
        labelNumeroTexto.setBounds(20, 95, 70, 30);
        labelNumeroTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        painelEcran.add(labelNumeroTexto);

        caixasDigitos = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            caixasDigitos[i] = new JLabel("", SwingConstants.CENTER);
            caixasDigitos[i].setBounds(95 + (i * 35), 95, 30, 35);
            caixasDigitos[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            caixasDigitos[i].setFont(new Font("Arial", Font.BOLD, 18));
            painelEcran.add(caixasDigitos[i]);
        }

        labelNomeCandidato = new JLabel("Nome: ");
        labelNomeCandidato.setBounds(20, 160, 300, 25);
        painelEcran.add(labelNomeCandidato);

        labelEspecialidadeCandidato = new JLabel("Especialidade: ");
        labelEspecialidadeCandidato.setBounds(20, 190, 300, 25);
        painelEcran.add(labelEspecialidadeCandidato);

        labelPartidoCandidato = new JLabel("Partido: ");
        labelPartidoCandidato.setBounds(20, 220, 300, 25);
        painelEcran.add(labelPartidoCandidato);

        labelFotoCandidato = new JLabel("FOTO", SwingConstants.CENTER);
        labelFotoCandidato.setBounds(330, 20, 140, 190);
        labelFotoCandidato.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        painelEcran.add(labelFotoCandidato);

        add(painelEcran, BorderLayout.CENTER);

        JPanel painelTeclado = new JPanel(new BorderLayout(5, 5));
        painelTeclado.setBackground(new Color(40, 40, 40));
        painelTeclado.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelBotoes = new JPanel(new GridLayout(4, 3, 8, 8));
        painelBotoes.setBackground(new Color(40, 40, 40));

        for (int i = 1; i <= 9; i++) {
            painelBotoes.add(criarBotaoNumerico(String.valueOf(i)));
        }

        painelBotoes.add(new JLabel(""));
        painelBotoes.add(criarBotaoNumerico("0"));
        painelBotoes.add(new JLabel(""));
        painelTeclado.add(painelBotoes, BorderLayout.CENTER);

        JPanel painelAcoes = new JPanel(new GridLayout(1, 3, 5, 5));
        painelAcoes.setBackground(new Color(40, 40, 40));

        JButton botaoBranco = new JButton("BRANCO");
        botaoBranco.setBackground(Color.WHITE);
        botaoBranco.addActionListener(e -> acaoBotaoBranco());

        JButton botaoCorrige = new JButton("CORRIGE");
        botaoCorrige.setBackground(new Color(255, 140, 0));
        botaoCorrige.addActionListener(e -> acaoBotaoCorrige());

        JButton botaoConfirma = new JButton("CONFIRMA");
        botaoConfirma.setBackground(new Color(46, 139, 87));
        botaoConfirma.setForeground(Color.WHITE);
        botaoConfirma.addActionListener(e -> acaoBotaoConfirma());

        painelAcoes.add(botaoBranco);
        painelAcoes.add(botaoCorrige);
        painelAcoes.add(botaoConfirma);

        painelTeclado.add(painelAcoes, BorderLayout.SOUTH);
        add(painelTeclado, BorderLayout.EAST);
    }

    private JButton criarBotaoNumerico(String digito) {
        JButton botao = new JButton(digito);
        botao.setBackground(new Color(25, 25, 25));
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.addActionListener(e -> adicionarDigito(digito));
        return botao;
    }

    private void adicionarDigito(String digito) {
        SomUtil.tocarTeclaNumero(); // Som de tecla 0 a 9
        if (digitosDigitados.length() < 5) {
            digitosDigitados.append(digito);
            atualizarEcraDigitos();

            if (digitosDigitados.length() == 5) {
                verificarCandidato();
            }
        }
    }

    private void atualizarEcraDigitos() {
        for (int i = 0; i < 5; i++) {
            if (i < digitosDigitados.length()) {
                caixasDigitos[i].setText(String.valueOf(digitosDigitados.charAt(i)));
            } else {
                caixasDigitos[i].setText("");
            }
        }
    }

    private void verificarCandidato() {
        String numero = digitosDigitados.toString();
        Candidato candidato = urna.buscarCandidatoPorNumeroECargo(numero, cargoAtual);

        if (candidato != null) {
            labelNomeCandidato.setText("Nome: " + candidato.obterNome());
            labelEspecialidadeCandidato.setText("Especialidade: " + candidato.obterEspecialidade());
            labelPartidoCandidato.setText("Partido: " + candidato.obterPartido());
            exibirFoto(candidato.obterCaminhoFoto());
        } else {
            labelNomeCandidato.setText("Nome: VOTO NULO");
            labelEspecialidadeCandidato.setText("Especialidade: -");
            labelPartidoCandidato.setText("Partido: -");
            exibirFoto("sem_foto.png");
        }
    }

    private void exibirFoto(String caminho) {
        if (caminho == null || caminho.trim().isEmpty()) {
            labelFotoCandidato.setIcon(null);
            labelFotoCandidato.setText("SEM FOTO");
            return;
        }

        String nomeArquivo = caminho.contains("/") ? caminho.substring(caminho.lastIndexOf("/") + 1) : caminho;
        BufferedImage imagemFinal = carregarImagemSegura(nomeArquivo);

        if (imagemFinal != null) {
            Image redimensionada = imagemFinal.getScaledInstance(140, 190, Image.SCALE_SMOOTH);
            labelFotoCandidato.setIcon(new ImageIcon(redimensionada));
            labelFotoCandidato.setText("");
        } else {
            BufferedImage fotoPadrao = carregarImagemSegura("sem_foto.png");
            if (fotoPadrao != null) {
                Image redimensionada = fotoPadrao.getScaledInstance(140, 190, Image.SCALE_SMOOTH);
                labelFotoCandidato.setIcon(new ImageIcon(redimensionada));
                labelFotoCandidato.setText("");
            } else {
                labelFotoCandidato.setIcon(null);
                labelFotoCandidato.setText("SEM FOTO");
            }
        }
    }

    private BufferedImage carregarImagemSegura(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
            return null;
        }

        String[] caminhosRecurso = {
                "/fotos/" + nomeArquivo,
                "/" + nomeArquivo,
                "fotos/" + nomeArquivo,
                nomeArquivo
        };

        for (String r : caminhosRecurso) {
            try {
                java.net.URL url = getClass().getResource(r);
                if (url == null) {
                    url = getClass().getClassLoader().getResource(r.startsWith("/") ? r.substring(1) : r);
                }
                if (url != null) {
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) return img;
                }
            } catch (Exception ignored) {}
        }

        File[] testesDisco = {
                new File("src/main/resources/fotos/" + nomeArquivo),
                new File("target/classes/fotos/" + nomeArquivo),
                new File("fotos/" + nomeArquivo),
                new File(nomeArquivo)
        };

        for (File f : testesDisco) {
            if (f.exists() && f.isFile()) {
                try {
                    BufferedImage img = ImageIO.read(f);
                    if (img != null) return img;
                } catch (Exception ignored) {}
            }
        }

        return null;
    }

    private void acaoBotaoCorrige() {
        SomUtil.tocarCorrige(); // Som de CORRIGE
        digitosDigitados.setLength(0);
        atualizarEcraDigitos();
        labelNomeCandidato.setText("Nome: ");
        labelEspecialidadeCandidato.setText("Especialidade: ");
        labelPartidoCandidato.setText("Partido: ");
        labelFotoCandidato.setIcon(null);
        labelFotoCandidato.setText("FOTO");
    }

    private void acaoBotaoBranco() {
        SomUtil.tocarBranco(); // Som de BRANCO
        digitosDigitados.setLength(0);
        atualizarEcraDigitos();
        labelNomeCandidato.setText("Nome: VOTO EM BRANCO");
        labelEspecialidadeCandidato.setText("Especialidade: -");
        labelPartidoCandidato.setText("Partido: -");
        labelFotoCandidato.setIcon(null);
        labelFotoCandidato.setText("");
    }

    private void acaoBotaoConfirma() {
        boolean ehVotoBranco = labelNomeCandidato.getText().contains("BRANCO");
        boolean ehVotoCincoDigitos = digitosDigitados.length() == 5;

        if (!ehVotoBranco && !ehVotoCincoDigitos) {
            JOptionPane.showMessageDialog(this, "Insira os 5 dígitos ou selecione BRANCO.");
            return;
        }

        // Som intermediário no Governador ou melodia FIM no Presidente
        if ("Governador".equals(cargoAtual)) {
            SomUtil.tocarConfirmaIntermediario();
        } else {
            SomUtil.tocarFimUrna();
        }

        if (ehVotoBranco) {
            urna.registarVotoBranco(cargoAtual);
            FirebaseService.registarVoto(cargoAtual, "BRANCO", "0");
        } else {
            String numero = digitosDigitados.toString();
            Candidato candidato = urna.buscarCandidatoPorNumeroECargo(numero, cargoAtual);

            if (candidato != null) {
                candidato.incrementarVoto();
                FirebaseService.registarVoto(cargoAtual, "VALIDO", numero);
            } else {
                urna.registarVotoNulo(cargoAtual);
                FirebaseService.registarVoto(cargoAtual, "NULO", numero);
            }
        }

        avancarProximoCargo();
    }

    private void avancarProximoCargo() {
        if ("Governador".equals(cargoAtual)) {
            cargoAtual = "Presidente";
            labelCargoCandidato.setText("PRESIDENTE");
            digitosDigitados.setLength(0);
            atualizarEcraDigitos();
            labelNomeCandidato.setText("Nome: ");
            labelEspecialidadeCandidato.setText("Especialidade: ");
            labelPartidoCandidato.setText("Partido: ");
            labelFotoCandidato.setIcon(null);
            labelFotoCandidato.setText("FOTO");
        } else {
            JOptionPane.showMessageDialog(this, "Voto concluído nos dois cargos! Próximo eleitor.");
            cargoAtual = "Governador";
            labelCargoCandidato.setText("GOVERNADOR");
            digitosDigitados.setLength(0);
            atualizarEcraDigitos();
            labelNomeCandidato.setText("Nome: ");
            labelEspecialidadeCandidato.setText("Especialidade: ");
            labelPartidoCandidato.setText("Partido: ");
            labelFotoCandidato.setIcon(null);
            labelFotoCandidato.setText("FOTO");

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja encerrar a votação e ver o boletim?", "Finalizar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                solicitarEncerramento();
            }
        }
    }

    private void solicitarEncerramento() {
        String senha = JOptionPane.showInputDialog(this, "Digite a senha de encerramento (99999):");
        if ("99999".equals(senha)) {
            this.dispose();
            new TelaRelatorio(urna).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Senha incorreta. Votação continua ativa.");
        }
    }
}