package br.com.moodie.service;

import java.util.List;

import br.com.moodie.model.Recomendacao;
import br.com.moodie.model.Usuario;
/**
 * Repositório responsável pela persistência das recomendações geradas e consulta de histórico.
 */
public interface HistoricoUsuarioRepository {
	/**
	 * Registra as recomendações enviadas ao usuário para evitar repetições futuras e auditoria.
	 * @param usuario
	 * @param recomendacoes
	 */
	void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes);
}
