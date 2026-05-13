package br.com.moodie.service;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.model.Filme;
import br.com.moodie.model.Recomendacao;
import br.com.moodie.model.Usuario;
import br.com.moodie.util.GeradorAleatorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RecomendadorServiceTest {
	// Nossos "Dublês" - Eles fingem ser os sistemas externos
    @Mock private CatalogoFilmeAPI catalogoMock;
    @Mock private HistoricoUsuarioRepository historicoRepoMock;
    @Mock private NotificadorPush notificadorMock;
    @Mock private GeradorAleatorio geradorMock;

    private RecomendadorService service;
    private Usuario usuario;
    private Filme filmePadrao;

    @BeforeEach
    void setup() {
        CalculadoraScore calculadoraReal = new CalculadoraScore();
        FiltroFilmes filtroReal = new FiltroFilmes();

        service = new RecomendadorService(catalogoMock, historicoRepoMock, 
                                          notificadorMock, geradorMock, 
                                          calculadoraReal, filtroReal);

        usuario = new Usuario("Maria", 25);
        usuario.getPerfil().setPesoGenero(Genero.ACAO, 1.0); // Para o filme não ser cortado

        filmePadrao = new Filme("F01", "Duro de Matar", 1988, 132, 
                List.of(Genero.ACAO), ClassificacaoEtaria.DOZE, Idioma.EN, 90);
    }

    // CT19:
    @Test
    @DisplayName("Deve salvar a lista de recomendações no repositório de histórico")
    void deve_SalvarNoHistorico_Quando_RecomendarComSucesso() throws Exception {
        // Ensinando o dublê: "Quando pedirem o catálogo, devolva essa lista com 1 filme"
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));

        List<Recomendacao> resultado = service.recomendar(usuario, 5);

        assertFalse(resultado.isEmpty());
        verify(historicoRepoMock, times(1)).registrarRecomendacao(eq(usuario), anyList());
    }

    // CT20:
    @Test
    @DisplayName("Deve enviar notificação push apenas se o usuário estiver com notificações ativas")
    void deve_EnviarNotificacao_Quando_UsuarioTemNotificacaoAtiva() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));
        usuario.setNotificacoesAtivas(true);

        service.recomendar(usuario, 5);

        verify(notificadorMock, times(1)).enviar(usuario);
    }
    
    // CT17 
    @Test
    @DisplayName("Deve retornar lista vazia e não quebrar se a API do catálogo cair")
    void deve_RetornarListaVazia_Quando_ApiLancarExcecao() throws Exception {
        
        when(catalogoMock.buscarTodos()).thenThrow(new RuntimeException("API Fora do Ar"));

       
        List<Recomendacao> resultado = service.recomendar(usuario, 5);

        
        assertTrue(resultado.isEmpty(), "Deve retornar lista vazia se a API falhar");
       
        verify(historicoRepoMock, never()).registrarRecomendacao(any(), any());
        verify(notificadorMock, never()).enviar(any());
    }
}
