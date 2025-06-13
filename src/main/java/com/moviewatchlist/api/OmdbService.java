package com.moviewatchlist.api;

import com.google.gson.*;
import com.moviewatchlist.core.FilmEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OmdbService {
    private static final String API_KEY = "c6f5e196"; 

    public static FilmEntry fetchFilm(String title) {
        try {
            String query = title.trim().replace(" ", "+");
            String endpoint = "http://www.omdbapi.com/?t=" + query + "&apikey=" + API_KEY;

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();

            FilmEntry film = new FilmEntry();
            film.title = json.get("Title").getAsString();
            film.year = json.get("Year").getAsString();
            film.director = json.get("Director").getAsString();
            film.genre = json.get("Genre").getAsString();

            return film;

        } catch (Exception e) {
            System.err.println("OMDb fetch error: " + e.getMessage());
            return new FilmEntry();
        }
    }
}
