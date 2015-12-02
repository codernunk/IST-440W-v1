package com.example.jesse.ist440W.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a shopping list, which contains a list of Ingredients.
 * Created by Jesse on 9/22/2015.
 */
public class ShoppingList {

    private Date date;
    private ArrayList<ShoppingListItem> list;

    public ShoppingList(ArrayList<ShoppingListItem> list) {
        this.list = list;
        date = new Date();
    }

    public ArrayList<ShoppingListItem> getList() {
        return list;
    }

    public Date getDate() { return date; }
}
