package com.moviewatchlist.service;

import com.moviewatchlist.core.FilmEntry;

public class DataCombiner {
    public static FilmEntry combine(FilmEntry omdb, FilmEntry tmdb) {
        FilmEntry film = new FilmEntry();
        film.title = omdb.title;
        film.year = omdb.year;
        film.director = omdb.director;
        film.genre = omdb.genre;
        film.similarTitles = tmdb.similarTitles;
        film.imagePaths = tmdb.imagePaths;
        return film;
    }
}
