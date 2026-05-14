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
		
		return catalogo.stream().filter(filme -> naoFoiAssistido(filme, perfil)).filter(filme -> faixaEtariaPermitida(filme, perfil)).filter(filme -> idiomaAceito(filme, perfil)).filter(filme -> generoExatoPermitido(filme, perfil)).filter(filme -> duracaoPermitida(filme, perfil)).collect(Collectors.toList());
	}
	
	private boolean naoFoiAssistido(Filme filme, PerfilCinefilo perfil) {
		return !perfil.getHistoricoAssistidos().contains(filme.getId());
	}
	
	private boolean faixaEtariaPermitida(Filme filme, PerfilCinefilo perfil) {
		if (perfil.getClassificacaoMaxima() == null) return true;
		return filme.getClassificacao().getIdadeMaxima() <= perfil.getClassificacaoMaxima().getIdadeMaxima();
	}
	
	private boolean idiomaAceito(Filme filme, PerfilCinefilo perfil) {
		if (perfil.getIdiomaAceitos().isEmpty()) return true;
		return perfil.getIdiomaAceitos().contains(filme.getIdioma());
	}
	
	private boolean generoExatoPermitido(Filme filme, PerfilCinefilo perfil) {
		if (filme.getGeneros().isEmpty()) return false;
		for (Genero genero: filme.getGeneros()) {
			if (perfil.getPesoGenero(genero) == 1.0) {
				return true;
			}
		}
		return false;
	}
	
	private boolean duracaoPermitida (Filme filme, PerfilCinefilo perfil) {
		if (perfil.getDuracaoMaxima() == 0) return true;
		return filme.getDuracaoMinutos() <= perfil.getDuracaoMaxima();
	}
}
