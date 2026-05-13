package br.com.moodie.service;

import br.com.moodie.model.Filme;
import br.com.moodie.model.Recomendacao;
import br.com.moodie.model.Usuario;
import br.com.moodie.util.GeradorAleatorio;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Serviço mestre de recomendação que orquestra a busca, filtragem e classificação de filmes.
 */
public class RecomendadorService {
	private final CatalogoFilmeAPI catalogo;
	private final HistoricoUsuarioRepository historicoRepositorio;
	private final NotificadorPush notificador;
	private final GeradorAleatorio gerador;
	private final CalculadoraScore calculadora;
	private final FiltroFilmes filtro;
	
	public RecomendadorService (CatalogoFilmeAPI catalogo, HistoricoUsuarioRepository historicoRepositorio, NotificadorPush notificador, GeradorAleatorio gerador, CalculadoraScore calculadora, FiltroFilmes filtro) {
		this.catalogo = catalogo;
		this.historicoRepositorio = historicoRepositorio;
		this.notificador = notificador;
		this.gerador = gerador;
		this.calculadora = calculadora;
		this.filtro = filtro;
	}
	
	/**
	 * Gera uma lista ordenada das melhores recomendações para o usuário.
	 * @param usuario
	 * @param topN
	 * @return
	 */
	public List<Recomendacao> recomendar(Usuario usuario, int topN) {
		List<Filme> filmesDisponiveis = buscarFilmesSeguro();
		List<Filme> filmesFiltrados = filtro.filtrar(filmesDisponiveis, usuario.getPerfil());
		
		if (filmesFiltrados.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Recomendacao> recomendacoes = gerarRecomendacaoOrdenadas(filmesFiltrados, usuario, topN);
		finalizarProcessoRecomendacao(usuario, recomendacoes);
		
		return recomendacoes;
	}
	
	/**
	 * Modo "Surpreenda-me": Sorteia um único filme aleatório que atenda aos filtros do usuário.
	 * @param usuario
	 * @return
	 */
	public Optional<Recomendacao> recomendarAleatorio(Usuario usuario) {
		List <Filme> filmesDisponiveis = buscarFilmesSeguro();
		List <Filme> filmesFiltrados = filtro.filtrar(filmesDisponiveis, usuario.getPerfil());
		
		if (filmesFiltrados.isEmpty()) {
			return Optional.empty();
		}
		
		Recomendacao recomendacao = sortearRecomendacao(filmesFiltrados, usuario);
		historicoRepositorio.registrarRecomendacao(usuario, List.of(recomendacao));
		
		return Optional.of(recomendacao);
	}
	
	private List<Filme> buscarFilmesSeguro() {
		try {
			return catalogo.buscarTodos();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
	private List<Recomendacao> gerarRecomendacaoOrdenadas (List<Filme> filmes, Usuario usuario, int topN) {
		return filmes.stream().map(filme -> criarRecomendacao(filme, usuario)).sorted(this::compararRecomendacoes).limit(topN).collect(Collectors.toList());
	}
	
	private Recomendacao criarRecomendacao(Filme filme, Usuario usuario) {
		int score = calculadora.calcular(filme, usuario.getPerfil());
		return new Recomendacao(filme, score, "Recomendado com base nas suas preferencias!");
	}
	
	private int compararRecomendacoes(Recomendacao r1, Recomendacao r2) {
		int cmpScore = Integer.compare(r2.getScore(), r1.getScore());
		if (cmpScore != 0) return cmpScore;
		
		int cmpPop = Integer.compare(r2.getFilme().getPopularidade(), r1.getFilme().getPopularidade());
		if (cmpPop != 0) return cmpPop;
		
		return gerador.sortearInteiro(-1, 1);
	}
	
	private void finalizarProcessoRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes) {
		historicoRepositorio.registrarRecomendacao(usuario, recomendacoes);
		
		if (usuario.isNotificacoesAtivas()) {
			try {
				notificador.enviar(usuario);
			} catch (Exception ignored) {
			}
		}
	}
	
	private Recomendacao sortearRecomendacao(List<Filme> filmesFiltrados, Usuario usuario) {
		int indexSorteado = gerador.sortearInteiro(0, filmesFiltrados.size() - 1);
		Filme filmeSorteado = filmesFiltrados.get(indexSorteado);
		
		int score = calculadora.calcular(filmeSorteado, usuario.getPerfil());
		return new Recomendacao(filmeSorteado, score, "Modo Surpreenda-me");
	}
}
