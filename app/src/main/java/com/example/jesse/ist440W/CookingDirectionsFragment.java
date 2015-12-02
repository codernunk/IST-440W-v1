package com.example.jesse.ist440W;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
        InstructionsListAdapter directionsArrayAdapter = new InstructionsListAdapter(getContext(), R.layout.recipe_direction_list_view, r.getInstructions());

        lvDirections = (ListView) view.findViewById(R.id.lvDirections);
        lvDirections.setAdapter(directionsArrayAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * An inner class that helps to construct list items
     * that show recipes.
     */
    private class InstructionsListAdapter extends ArrayAdapter<Instruction> {

        private Context _context;

        public InstructionsListAdapter(Context context, int textViewResourceId, List<Instruction> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this._context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Instruction rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.recipe_direction_list_view, null);

                // Set the display's name to the recipe's name
                TextView directionStep = (TextView) convertView.findViewById(R.id.directionStep);
                directionStep.setText((position+1) + ". ");

                TextView directionsDetails = (TextView) convertView.findViewById(R.id.directions);
                directionsDetails.setText(rowItem.getInstructions());

            } else {


            }

            return convertView;
        }
    }
}
