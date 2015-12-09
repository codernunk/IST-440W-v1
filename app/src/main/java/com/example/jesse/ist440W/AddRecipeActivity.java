package com.example.jesse.ist440W;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jesse.ist440W.R;
import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Ingredient;
import com.example.jesse.ist440W.models.Instruction;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.services.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private Recipe recipe;

    private EditText txtRecipeName;
    private Spinner spRecipeType;
    private EditText txtPrepTime;
    private EditText txtCookTime;
    private EditText txtYieldValue;
    private EditText txtYieldDescriptor;
    private Button btnAddIngredient;
    private Button btnAddDirection;
    private Button btnUploadImage;
    private Button btnAddRecipe;
    private ListView lvIngredients;
    private ListView lvDirections;
    private ImageView ivImage;

    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Instruction> instructions = new ArrayList<>();
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the fields
        txtRecipeName = (EditText) findViewById(R.id.txtRecipeName);
        txtPrepTime = (EditText) findViewById(R.id.txtPrepTime);
        txtCookTime = (EditText) findViewById(R.id.txtCookTime);
        txtYieldValue = (EditText) findViewById(R.id.txtYieldValue);
        txtYieldDescriptor = (EditText) findViewById(R.id.txtYieldDescriptor);
        spRecipeType = (Spinner) findViewById(R.id.spRecipeType);
        btnAddIngredient = (Button) findViewById(R.id.btnAddIngredient);
        btnAddDirection = (Button) findViewById(R.id.btnAddDirection);
        btnUploadImage = (Button) findViewById(R.id.btnUploadImage);
        btnAddRecipe = (Button) findViewById(R.id.btnAddRecipe);
        lvIngredients = (ListView) findViewById(R.id.lvIngredients);
        lvDirections = (ListView) findViewById(R.id.lvDirections);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        // Spinner
        ArrayList<String> foodTypes = new ArrayList<>();
        for (Recipe.FoodType f : Recipe.FoodType.values()){
            foodTypes.add(f.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, foodTypes);
        spRecipeType.setAdapter(adapter);

        // Ingredients list view
        final IngredientListAdapter ila = new IngredientListAdapter(this, R.layout.recipe_ingredient_list_view_b, ingredients);
        lvIngredients.setAdapter(ila);

        // Directions list view
        final InstructionsListAdapter insla = new InstructionsListAdapter(this, R.layout.recipe_direction_list_view, instructions);
        lvDirections.setAdapter(insla);

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ila.add(new Ingredient(-1, 10, "STEAK", "12 oz"));
                ila.notifyDataSetChanged();
            }
        });
        btnAddDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insla.add(new Instruction(-1, "do stuff", -1));
                insla.notifyDataSetChanged();
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phototPickerIntent = new Intent(Intent.ACTION_PICK);
                phototPickerIntent.setType("image/*");
                startActivityForResult(phototPickerIntent, 100);
            }
        });
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate

                Recipe r = new Recipe(-1,
                        txtRecipeName.getText().toString(),
                        Recipe.FoodType.Breakfast,
                        Integer.parseInt(txtPrepTime.getText().toString()),
                        Integer.parseInt(txtCookTime.getText().toString()),
                        Integer.parseInt(txtYieldValue.getText().toString()),
                        txtYieldDescriptor.getText().toString(),
                        ingredients.toArray(new Ingredient[ingredients.size()]),
                        instructions.toArray(new Instruction[instructions.size()])
                        );

                if (image != null){
                    r.setImage(image);
                }

                App.getInstance().getDataAccess().insertRecipe(r);

                App.getInstance().getRecipes().add(r);

                // Open up the new activity
                //AddRecipeActivity.this.finish();

                Intent i = new Intent(AddRecipeActivity.this, RecipeDetailsActivity.class);
                i.putExtra("Recipe", r.getRecipeId());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        ivImage.setImageBitmap(bmp);
                        image = stream.toByteArray();
                    }catch (Exception e) {
                        Log.e("ERROR", e.getMessage());
                    }
                }
        }
    }

    private class IngredientListAdapter extends ArrayAdapter<Ingredient> {
        private Context _context;

        public IngredientListAdapter(Context context, int textViewResourceId, List<Ingredient> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this._context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final Ingredient rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.recipe_ingredient_list_view_b, null);

                // Set the display name to the recipe's name
                TextView ingredientQuantity = (TextView) convertView.findViewById(R.id.ingredientQuantity);
                ingredientQuantity.setText(Utils.formatQuantity(rowItem.getQuantity()) + " " + rowItem.getDescriptor());

                TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientName);
                ingredientName.setText(rowItem.getName());

            } else {
                // Set the display name to the recipe's name
                TextView ingredientQuantity = (TextView) convertView.findViewById(R.id.ingredientQuantity);
                ingredientQuantity.setText(Utils.formatQuantity(rowItem.getQuantity()) + " " + rowItem.getDescriptor());

                TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientName);
                ingredientName.setText(rowItem.getName());
            }

            return convertView;
        }
    }

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
