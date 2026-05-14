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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return Collections.emptyList();
        }

        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
        JsonArray resultados = jsonObject.getAsJsonArray("results");
        List<Filme> filmesEncontrados = new ArrayList<>();

        for (JsonElement elemento : resultados) {
            JsonObject filmeJson = elemento.getAsJsonObject();

            String id = filmeJson.get("id").getAsString();
            String titulo = filmeJson.get("title").getAsString();
            
            String dataLancamento = filmeJson.has("release_date") && !filmeJson.get("release_date").getAsString().isEmpty() 
            		? filmeJson.get("release_date").getAsString() : "2024";
            int ano = dataLancamento.length() >= 4 ? Integer.parseInt(dataLancamento.substring(0, 4)) : 2024;
            
            // Usamos getAsDouble com cast para evitar erros de parse do Gson com números decimais
            int popularidade = (int) filmeJson.get("popularity").getAsDouble();

            // --- O MAPEAMENTO DE GÊNEROS (A PEÇA QUE FALTAVA) ---
            List<Genero> generosDoFilme = new ArrayList<>();
            if (filmeJson.has("genre_ids")) {
                JsonArray genreIds = filmeJson.getAsJsonArray("genre_ids");
                for (JsonElement idElement : genreIds) {
                    int genreId = idElement.getAsInt();
                    switch (genreId) {
                        case 28: generosDoFilme.add(Genero.ACAO); break;
                        case 35: generosDoFilme.add(Genero.COMEDIA); break;
                        case 18: generosDoFilme.add(Genero.DRAMA); break;
                        case 878: generosDoFilme.add(Genero.FICCAO_CIENTIFICA); break;
                        case 10749: generosDoFilme.add(Genero.ROMANCE); break;
                        case 27: generosDoFilme.add(Genero.TERROR); break;
                        case 99: generosDoFilme.add(Genero.DOCUMENTARIO); break;
                    }
                }
            }

            // Simula uma duração variando entre 90 e 150 min
            int duracaoSimulada = 90 + (popularidade % 60);

            Filme filme = new Filme(id, titulo, ano, duracaoSimulada, generosDoFilme, ClassificacaoEtaria.LIVRE, Idioma.PT_BR, popularidade);
            filmesEncontrados.add(filme);
        }

        return filmesEncontrados;
    }
}