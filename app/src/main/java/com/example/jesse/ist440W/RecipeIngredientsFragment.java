package com.example.jesse.ist440W;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.jesse.ist440W.models.Ingredient;
import com.example.jesse.ist440W.models.Instruction;
import com.example.jesse.ist440W.models.Recipe;

import java.util.ArrayList;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                // Make a new shopping list activity or whatever
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

                // Set the display's name to the recipe's name
                TextView ingredientQuantity = (TextView) convertView.findViewById(R.id.ingredientQuantity);
                ingredientQuantity.setText(rowItem.getQuantity()+ " " + rowItem.getDescriptor());

                TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientName);
                ingredientName.setText(rowItem.getName());

                CheckBox ingredientChecked = (CheckBox) convertView.findViewById(R.id.ingredientChecked);
                ingredientChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            _selectedIngredients.add(rowItem);
                        }else{
                            _selectedIngredients.remove(rowItem);
                        }

                    }
                });

                _checkBoxes.add(ingredientChecked);
                //ingredientQuantity.setText(rowItem.getName());

            } else {
                // Set the display's name to the recipe's name
//                TextView recipeName = (TextView) convertView.findViewById(R.id.recipeName);
//                recipeName.setText(rowItem.getName());

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
