package br.com.moodie.model;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Filme {
	private final String id;
	private final String titulo;
	private final int ano;
	private final int duracaoMinutos;
	private final List<Genero> generos;
	private final ClassificacaoEtaria classificacao;
	private final Idioma idioma;
	private final int popularidade;
	
	public Filme(String id, String titulo, int ano, int duracaoMinutos, List<Genero> generos, ClassificacaoEtaria classificacao, Idioma idioma, int popularidade) {
		this.id = id;
		this.titulo = titulo;
		this.ano = ano;
		this.duracaoMinutos = duracaoMinutos;
		this.generos = Collections.unmodifiableList(generos); //imutavel (copia nao modificavel da lista)
		this.classificacao = classificacao;
		this.idioma = idioma;
		this.popularidade = popularidade;
	}
	
	public String getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public int getAno() {
		return ano;
	}
	public int getDuracaoMinutos() {
		return duracaoMinutos;
	}
	public List<Genero> getGeneros() {
		return generos;
	}
	public ClassificacaoEtaria getClassificacao() {
		return classificacao;
	}
	public Idioma getIdioma() {
		return idioma;
	}
	public int getPopularidade() {
		return popularidade;
	}
	
	@Override //obrigatorio para os testes unitarios
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Filme filme = (Filme) o;
		return Objects.equals(id, filme.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
