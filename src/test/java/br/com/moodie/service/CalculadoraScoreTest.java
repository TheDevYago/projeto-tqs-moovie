package br.com.moodie.service;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.model.Filme;
import br.com.moodie.model.PerfilCinefilo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unitario")
class CalculadoraScoreTest {
	private CalculadoraScore calculadora;
	private PerfilCinefilo perfil;
	
	@BeforeEach
	void setup() {
		calculadora = new CalculadoraScore();
		perfil = new PerfilCinefilo();
		perfil.setFaixaDuracao(90, 150);
	}
	
	@Test
	@DisplayName("Score de genero deve ser 100 quando todos os generos do filme tem peso 1.0 no perfil")
	void deve_RetornarScoreMaximoDeGenero_Quando_FilmeTemApenasGenerosAmados() {
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfil.setPesoGenero(Genero.DRAMA, 1.0);
		
		Filme filme = new Filme("F1", "Interestelar", 2014, 120, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.EN, 0);
		
		int score = calculadora.calcular(filme, perfil);
		
		assertEquals(70, score);
	}
	
	@ParameterizedTest
	@CsvSource({"120, 70", "160, 68", "80, 68", "250, 50"})
	@DisplayName("Deve reduzir o score de duracao proporcionalmente quando fora da faixa")
	void deve_ReduzirScore_Quando_DuracaoForaDaFaixa(int duracaoFilme, int scoreEsperado) {
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		Filme filme = new Filme("F2", "Teste Duracao", 2026, duracaoFilme, List.of(Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 0);
		
		int score = calculadora.calcular(filme, perfil);
		
		assertEquals(scoreEsperado, score);
	}
}
