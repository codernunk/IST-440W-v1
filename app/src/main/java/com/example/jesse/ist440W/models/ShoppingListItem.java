package com.example.jesse.ist440W.models;

/**
 * Represents one item in the shopping list.
 */
public class ShoppingListItem {
    private int shoppingListItemId;
    private Ingredient ingredient;
    private float quantity;// Overrides Ingredient's value
    private boolean isDone;

    public ShoppingListItem(int shoppingListItemId, Ingredient ingredient, float quantity) {
        this.shoppingListItemId = shoppingListItemId;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public int getShoppingListItemId() {
        return shoppingListItemId;
    }

    public void setShoppingListItemId(int shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
}