package br.com.moodie.service;

import br.com.moodie.enums.Genero;
import br.com.moodie.model.Filme;
import br.com.moodie.model.PerfilCinefilo;
/**
 * Componente responsável pelo cálculo do score de afinidade de um filme para um perfil específico.
 */
public class CalculadoraScore {
	private static final double PESO_GENERO = 0.50;
	private static final double PESO_DURACAO = 0.20;
	private static final double PESO_POPULARIDADE = 0.15;
	private static final double PESO_AFINIDADE = 0.15;
	/**
	 * Calcula a pontuação final de um filme (0 a 100) para o perfil fornecido.
	 * @param filme
	 * @param perfil
	 * @return
	 */
	public int calcular (Filme filme, PerfilCinefilo perfil) {
		double scoreGenero = calcularScoreGenero(filme, perfil);
		double scoreDuracao = calcularScoreDuracao(filme, perfil);
		double scorePopularidade = filme.getPopularidade();
		double scoreAfinidade = calcularScoreAfinidade(filme, perfil);
		double pontuacaoFinal = (scoreGenero * PESO_GENERO) + (scoreDuracao * PESO_DURACAO) + (scorePopularidade * PESO_POPULARIDADE) + (scoreAfinidade * PESO_AFINIDADE);
		
		int scoreArredondado = (int) Math.round(pontuacaoFinal);
		return Math.max(0, Math.min(100, scoreArredondado));
	}
	
	private double calcularScoreGenero(Filme filme, PerfilCinefilo perfil) {
		if (filme.getGeneros().isEmpty()) return 0.0;
		
		double somaPesos = 0.0;
		for (Genero genero : filme.getGeneros()) {
			somaPesos += perfil.getPesoGenero(genero);
		}
		
		return (somaPesos / filme.getGeneros().size()) * 100; // media pond. convertida para a escala
	}
	
	private double calcularScoreDuracao(Filme filme, PerfilCinefilo perfil) {
		int duracao = filme.getDuracaoMinutos();
		int minimo = perfil.getDuracaoMinima();
		int maximo = perfil.getDuracaoMaxima();
		
		if (duracao >= minimo && duracao <= maximo) {
			return 100.0;
		}
		
		int diferenca = (duracao < minimo) ? (minimo - duracao) : (duracao - maximo);
		return Math.max(0.0, 100.0 - diferenca);
	}
	
	private double calcularScoreAfinidade(Filme filme, PerfilCinefilo perfil) {
		boolean temNotaAlta = perfil.getHistoricoAssistidos().stream().map(id -> perfil.getNotaPara(id)).anyMatch(nota -> nota != null && nota >= 4);
		return temNotaAlta ? 100.0 : 0.0;
	}
}
