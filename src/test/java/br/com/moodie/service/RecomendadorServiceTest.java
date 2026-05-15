package br.com.moodie.service;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.model.Filme;
import br.com.moodie.model.PerfilCinefilo;
import br.com.moodie.model.Recomendacao;
import br.com.moodie.model.Usuario;
import br.com.moodie.util.GeradorAleatorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("integracao")
@ExtendWith(MockitoExtension.class)
class RecomendadorServiceTest {
    @Mock private CatalogoFilmeAPI catalogoMock;
    @Mock private HistoricoUsuarioRepository historicoRepoMock;
    @Mock private NotificadorPush notificadorMock;
    @Mock private GeradorAleatorio geradorMock;
    
    @InjectMocks private RecomendadorService service;

    private CalculadoraScore calculadoraSpy;
    private Usuario usuario;
    private Filme filmePadrao;
    private PerfilCinefilo perfil;

    @BeforeEach
    void setup() {
    	calculadoraSpy = spy(new CalculadoraScore());
        FiltroFilmes filtroReal = new FiltroFilmes();

        service = new RecomendadorService(catalogoMock, historicoRepoMock, 
                                          notificadorMock, geradorMock, 
                                          calculadoraSpy, filtroReal);

        usuario = new Usuario("Yago", 25);
        usuario.getPerfil().setPesoGenero(Genero.ACAO, 1.0);
        filmePadrao = new Filme("F01", "Duro de Matar", 1988, 132, List.of(Genero.ACAO), ClassificacaoEtaria.DOZE, Idioma.EN, 90);
    }
    
    @AfterEach
    @DisplayName("Limpeza pós-teste")
    void tearDown() {
    	usuario = null;
        if (calculadoraSpy != null) {
            Mockito.reset(calculadoraSpy);
            Mockito.reset(catalogoMock, historicoRepoMock, notificadorMock);
        } 
    }

