
package com.example.jesse.ist440W;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jesse.ist440W.data.local.SQLiteDataAccess;
import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Ingredient;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.services.SyncService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends ListFragment {

    private RecipeListAdapter adapter;

    private ArrayList<Recipe> filteredRecipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        filteredRecipes = new ArrayList<Recipe>();
        for (Recipe r : App.getInstance().getRecipes()){
            filteredRecipes.add(r);
        }

        adapter = new RecipeListAdapter(this.getContext(), R.layout.recipe_list_view, filteredRecipes);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe r = (Recipe) parent.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), RecipeDetailsActivity.class);
                i.putExtra("Recipe", r.getRecipeId());
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    public void filter(String searchStr){
        filteredRecipes = new ArrayList<Recipe>();
        for (Recipe r : App.getInstance().getRecipes()){
            if (r.getName().toLowerCase().contains(searchStr)){
                filteredRecipes.add(r);
            }else{
                for (Ingredient i : r.getIngredients()){
                    if (i.getName().toLowerCase().contains(searchStr)){
                        filteredRecipes.add(r);
                        break;
                    }
                }
            }
        }
        adapter.refresh(filteredRecipes);
    }

    public void resetFilter(){
        filteredRecipes = new ArrayList<Recipe>();
        for (Recipe r : App.getInstance().getRecipes()){
            filteredRecipes.add(r);
        }
        adapter.refresh(filteredRecipes);
    }

    /**
     * An inner class that helps to construct list items
     * that show recipes.
     */
    private class RecipeListAdapter extends ArrayAdapter<Recipe>{

        private Context _context;

        public RecipeListAdapter(Context context, int textViewResourceId, List<Recipe> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this._context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Recipe rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.recipe_list_view, null);

                // Set the display's name to the recipe's name
                TextView recipeName = (TextView) convertView.findViewById(R.id.recipeName);
                recipeName.setText(rowItem.getName());

                // Sets the image of the icon to the recipe's image (uses the search icon temporarily)
                ImageView recipeImage = (ImageView) convertView.findViewById(R.id.recipeThumbnail);
                recipeImage.setImageBitmap(BitmapFactory.decodeByteArray(rowItem.getImage(), 0, rowItem.getImage().length));

                RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rbRating);
                ratingBar.setRating(rowItem.getRating());
            }else{
                // Set the display's name to the recipe's name
                TextView recipeName = (TextView) convertView.findViewById(R.id.recipeName);
                recipeName.setText(rowItem.getName());

                // Sets the image of the icon to the recipe's image (uses the search icon temporarily)
                ImageView recipeImage = (ImageView) convertView.findViewById(R.id.recipeThumbnail);
                recipeImage.setImageBitmap(BitmapFactory.decodeByteArray(rowItem.getImage(), 0, rowItem.getImage().length));

                RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rbRating);
                ratingBar.setRating(rowItem.getRating());
            }

            return convertView;
        }

        public void refresh(List<Recipe> recipes){
            clear();
            addAll(recipes);
            notifyDataSetChanged();
        }

    }

}