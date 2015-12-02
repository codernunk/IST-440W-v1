package com.example.jesse.ist440W;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        List<String> ingredientsList = new ArrayList<String>();

        for (Ingredient i : r.getIngredients()){
            ingredientsList.add(i.getQuantity() + " " + i.getDescriptor() + " " + i.getName());
        }

        ArrayAdapter<String> ingredientsArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                ingredientsList);

        _lvIngredients = (ListView) view.findViewById(R.id.lvIngredients);
        _lvIngredients.setAdapter(ingredientsArrayAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}
