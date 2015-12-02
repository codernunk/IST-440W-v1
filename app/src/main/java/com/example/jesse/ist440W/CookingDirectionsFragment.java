package com.example.jesse.ist440W;

import android.app.Activity;
import android.net.Uri;
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
import com.example.jesse.ist440W.services.Utils;

import java.util.ArrayList;
import java.util.List;


public class CookingDirectionsFragment extends Fragment {

    TextView txtRecipeName;
    ListView lvDirections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cooking_directions, container, false);
        Recipe r = ((RecipeDetailsActivity)getActivity()).getCurrentRecipe();

        txtRecipeName = (TextView)view.findViewById(R.id.tvRecipeName);
        txtRecipeName.setText(r.getName());

        // Display the instructions as a list of strings
        List<String> directionsList = new ArrayList<String>();

        for (Instruction i : r.getInstructions()){
            directionsList.add(i.getOrderId() + ". " + i.getInstructions());
        }

        ArrayAdapter<String> directionsArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                directionsList);

        lvDirections = (ListView) view.findViewById(R.id.lvDirections);
        lvDirections.setAdapter(directionsArrayAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}
