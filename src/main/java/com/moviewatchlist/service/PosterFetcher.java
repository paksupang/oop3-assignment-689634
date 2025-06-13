package com.moviewatchlist.service;

import com.moviewatchlist.core.FilmEntry;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

public class PosterFetcher {

    public static void downloadImages(FilmEntry film) {
        try {
            Path imageDir = Paths.get("images");
            Files.createDirectories(imageDir); // create folder if not exists

            int count = 0;
            for (String imageUrl : film.imagePaths) {
                if (count >= 3) break;
                URL url = new URL(imageUrl);
                try (InputStream in = url.openStream()) {
                    String filename = "images/" + film.title.replaceAll("\\s+", "_") + "_" + count + ".jpg";
                    Files.copy(in, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
                    film.imagePaths.set(count, filename);
                }
                count++;
            }

        } catch (Exception e) {
            System.err.println("Image download error: " + e.getMessage());
        }
    }
}
