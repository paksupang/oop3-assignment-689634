package com.moviewatchlist.core;

import com.moviewatchlist.api.OmdbService;
import com.moviewatchlist.api.TmdbService;
import com.moviewatchlist.db.FilmRepository;
import com.moviewatchlist.rest.ApiServer;
import com.moviewatchlist.service.DataCombiner;
import com.moviewatchlist.service.PosterFetcher;

import java.util.Scanner;
import java.util.concurrent.*;

public class WatchlistManager {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a movie title: ");
        String title = scanner.nextLine();
        scanner.close();

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<FilmEntry> omdbFuture = pool.submit(() -> OmdbService.fetchFilm(title));
            Future<FilmEntry> tmdbFuture = pool.submit(() -> TmdbService.fetchFilmExtras(title));

            FilmEntry fromOmdb = omdbFuture.get();
            FilmEntry fromTmdb = tmdbFuture.get();

            FilmEntry combined = DataCombiner.combine(fromOmdb, fromTmdb);

            PosterFetcher.downloadImages(combined);
            FilmRepository.save(combined);

            System.out.println("Film successfully added to your watchlist!");
            ApiServer.start();

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
