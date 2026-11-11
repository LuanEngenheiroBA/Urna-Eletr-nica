/**
 * Universidade Centro Universitário Jorge Amado / Disciplina: Programação Orientada a Objetos
 * Trabalho Prático: AV3 - Urna Eletrônica Simplificada
 *
 * Descrição: Centraliza as regras de negócio da eleição, gerindo os candidatos,
 * a contagem de votos válidos, brancos e nulos por cargo, e o estado da votação.
 *
 * Padrão Arquitetural: MVC
 * Tecnologia: Java Swing & Firebase Firestore
 *
 * @author Luan Gabriel, Cauã dos Santos
 * @version 1.0
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class Urna {
    private List<Candidato> listaCandidatos;
    private int votosBrancosGovernador;
    private int votosNulosGovernador;
    private int votosBrancosPresidente;
    private int votosNulosPresidente;
    private boolean votacaoAtiva;

    public Urna() {
        this.listaCandidatos = new ArrayList<>();
        this.votosBrancosGovernador = 0;
        this.votosNulosGovernador = 0;
        this.votosBrancosPresidente = 0;
        this.votosNulosPresidente = 0;
        this.votacaoAtiva = false;
        inicializarCandidatos();
    }

    private void inicializarCandidatos() {
        listaCandidatos.add(new Candidato("10001", "Dr. Marcelo", "Médico Cirurgião", "Partido da Saúde (PS)", "Governador", "/fotos/candidato_10001_Dr_Marcelo.png"));
        listaCandidatos.add(new Candidato("10002", "Eng.ª Camila", "Engenheira Civil", "Partido da Infraestrutura (PI)", "Governador", "/fotos/candidato_10002_Eng_Camila.png"));
        listaCandidatos.add(new Candidato("10003", "Prof. Rodrigo", "Treinador Desportivo", "Partido dos Esportes (PEsp)", "Governador", "/fotos/candidato_10003_Prof_Rodrigo.png"));
        listaCandidatos.add(new Candidato("10004", "Prof.ª Helena", "Pedagoga", "Partido da Educação (PEd)", "Governador", "/fotos/candidato_10004_Prof_Helena.png"));

        listaCandidatos.add(new Candidato("20001", "Dra. Juliana", "Advogada", "Partido da Justiça (PJ)", "Presidente", "/fotos/candidato_20001_Dra_Juliana.png"));
        listaCandidatos.add(new Candidato("20002", "Dr. Lucas", "Psicólogo Clínico", "Partido do Bem-Estar (PBE)", "Presidente", "/fotos/candidato_20002_Dr_Lucas.png"));
        listaCandidatos.add(new Candidato("20003", "Dr. Arthur", "Biólogo e Pesquisador", "Partido da Ciência (PCi)", "Presidente", "/fotos/candidato_20003_Dr_Arthur.png"));
        listaCandidatos.add(new Candidato("20004", "Fernanda", "Treinadora de Alta Performance", "Partido dos Esportes (PEsp)", "Presidente", "/fotos/candidato_20004_Fernanda.png"));
    }

    public Candidato buscarCandidatoPorNumeroECargo(String numero, String cargo) {
        for (Candidato c : listaCandidatos) {
            if (c.obterNumero().equals(numero) && c.obterCargo().equalsIgnoreCase(cargo)) {
                return c;
            }
        }
        return null;
    }

    public void registarVotoBranco(String cargo) {
        if ("Governador".equalsIgnoreCase(cargo)) {
            votosBrancosGovernador++;
        } else {
            votosBrancosPresidente++;
        }
    }

    public void registarVotoNulo(String cargo) {
        if ("Governador".equalsIgnoreCase(cargo)) {
            votosNulosGovernador++;
        } else {
            votosNulosPresidente++;
        }
    }

    public List<Candidato> obterListaCandidatos() { return listaCandidatos; }
    public int obterVotosBrancosGovernador() { return votosBrancosGovernador; }
    public int obterVotosNulosGovernador() { return votosNulosGovernador; }
    public int obterVotosBrancosPresidente() { return votosBrancosPresidente; }
    public int obterVotosNulosPresidente() { return votosNulosPresidente; }

    public boolean isVotacaoAtiva() { return votacaoAtiva; }
    public void setVotacaoAtiva(boolean ativa) { this.votacaoAtiva = ativa; }
}