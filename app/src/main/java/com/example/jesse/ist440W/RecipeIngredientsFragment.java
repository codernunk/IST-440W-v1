package com.example.jesse.ist440W;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jesse.ist440W.data.local.SQLiteDataAccess;
import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Ingredient;
import com.example.jesse.ist440W.models.Instruction;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.models.ShoppingList;
import com.example.jesse.ist440W.models.ShoppingListItem;
import com.example.jesse.ist440W.services.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecipeIngredientsFragment extends Fragment {

    TextView _txtRecipeName;
    ListView _lvIngredients;
    Button _btnAddToShoppingList;
    CheckBox _chkSelectAll;

    ArrayList<Ingredient> _selectedIngredients = new ArrayList<Ingredient>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        Recipe r = ((RecipeDetailsActivity)getActivity()).getCurrentRecipe();

        _txtRecipeName = (TextView)view.findViewById(R.id.tvRecipeName);
        _txtRecipeName.setText(r.getName());

        // Display the ingredients as a list of strings
        final IngredientListAdapter ingredientsArrayAdapter = new IngredientListAdapter(this.getContext(), R.layout.recipe_ingredient_list_view, r.getIngredients());

        _lvIngredients = (ListView) view.findViewById(R.id.lvIngredients);
        _lvIngredients.setAdapter(ingredientsArrayAdapter);

        _btnAddToShoppingList = (Button) view.findViewById(R.id.btnAddToShoppingList);
        _btnAddToShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_selectedIngredients.size() > 0){

                    ArrayList<String> _shoppingListNames = new ArrayList<String>();
                    _shoppingListNames.add("+ Create new shopping list");
                    for (ShoppingList s : App.getInstance().getShoppingLists()){
                        _shoppingListNames.add(s.getQualifiedName());
                    }

                    // Make a new shopping list activity or whatever
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("Add to Shopping List")
                            .setItems(_shoppingListNames.toArray(new String[_shoppingListNames.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0){
                                        showNameShoppingListDialog();
                                    }else if (which > 0){
                                        addToShoppingList(which);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    dialog.show();
                }else{
                    // Make a new shopping list activity or whatever
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("Add to Shopping List")
                            .setMessage("You must select at least one ingredient to add to a shopping list.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    dialog.show();
                }

            }
        });

        _chkSelectAll = (CheckBox) view.findViewById(R.id.chkSelectAll);
        _chkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ingredientsArrayAdapter.selectAllCheckBoxes(isChecked);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void showNameShoppingListDialog(){
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_name_shopping_list, null);
        final TextView tv = (TextView) view.findViewById(R.id.txtName);

        AlertDialog nameDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Name Shopping List")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeShoppingList(tv.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        nameDialog.show();
    }

    public void makeShoppingList(String name){
        try{
            if (name.isEmpty())
                name = "Unnamed List";

            ShoppingList sl = new ShoppingList(name, new Date());
            App.getInstance().getShoppingLists().add(sl);

            for (Ingredient i : _selectedIngredients){
                sl.getList().add(new ShoppingListItem(-1, i, 1));
            }

            App.getInstance().getDataAccess().insertShoppingList(sl);

            Intent i = new Intent(getActivity(), ShoppingListDetailsActivity.class);
            i.putExtra("ShoppingList", sl.getShoppingListId());
            startActivity(i);
        }catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }
    }

    public void addToShoppingList(int which){
        try{
            ShoppingList sl = App.getInstance().getShoppingLists().get(which-1);

            for (Ingredient i : _selectedIngredients){
                sl.getList().add(new ShoppingListItem(-1, i, 1));
            }

            App.getInstance().getDataAccess().updateShoppingList(sl);

            Intent i = new Intent(getActivity(), ShoppingListDetailsActivity.class);
            i.putExtra("ShoppingList", sl.getShoppingListId());
            startActivity(i);
        }catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }
    }

    /**
     * An inner class that helps to construct list items
     * that show recipes.
     */
    private class IngredientListAdapter extends ArrayAdapter<Ingredient> {

        private Context _context;
        private ArrayList<CheckBox> _checkBoxes;

        public IngredientListAdapter(Context context, int textViewResourceId, List<Ingredient> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this._context = context;
            _checkBoxes = new ArrayList<CheckBox>();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final Ingredient rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.recipe_ingredient_list_view, null);

                // Set the display name to the recipe's name
                TextView ingredientQuantity = (TextView) convertView.findViewById(R.id.ingredientQuantity);
                ingredientQuantity.setText(Utils.formatQuantity(rowItem.getQuantity()) + " " + rowItem.getDescriptor());

                TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientName);
                ingredientName.setText(rowItem.getName());

                CheckBox ingredientChecked = (CheckBox) convertView.findViewById(R.id.ingredientChecked);
                ingredientChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && !_selectedIngredients.contains(rowItem)) {
                            _selectedIngredients.add(rowItem);
                        } else if (!isChecked) {
                            _selectedIngredients.remove(rowItem);
                        }

                    }
                });

                _checkBoxes.add(ingredientChecked);

            } else {
            }

            return convertView;
        }

        public void selectAllCheckBoxes(boolean isChecked){
            for (CheckBox c: _checkBoxes){
                c.setChecked(isChecked);
            }
        }

    }
}
