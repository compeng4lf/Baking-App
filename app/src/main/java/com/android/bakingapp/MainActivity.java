package com.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.bakingapp.Adapter.RecipeCardAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeCardAdapter.ListItemClickListener{


    @BindView(R.id.rv_recipecard) RecyclerView recyclerView;
    private List<com.android.bakingapp.Recipes> mRecipes;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ParseRecipes p = new ParseRecipes();

        mRecipes = (List<com.android.bakingapp.Recipes>) p.getJSON(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeCardAdapter(mRecipes, getApplicationContext(), this);
        recyclerView.setAdapter(mAdapter);


    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

        com.android.bakingapp.Recipes intentDataHolder = mRecipes.get(clickedItemIndex);
        Context context = MainActivity.this;
        Class destinationActivity = RecipeStepsActivity.class;
        Intent intent = new Intent(context, destinationActivity);
        intent.putExtra("RecipeSteps", intentDataHolder);
        startActivity(intent);

    }
}
