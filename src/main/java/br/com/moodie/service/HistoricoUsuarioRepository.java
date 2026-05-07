package br.com.moodie.service;

import java.util.List;

import br.com.moodie.model.Recomendacao;
import br.com.moodie.model.Usuario;

public interface HistoricoUsuarioRepository {
	void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes);
}
