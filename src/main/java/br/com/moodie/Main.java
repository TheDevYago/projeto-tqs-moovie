package br.com.moodie;

import br.com.moodie.enums.Genero;
import br.com.moodie.model.Recomendacao;
import br.com.moodie.model.Usuario;
import br.com.moodie.service.*;
import br.com.moodie.util.GeradorAleatorio;

import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("=========================================");
		System.out.println("BEM-VINDO AO MOOVIE!");
		System.out.println("=========================================");
		
		System.out.print("Para Começarmos, Como Você se Chama: ");
		String nome = scanner.nextLine();
		
		Usuario usuarioAtivo = new Usuario(nome, 20);
		
		for (Genero g : Genero.values()) {
			usuarioAtivo.getPerfil().setPesoGenero(g, 0.5);
		}
		
		System.out.print("Olá, " + nome + "! Quanto tempo você tem para assistir ao filme: ");
		int tempoDisponivel = scanner.nextInt();
		usuarioAtivo.getPerfil().setFaixaDuracao(0, tempoDisponivel);
		
		System.out.println("\nQual é a tua vibe para hoje?");
		System.out.println("1 - Ação | 2 - Comédia | 3 - Ficção Científica | 4 - Terror | 5 - Romance");
		System.out.print("Digita o número correspondente: ");
		
		int opcaoGenero = scanner.nextInt();
		
		switch (opcaoGenero) {
			case 1: usuarioAtivo.getPerfil().setPesoGenero(Genero.ACAO, 1.0); break;
			case 2: usuarioAtivo.getPerfil().setPesoGenero(Genero.COMEDIA, 1.0); break;
			case 3: usuarioAtivo.getPerfil().setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0); break;
			case 4: usuarioAtivo.getPerfil().setPesoGenero(Genero.TERROR, 1.0); break;
			case 5: usuarioAtivo.getPerfil().setPesoGenero(Genero.ROMANCE, 1.0); break;
			default: System.out.println("Opção inválida, vamos procurar de tudo um pouco!"); break;
		}
		
		System.out.println("\n A processar as tuas respostas e a consultar a nosso catalogo");
		
		CatalogoFilmeAPI catalogo = new CatalogoMock();
		CalculadoraScore calculadora = new CalculadoraScore();
		FiltroFilmes filtro = new FiltroFilmes();
		GeradorAleatorio gerador = new GeradorAleatorio() {
			@Override
			public int sortearInteiro(int min, int max) {
				return min + (int)(Math.random() * ((max - min) + 1));
			}
		};
		HistoricoUsuarioRepository historicoConsole = (u, recomendacoes) -> {};
		NotificadorPush notificadorConsole = u -> {};
		RecomendadorService recomendador = new RecomendadorService(catalogo, historicoConsole, notificadorConsole, gerador, calculadora, filtro);
		List<Recomendacao> recomendacoes = recomendador.recomendar(usuarioAtivo, 3);
		
		System.out.println("\n=========================================");
		if(recomendacoes.isEmpty()) {
			System.out.println("❌ Poxa, não encontrámos nenhum filme com esses critérios exatos hoje.");
		} else {
			System.out.println("🌟 A TUA CURADORIA PERFEITA PARA HOJE:");
			for (Recomendacao r : recomendacoes) {
				System.out.println("🍿 " + r.getFilme().getTitulo());
				System.out.println("   ⭐ Score de Afinidade: " + r.getScore() + "/100");
				System.out.println("   ⏱️ Duração: " + r.getFilme().getDuracaoMinutos() + " min");
				System.out.println("   ---");
			}
		}
		System.out.println("=========================================");
		scanner.close();
	}
}