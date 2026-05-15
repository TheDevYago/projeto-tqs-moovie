package br.com.moodie;

import br.com.moodie.enums.*;
import br.com.moodie.model.*;
import br.com.moodie.service.*;
import br.com.moodie.util.GeradorAleatorio;
import java.util.List;
import java.util.Scanner;

public class MoodieApp {

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        imprimirHeader();

        // 1. Setup (Injeção de Dependência)
        CatalogoFilmeAPI catalogo = new CatalogoMock();
        CalculadoraScore calculadora = new CalculadoraScore();
        FiltroFilmes filtro = new FiltroFilmes();
        
        // Lambdas para as interfaces de infra
        HistoricoUsuarioRepository repo = (u, recs) -> {}; 
        NotificadorPush notificador = (u) -> {}; 
        GeradorAleatorio gerador = (min, max) -> (int) (Math.random() * (max - min + 1) + min);

        RecomendadorService moodie = new RecomendadorService(
            catalogo, repo, notificador, gerador, calculadora, filtro
        );

        // 2. Entrada de Dados
        System.out.print("Nome do cinéfilo: ");
        String nome = leitor.nextLine();
        Usuario usuario = new Usuario(nome, 21);
        PerfilCinefilo perfil = usuario.getPerfil();

        System.out.println("\n--- O QUE VOCÊ QUER ASSISTIR HOJE? ---");
        System.out.println("Gêneros disponíveis: ACAO, DRAMA, FICCAO_CIENTIFICA, TERROR, COMEDIA");
        System.out.print("Sua preferência nº1: ");
        try {
            Genero g = Genero.valueOf(leitor.nextLine().toUpperCase());
            perfil.setPesoGenero(g, 1.0);
        } catch (Exception e) {
            perfil.setPesoGenero(Genero.ACAO, 1.0); // Fallback
        }

        System.out.print("Duração máxima desejada (min): ");
        int duracaoMax = Integer.parseInt(leitor.nextLine());
        perfil.setFaixaDuracao(60, duracaoMax);

        // Configurações padrão para garantir que os filmes do Mock passem
        perfil.adicionarIdioma(Idioma.PT_BR);
        perfil.adicionarIdioma(Idioma.EN);
        perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZOITO);

        // 3. O "Pulo do Gato": Garantir 3 Opções
        System.out.println("\n🔍 Gerando seu TOP 3 personalizado...");

        try {
            // Chamada oficial pedindo 3
            List<Recomendacao> resultado = moodie.recomendar(usuario, 3);

            if (resultado.size() < 3) {
                System.out.println("⚠️ Seus filtros foram muito restritos! Mostrando o que temos disponível:");
            }

            System.out.println("\n==================================================");
            System.out.println("             🍿 SUAS 3 RECOMENDAÇÕES             ");
            System.out.println("==================================================");

            for (int i = 0; i < resultado.size(); i++) {
                imprimirCard(resultado.get(i), i + 1);
            }

            // Se o catálogo for pequeno e não tiver 3, avisamos
            if (resultado.size() < 3) {
                System.out.println("\n💡 Dica: Adicione mais filmes ao CatalogoMock para ver mais opções!");
            }

        } catch (Exception e) {
            System.err.println("💥 Erro técnico: " + e.getMessage());
        } finally {
            leitor.close();
        }
    }

    private static void imprimirHeader() {
        System.out.println("==================================================");
        System.out.println("          MOODIE AI - RANKING DE CINEMA           ");
        System.out.println("==================================================");
    }

    private static void imprimirCard(Recomendacao rec, int rank) {
        Filme f = rec.getFilme();
        String medalha = (rank == 1) ? "🥇" : (rank == 2 ? "🥈" : "🥉");
        
        System.out.printf("%s %-15s | %d pts | %d min | %s\n", 
                          medalha, 
                          f.getTitulo(), 
                          rec.getScore(), // Usando %d porque seu score é Integer
                          f.getDuracaoMinutos(),
                          f.getGeneros().get(0));
    }
}