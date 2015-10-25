package com.example.jesse.ist440W.models;

/**
 * Represents one unit of a specific ingredient in a recipe.
 * Created by Jesse on 9/22/2015.
 */
public class Ingredient {
    private int ingredientId;
    private float quantity;// Represented as a float, temporarily
    private String name;

    public Ingredient(int ingredientId, float quantity, String name) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.name = name;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
