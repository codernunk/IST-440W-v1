package com.example.jesse.ist440W.models;

import android.os.Debug;

import java.util.ArrayList;

/**
 * A singleton class that holds data used by the entire app.
 * Created by Jesse on 10/27/2015.
 */
public class App {

    public static final String LOG_TITLE = "Recipe";

    private static App _instance;

    private ArrayList<Recipe> _recipes;

    private Recipe[] _defaultRecipes = new Recipe[]{
            new Recipe(1, "Steak", Recipe.FoodType.Dinner, 2000, 1000, 1, "steak",
                    new Ingredient[]{
                            new Ingredient(1, 1, "steak", "12 oz"),
                            new Ingredient(1, 1, "A1 steak sauce", "12 oz bottle"),
                            new Ingredient(1, 1, "worcestershire sauce", "1 tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(1, "Grill the steak", 1),
                            new Instruction(1, "Put sauces on it", 2),
                    }
            ),
            new Recipe(1, "Cake", Recipe.FoodType.Dessert, 1000, 2000, 8, "slice(s)",
                    new Ingredient[]{
                            new Ingredient(1, 1, "cake", "12 oz"),
                            new Ingredient(1, 1, "vanilla icing", "container"),
                            new Ingredient(1, 1, "eggs", "1 tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(1, "Grill the cake", 1),
                            new Instruction(1, "Put icing on it", 2),
                    }
            ),
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
