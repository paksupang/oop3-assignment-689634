package com.moviewatchlist.db;

import com.moviewatchlist.core.FilmEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilmRepository {

    static {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "year TEXT, " +
                "director TEXT, " +
                "genre TEXT, " +
                "images TEXT, " +
                "similar TEXT, " +
                "watched BOOLEAN, " +
                "rating INTEGER" +
                ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(FilmEntry film) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO movies (title, year, director, genre, images, similar, watched, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, film.title);
            ps.setString(2, film.year);
            ps.setString(3, film.director);
            ps.setString(4, film.genre);
            ps.setString(5, String.join(",", film.imagePaths));
            ps.setString(6, String.join(",", film.similarTitles));
            ps.setBoolean(7, film.watched);
            ps.setInt(8, film.rating);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<FilmEntry> list(int page, int limit) {
        List<FilmEntry> films = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM movies LIMIT ? OFFSET ?");
            ps.setInt(1, limit);
            ps.setInt(2, (page - 1) * limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FilmEntry film = new FilmEntry();
                film.title = rs.getString("title");
                film.year = rs.getString("year");
                film.director = rs.getString("director");
                film.genre = rs.getString("genre");
                film.imagePaths = Arrays.asList(rs.getString("images").split(","));
                film.similarTitles = Arrays.asList(rs.getString("similar").split(","));
                film.watched = rs.getBoolean("watched");
                film.rating = rs.getInt("rating");
                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    public static void updateWatched(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            PreparedStatement ps = conn.prepareStatement("UPDATE movies SET watched = 1 WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRating(int id, int rating) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            PreparedStatement ps = conn.prepareStatement("UPDATE movies SET rating = ? WHERE id = ?");
            ps.setInt(1, rating);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM movies WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
