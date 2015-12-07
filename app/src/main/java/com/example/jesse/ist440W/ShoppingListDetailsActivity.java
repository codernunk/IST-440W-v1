package com.example.jesse.ist440W;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Ingredient;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.models.ShoppingList;
import com.example.jesse.ist440W.models.ShoppingListItem;
import com.example.jesse.ist440W.services.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDetailsActivity extends AppCompatActivity {

    private TextView _txtTitle;
    private TextView _txtDate;
    private ListView _lvItems;

    private ShoppingList _currentShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the recipe
        Intent intent = getIntent();

        if (intent != null){
            int id = intent.getIntExtra("ShoppingList", -1);

            for (ShoppingList sl :  App.getInstance().getShoppingLists()){
                if (sl.getShoppingListId() == id) {
                    _currentShoppingList = sl;
                    break;
                }
            }

            _txtTitle = (TextView) findViewById(R.id.txtShoppingListTitle);
            _txtTitle.setText(_currentShoppingList.getTitle());

            _txtDate = (TextView) findViewById(R.id.txtShoppingListDate);
            _txtDate.setText("Created on " + new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(_currentShoppingList.getDate()));

            ItemsListAdapter adapter = new ItemsListAdapter(getApplicationContext(), R.layout.recipe_ingredient_list_shopping_list_view, _currentShoppingList.getList());

            _lvItems = (ListView) findViewById(R.id.listView);
            _lvItems.setAdapter(adapter);
        }

    }

    /**
     * An inner class that helps to construct list items
     * that show recipes.
     */
    private class ItemsListAdapter extends ArrayAdapter<ShoppingListItem> {

        private Context _context;

        public ItemsListAdapter(Context context, int textViewResourceId, List<ShoppingListItem> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this._context = context;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ShoppingListItem rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.recipe_ingredient_list_shopping_list_view, null);

                TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientName);
                ingredientName.setText(rowItem.getIngredient().getName());

                CheckBox ingredientChecked = (CheckBox) convertView.findViewById(R.id.ingredientChecked);
                ingredientChecked.setChecked(rowItem.isDone());

                ingredientChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        rowItem.setIsDone(isChecked);
                        App.getInstance().getDataAccess().updateShoppingListItem(_currentShoppingList, rowItem);
                    }
                });

                ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = new AlertDialog.Builder(ShoppingListDetailsActivity.this)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to remove the ingredient, "+rowItem.getIngredient().getName()+ ", from the shopping list?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        App.getInstance().getDataAccess().deleteShoppingListItem(rowItem);
                                        _currentShoppingList.getList().remove(rowItem);
                                        ItemsListAdapter.this.notifyDataSetChanged();
                                        //refresh();
                                    }
                                })
                                .setNegativeButton("Cancel", null).create();

                        ad.show();
                    }
                });

            } else {
                TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientName);
                ingredientName.setText(rowItem.getIngredient().getName());

                CheckBox ingredientChecked = (CheckBox) convertView.findViewById(R.id.ingredientChecked);
                ingredientChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        rowItem.setIsDone(isChecked);
                        App.getInstance().getDataAccess().updateShoppingListItem(_currentShoppingList, rowItem);
                    }
                });
                ingredientChecked.setChecked(rowItem.isDone());

                ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = new AlertDialog.Builder(ShoppingListDetailsActivity.this)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to remove the ingredient, "+rowItem.getIngredient().getName()+ ", from the shopping list?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        App.getInstance().getDataAccess().deleteShoppingListItem(rowItem);
                                        _currentShoppingList.getList().remove(rowItem);
                                        ItemsListAdapter.this.notifyDataSetChanged();
                                        //refresh();
                                    }
                                })
                                .setNegativeButton("Cancel", null).create();

                        ad.show();
                    }
                });
            }

            return convertView;
        }

        public void refresh(){
            clear();
            addAll(_currentShoppingList.getList());
            notifyDataSetChanged();
        }
    }

}
