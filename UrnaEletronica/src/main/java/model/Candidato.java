/**
 * Universidade Centro Universitário Jorge Amado / Disciplina: Programação Orientada a Objetos
 * Trabalho Prático: AV3 - Urna Eletrônica Simplificada
 *
 * Descrição: Representa a entidade Candidato contendo número, nome,
 * especialidade, partido, cargo, caminho da foto e total de votos.
 *
 * Padrão Arquitetural: MVC
 * Tecnologia: Java Swing & Firebase Firestore
 *
 * @author Luan Gabriel, Cauã dos Santos
 * @version 1.0
 */
package model;

public class Candidato {
    private String numero;
    private String nome;
    private String especialidade;
    private String partido;
    private String cargo;
    private String caminhoFoto;
    private int quantidadeVotos;

    public Candidato(String numero, String nome, String especialidade, String partido, String cargo, String caminhoFoto) {
        this.numero = numero;
        this.nome = nome;
        this.especialidade = especialidade;
        this.partido = partido;
        this.cargo = cargo;
        this.caminhoFoto = caminhoFoto;
        this.quantidadeVotos = 0;
    }

    public String obterNumero() { return numero; }
    public String obterNome() { return nome; }
    public String obterEspecialidade() { return especialidade; }
    public String obterPartido() { return partido; }
    public String obterCargo() { return cargo; }
    public String obterCaminhoFoto() { return caminhoFoto; }
    public int obterQuantidadeVotos() { return quantidadeVotos; }

    public void incrementarVoto() {
        this.quantidadeVotos++;
    }
}