    // CT17:
    @Test
    @DisplayName("Deve salvar a lista de recomendações no repositório de histórico")
    void deve_SalvarNoHistorico_Quando_RecomendarComSucesso() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));

        List<Recomendacao> resultado = service.recomendar(usuario, 5);

        assertFalse(resultado.isEmpty());
        verify(historicoRepoMock, times(1)).registrarRecomendacao(eq(usuario), anyList());
    }

    // CT18:
    @Test
    @DisplayName("Deve enviar notificação push apenas se o usuário estiver com notificações ativas")
    void deve_EnviarNotificacao_Quando_UsuarioTemNotificacaoAtiva() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));
        usuario.setNotificacoesAtivas(true);

        service.recomendar(usuario, 5);

        verify(notificadorMock, times(1)).enviar(usuario);
    }
    
    // CT19
    @Test
    @DisplayName("Deve retornar lista vazia e não quebrar se a API do catálogo cair")
    void deve_RetornarListaVazia_Quando_ApiLancarExcecao() throws Exception {
        when(catalogoMock.buscarTodos()).thenThrow(new RuntimeException("API Fora do Ar"));

        List<Recomendacao> resultado = service.recomendar(usuario, 5);
        
        assertTrue(resultado.isEmpty(), "Deve retornar lista vazia se a API falhar");
        verify(historicoRepoMock, never()).registrarRecomendacao(any(), any());
        verify(notificadorMock, never()).enviar(any());
    }
    
    // CT20
    @Test
    @DisplayName("Deve garantir que o cálculo real foi executado pelo Spy")
    void deve_ExecutarCalculoReal_Quando_Recomendar() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));

        service.recomendar(usuario, 5);

        verify(calculadoraSpy, atLeastOnce()).calcular(any(Filme.class), any(), anyList());
    }
    
    // CT21
    @Test
    @DisplayName("Deve ignorar notificação se o usuário desativou o recurso")
    void deve_NaoEnviarNotificacao_Quando_UsuarioTemNotificacaoInativa() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));
        usuario.setNotificacoesAtivas(false);

        service.recomendar(usuario, 5);

        verify(notificadorMock, never()).enviar(any());
    }
    
    @Test
    @DisplayName("Deve respeitar o tamanho N e ordenar por score decrescente")
    void deve_OrdenarPorScore_ERespeitarLimite() throws Exception {
        Filme ruim = new Filme("1", "Ruim", 2024, 120, List.of(Genero.DRAMA), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 10);
        Filme bom = filmePadrao;
        
        when(catalogoMock.buscarTodos()).thenReturn(List.of(ruim, bom));

        List<Recomendacao> resultado = service.recomendar(usuario, 1);

        assertAll(
            () -> assertEquals(1, resultado.size(), "Deve respeitar o limite N=1"),
            () -> assertEquals("F01", resultado.get(0).getFilme().getId(), "O melhor filme deve vir primeiro")
        );
    }
    
    @Test
    @DisplayName("Deve usar popularidade como critério de desempate para scores iguais")
    void deve_DesempatarPorPopularidade() throws Exception {
        Filme popular = new Filme("P", "Popular", 2024, 120, List.of(Genero.ACAO), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 500);
        Filme comum = new Filme("C", "Comum", 2024, 120, List.of(Genero.ACAO), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 50);
        
        when(catalogoMock.buscarTodos()).thenReturn(List.of(comum, popular));

        List<Recomendacao> resultado = service.recomendar(usuario, 10);

        assertEquals("P", resultado.get(0).getFilme().getId(), "O filme mais popular deve vencer o empate de score");
    }
    
    @Test
    @DisplayName("Deve retornar recomendação vazia quando o catálogo da API for vazio")
    void deve_RetornarVazio_Quando_CatalogoForVazio() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of());

        List<Recomendacao> resultado = service.recomendar(usuario, 5);

        assertTrue(resultado.isEmpty());
    }
    
    @Test
    @DisplayName("Deve retornar um filme aleatório presente no conjunto filtrado (Modo Surpreenda-me)")
    void deve_RetornarFilmeAleatorio_NoModoSurpreendaMe() throws Exception {
        when(catalogoMock.buscarTodos()).thenReturn(List.of(filmePadrao));
        
        when(geradorMock.sortearInteiro(eq(0), anyInt())).thenReturn(0);

        Optional<Recomendacao> resultadoOpt = service.recomendarAleatorio(usuario);

        assertAll("Validação do Modo Surpreenda-me",
            () -> assertTrue(resultadoOpt.isPresent(), "A recomendação deveria estar presente"),
            () -> assertEquals(filmePadrao.getId(), resultadoOpt.get().getFilme().getId(), "O filme sorteado deve ser o esperado"),
            () -> assertEquals("Modo Surpreenda-me", resultadoOpt.get().getJustificativa()),
            () -> verify(historicoRepoMock, times(1)).registrarRecomendacao(eq(usuario), anyList())
        );
    }
    
    @Test
    @DisplayName("Deve validar a ordem exata do ranking usando ArrayEquals")
    void deve_ValidarOrdemExataDoRanking() throws Exception {
        Filme f1 = new Filme("F1", "Top 1", 2024, 120, List.of(Genero.ACAO), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 100);
        Filme f2 = new Filme("F2", "Top 2", 2024, 120, List.of(Genero.ACAO), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 50);
        
        when(catalogoMock.buscarTodos()).thenReturn(List.of(f2, f1));

        List<Recomendacao> resultado = service.recomendar(usuario, 2);
        
        String[] idsEsperados = {"F1", "F2"};
        String[] idsObtidos = resultado.stream().map(r -> r.getFilme().getId()).toArray(String[]::new);
        
        assertArrayEquals(idsEsperados, idsObtidos, "A ordem dos IDs no ranking deve ser exatamente F1 depois F2");
    }
    
    @Test
    @DisplayName("Cenário de Integração: Deve recomendar filmes corretamente usando o pipeline real (Filtro + Calculadora + Catálogo Mockado)")
    void deve_RetornarRankingCorreto_NoCenarioDeIntegracao() throws Exception {
        CalculadoraScore calculadoraReal = new CalculadoraScore();
        FiltroFilmes filtroReal = new FiltroFilmes();
        CatalogoFilmeAPI catalogoRealista = new CatalogoMock();
       
        RecomendadorService serviceIntegrado = new RecomendadorService(
                catalogoRealista,      
                historicoRepoMock,     
                notificadorMock,       
                geradorMock,           
                calculadoraReal,       
                filtroReal             
            );

        usuario.getPerfil().setPesoGenero(Genero.ACAO, 1.0);
        usuario.getPerfil().setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.8);
        usuario.getPerfil().setFaixaDuracao(120, 180);
        usuario.getPerfil().adicionarIdioma(Idioma.PT_BR);
        usuario.getPerfil().adicionarIdioma(Idioma.EN);
        usuario.getPerfil().setClassificacaoMaxima(ClassificacaoEtaria.DEZOITO);

        List<Recomendacao> resultado = serviceIntegrado.recomendar(usuario, 3);

        assertAll("Validação do Pipeline de Integração",
            () -> assertFalse(resultado.isEmpty(), "O pipeline deveria retornar filmes do CatalogoMock"),
            () -> assertTrue(resultado.get(0).getScore() > 50, "O primeiro filme deveria ter um score alto baseado no perfil"),
            () -> assertTrue(resultado.get(0).getFilme().getGeneros().contains(Genero.ACAO), "O topo do ranking deveria ser um filme de Ação (Peso 1.0)")
        );
        
        verify(historicoRepoMock, atLeastOnce()).registrarRecomendacao(eq(usuario), anyList());
    }
    
    @Test
    @DisplayName("Deve usar Stub Sequencial e ArgumentCaptor para validar registro")
    void deve_ValidarRegistroComCaptor_E_StubSequencial() throws Exception {
        when(catalogoMock.buscarTodos())
            .thenReturn(List.of(filmePadrao))
            .thenReturn(List.of());

        service.recomendar(usuario, 1);
        
        ArgumentCaptor<List<Recomendacao>> captor = ArgumentCaptor.forClass(List.class);
        verify(historicoRepoMock).registrarRecomendacao(eq(usuario), captor.capture());
        
        List<Recomendacao> listaCapturada = captor.getValue();
        assertEquals(1, listaCapturada.size());
        assertEquals("F01", listaCapturada.get(0).getFilme().getId());
    }
    
}
