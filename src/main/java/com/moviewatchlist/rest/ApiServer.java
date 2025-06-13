package com.moviewatchlist.rest;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.moviewatchlist.db.FilmRepository;

public class ApiServer {

    public static void start() {
        port(4567);
        Gson gson = new Gson();

        get("/api/v1/movies", (req, res) -> {
            try {
                int page = Integer.parseInt(req.queryParams("page"));
                int limit = Integer.parseInt(req.queryParams("limit"));
                res.type("application/json");
                return gson.toJson(FilmRepository.list(page, limit));
            } catch (Exception e) {
                res.status(400);
                return "Invalid request: " + e.getMessage();
            }
        });

        post("/api/v1/movies/:id/watched", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                FilmRepository.updateWatched(id);
                return "Watched flag updated for movie ID " + id;
            } catch (Exception e) {
                res.status(400);
                return "Error: " + e.getMessage();
            }
        });

        post("/api/v1/movies/:id/rating", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                int rating = Integer.parseInt(req.queryParams("value"));
                if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be 1–5");
                FilmRepository.updateRating(id, rating);
                return "Rating updated for movie ID " + id;
            } catch (Exception e) {
                res.status(400);
                return "Error: " + e.getMessage();
            }
        });

        delete("/api/v1/movies/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                FilmRepository.delete(id);
                return "Deleted movie with ID " + id;
            } catch (Exception e) {
                res.status(400);
                return "Error: " + e.getMessage();
            }
        });
    }
}
