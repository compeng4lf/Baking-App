package com.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bakingapp.Adapter.RecipeStepsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity  {

    private com.android.bakingapp.Step mStepDetails;
    @BindView(R.id.btn_Prev) Button mBtnPrevious;
    @BindView(R.id.btn_Next) Button mBtnNext;
    private int mRecipeStepNum;
    private List<com.android.bakingapp.Step> mAllStepDetails;
    private ArrayList<com.android.bakingapp.Ingredient> mAllIngredients;
    private String tempUri = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOrientation();
        if (isHandsetAndLandscape()){
            hideSystemUI();
        }
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null){

            mAllStepDetails = savedInstanceState.getParcelableArrayList("STEP");

        }

        if (savedInstanceState==null){

            Intent intent = getIntent();
            if (intent.hasExtra("RecipeStepDetails")) {
                mStepDetails = (com.android.bakingapp.Step) intent.getParcelableExtra("RecipeStepDetails");
                mRecipeStepNum = Integer.parseInt(mStepDetails.getId());

                if (intent.hasExtra("AllRecipeSteps")) {
                    mAllStepDetails = intent.getParcelableArrayListExtra("AllRecipeSteps");
                }

                if (intent.hasExtra("AllIngredients")){
                    mAllIngredients = intent.getParcelableArrayListExtra("AllIngredients");
                }

                String tempDesc = mStepDetails.getDescription();
                if (mStepDetails.getVideoURL().isEmpty()) {
                    tempUri = null;
                } else {
                    tempUri = mStepDetails.getVideoURL();
                }

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                RecipeDetailFragment mRecipeDetailFragment = new RecipeDetailFragment();

                mRecipeDetailFragment.setRecipeDescription(tempDesc);
                mRecipeDetailFragment.setRecipeUri(tempUri);

                fragmentTransaction.add(R.id.frag_RecipeDetails, mRecipeDetailFragment).commit();

                UpdateBakingWidgets(mAllIngredients);

            }
        }



        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUINext();
            }
        });


        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUIPrev();

            }
        });

    }



    public void UpdateUINext(){


        if (mRecipeStepNum >= 0  && mRecipeStepNum < mAllStepDetails.size()-1){

            RecipeDetailFragment mRecipeDetailFragment = new RecipeDetailFragment();

            String tempDesc = mAllStepDetails.get(mRecipeStepNum + 1).getDescription();
            if (mAllStepDetails.get(mRecipeStepNum + 1).getVideoURL().isEmpty()) {
                tempUri = null;
            }
            else{
                tempUri = mAllStepDetails.get(mRecipeStepNum + 1).getVideoURL();
            }

            mRecipeDetailFragment.setRecipeDescription(tempDesc);
            mRecipeDetailFragment.setRecipeUri(tempUri);



            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frag_RecipeDetails, mRecipeDetailFragment)
                    .commit();

            mRecipeStepNum = mRecipeStepNum + 1;
            Log.i("StepNum", String.valueOf(mRecipeStepNum));

        }
        else{
            Toast.makeText(this, "This is the last step.", Toast.LENGTH_SHORT).show();
        }


    }

    public void UpdateUIPrev(){

        if (mRecipeStepNum > 0  && mRecipeStepNum < mAllStepDetails.size()){

            RecipeDetailFragment mRecipeDetailFragment = new RecipeDetailFragment();

            String tempDesc = mAllStepDetails.get(mRecipeStepNum - 1).getDescription();
            if (mAllStepDetails.get(mRecipeStepNum - 1).getVideoURL().isEmpty()) {
                tempUri = null;
            }
            else{
                tempUri = mAllStepDetails.get(mRecipeStepNum - 1).getVideoURL();
            }

            mRecipeDetailFragment.setRecipeDescription(tempDesc);
            mRecipeDetailFragment.setRecipeUri(tempUri);


            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frag_RecipeDetails, mRecipeDetailFragment)
                    .commit();

            mRecipeStepNum = mRecipeStepNum - 1;

        }
        else{
            Toast.makeText(this, "This is the first step.", Toast.LENGTH_SHORT).show();
        }

    }


    private void UpdateBakingWidgets(ArrayList<com.android.bakingapp.Ingredient> fromActivityIngredientsList){

        Intent intent =  new Intent("android.appwidget.action.APPWIDGET_INGREDIENTS");
        intent.setAction("android.appwidget.action.APPWIDGET_INGREDIENTS");
        intent.putExtra("FROM_ACTIVITY_INGREDIENTS_LIST", fromActivityIngredientsList);
        sendBroadcast(intent);

    }


    public void checkOrientation(){

        int orientation = getResources().getConfiguration().orientation;

    }

    private boolean isHandsetAndLandscape() {

        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && getResources().getConfiguration().screenWidthDp <= 900;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("STEP", (ArrayList<? extends Parcelable>) mAllStepDetails);

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}