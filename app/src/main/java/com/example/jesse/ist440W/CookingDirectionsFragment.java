package com.example.jesse.ist440W;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.services.Utils;


public class CookingDirectionsFragment extends Fragment {

    TextView txtRecipeName;

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
//        txtMealType = (TextView)view.findViewById(R.id.tvMealType);
//        txtYield = (TextView)view.findViewById(R.id.tvYields);
//        txtPrepTime = (TextView)view.findViewById(R.id.tvPrepTime);
//        txtCookTime = (TextView)view.findViewById(R.id.tvCookTime);

        txtRecipeName.setText(r.getName());
//        txtMealType.setText(r.getType().getName());
//        txtYield.setText(r.getYield() + " " + r.getYieldDescriptor());
//        txtPrepTime.setText(Utils.formatTime(r.getPrepTime()));
//        txtCookTime.setText(Utils.formatTime(r.getCookTime()));

        // Inflate the layout for this fragment
        return view;
    }
}
