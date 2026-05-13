package br.com.moodie.service;

import br.com.moodie.enums.Genero;
import br.com.moodie.model.Filme;
import br.com.moodie.model.PerfilCinefilo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Componente responsável pela filtragem preliminar do catálogo de filmes.
 */
public class FiltroFilmes {
	/**
	 * Filtra uma lista de filmes com base nas restrições definidas no perfil do usuário.
	 * @param catalogo
	 * @param perfil
	 * @return
	 */
	public List<Filme> filtrar(List<Filme> catalogo, PerfilCinefilo perfil) {
		if (catalogo == null || catalogo.isEmpty()) {
			return Collections.emptyList();
		}
		
		return catalogo.stream().filter(filme -> naoFoiAssistido(filme, perfil)).filter(filme -> faixaEtariaPermitida(filme, perfil)).filter(filme -> idiomaAceito(filme, perfil)).filter(filme -> generoPermitido(filme, perfil)).collect(Collectors.toList());
	}
	
	private boolean naoFoiAssistido(Filme filme, PerfilCinefilo perfil) {
		return !perfil.getHistoricoAssistidos().contains(filme.getId()); //true, se n estiver no historico
	}
	
	private boolean faixaEtariaPermitida(Filme filme, PerfilCinefilo perfil) {
		if (perfil.getClassificacaoMaxima() == null) return true;
		return filme.getClassificacao().getIdadeMaxima() <= perfil.getClassificacaoMaxima().getIdadeMaxima(); // compara os valores do enum
	}
	
	private boolean idiomaAceito(Filme filme, PerfilCinefilo perfil) {
		if (perfil.getIdiomaAceitos().isEmpty()) return true;
		return perfil.getIdiomaAceitos().contains(filme.getIdioma()); // true, se o idioma esta na lista de aceitos
	}
	
	private boolean generoPermitido(Filme filme, PerfilCinefilo perfil) {
		for (Genero genero: filme.getGeneros()) { // exclui o filme q tenha peso exatamento a 0.0
			if (perfil.getPesoGenero(genero) == 0.0) {
				return false;
			}
		}
		return true;
	}
}
