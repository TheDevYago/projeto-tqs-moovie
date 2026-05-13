package br.com.moodie.service;

import java.util.List;

import br.com.moodie.model.Filme;
/**
 * Interface de integração com o provedor de dados de filmes
 */
public interface CatalogoFilmeAPI {
	/**
	 * Recupera todos os títulos disponíveis no catálogo externo.
	 * @return
	 * @throws Exception
	 */
	List<Filme> buscarTodos() throws Exception;
}
