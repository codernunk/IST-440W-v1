package com.example.jesse.ist440W;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.services.Utils;

import org.w3c.dom.Text;

public class RecipeDetailsFragment extends Fragment {

    TextView txtRecipeName;
    TextView txtMealType;
    TextView txtYield;
    TextView txtPrepTime;
    TextView txtCookTime;

    ImageView ivRecipeImage;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        final Recipe r = ((RecipeDetailsActivity)getActivity()).getCurrentRecipe();

        txtRecipeName = (TextView)view.findViewById(R.id.tvRecipeName);
        txtMealType = (TextView)view.findViewById(R.id.tvMealType);
        txtYield = (TextView)view.findViewById(R.id.tvYields);
        txtPrepTime = (TextView)view.findViewById(R.id.tvPrepTime);
        txtCookTime = (TextView)view.findViewById(R.id.tvCookTime);

        txtRecipeName.setText(r.getName());
        txtMealType.setText(r.getType().getName());
        txtYield.setText(r.getYield() + " " + r.getYieldDescriptor());
        txtPrepTime.setText(Utils.formatTime(r.getPrepTime()));
        txtCookTime.setText(Utils.formatTime(r.getCookTime()));

        ivRecipeImage = (ImageView)view.findViewById(R.id.ivRecipeImage);
        ivRecipeImage.setImageBitmap(BitmapFactory.decodeByteArray(r.getImage(), 0, r.getImage().length));

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rbRating);
        ratingBar.setRating(r.getRating());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                r.setRating(rating);
                App.getInstance().getDataAccess().updateRecipe(r);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
