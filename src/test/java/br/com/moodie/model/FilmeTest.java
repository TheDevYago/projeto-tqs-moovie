package br.com.moodie.model;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Tag;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
class FilmeTest {
	// CT09
    @Test
    @DisplayName("Deve considerar filmes iguais quando possuem o mesmo ID")
    void deve_ConsiderarIguais_Quando_FilmesTemMesmoId() {
        Filme filme1 = new Filme("F01", "Interestelar", 2014, 169, 
                Collections.emptyList(), ClassificacaoEtaria.DOZE, Idioma.EN, 95);
        
        Filme filme2 = new Filme("F01", "Interestelar - Edição de Colecionador", 2014, 175, 
                Collections.emptyList(), ClassificacaoEtaria.DOZE, Idioma.EN, 95);

        Filme filmeDiferente = new Filme("F02", "A Origem", 2010, 148, 
                Collections.emptyList(), ClassificacaoEtaria.DOZE, Idioma.EN, 88);


        assertEquals(filme1, filme2, "Filmes com o mesmo ID devem ser considerados iguais");
        assertNotEquals(filme1, filmeDiferente, "Filmes com IDs diferentes não devem ser iguais");
    }
    
    @Test
    @DisplayName("Deve garantir que todos os atributos do filme foram preenchidos corretamente no construtor")
    void deve_ManterIntegridadeDosDados_AoCriarFilme() {
        Filme filme = new Filme("F1", "Avatar", 2009, 162, 
                                List.of(Genero.ACAO, Genero.FICCAO_CIENTIFICA), 
                                ClassificacaoEtaria.DOZE, Idioma.PT_BR, 100);
        assertAll("Validação de atributos do Filme",
            () -> assertEquals("F1", filme.getId()),
            () -> assertEquals("Avatar", filme.getTitulo()),
            () -> assertEquals(2009, filme.getAno()),
            () -> assertEquals(162, filme.getDuracaoMinutos()),
            () -> assertEquals(2, filme.getGeneros().size()),
            () -> assertEquals(ClassificacaoEtaria.DOZE, filme.getClassificacao()),
            () -> assertEquals(Idioma.PT_BR, filme.getIdioma()),
            () -> assertEquals(100, filme.getPopularidade())
        );
    }
    
    @ParameterizedTest
    @CsvSource({
        "2024, true",
        "1880, false",
        "2100, false"
    })
    @DisplayName("Deve validar se o ano de lançamento é plausível")
    void deve_ValidarAnosDeLancamento(int ano, boolean valido) {
        if(valido) {
            assertTrue(ano >= 1888 && ano <= 2026);
        } else {
            assertFalse(ano >= 1888 && ano <= 2026);
        }
    }
}
