package com.moviewatchlist.api;

import com.google.gson.*;
import com.moviewatchlist.core.FilmEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TmdbService {
    private static final String API_KEY = "8b3b48a508470270060bd462d86d2a10";

    public static FilmEntry fetchFilmExtras(String title) {
        FilmEntry film = new FilmEntry();
        try {
            String query = title.trim().replace(" ", "%20");
            String searchUrl = "https://api.themoviedb.org/3/search/movie?query=" + query + "&api_key=" + API_KEY;
            JsonObject searchResult = getJson(searchUrl);

            if (searchResult.getAsJsonArray("results").size() == 0) return film;

            JsonObject movieObj = searchResult.getAsJsonArray("results").get(0).getAsJsonObject();
            int movieId = movieObj.get("id").getAsInt();

            // Get images
            String imagesUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/images?api_key=" + API_KEY;
            JsonObject imageResult = getJson(imagesUrl);
            JsonArray backdrops = imageResult.getAsJsonArray("backdrops");
            for (int i = 0; i < Math.min(3, backdrops.size()); i++) {
                String path = backdrops.get(i).getAsJsonObject().get("file_path").getAsString();
                film.imagePaths.add("https://image.tmdb.org/t/p/w780" + path);
            }

            // Get similar movies
            String similarUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + API_KEY;
            JsonObject similarResult = getJson(similarUrl);
            JsonArray similarArray = similarResult.getAsJsonArray("results");
            for (int i = 0; i < Math.min(5, similarArray.size()); i++) {
                String titleSimilar = similarArray.get(i).getAsJsonObject().get("title").getAsString();
                film.similarTitles.add(titleSimilar);
            }

        } catch (Exception e) {
            System.err.println("TMDb fetch error: " + e.getMessage());
        }
        return film;
    }

    private static JsonObject getJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) result.append(line);
        reader.close();

        return JsonParser.parseString(result.toString()).getAsJsonObject();
    }
}
