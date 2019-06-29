package com.android.bakingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakingapp.R;

import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeViewHolder> {

    List<com.android.bakingapp.Recipes> recipes;
    Context context;
    final private ListItemClickListener mOnClickListener;


    public interface ListItemClickListener{
        public void onListItemClick(int clickedItemIndex);
    }

    public RecipeCardAdapter (List<com.android.bakingapp.Recipes> recipes, Context context, ListItemClickListener listItemClickListener){
        this.recipes = recipes;
        this.context = context;
        mOnClickListener = listItemClickListener;

    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_recipecard,viewGroup,false );
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(layout);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int position) {


        com.android.bakingapp.Recipes r = recipes.get(position);

        String recipeName = r.getName();

        viewHolder.mRecipeName.setText(recipeName);


    }

    @Override
    public int getItemCount() {

        return recipes.size();
    }



    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mRecipeName;

        public RecipeViewHolder(@NonNull View itemView){
            super(itemView);

            mRecipeName = (TextView) itemView.findViewById(R.id.tv_RecipeName);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
