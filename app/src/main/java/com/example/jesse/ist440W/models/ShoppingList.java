package com.example.jesse.ist440W.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a shopping list, which contains a list of Ingredients.
 * Created by Jesse on 9/22/2015.
 */
public class ShoppingList implements java.io.Serializable {



    private int shoppingListId;
    private String title;
    private Date date;
    private ArrayList<ShoppingListItem> list;

    public ShoppingList(String title, Date date){
        this.title = title;
        this.date = date;
        this.list = new ArrayList<ShoppingListItem>();
    }

    public ShoppingList(int id, String title, Date date){
        this(title, date);
        this.shoppingListId = id;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }
    public String getTitle() { return title; }
    public Date getDate() { return date; }

    public ArrayList<ShoppingListItem> getList() {
        return list;
    }

    public String getQualifiedName() {
        return title + ": " + new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }


    public void addItem(ShoppingListItem sli){
        this.list.add(sli);
    }

}
