package br.com.moodie.model;

import br.com.moodie.enums.Genero;
import br.com.moodie.exception.DuracaoInvalidaException;
import br.com.moodie.exception.PerfilIncompletoException;
import br.com.moodie.exception.PesoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
class PerfilCinefiloTest {
	private PerfilCinefilo perfil;

    @BeforeEach
    void setup() {
        perfil = new PerfilCinefilo();
    }

    // CT01
    @Test
    @DisplayName("Deve aceitar peso quando estiver dentro do limite de 0.0 a 1.0")
    void deve_AceitarPeso_Quando_DentroDoLimite() {
        assertDoesNotThrow(() -> perfil.setPesoGenero(Genero.ACAO, 0.8));
        assertEquals(0.8, perfil.getPesoGenero(Genero.ACAO));
    }

    // CT02
    @Test
    @DisplayName("Deve lançar exceção quando o peso for maior que 1.0")
    void deve_LancarExcecao_Quando_PesoAcimaDoLimite() {
        assertThrows(PesoInvalidoException.class, () -> {
            perfil.setPesoGenero(Genero.DRAMA, 1.5);
        });
    }

    // CT03
    @Test
    @DisplayName("Deve lançar exceção quando o peso for menor que 0.0")
    void deve_LancarExcecao_Quando_PesoAbaixoDoLimite() {
        assertThrows(PesoInvalidoException.class, () -> {
            perfil.setPesoGenero(Genero.TERROR, -0.1);
        });
    }

    // CT04
    @Test
    @DisplayName("Deve lançar exceção quando a duração mínima for maior que a máxima")
    void deve_LancarExcecao_Quando_FaixaDeDuracaoInvalida() {
        assertThrows(DuracaoInvalidaException.class, () -> {
            perfil.setFaixaDuracao(120, 90);
        });
    }

    // CT05
    @Test
    @DisplayName("Deve lançar exceção quando a nota atribuida for fora de 1 a 5")
    void deve_LancarExcecao_Quando_NotaForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class, () -> {
            perfil.adicionarNota("F01", 6);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            perfil.adicionarNota("F02", 0);
        });
    }
    
    // CT06
    @Test
    @DisplayName("Deve criar perfil com sucesso e estado inicial limpo")
    void deve_CriarPerfilComSucesso() {
        assertNotNull(perfil.getIdiomaAceitos());
        assertTrue(perfil.getHistoricoAssistidos().isEmpty());
    }

    // CT07
    @Test
    @DisplayName("Deve permitir a atualização de pesos existentes")
    void deve_AtualizarPerfilComNovosPesos() throws PesoInvalidoException {
        perfil.setPesoGenero(Genero.DRAMA, 0.5);
        perfil.setPesoGenero(Genero.DRAMA, 0.9);
        
        assertEquals(0.9, perfil.getPesoGenero(Genero.DRAMA));
    }

    // CT08
    @ParameterizedTest
    @CsvSource({
        "0.1, true",
        "0.5, true",
        "1.0, true",
        "1.1, false",
        "-0.1, false"
    })
    @DisplayName("Validar múltiplos limites de peso via CSV")
    void deve_ValidarPesosParametrizados(double peso, boolean valido) {
        if (valido) {
            assertDoesNotThrow(() -> perfil.setPesoGenero(Genero.ACAO, peso));
        } else {
            assertThrows(PesoInvalidoException.class, () -> perfil.setPesoGenero(Genero.ACAO, peso));
        }
    }
    
    @Test
    @DisplayName("Deve lançar PerfilIncompletoException quando tentar recomendar sem gêneros definidos")
    void deve_LancarExcecao_Quando_PerfilNaoTemGeneros() {
        PerfilCinefilo perfilVazio = new PerfilCinefilo();
        
        assertThrows(PerfilIncompletoException.class, () -> {
            perfilVazio.validarParaRecomendacao(); 
        });
    }
    
}
