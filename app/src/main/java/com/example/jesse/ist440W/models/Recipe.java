package com.example.jesse.ist440W.models;

import java.io.Serializable;
import java.util.ArrayList;

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
    private ArrayList<Instruction> instructions;

    public Recipe(int recipeId, String name, FoodType type, int prepTime, int cookTime, int yield, String yieldDescriptor) {
        this.recipeId = recipeId;
        this.name = name;
        this.type = type;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.yield = yield;
        this.yieldDescriptor = yieldDescriptor;
        this.ingredients = new ArrayList<Ingredient>();
        this.instructions = new ArrayList<Instruction>();
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

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public enum FoodType {
        Breakfast("Breakfast"), Lunch("Lunch"), Dinner("Dinner"),
        Dessert("Dessert"), Snack("Snack"), Other("Other");

        private String name;

        FoodType(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }
}
