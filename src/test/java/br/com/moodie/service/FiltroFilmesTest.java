package br.com.moodie.service;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.model.Filme;
import br.com.moodie.model.PerfilCinefilo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
class FiltroFilmesTest {
	private FiltroFilmes filtro;
    private PerfilCinefilo perfil;
    private Filme filmePadrao;

    @BeforeEach
    void setup() {
        filtro = new FiltroFilmes();
        perfil = new PerfilCinefilo();
        
        perfil.setPesoGenero(Genero.ACAO, 1.0);
        
        
        filmePadrao = new Filme("F01", "Filme Padrão", 2026, 120, 
                List.of(Genero.ACAO), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 80);
    }

    // CT12
    @Test
    @DisplayName("Deve remover o filme da lista quando ele já estiver no histórico de assistidos")
    void deve_RemoverFilme_Quando_JaFoiAssistido() {
      
        perfil.marcarComoAssistido("F01");
        List<Filme> catalogo = List.of(filmePadrao);

        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        assertTrue(resultado.isEmpty(), "O filme deveria ter sido filtrado pois já foi assistido");
    }

    // CT13
    @Test
    @DisplayName("Deve remover o filme quando a classificação etária for maior que a permitida")
    void deve_RemoverFilme_Quando_AcimaDaClassificacaoMaxima() {
        perfil.setClassificacaoMaxima(ClassificacaoEtaria.DOZE);
        Filme filmeParaMaiores = new Filme("F02", "Filme Adulto", 2026, 120, 
                List.of(Genero.ACAO), ClassificacaoEtaria.DEZOITO, Idioma.PT_BR, 80);
        
        List<Filme> catalogo = List.of(filmePadrao, filmeParaMaiores);

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

      
        assertEquals(1, resultado.size());
        assertFalse(resultado.contains(filmeParaMaiores), "O filme +18 deveria ter sido removido");
    }

    // CT14
    @Test
    @DisplayName("Deve remover o filme quando o idioma não estiver na lista de aceitos")
    void deve_RemoverFilme_Quando_IdiomaNaoAceito() {
       
        perfil.adicionarIdioma(Idioma.PT_BR);
        perfil.adicionarIdioma(Idioma.EN);
        
        Filme filmeFrances = new Filme("F03", "Le Film", 2026, 120, 
                List.of(Genero.DRAMA), ClassificacaoEtaria.LIVRE, Idioma.FR, 80);
        
        List<Filme> catalogo = List.of(filmePadrao, filmeFrances);

        
        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

       
        assertEquals(1, resultado.size());
        assertFalse(resultado.contains(filmeFrances), "O filme em francês deveria ter sido removido");
    }

    // CT15 
    @Test
    @DisplayName("Deve remover o filme quando ele possuir algum gênero com peso zero no perfil")
    void deve_RemoverFilme_Quando_GeneroTemPesoZero() {
        perfil.setPesoGenero(Genero.TERROR, 0.0);
        
        Filme filmeTerror = new Filme("F04", "O Susto", 2026, 120, 
                List.of(Genero.TERROR, Genero.DRAMA), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 80);
        
        List<Filme> catalogo = List.of(filmePadrao, filmeTerror);

        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        assertEquals(1, resultado.size());
        assertFalse(resultado.contains(filmeTerror), "O filme com gênero de peso zero deveria ser removido");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia e não nula quando o catálogo recebido for vazio")
    void deve_RetornarListaVazia_Quando_CatalogoVazio() {
     
        List<Filme> catalogoVazio = new ArrayList<>();

        List<Filme> resultado = filtro.filtrar(catalogoVazio, perfil);

        assertNotNull(resultado, "A lista retornada nunca deve ser null");
        assertTrue(resultado.isEmpty(), "A lista retornada deve ser vazia");
    }
}
