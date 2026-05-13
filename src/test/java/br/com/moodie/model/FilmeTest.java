package br.com.moodie.model;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Idioma;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Tag("unitario")
class FilmeTest {
	// CT06
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
}
