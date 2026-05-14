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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	
    // CT10
	@Test
	@DisplayName("Score de genero deve ser 100 quando todos os generos do filme tem peso 1.0 no perfil")
	void deve_RetornarScoreMaximoDeGenero_Quando_FilmeTemApenasGenerosAmados() {
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfil.setPesoGenero(Genero.DRAMA, 1.0);
		
		Filme filme = new Filme("F1", "Interestelar", 2014, 120, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.EN, 0);
		
		int score = calculadora.calcular(filme, perfil);
		
		assertEquals(70, score);
	}
	
    // CT11
	@ParameterizedTest
	@CsvSource({"120, 70", "160, 68", "80, 68", "250, 50"})
	@DisplayName("Deve reduzir o score de duracao proporcionalmente quando fora da faixa")
	void deve_ReduzirScore_Quando_DuracaoForaDaFaixa(int duracaoFilme, int scoreEsperado) {
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		Filme filme = new Filme("F2", "Teste Duracao", 2026, duracaoFilme, List.of(Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 0);
		
		int score = calculadora.calcular(filme, perfil);
		
		assertEquals(scoreEsperado, score);
	}
	
	@Test
	@DisplayName("Deve retornar score baixo quando o gênero tem peso reduzido no perfil")
	void deve_RetornarScoreBaixo_Quando_GeneroNaoEhPreferido() {
	    perfil.setPesoGenero(Genero.TERROR, 0.2);
	    Filme filme = new Filme("F1", "Terror", 2024, 120, List.of(Genero.TERROR), 
	                            ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 0);
	    
	    int score = calculadora.calcular(filme, perfil);
	    assertEquals(30, score, "O score deveria ser 30 para um peso de gênero 0.4");
	}
	
	@Test
	@DisplayName("Deve reduzir o score quando a duração ultrapassa exatamente 30 min da preferência")
	void deve_ReduzirScore_Quando_DuracaoTrintaMinAcimaDoLimite() {
	    Filme filmeTrintaMinAcima = new Filme("F2", "Limite", 2024, 180, 
	                                          List.of(Genero.FICCAO_CIENTIFICA), 
	                                          ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 0);
	    perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
	    
	    int score = calculadora.calcular(filmeTrintaMinAcima, perfil);
	    
	    assertTrue(score < 100, "O score deve sofrer penalidade por estar 30 min acima");
	}
	
	@Test
	@DisplayName("Deve garantir que o score final permaneça no intervalo [0, 100]")
	void deve_ManterScoreDentroDosLimites() {
	    perfil.setPesoGenero(Genero.ACAO, 1.0);
	    Filme filmeLongo = new Filme("F3", "Infinito", 2024, 9999, List.of(Genero.ACAO), 
	                                 ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 0);
	    
	    int score = calculadora.calcular(filmeLongo, perfil);
	    
	    assertTrue(score >= 0 && score <= 100, "O score nunca deve ser negativo ou maior que 100");
	}
}
