package br.com.moodie.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

@Tag("unitario")
class CatalogoMockTest {
	@Test
	void deve_RetornarListaPopulada_Quando_BuscarTodos () throws Exception {
		CatalogoMock catalogo = new CatalogoMock();
		assertFalse(catalogo.buscarTodos().isEmpty(), "O catálogo mock não deve ser vazio");
	}
}
