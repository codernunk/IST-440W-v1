package com.example.jesse.ist440W.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A service that helps with syncing recipe data to the "cloud".
 * Created by Jesse on 11/7/2015.
 */
public class SyncService {

    public static final String URL_BASE = "http://1recipeshoppinglisttestapi.azurewebsites.net/api/";

    public static void GetRecipe()  {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return getRequestJSON("recipe", "GET", null);
            }
        };

        task.execute();

    }

    public static void SyncRecipes(Recipe... recipes){
        AsyncTask<Recipe, Void, String> task = new AsyncTask<Recipe, Void, String>() {
            @Override
            protected String doInBackground(Recipe... params) {
                ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();

                try {
                    for (Recipe r : params) {
                        JSONObject recipe = new JSONObject();
                        recipe.put("RecipeID", r.getRecipeId());
                        recipe.put("Name", r.getName());
                        recipe.put("Type", r.getType());
                        recipe.put("PrepTime", r.getPrepTime());
                        recipe.put("CookTime", r.getCookTime());
                        recipe.put("Yield", r.getYield());
                        recipe.put("YieldDescriptor", r.getYieldDescriptor());
                        recipe.put("Ingredients", r.getIngredients());
                    }
                } catch (Exception e){

                }
                String result = getRequestJSON("recipe", "POST", jsons).toString();
                return result;
            }
        };
        task.execute(recipes);
    }

    private static String getResponse(String urlAction, String requestMethod, ArrayList<JSONObject> params){
        try {
            URL site = new URL(URL_BASE + urlAction);

            HttpURLConnection connection = (HttpURLConnection) site.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStream os = connection.getOutputStream();
            BufferedWriter connectionOutput = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            for (JSONObject jo : params){
                connectionOutput.write(jo.toString());
            }

            connectionOutput.flush();
            connectionOutput.close();

            InputStream in = new BufferedInputStream(connection.getInputStream());

            StringBuilder sb = new StringBuilder();
            byte[] contents = new byte[1024];
            int bytesRead = 0;

            while ((bytesRead = in.read(contents)) != -1) {
                sb.append(new String(contents, 0, bytesRead));
            }

            Log.d(App.LOG_TITLE, sb.toString());

            connection.disconnect();

            return sb.toString();
        } catch (Exception e) {
            Log.e(App.LOG_TITLE, e.getMessage());
        }

        return null;
    }

    public static JSONArray getRequestJSON(String urlAction, String requestMethod, ArrayList<JSONObject> params){
        try {
            String response = getResponse(urlAction, requestMethod, params);

            if (response != null)
                return new JSONArray("[]");
        } catch (Exception e) {
            Log.e(App.LOG_TITLE, e.getMessage());
        }
        return null;
    }

}
