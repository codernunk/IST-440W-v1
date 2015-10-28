package com.example.jesse.ist440W.models;

import android.os.Debug;

import java.util.ArrayList;

/**
 * A singleton class that holds data used by the entire app.
 * Created by Jesse on 10/27/2015.
 */
public class App {

    private static App _instance;

    private ArrayList<Recipe> _recipes;

    private Recipe[] _defaultRecipes = new Recipe[]{
            new Recipe(1, "Steak", Recipe.FoodType.Dinner, 1, 1, 1, "steak"),
            new Recipe(1, "Cake", Recipe.FoodType.Dessert, 1, 1, 1, "slices"),
    };

    public static App getInstance(){
        if (_instance == null)
            _instance = new App();
        return _instance;
    }

    public App(){
        try{
            if (_instance != null)
                throw new Exception("App is a singleton.  Use the getInstance() method to access App.");

            _recipes = new ArrayList<Recipe>();

            for (Recipe r : _defaultRecipes)
                _recipes.add(r);

        }catch(Exception e){
        }
    }

    public ArrayList<Recipe> getRecipes() {
        return _recipes;
    }
}
