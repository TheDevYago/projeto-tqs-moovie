package br.com.moodie.service;

import java.util.List;

import br.com.moodie.model.Filme;

public interface CatalogoFilmeAPI {
	List<Filme> buscarTodos() throws Exception;
}
