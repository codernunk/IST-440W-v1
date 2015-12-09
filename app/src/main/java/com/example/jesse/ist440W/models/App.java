package com.example.jesse.ist440W.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.util.Log;

import com.example.jesse.ist440W.R;
import com.example.jesse.ist440W.data.local.SQLiteDataAccess;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A singleton class that holds data used by the entire app.
 * Created by Jesse on 10/27/2015.
 */
public class App {

    public static final String LOG_TITLE = "Recipe";

    private static App _instance;

    private Context context;
    private SQLiteDataAccess _dataAccess;
    private ArrayList<Recipe> _recipes;
    private ArrayList<ShoppingList> _shoppingLists;

    private Recipe[] _defaultRecipes = new Recipe[]{
            new Recipe(-1, "Steak", Recipe.FoodType.Dinner, 1800, 3600, 1, "steak",
                    new Ingredient[]{
                            new Ingredient(-1, 1, "steak", "12 oz"),
                            new Ingredient(-1, 1, "A1 steak sauce", "12 oz bottle"),
                            new Ingredient(-1, 1, "worcestershire sauce", "tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(-1, "Grill the steak", 1),
                            new Instruction(-1, "Put sauces on it", 2),
                    }
            ),
            new Recipe(-1, "Cake", Recipe.FoodType.Dessert, 1800, 1800, 8, "slices",
                    new Ingredient[]{
                            new Ingredient(-1, 1, "cake mix", "12 oz"),
                            new Ingredient(-1, 1, "vanilla icing", "container"),
                            new Ingredient(-1, 1, "eggs", "tbsp"),
                    },
                    new Instruction[]{
                            new Instruction(-1, "Grill the cake", 1),
                            new Instruction(-1, "Put icing on it", 2),
                    }
            ),
            new Recipe(-1, "Hamburger", Recipe.FoodType.Dinner, 900, 900, 8, "burgers",
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
            new Recipe(-1, "Lemonade", Recipe.FoodType.Drink, 600, 0, 2, "liters",
                    new Ingredient[]{
                            new Ingredient(-1, 8, "lemons", ""),
                            new Ingredient(-1, 6, "sugar", "tbsp"),
                            new Ingredient(-1, 2, "water", "liter"),
                    },
                    new Instruction[]{
                            new Instruction(-1, "Fill a 2L pitcher with water.", 1),
                            new Instruction(-1, "Cut the lemons in half.", 2),
                            new Instruction(-1, "Squeeze each half of the lemons into the pitcher.", 3),
                            new Instruction(-1, "Stir in the sugar.", 4),
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

            byte[] steakImg = getImage(c, R.mipmap.ic_teststeak);
            byte[] cakeImg = getImage(c, R.mipmap.ic_testcake);
            byte[] burgerImg = getImage(c, R.mipmap.ic_testburger);
            byte[] lemonadeImg = getImage(c, R.mipmap.ic_testlemonade);

            _defaultRecipes[0].setImage(steakImg);
            _defaultRecipes[1].setImage(cakeImg);
            _defaultRecipes[2].setImage(burgerImg);
            _defaultRecipes[3].setImage(lemonadeImg);

            _defaultRecipes[0].setRating(5);
            _defaultRecipes[1].setRating(4);
            _defaultRecipes[2].setRating(3);
            _defaultRecipes[3].setRating(3.5f);

            _dataAccess = new SQLiteDataAccess(context);

            _recipes = _dataAccess.selectRecipes();
            _shoppingLists = _dataAccess.selectShoppingLists();
        }catch(Exception e){
            Log.e("ERRORS", e.getMessage());
        }
    }

    public byte[] getImage(Context c, int resource){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public SQLiteDataAccess getDataAccess() { return _dataAccess; }

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
