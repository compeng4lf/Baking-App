package com.android.bakingapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParseRecipes {


    private String mJSON;

    private ArrayList<com.android.bakingapp.Recipes> mRecipeResults = new ArrayList<com.android.bakingapp.Recipes>();
    private com.android.bakingapp.Recipes mRecipe = new com.android.bakingapp.Recipes();

    private List<com.android.bakingapp.Ingredient> ingredient = new ArrayList<com.android.bakingapp.Ingredient>();
    private com.android.bakingapp.Ingredient tempIngredient = new com.android.bakingapp.Ingredient();

    private List<com.android.bakingapp.Step> step = new ArrayList<com.android.bakingapp.Step>();
    private com.android.bakingapp.Step tempStep = new com.android.bakingapp.Step();

    public List<com.android.bakingapp.Recipes> getJSON(Context context){

        try {

            //Get stream to read JSON file
            InputStream inputStream = context.getAssets().open("recipe.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            //Final String read from file
            mJSON = new String(buffer, "UTF-8");

            JSONArray k = new JSONArray(mJSON);

            for (int i = 0; i < k.length(); i++) {


                JSONObject temp = k.getJSONObject(i);


                String mNames = temp.getString("name");
                //Create arrays to read Ingredients and Steps
                JSONArray recipearray = temp.getJSONArray("ingredients");
                JSONArray mSteps = temp.getJSONArray("steps");

                String mServings = temp.getString("servings");
                String mImage = temp.getString("image");

                //Ingredients are added to list.
                for (int j = 0; j < recipearray.length(); j++) {

                    JSONObject mIngredients = recipearray.getJSONObject(j);

                    tempIngredient.setIngredient(mIngredients.getString("ingredient"));
                    tempIngredient.setMeasure(mIngredients.getString("measure"));
                    tempIngredient.setQuantity(mIngredients.getString("quantity"));

                    ingredient.add(j, tempIngredient);

                    tempIngredient = new com.android.bakingapp.Ingredient();
                }

                //Steps are placed into List
                for (int j = 0; j < mSteps.length(); j++) {

                    JSONObject Steps = mSteps.getJSONObject(j);

                    //Steps
                    tempStep.setId(Steps.getString("id"));
                    tempStep.setShortDescription(Steps.getString("shortDescription"));
                    tempStep.setDescription(Steps.getString("description"));
                    tempStep.setVideoURL(Steps.getString("videoURL"));
                    tempStep.setThumbnailURL(Steps.getString("thumbnailURL"));

                    step.add(j, tempStep);

                    tempStep = new com.android.bakingapp.Step();
                }

                //Add objects into Recipe object and then add to list.
                mRecipe.setName(mNames);
                mRecipe.setServings(mServings);
                mRecipe.setImage(mImage);
                mRecipe.setIngredients(ingredient);
                mRecipe.setSteps(step);

                mRecipeResults.add(i, mRecipe);

                mRecipe = new com.android.bakingapp.Recipes();
                step = new ArrayList<com.android.bakingapp.Step>();
                ingredient = new ArrayList<com.android.bakingapp.Ingredient>();

            }
        }

        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mRecipeResults;
    }


}
