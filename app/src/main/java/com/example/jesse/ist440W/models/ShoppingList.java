package com.example.jesse.ist440W.models;

import java.util.ArrayList;

/**
 * Represents a shopping list, which contains a list of Ingredients.
 * Created by Jesse on 9/22/2015.
 */
public class ShoppingList {

    private ArrayList<ShoppingListItem> list;

    public ShoppingList(ArrayList<ShoppingListItem> list) {
        this.list = list;
    }

    public ArrayList<ShoppingListItem> getList() {
        return list;
    }

    /**
     * Represents one item in the shopping list.
     */
    class ShoppingListItem {
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
}
