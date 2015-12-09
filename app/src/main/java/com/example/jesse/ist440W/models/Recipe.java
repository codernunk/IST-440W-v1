package com.example.jesse.ist440W.models;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.example.jesse.ist440W.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Represents one recipe.
 * Created by Jesse on 9/22/2015.
 */
public class Recipe implements Serializable {
    private int recipeId;
    private String name;
    private FoodType type;
    private ArrayList<Ingredient> ingredients;
    private int prepTime;
    private int cookTime;
    private int yield;
    private String yieldDescriptor;
    private byte[] image;
    private float rating;
    private ArrayList<Instruction> instructions;

    public Recipe(int recipeId, String name, FoodType type, int prepTime, int cookTime, int yield, String yieldDescriptor, byte[] image, float rating) {
        this.recipeId = recipeId;
        this.name = name;
        this.type = type;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.yield = yield;
        this.yieldDescriptor = yieldDescriptor;
        this.image = image;
        this.rating = rating;
        this.ingredients = new ArrayList<Ingredient>();
        this.instructions = new ArrayList<Instruction>();
    }

    public Recipe(int recipeId, String name, FoodType type, int prepTime, int cookTime, int yield, String yieldDescriptor, Ingredient[] ingredients, Instruction[] instructions) {
        this(recipeId, name, type, prepTime, cookTime, yield, yieldDescriptor, null, 0);

        this.ingredients = new ArrayList<Ingredient>(Arrays.asList(ingredients));
        this.instructions = new ArrayList<Instruction>(Arrays.asList(instructions));
    }

    public void addIngredient(Ingredient i){
        this.ingredients.add(i);
    }

    public void addInstruction(Instruction i){
        this.instructions.add(i);
    }

    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getYield() {
        return yield;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public String getYieldDescriptor() {
        return yieldDescriptor;
    }

    public void setYieldDescriptor(String yieldDescriptor) {
        this.yieldDescriptor = yieldDescriptor;
    }

    public byte[] getImage() { return image; }

    public void setImageFromResource(){
        Bitmap b = decodeResource(Resources.getSystem(), R.mipmap.ic_teststeak);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        image = stream.toByteArray();
    }

    public void setImage(byte[] image){
        this.image = image;
    }

    public float getRating() { return this.rating; }

    public void setRating(float value) { this.rating = value; }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public enum FoodType {
        Breakfast(0, "Breakfast"), Lunch(1, "Lunch"), Dinner(2, "Dinner"),
        Dessert(3, "Dessert"), Snack(4, "Snack"), Drink(5, "Drink"), Other(6, "Other");

        private int id;
        private String name;

        FoodType(int id, String name){
            this.id = id;
            this.name = name;
        }

        public int getId() { return this.id; }
        public String getName(){
            return this.name;
        }

        public static FoodType fromInt(int val){
            FoodType[] values = FoodType.values();
            for (FoodType t : values){
                if (t.getId() == val)
                    return t;
            }
            return FoodType.Other;
        }
    }
}
