package com.example.jesse.ist440W.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Ingredient;
import com.example.jesse.ist440W.models.Instruction;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.models.ShoppingList;
import com.example.jesse.ist440W.models.ShoppingListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Represents the base structure for all queries
 * for local data storage.
 * Created by Jesse on 10/13/2015.
 */
public class SQLiteDataAccess extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "ist440w.team1.recipeshoppinglist.app";

    public SQLiteDataAccess(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Recipes table
        RecipeTable.createTable(db);
        IngredientsTable.createTable(db);
        InstructionsTable.createTable(db);
        //RecipeIngredientsTable.createTable(db);
        ShoppingListsTable.createTable(db);
        ShoppingListItemsTable.createTable(db);

        for (Recipe r : App.getInstance().getDefaultRecipes()){
            insertRecipe(db, r);
        }
        // TODO: Add other tables
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Recipes");
        db.execSQL("DROP TABLE IF EXISTS Ingredients");
        db.execSQL("DROP TABLE IF EXISTS Instructions");
        db.execSQL("DROP TABLE IF EXISTS RecipesIngredients");
        db.execSQL("DROP TABLE IF EXISTS ShoppingLists");
        db.execSQL("DROP TABLE IF EXISTS ShoppingListItems");
        onCreate(db);
    }

    public ArrayList<Recipe> selectRecipes(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + RecipeTable.TABLE_NAME + " ORDER BY RecipeID ", null);
        Cursor ingredientsResults = db.rawQuery("SELECT * FROM " + IngredientsTable.TABLE_NAME + " ORDER BY RecipeID", null);
        Cursor resInstructions = db.rawQuery("SELECT * FROM " + InstructionsTable.TABLE_NAME + " ORDER BY RecipeID, OrderID", null);

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        while (results.moveToNext()){
            Recipe r = new Recipe(results.getInt(results.getColumnIndex(RecipeTable.COLUMN_NAME_RECIPE_ID)),
                    results.getString(results.getColumnIndex(RecipeTable.COLUMN_NAME_NAME)),
                    Recipe.FoodType.fromInt(results.getInt(results.getColumnIndex(RecipeTable.COLUMN_NAME_TYPE))),
                    results.getInt(results.getColumnIndex(RecipeTable.COLUMN_NAME_PREP_TIME)),
                    results.getInt(results.getColumnIndex(RecipeTable.COLUMN_NAME_COOK_TIME)),
                    results.getInt(results.getColumnIndex(RecipeTable.COLUMN_NAME_YIELD)),
                    results.getString(results.getColumnIndex(RecipeTable.COLUMN_NAME_YIELD_DESCRIPTOR)),
                    results.getBlob(results.getColumnIndex(RecipeTable.COLUMN_NAME_IMAGE)),
                    results.getFloat(results.getColumnIndex(RecipeTable.COLUMN_NAME_RATING))
                    );


            while (ingredientsResults.moveToNext()){
                if (ingredientsResults.getInt(ingredientsResults.getColumnIndex(IngredientsTable.COLUMN_NAME_RECIPE_ID)) == r.getRecipeId()){
                    Ingredient i = new Ingredient(ingredientsResults.getInt(ingredientsResults.getColumnIndex(IngredientsTable.COLUMN_NAME_INGREDIENT_ID)),
                            ingredientsResults.getFloat(ingredientsResults.getColumnIndex(IngredientsTable.COLUMN_NAME_QUANTITY)),
                            ingredientsResults.getString(ingredientsResults.getColumnIndex(IngredientsTable.COLUMN_NAME_NAME)),
                            ingredientsResults.getString(ingredientsResults.getColumnIndex(IngredientsTable.COLUMN_NAME_UNIT))
                    );

                    r.addIngredient(i);
                }
            }
            ingredientsResults.moveToFirst();

            while (resInstructions.moveToNext()){
                int recId = resInstructions.getInt(resInstructions.getColumnIndex(InstructionsTable.COLUMN_NAME_RECIPE_ID));
                if (recId == r.getRecipeId()){
                    Instruction i = new Instruction(
                        resInstructions.getInt(resInstructions.getColumnIndex(InstructionsTable.COLUMN_NAME_INSTRUCTION_ID)),
                        resInstructions.getString(resInstructions.getColumnIndex(InstructionsTable.COLUMN_NAME_DESCRIPTION)),
                        resInstructions.getInt(resInstructions.getColumnIndex(InstructionsTable.COLUMN_NAME_ORDER))
                    );

                    r.addInstruction(i);
                }
            }
            resInstructions.moveToFirst();

            recipes.add(r);
        }

        return recipes;
    }

    public long insertRecipe(Recipe r){
        return insertRecipe(this.getWritableDatabase(), r);
    }

    public long insertRecipe(SQLiteDatabase db, Recipe r){
        long newRowId = RecipeTable.insert(db, r);

        // Insert the ingredients
        for (Ingredient i : r.getIngredients()){
            IngredientsTable.insert(db, r, i);
        }

        for (int i = 0; i < r.getInstructions().size(); i++){
            Instruction ins = r.getInstructions().get(i);
            ins.setOrderId(i);

            InstructionsTable.insert(db, r, ins);
        }

        return newRowId;
    }

    public void updateRecipe(Recipe r){
        SQLiteDatabase db = this.getWritableDatabase();

        RecipeTable.update(db, r);

        // Update the ingredients, etc.
    }

    public boolean deleteRecipe(Recipe r){
        SQLiteDatabase db = this.getWritableDatabase();
        return RecipeTable.delete(db, r) && IngredientsTable.delete(db, r) && InstructionsTable.delete(db, r);
    }

    public ArrayList<ShoppingList> selectShoppingLists(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor results = db.rawQuery("SELECT * FROM " + ShoppingListsTable.TABLE_NAME, null);
            Cursor results2 =
                    db.rawQuery("SELECT * FROM ShoppingListItems, Ingredients WHERE ShoppingListItems.IngredientID = Ingredients.IngredientID", null);

            ArrayList<ShoppingList> lists = new ArrayList<ShoppingList>();

            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());

            while (results.moveToNext()) {

                ShoppingList sl = new ShoppingList(
                        results.getInt(results.getColumnIndex(ShoppingListsTable.COLUMN_NAME_SHOPPING_LIST_ID)),
                        results.getString(results.getColumnIndex(ShoppingListsTable.COLUMN_NAME_TITLE)),
                        sdf.parse(results.getString(results.getColumnIndex(ShoppingListsTable.COLUMN_NAME_CREATE_DATE)))
                );

                while (results2.moveToNext()) {
                    if (results2.getInt(results2.getColumnIndex(ShoppingListItemsTable.COLUMN_NAME_SHOPPING_LIST_ID)) == sl.getShoppingListId()) {

                        Ingredient i = new Ingredient(results2.getInt(results2.getColumnIndex(IngredientsTable.COLUMN_NAME_INGREDIENT_ID)),
                                results2.getFloat(results2.getColumnIndex(IngredientsTable.COLUMN_NAME_QUANTITY)),
                                results2.getString(results2.getColumnIndex(IngredientsTable.COLUMN_NAME_NAME)),
                                results2.getString(results2.getColumnIndex(IngredientsTable.COLUMN_NAME_UNIT))
                        );

                        ShoppingListItem sli = new ShoppingListItem(
                                results2.getInt(results2.getColumnIndex(ShoppingListItemsTable.COLUMN_NAME_SHOPPING_LIST_ITEM_ID)),
                                i,
                                0
                        );
                        sli.setIsDone(results2.getInt(results2.getColumnIndex(ShoppingListItemsTable.COLUMN_NAME_IS_CHECKED)) == 1 ? true : false);

                        sl.addItem(sli);
                    }
                }
                results2.moveToFirst();

                lists.add(sl);
            }

            return lists;
        }catch (Exception e){
            Log.e("ERRORS", e.getMessage());
            return null;
        }
    }

    public long insertShoppingList(ShoppingList sl){
        return insertShoppingList(this.getWritableDatabase(), sl);
    }

    public long insertShoppingList(SQLiteDatabase db, ShoppingList sl){
        long newRowId = ShoppingListsTable.insert(db, sl);

        for (ShoppingListItem sli : sl.getList()){
            ShoppingListItemsTable.insert(db, sl, sli);
        }

        return newRowId;
    }

    public void updateShoppingList(ShoppingList sl){
        updateShoppingList(this.getWritableDatabase(), sl);
    }

    public void updateShoppingList(SQLiteDatabase db, ShoppingList sl){

        for (ShoppingListItem sli : sl.getList()){
            if (sli.getShoppingListItemId() != -1){
                ShoppingListItemsTable.update(db, sl, sli);
            } else {
                ShoppingListItemsTable.insert(db, sl, sli);
            }

        }
    }

    public void updateShoppingListItem(ShoppingList sl, ShoppingListItem sli){
        ShoppingListItemsTable.update(this.getWritableDatabase(), sl, sli);
    }

    public boolean deleteShoppingListItem(ShoppingListItem sli){
        return ShoppingListItemsTable.delete(this.getWritableDatabase(), sli);
    }

    /* Inner class that defines the table contents */
    public static abstract class RecipeTable implements BaseColumns {
        public static final String TABLE_NAME = "Recipes";
        public static final String COLUMN_NAME_RECIPE_ID = "RecipeID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_TYPE = "Type";
        public static final String COLUMN_NAME_PREP_TIME = "PrepTime";
        public static final String COLUMN_NAME_COOK_TIME = "CookTime";
        public static final String COLUMN_NAME_YIELD = "Yield";
        public static final String COLUMN_NAME_YIELD_DESCRIPTOR = "YieldDescriptor";
        public static final String COLUMN_NAME_IMAGE = "Image";
        public static final String COLUMN_NAME_RATING = "Rating";

        public static final String COLUMN_TYPE_RECIPE_ID = "INTEGER";
        public static final String COLUMN_TYPE_NAME = "TEXT";
        public static final String COLUMN_TYPE_TYPE = "INTEGER";
        public static final String COLUMN_TYPE_PREP_TIME = "TEXT";
        public static final String COLUMN_TYPE_COOK_TIME = "TEXT";
        public static final String COLUMN_TYPE_YIELD = "INTEGER";
        public static final String COLUMN_TYPE_YIELD_DESCRIPTOR = "TEXT";
        public static final String COLUMN_TYPE_IMAGE = "BLOB";
        public static final String COLUMN_TYPE_RATING = "DECIMAL(5,0)";

        public static void createTable(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE Recipes (" +
                            COLUMN_NAME_RECIPE_ID + " " + COLUMN_TYPE_RECIPE_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            COLUMN_NAME_NAME + " " + COLUMN_TYPE_NAME + " ," +
                            COLUMN_NAME_TYPE + " " + COLUMN_TYPE_TYPE + " ," +
                            COLUMN_NAME_PREP_TIME + " " + COLUMN_TYPE_PREP_TIME + " ," +
                            COLUMN_NAME_COOK_TIME + " " + COLUMN_TYPE_COOK_TIME + " ," +
                            COLUMN_NAME_YIELD + " " + COLUMN_TYPE_YIELD + " ," +
                            COLUMN_NAME_YIELD_DESCRIPTOR + " " + COLUMN_TYPE_YIELD_DESCRIPTOR + " ," +
                            COLUMN_NAME_IMAGE + " " + COLUMN_TYPE_IMAGE + " ," +
                            COLUMN_NAME_RATING + " " + COLUMN_TYPE_RATING +
                            ")"
            );
        }

        public static long insert(SQLiteDatabase db, Recipe r){
            ContentValues values = new ContentValues();
            //values.put(COLUMN_NAME_RECIPE_ID, r.getRecipeId());
            values.put(COLUMN_NAME_NAME, r.getName());
            values.put(COLUMN_NAME_TYPE, r.getType().getId());
            values.put(COLUMN_NAME_PREP_TIME, r.getPrepTime());
            values.put(COLUMN_NAME_COOK_TIME, r.getCookTime());
            values.put(COLUMN_NAME_YIELD, r.getYield());
            values.put(COLUMN_NAME_YIELD_DESCRIPTOR, r.getYieldDescriptor());
            values.put(COLUMN_NAME_IMAGE, r.getImage());
            values.put(COLUMN_NAME_RATING, r.getRating());

            long newRowId = db.insert(RecipeTable.TABLE_NAME, "null", values);

            r.setRecipeId((int)newRowId);

            return newRowId;
        }

        public static void update(SQLiteDatabase db, Recipe r){
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_RECIPE_ID, r.getRecipeId());
            values.put(COLUMN_NAME_NAME, r.getName());
            values.put(COLUMN_NAME_TYPE, r.getType().getId());
            values.put(COLUMN_NAME_PREP_TIME, r.getPrepTime());
            values.put(COLUMN_NAME_COOK_TIME, r.getCookTime());
            values.put(COLUMN_NAME_YIELD, r.getYield());
            values.put(COLUMN_NAME_YIELD_DESCRIPTOR, r.getYieldDescriptor());
            values.put(COLUMN_NAME_IMAGE, r.getImage());
            values.put(COLUMN_NAME_RATING, r.getRating());

            db.update(TABLE_NAME, values, "RecipeID = ? ", new String[]{Integer.toString(r.getRecipeId())});
        }

        public static boolean delete(SQLiteDatabase db, Recipe r){
            return db.delete(TABLE_NAME,
                    "RecipeID = ? ",
                    new String[] { Integer.toString(r.getRecipeId()) }) > 0;
        }
    }

    /* Inner class that defines the table contents */
    public static abstract class IngredientsTable implements BaseColumns {
        public static final String TABLE_NAME = "Ingredients";
        public static final String COLUMN_NAME_INGREDIENT_ID = "IngredientID";
        public static final String COLUMN_NAME_RECIPE_ID = "RecipeID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_TYPE = "Type";
        public static final String COLUMN_NAME_UNIT = "Unit";
        public static final String COLUMN_NAME_QUANTITY = "Quantity";

        public static final String COLUMN_TYPE_INGREDIENT_ID = "INTEGER";
        public static final String COLUMN_TYPE_RECIPE_ID = "INTEGER";
        public static final String COLUMN_TYPE_NAME = "TEXT";
        public static final String COLUMN_TYPE_TYPE = "INTEGER";
        public static final String COLUMN_TYPE_UNIT = "TEXT";
        public static final String COLUMN_TYPE_QUANTITY = "DECIMAL(5,0)";

        public static void createTable(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE Ingredients (" +
                            COLUMN_NAME_INGREDIENT_ID + " " + COLUMN_TYPE_INGREDIENT_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            COLUMN_NAME_RECIPE_ID + " " + COLUMN_TYPE_RECIPE_ID + " NOT NULL," +
                            COLUMN_NAME_NAME + " " + COLUMN_TYPE_NAME + " ," +
                            COLUMN_NAME_TYPE + " " + COLUMN_TYPE_TYPE + " ," +
                            COLUMN_NAME_UNIT + " " + COLUMN_TYPE_UNIT + " ," +
                            COLUMN_NAME_QUANTITY + " " + COLUMN_TYPE_QUANTITY +
                            ")"
            );
        }

        public static long insert(SQLiteDatabase db, Recipe r, Ingredient i){
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_RECIPE_ID, r.getRecipeId());
            values.put(COLUMN_NAME_NAME, i.getName());
            //values.put(COLUMN_NAME_TYPE, i.getDescriptor());
            values.put(COLUMN_NAME_UNIT, i.getDescriptor());
            values.put(COLUMN_NAME_QUANTITY, i.getQuantity());

            long newRowId = db.insert(TABLE_NAME, "null", values);

            i.setIngredientId((int) newRowId);

            return newRowId;
        }

        public static boolean delete(SQLiteDatabase db, Recipe r){
            return db.delete(TABLE_NAME,
                    "RecipeID = ? ",
                    new String[] { Integer.toString(r.getRecipeId()) }) > 0;
        }
    }

    /* Inner class that defines the table contents */
    public static abstract class InstructionsTable implements BaseColumns {
        public static final String TABLE_NAME = "Instructions";
        public static final String COLUMN_NAME_INSTRUCTION_ID = "InstructionID";
        public static final String COLUMN_NAME_RECIPE_ID = "RecipeID";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_ORDER = "OrderID";

        public static final String COLUMN_TYPE_INSTRUCTION_ID = "INTEGER";
        public static final String COLUMN_TYPE_RECIPE_ID = "INTEGER";
        public static final String COLUMN_TYPE_DESCRIPTION = "TEXT";
        public static final String COLUMN_TYPE_ORDER = "INTEGER";

        public static void createTable(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE Instructions (" +
                            COLUMN_NAME_INSTRUCTION_ID + " " + COLUMN_TYPE_INSTRUCTION_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            COLUMN_NAME_RECIPE_ID + " " + COLUMN_TYPE_RECIPE_ID + " ," +
                            COLUMN_NAME_DESCRIPTION + " " + COLUMN_TYPE_DESCRIPTION + " ," +
                            COLUMN_NAME_ORDER + " " + COLUMN_TYPE_ORDER +
                            ")"
            );
        }

        public static long insert(SQLiteDatabase db, Recipe r, Instruction i){
            ContentValues values = new ContentValues();
            //values.put(COLUMN_NAME_INSTRUCTION_ID, i.getInstructionId());
            values.put(COLUMN_NAME_RECIPE_ID, r.getRecipeId());
            values.put(COLUMN_NAME_DESCRIPTION, i.getInstructions());
            values.put(COLUMN_NAME_ORDER, i.getOrderId());

            return db.insert(TABLE_NAME, "null", values);
        }

        public static boolean delete(SQLiteDatabase db, Recipe r){
            return db.delete(TABLE_NAME,
                    "RecipeID = ? ",
                    new String[] { Integer.toString(r.getRecipeId()) }) > 0;
        }
    }

    /* Inner class that defines the table contents */
