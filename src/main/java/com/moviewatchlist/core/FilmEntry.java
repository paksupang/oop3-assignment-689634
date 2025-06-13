package com.moviewatchlist.core;

import java.util.ArrayList;
import java.util.List;

public class FilmEntry {
    public String title;
    public String year;
    public String director;
    public String genre;
    public List<String> imagePaths = new ArrayList<>();
    public List<String> similarTitles = new ArrayList<>();
    public boolean watched = false;
    public int rating = 0;
}
