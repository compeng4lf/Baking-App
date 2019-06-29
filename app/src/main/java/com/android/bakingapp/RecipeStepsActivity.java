package com.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bakingapp.Adapter.RecipeStepsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsActivity extends AppCompatActivity implements RecipeStepsAdapter.ListItemClickListener {


    @BindView(R.id.rv_RecipeSteps) RecyclerView recyclerView;
    @BindView(R.id.tv_Ingredients) TextView mTextIngredients;
    private List<com.android.bakingapp.Step> mSteps;
    private com.android.bakingapp.Step mTwoPaneStep;
    private List<com.android.bakingapp.Ingredient> mIngredients;
    private com.android.bakingapp.Recipes mRecipes;
    private RecyclerView.Adapter mAdapter;
    private String mIngred;
    private String mQuantity;
    private String mMeasure;
    private String tempDesc;

    private boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        ButterKnife.bind(this);

        if (savedInstanceState != null){

        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("RecipeSteps")) {
            mRecipes = (com.android.bakingapp.Recipes) intent.getParcelableExtra("RecipeSteps");
            mSteps = mRecipes.getSteps();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new RecipeStepsAdapter(mSteps, getApplicationContext(), this);
            recyclerView.setAdapter(mAdapter);

            mIngredients = mRecipes.getIngredients();

            for (int i = 0; i < mIngredients.size(); i++) {

                mIngred = mIngredients.get(i).getIngredient();
                mQuantity = mIngredients.get(i).getQuantity();
                mMeasure = mIngredients.get(i).getMeasure();

                mTextIngredients.append(mQuantity + " " + mMeasure + ": " + mIngred + "\n");

            }

        } else {

            Toast.makeText(this, "An error occurred go back and try again.", Toast.LENGTH_LONG).show();
        }


        if (findViewById(R.id.frag_twopane) != null) {
            mTwoPane = true;

            RecipeDetailFragment mRecipeDetailFragment = new RecipeDetailFragment();

            if (mTwoPaneStep == null){

                tempDesc = "Please choose a step to view details";

            }else {



                tempDesc = mTwoPaneStep.getDescription();
                mRecipeDetailFragment.setRecipeUri(mTwoPaneStep.getVideoURL());
            }

            mRecipeDetailFragment.setRecipeDescription(tempDesc);


            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frag_twopane, mRecipeDetailFragment)
                    .commit();

        } else {
            mTwoPane = false;
        }


    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

        com.android.bakingapp.Step intentDataHolder = mSteps.get(clickedItemIndex);

        if (mTwoPane){
            UpdateTwoPane(intentDataHolder);
            UpdateBakingWidgets((ArrayList<com.android.bakingapp.Ingredient>) mIngredients);
        }
        else {
            Context context = RecipeStepsActivity.this;
            Class destinationActivity = RecipeDetailActivity.class;
            Intent intent = new Intent(context, destinationActivity);
            intent.putExtra("RecipeStepDetails", intentDataHolder);
            intent.putParcelableArrayListExtra("AllRecipeSteps", (ArrayList<? extends Parcelable>) mSteps);
            intent.putParcelableArrayListExtra("AllIngredients", (ArrayList<? extends Parcelable>) mIngredients);
            startActivity(intent);
        }
    }

    public void UpdateTwoPane(com.android.bakingapp.Step step){

        RecipeDetailFragment mRecipeDetailFragment = new RecipeDetailFragment();
        mRecipeDetailFragment.setRecipeDescription(step.getDescription());
        mRecipeDetailFragment.setRecipeUri(step.getVideoURL());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frag_twopane,mRecipeDetailFragment ).commit();


    }

    private void UpdateBakingWidgets(ArrayList<com.android.bakingapp.Ingredient> fromActivityIngredientsList){

        Intent intent =  new Intent("android.appwidget.action.APPWIDGET_INGREDIENTS");
        intent.setAction("android.appwidget.action.APPWIDGET_INGREDIENTS");
        intent.putExtra("FROM_ACTIVITY_INGREDIENTS_LIST", fromActivityIngredientsList);
        sendBroadcast(intent);

    }


}
