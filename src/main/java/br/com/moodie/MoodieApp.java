package br.com.moodie;

import br.com.moodie.enums.*;
import br.com.moodie.model.*;
import br.com.moodie.service.*;
import br.com.moodie.util.GeradorAleatorio;
import java.util.*;

public class MoodieApp {

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        
        // 1. Setup Interno do Sistema
        CatalogoFilmeAPI catalogo = new CatalogoMock();
        CalculadoraScore calculadora = new CalculadoraScore();
        FiltroFilmes filtro = new FiltroFilmes();
        HistoricoUsuarioRepository repo = (u, recs) -> {}; 
        NotificadorPush notificador = (u) -> {}; 
        GeradorAleatorio gerador = (min, max) -> (int) (Math.random() * (max - min + 1) + min);

        RecomendadorService moodie = new RecomendadorService(
            catalogo, repo, notificador, gerador, calculadora, filtro
        );

        System.out.println("==================================================");
        System.out.println("          BEM-VINDO AO MOODIE SELECTOR            ");
        System.out.println("==================================================\n");

        // 2. Cadastro do Usuário
        System.out.print("Digite seu nome: ");
        String nome = leitor.nextLine();
        System.out.print("Digite sua idade: ");
        int idade = Integer.parseInt(leitor.nextLine());

        Usuario usuario = new Usuario(nome, idade);
        PerfilCinefilo perfil = usuario.getPerfil();

        // 3. Definição de Pesos de Gênero (Dinâmico)
        System.out.println("\n--- CONFIGURAÇÃO DE PREFERÊNCIAS ---");
        System.out.println("Para cada gênero, dê uma nota de 0.0 (odeio) a 1.0 (amo):");
        
        for (Genero g : Genero.values()) {
            System.out.print(" > " + g + ": ");
            try {
                double peso = Double.parseDouble(leitor.nextLine());
                perfil.setPesoGenero(g, peso);
            } catch (Exception e) {
                System.out.println("   [Aviso] Valor inválido. Definindo como 0.0");
                perfil.setPesoGenero(g, 0.0);
            }
        }

        // 4. Definição de Tempo e Restrições
        System.out.println("\n--- RESTRIÇÕES DE FILME ---");
        System.out.print("Tempo MÍNIMO que você tem (min): ");
        int tMin = Integer.parseInt(leitor.nextLine());
        System.out.print("Tempo MÁXIMO que você tem (min): ");
        int tMax = Integer.parseInt(leitor.nextLine());
        perfil.setFaixaDuracao(tMin, tMax);

        System.out.println("Qual classificação máxima você aceita?");
        System.out.println("Opções: LIVRE, DEZ, DOZE, QUATORZE, DEZESSEIS, DEZOITO");
        try {
            String classifStr = leitor.nextLine().toUpperCase();
            perfil.setClassificacaoMaxima(ClassificacaoEtaria.valueOf(classifStr));
        } catch (Exception e) {
            perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZOITO);
        }

        System.out.println("Quais idiomas você aceita? (Separe por vírgula. Ex: PT_BR, EN)");
        String[] ids = leitor.nextLine().toUpperCase().split(",");
        for (String id : ids) {
            try {
                perfil.adicionarIdioma(Idioma.valueOf(id.trim()));
            } catch (Exception e) { /* ignora idiomas inválidos */ }
        }

        // 5. O Resultado: Sorteio de Filmes
        System.out.println("\n🔍 CRUZANDO DADOS E GERANDO RECOMENDAÇÕES...");
        
        try {
            List<Recomendacao> recomendacoes = moodie.recomendar(usuario, 3);

            if (recomendacoes.isEmpty()) {
                System.out.println("❌ Poxa, nenhum filme do catálogo bate com todos os seus filtros atuais.");
            } else {
                System.out.println("\n⭐ TOP " + recomendacoes.size() + " FILMES PARA VOCÊ AGORA:");
                System.out.println("--------------------------------------------------");
                for (int i = 0; i < recomendacoes.size(); i++) {
                    Recomendacao rec = recomendacoes.get(i);
                    Filme f = rec.getFilme();
                    System.out.printf("%dº | %s (%d)\n", (i+1), f.getTitulo(), f.getAno());
                    System.out.printf("   Score: %d pts | Gênero: %s | %d min\n", 
                                      rec.getScore(), f.getGeneros().get(0), f.getDuracaoMinutos());
                    System.out.println("--------------------------------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("💥 Erro ao processar: " + e.getMessage());
        } finally {
            leitor.close();
        }
    }
}