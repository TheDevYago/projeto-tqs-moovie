package br.com.moodie.service;

import java.util.List;

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
	public int calcular (Filme filme, PerfilCinefilo perfil, List<Filme> catalogoCompleto) {
		double scoreGenero = calcularScoreGenero(filme, perfil);
		double scoreDuracao = calcularScoreDuracao(filme, perfil);
		double scorePopularidade = filme.getPopularidade();
		double scoreAfinidade = calcularScoreAfinidade(filme, perfil, catalogoCompleto);
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
		
		return (somaPesos / filme.getGeneros().size()) * 100;
	}
	
	private double calcularScoreDuracao(Filme filme, PerfilCinefilo perfil) {
		int duracao = filme.getDuracaoMinutos();
		int minimo = perfil.getDuracaoMinima();
		int maximo = perfil.getDuracaoMaxima();
		// dentro da faixa, ta perfeito
		if (duracao >= minimo && duracao <= maximo) {
			return 100.0;
		}
		// fora da faixa de tempo, perde ponto a cada min extra
		int diferenca = (duracao < minimo) ? (minimo - duracao) : (duracao - maximo);
		return Math.max(0.0, 100.0 - diferenca);
	}
	
	private double calcularScoreAfinidade(Filme filmeAlvo, PerfilCinefilo perfil, List<Filme> catalogo) {
        for (Genero genero : filmeAlvo.getGeneros()) {
            // verifica se o usuário deu nota >= 4 para ALGUM filme que possui ESTE gênero
            boolean temAfinidade = perfil.getNotas().entrySet().stream()
                .filter(entry -> entry.getValue() >= 4) // Pega as notas altas
                .map(entry -> entry.getKey()) // Pega os IDs dos filmes avaliados
                .flatMap(id -> catalogo.stream().filter(f -> f.getId().equals(id))) // Encontra o filme no catálogo
                .anyMatch(filmePassado -> filmePassado.getGeneros().contains(genero)); // O filme passado tem o gênero alvo?

            if (temAfinidade) {
                return 100.0; // Achou um filme do mesmo gênero com nota alta! Bônus máximo.
            }
        }
        return 0.0;
    }
}
