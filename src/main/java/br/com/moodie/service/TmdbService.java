package br.com.moodie.service;

import br.com.moodie.enums.ClassificacaoEtaria;
import br.com.moodie.enums.Genero;
import br.com.moodie.enums.Idioma;
import br.com.moodie.model.Filme;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TmdbService implements CatalogoFilmeAPI {
    
    private static final String API_KEY = "451ed5753ac09675cc8a956b0a8c23c9"; 
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/popular?language=pt-BR&api_key=" + API_KEY;
    
    private final HttpClient httpClient;
    private final Gson gson;

    public TmdbService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    public List<Filme> buscarTodos() throws Exception {
    	List<Filme> todosOsFilmes = new ArrayList<>();
    	
    	for(int pagina = 1; pagina<= 5; pagina++) {
    		String urlComPagina = BASE_URL + "&page=" + pagina;
    		
    		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlComPagina)).GET().build();
    		HttpResponse <String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    		
    		if (response.statusCode() != 200) continue;
    		
    		JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
    		JsonArray resultados = jsonObject.getAsJsonArray("results");
    		
    		for (JsonElement elemento : resultados) {
    			JsonObject filmeJson = elemento.getAsJsonObject();
    			
    			String id = filmeJson.get("id").getAsString();
                String titulo = filmeJson.get("title").getAsString();
                String dataLancamento = filmeJson.has("release_date") && !filmeJson.get("release_date").getAsString().isEmpty() 
                        ? filmeJson.get("release_date").getAsString() : "2024";
                int ano = dataLancamento.length() >= 4 ? Integer.parseInt(dataLancamento.substring(0, 4)) : 2024;
                int popularidade = (int) filmeJson.get("popularity").getAsDouble();
                
                List<Genero> generoDoFilme = new ArrayList<>();
                if (filmeJson.has("genre_ids") ) {
                	JsonArray genreIds = filmeJson.getAsJsonArray("genre_ids");
					for(JsonElement idElement : genreIds) {
                		int generoID = idElement.getAsInt();
                		switch (generoID) {
                        	case 28: generoDoFilme.add(Genero.ACAO); break;
                        	case 35: generoDoFilme.add(Genero.COMEDIA); break;
                        	case 18: generoDoFilme.add(Genero.DRAMA); break;
                        	case 878: generoDoFilme.add(Genero.FICCAO_CIENTIFICA); break;
                        	case 10749: generoDoFilme.add(Genero.ROMANCE); break;
                        	case 27: generoDoFilme.add(Genero.TERROR); break;
                        	case 99: generoDoFilme.add(Genero.DOCUMENTARIO); break;
                		}
                	}
                }
                int duracaoSimulada = 90 + (popularidade % 60);
                
                Filme filme = new Filme(id, titulo, ano, duracaoSimulada, generoDoFilme, ClassificacaoEtaria.LIVRE, Idioma.PT_BR, popularidade);
                todosOsFilmes.add(filme);
    		}
    	}
    	System.out.println("✅ Catálogo carregado com " + todosOsFilmes.size() + " filmes da TMDB.");
    	return todosOsFilmes;
    }
}