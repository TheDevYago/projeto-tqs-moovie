package br.com.moodie.service;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.model.Filme;
import java.util.ArrayList;
import java.util.List;

public class CatalogoMock implements CatalogoFilmeAPI{
	@Override
	public List<Filme> buscarTodos() {
		List<Filme> filmes = new ArrayList<>();
		
		// AÇAO
		filmes.add(new Filme("1", "Duro de Matar", 1988, 132, List.of(Genero.ACAO), ClassificacaoEtaria.DEZOITO, Idioma.EN, 95));
        filmes.add(new Filme("2", "John Wick", 2014, 101, List.of(Genero.ACAO), ClassificacaoEtaria.DEZESSEIS, Idioma.EN, 90));
        filmes.add(new Filme("3", "Mad Max: Estrada da Fúria", 2015, 120, List.of(Genero.ACAO, Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.QUATORZE, Idioma.EN, 98));
        filmes.add(new Filme("4", "Tropa de Elite", 2007, 115, List.of(Genero.ACAO, Genero.DRAMA), ClassificacaoEtaria.DEZOITO, Idioma.PT_BR, 99));
        
        // DRAMA
        filmes.add(new Filme("5", "O Poderoso Chefão", 1972, 175, List.of(Genero.DRAMA), ClassificacaoEtaria.QUATORZE, Idioma.EN, 100));
        filmes.add(new Filme("6", "Parasita", 2019, 132, List.of(Genero.DRAMA, Genero.ROMANCE), ClassificacaoEtaria.DEZESSEIS, Idioma.JP, 92));
        filmes.add(new Filme("7", "Cidade de Deus", 2002, 130, List.of(Genero.DRAMA), ClassificacaoEtaria.DEZOITO, Idioma.PT_BR, 97));
        filmes.add(new Filme("8", "A Lista de Schindler", 1993, 195, List.of(Genero.DRAMA), ClassificacaoEtaria.QUATORZE, Idioma.EN, 96));
        
     // FICÇÃO CIENTÍFICA
        filmes.add(new Filme("9", "Interestelar", 2014, 169, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA), ClassificacaoEtaria.DEZ, Idioma.EN, 94));
        filmes.add(new Filme("10", "Matrix", 1999, 136, List.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO), ClassificacaoEtaria.DOZE, Idioma.EN, 93));
        filmes.add(new Filme("11", "Blade Runner 2049", 2017, 164, List.of(Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.QUATORZE, Idioma.EN, 85));
        filmes.add(new Filme("12", "A Chegada", 2016, 116, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA), ClassificacaoEtaria.DEZ, Idioma.EN, 88));
        
     // COMÉDIA
        filmes.add(new Filme("13", "Superbad", 2007, 113, List.of(Genero.COMEDIA), ClassificacaoEtaria.DEZESSEIS, Idioma.EN, 82));
        filmes.add(new Filme("14", "Se Beber, Não Case", 2009, 100, List.of(Genero.COMEDIA), ClassificacaoEtaria.QUATORZE, Idioma.EN, 80)); 
        filmes.add(new Filme("15", "The Hangover", 2009, 100, List.of(Genero.COMEDIA), ClassificacaoEtaria.QUATORZE, Idioma.EN, 88));
        filmes.add(new Filme("16", "Minha Mãe é uma Peça", 2013, 84, List.of(Genero.COMEDIA), ClassificacaoEtaria.LIVRE, Idioma.PT_BR, 85));
        
     // TERROR
        filmes.add(new Filme("17", "O Iluminado", 1980, 146, List.of(Genero.TERROR), ClassificacaoEtaria.DEZESSEIS, Idioma.EN, 91));
        filmes.add(new Filme("18", "Hereditário", 2018, 127, List.of(Genero.TERROR, Genero.DRAMA), ClassificacaoEtaria.DEZESSEIS, Idioma.EN, 78));
        filmes.add(new Filme("19", "Corra!", 2017, 104, List.of(Genero.TERROR), ClassificacaoEtaria.QUATORZE, Idioma.EN, 89));
        filmes.add(new Filme("20", "Invocação do Mal", 2013, 112, List.of(Genero.TERROR), ClassificacaoEtaria.QUATORZE, Idioma.EN, 84));
        
     // ROMANCE
        filmes.add(new Filme("21", "Questão de Tempo", 2013, 123, List.of(Genero.ROMANCE, Genero.DRAMA), ClassificacaoEtaria.DOZE, Idioma.EN, 87));
        filmes.add(new Filme("22", "La La Land", 2016, 128, List.of(Genero.ROMANCE, Genero.DRAMA), ClassificacaoEtaria.LIVRE, Idioma.EN, 90));
        filmes.add(new Filme("23", "Orgulho e Preconceito", 2005, 129, List.of(Genero.ROMANCE, Genero.DRAMA), ClassificacaoEtaria.LIVRE, Idioma.EN, 86));
        filmes.add(new Filme("24", "Como Perder um Homem em 10 Dias", 2003, 116, List.of(Genero.ROMANCE, Genero.COMEDIA), ClassificacaoEtaria.DOZE, Idioma.EN, 75));
        
     // DOCUMENTÁRIO / OUTROS
        filmes.add(new Filme("25", "O Dilema das Redes", 2020, 94, List.of(Genero.DOCUMENTARIO), ClassificacaoEtaria.DOZE, Idioma.EN, 83));
        filmes.add(new Filme("26", "Democracia em Vertigem", 2019, 121, List.of(Genero.DOCUMENTARIO), ClassificacaoEtaria.DOZE, Idioma.PT_BR, 70));
        filmes.add(new Filme("27", "Toy Story", 1995, 81, List.of(Genero.COMEDIA), ClassificacaoEtaria.LIVRE, Idioma.EN, 92));
        filmes.add(new Filme("28", "O Rei Leão", 1994, 88, List.of(Genero.DRAMA), ClassificacaoEtaria.LIVRE, Idioma.EN, 95));
        filmes.add(new Filme("29", "Shrek", 2001, 90, List.of(Genero.COMEDIA), ClassificacaoEtaria.LIVRE, Idioma.EN, 88));
        filmes.add(new Filme("30", "Spirited Away", 2001, 125, List.of(Genero.DRAMA, Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.LIVRE, Idioma.JP, 94));
        
        return filmes;
	}
}
