package com.example.jesse.ist440W.models;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import com.example.jesse.ist440W.data.local.SQLiteDataAccess;

import java.util.ArrayList;

/**
 * A singleton class that holds data used by the entire app.
 * Created by Jesse on 10/27/2015.
 */
public class App {

    public static final String LOG_TITLE = "Recipe";

    private static App _instance;

    private Context context;

    private ArrayList<Recipe> _recipes;

    private ArrayList<ShoppingList> _shoppingLists;

    private Recipe[] _defaultRecipes = new Recipe[]{
            new Recipe(-1, "Steak", Recipe.FoodType.Dinner, 2000, 1000, 1, "steak",
                    new Ingredient[]{
                            new Ingredient(-1, 1, "steak", "12 oz"),
                            new Ingredient(-1, 1, "A1 steak sauce", "12 oz bottle"),
                            new Ingredient(-1, 1, "worcestershire sauce", "1 tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(-1, "Grill the steak", 1),
                            new Instruction(-1, "Put sauces on it", 2),
                    }
            ),
            new Recipe(-1, "Cake", Recipe.FoodType.Dessert, 1000, 2000, 8, "slice(s)",
                    new Ingredient[]{
                            new Ingredient(-1, 1, "cake", "12 oz"),
                            new Ingredient(-1, 1, "vanilla icing", "container"),
                            new Ingredient(-1, 1, "eggs", "1 tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(-1, "Grill the cake", 1),
                            new Instruction(-1, "Put icing on it", 2),
                    }
            ),
            new Recipe(-1, "Hamburger", Recipe.FoodType.Dinner, 1000, 2000, 8, "slice(s)",
                    new Ingredient[]{
                            new Ingredient(-1, 12, "ground beef", "lb"),
                            new Ingredient(-1, 1, "chopped onion", ""),
                            new Ingredient(-1, 1, "worcestershire sauce", "tbsp"),
                            new Ingredient(-1, 6, "hamburger buns", ""),
                    },
                    new Instruction[]{
                            new Instruction(-1, "For the ground beef into patties", 1),
                            new Instruction(-1, "Place the patties onto the grill", 2),
                            new Instruction(-1, "Cook for about 10 minutes on each side", 3),
                    }
            ),
            new Recipe(-1, "Lemonade", Recipe.FoodType.Drink, 1000, 0, 2, "liters",
                    new Ingredient[]{
                            new Ingredient(-1, 1, "cake", "12 oz"),
                            new Ingredient(-1, 1, "vanilla icing", "container"),
                            new Ingredient(-1, 1, "eggs", "1 tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(-1, "Grill the cake", 1),
                            new Instruction(-1, "Put icing on it", 2),
                    }
            ),
    };

    public static App getInstance(){
        if (_instance == null)
            _instance = new App();
        return _instance;
    }

    public App()
    {
        try {
            if (_instance != null)
                throw new Exception("App is a singleton.  Use the getInstance() method to access App.");
        }catch (Exception e){
            Log.e("ERRORS", e.getMessage());
        }
    }

    public void init(Context c){
        try{
            if (context != null)
                return;

            context = c;

            _recipes = new ArrayList<Recipe>();

            for (Recipe r : _defaultRecipes)
                _recipes.add(r);

            ArrayList<Recipe> others = (new SQLiteDataAccess(context)).selectRecipes();
            _recipes.addAll(others);

            _shoppingLists = new ArrayList<ShoppingList>();

            ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();

            items.add(new ShoppingListItem(1, new Ingredient(1, 12, "Steak", "oz"), 1));

            ShoppingList test = new ShoppingList(items);

            _shoppingLists.add(test);

        }catch(Exception e){
            Log.e("ERRORS", e.getMessage());
        }
    }

    public ArrayList<Recipe> getRecipes() {
        return _recipes;
    }
    public Recipe[] getDefaultRecipes() {
        return _defaultRecipes;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        return _shoppingLists;
    }


}
