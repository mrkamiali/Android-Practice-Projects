package com.example.kamranali.expandableviewsample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kamranali on 21/12/2016.
 */

public class DataProvider {

    public static HashMap<String, List<String>> getInfo() {

        HashMap<String, List<String>> moviesCategory = new HashMap<>();
        List<String> actionMovies = new ArrayList<>();
        List<String> comdeyMovies = new ArrayList<>();
        List<String> horrorMovies = new ArrayList<>();
        List<String> fightingMovies = new ArrayList<>();
        List<String> romanticMovies = new ArrayList<>();
        List<String> tafreeMovies = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            actionMovies.add(i, i + " ActionMovies");
        }
        for (int i = 0; i < 10; i++) {
            comdeyMovies.add(i, i + " ComdeyMovies");
        }
        for (int i = 0; i < 10; i++) {
            horrorMovies.add(i, i + " HorrorMovies");
        }
        for (int i = 0; i < 10; i++) {
            fightingMovies.add(i, i + " FightingMovies");
        }
        for (int i = 0; i < 10; i++) {
            romanticMovies.add(i, i + " RomanticMovies");
        }
        for (int i = 0; i < 10; i++) {
            tafreeMovies.add(i, i + " TafreeMovies");
        }
        moviesCategory.put("ActionMovies",actionMovies);
        moviesCategory.put("ComdeyMovies",comdeyMovies);
        moviesCategory.put("HorrorMovies",horrorMovies);
        moviesCategory.put("FightingMovies",fightingMovies);
        moviesCategory.put("RomanticMovies",romanticMovies);
        moviesCategory.put("TafreeMovies",tafreeMovies);

        return moviesCategory;
    }
}