//    public static abstract class RecipeIngredientsTable implements BaseColumns {
//        public static final String TABLE_NAME = "RecipeIngredients";
//        public static final String COLUMN_NAME_RECIPE_ID = "RecipeID";
//        public static final String COLUMN_NAME_INGREDIENT_ID = "IngredientID";
//        public static final String COLUMN_NAME_QUANTITY = "Quantity";
//
//        public static final String COLUMN_TYPE_RECIPE_ID = "INTEGER";
//        public static final String COLUMN_TYPE_INGREDIENT_ID = "INTEGER";
//        public static final String COLUMN_TYPE_QUANTITY = "DECIMAL(5,0)";
//
//        public static void createTable(SQLiteDatabase db){
//            db.execSQL(
//                    "CREATE TABLE RecipeIngredients (" +
//                            COLUMN_NAME_RECIPE_ID + " " + COLUMN_TYPE_RECIPE_ID + " ," +
//                            COLUMN_NAME_INGREDIENT_ID + " " + COLUMN_TYPE_INGREDIENT_ID + " ," +
//                            COLUMN_NAME_QUANTITY + " " + COLUMN_TYPE_QUANTITY +
//                            ", PRIMARY KEY (" + COLUMN_NAME_RECIPE_ID + ", " + COLUMN_NAME_INGREDIENT_ID + ")" +
//                            ")"
//            );
//        }
//
//        public static long insert(SQLiteDatabase db, Recipe r, Ingredient i){
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_NAME_RECIPE_ID, r.getRecipeId());
//            values.put(COLUMN_NAME_INGREDIENT_ID, i.getIngredientId());
//            values.put(COLUMN_NAME_QUANTITY, i.getQuantity());
//
//            return db.insert(TABLE_NAME, "null", values);
//        }
//    }

    /* Inner class that defines the table contents */
    public static abstract class ShoppingListsTable implements BaseColumns {
        public static final String TABLE_NAME = "ShoppingLists";
        public static final String COLUMN_NAME_SHOPPING_LIST_ID = "ShoppingListID";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_CREATE_DATE = "CreateDate";

        public static final String COLUMN_TYPE_SHOPPING_LIST_ID = "INTEGER";
        public static final String COLUMN_TYPE_TITLE = "TEXT";
        public static final String COLUMN_TYPE_CREATE_DATE = "DATETIME";

        public static void createTable(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE ShoppingLists (" +
                            COLUMN_NAME_SHOPPING_LIST_ID + " " + COLUMN_TYPE_SHOPPING_LIST_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            COLUMN_NAME_TITLE + " " + COLUMN_TYPE_TITLE + " , " +
                            COLUMN_NAME_CREATE_DATE + " " + COLUMN_TYPE_CREATE_DATE +
                            ")"
            );
        }
        public static long insert(SQLiteDatabase db, ShoppingList sl){
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_TITLE, sl.getTitle());
            values.put(COLUMN_NAME_CREATE_DATE, new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(sl.getDate()));

            long newRowId = db.insert(TABLE_NAME, "null", values);

            sl.setShoppingListId((int)newRowId);

            return newRowId;
        }
    }

    /* Inner class that defines the table contents */
    public static abstract class ShoppingListItemsTable implements BaseColumns {
        public static final String TABLE_NAME = "ShoppingListItems";
        public static final String COLUMN_NAME_SHOPPING_LIST_ITEM_ID = "ShoppingListItemID";
        public static final String COLUMN_NAME_SHOPPING_LIST_ID = "ShoppingListID";
        public static final String COLUMN_NAME_INGREDIENT_ID = "IngredientID";
        public static final String COLUMN_NAME_QUANTITY = "Quantity";
        public static final String COLUMN_NAME_IS_CHECKED = "IsChecked";

        public static final String COLUMN_TYPE_SHOPPING_LIST_ITEM_ID = "INTEGER";
        public static final String COLUMN_TYPE_SHOPPING_LIST_ID = "INTEGER";
        public static final String COLUMN_TYPE_INGREDIENT_ID = "INTEGER";
        public static final String COLUMN_TYPE_QUANTITY = "DECIMAL(5,0)";
        public static final String COLUMN_TYPE_IS_CHECKED = "BIT";

        public static void createTable(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE ShoppingListItems (" +
                            COLUMN_NAME_SHOPPING_LIST_ITEM_ID + " " + COLUMN_TYPE_SHOPPING_LIST_ITEM_ID + " PRIMARY KEY AUTOINCREMENT," +
                            COLUMN_NAME_SHOPPING_LIST_ID + " " + COLUMN_TYPE_SHOPPING_LIST_ID + " , " +
                            COLUMN_NAME_INGREDIENT_ID + " " + COLUMN_TYPE_INGREDIENT_ID + " , " +
                            COLUMN_NAME_QUANTITY + " " + COLUMN_TYPE_QUANTITY + " , " +
                            COLUMN_NAME_IS_CHECKED + " " + COLUMN_TYPE_IS_CHECKED +
                            ")"
            );
        }

        public static long insert(SQLiteDatabase db, ShoppingList sl, ShoppingListItem sli){
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_SHOPPING_LIST_ID, sl.getShoppingListId());
            values.put(COLUMN_NAME_INGREDIENT_ID, sli.getIngredient().getIngredientId());
            //values.put(COLUMN_NAME_QUANTITY, sl.getTitle());
            values.put(COLUMN_NAME_IS_CHECKED, sli.isDone());

            long newRowId = db.insert(TABLE_NAME, "null", values);

            sli.setShoppingListItemId((int) newRowId);

            return newRowId;
        }

        public static void update(SQLiteDatabase db, ShoppingList sl, ShoppingListItem sli){
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_SHOPPING_LIST_ID, sl.getShoppingListId());
            values.put(COLUMN_NAME_INGREDIENT_ID, sli.getIngredient().getIngredientId());
            //values.put(COLUMN_NAME_QUANTITY, sl.getTitle());
            values.put(COLUMN_NAME_IS_CHECKED, sli.isDone());

            db.update(TABLE_NAME, values, "ShoppingListItemID = ? ", new String[]{Integer.toString(sli.getShoppingListItemId())});
        }

        public static boolean delete(SQLiteDatabase db, ShoppingListItem sli){
            return db.delete(TABLE_NAME,
                    "ShoppingListItemID = ? ",
                    new String[] { Integer.toString(sli.getShoppingListItemId()) }) > 0;
        }
    }

    //NOTE: I copied this code as a template:
    // http://www.tutorialspoint.com/android/android_sqlite_database.htm
}
