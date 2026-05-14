package br.com.moodie.model;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.exception.DuracaoInvalidaException;
import br.com.moodie.exception.PesoInvalidoException;

import java.util.*;
/**
 * Armazena as preferências, restrições e histórico de um usuário específico.
 */
public class PerfilCinefilo {
	private final Map<Genero, Double> pesosGeneros = new EnumMap<>(Genero.class);
	private int duracaoMinima;
	private int duracaoMaxima;
	private ClassificacaoEtaria classificacaoMaxima;
	private final List<Idioma> idiomasAceitos = new ArrayList<>();
	private final Set<String> historicoAssistidos = new HashSet<>();
	private Map<String, Integer> notas = new HashMap<>();
	
	public void setPesoGenero(Genero genero, double peso) {
		if(peso < 0.0 || peso > 1.0) {
			throw new PesoInvalidoException("O peso do gênero deve estar entre 0.0 e 1.0");
		}
		pesosGeneros.put(genero, peso);
	}
	
	public void setFaixaDuracao(int minima, int maxima) {
		if (minima > maxima) {
			throw new DuracaoInvalidaException("A duração mínima não pode ser maior que a máxima");
		}
		this.duracaoMinima = minima;
		this.duracaoMaxima = maxima;
	}
	
	public void adicionarNota(String filmeId, int nota) {
		if (nota < 1 || nota > 5) {
			throw new IllegalArgumentException("A nota deve ser de 1 a 5");
		}
		notas.put(filmeId, nota);
	}
	
	public void marcarComoAssistido (String filmeId) {
		historicoAssistidos.add(filmeId);
	}
	
	public void setClassificacaoMaxima (ClassificacaoEtaria classificacaoMaxima) {
		this.classificacaoMaxima = classificacaoMaxima;
	}
	
	public void adicionarIdioma(Idioma idioma) {
		if (!idiomasAceitos.contains(idioma)) {
			idiomasAceitos.add(idioma);
		}
	}
	
	public double getPesoGenero(Genero genero) {return pesosGeneros.getOrDefault(genero, 0.0); }
	public int getDuracaoMinima() {return duracaoMinima; }
	public int getDuracaoMaxima() {return duracaoMaxima; }
	public ClassificacaoEtaria getClassificacaoMaxima() {return classificacaoMaxima; }
	public List<Idioma> getIdiomaAceitos() {return Collections.unmodifiableList(idiomasAceitos); }
	public Set<String> getHistoricoAssistidos() {return Collections.unmodifiableSet(historicoAssistidos); }
	public Integer getNotaPara(String filmeId) {return notas.get(filmeId); }

	
}