package com.android.bakingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.Recipes;
import com.android.bakingapp.Step;

import org.w3c.dom.Text;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder>{
    List<Step> Steps;
    Context context;
    final private RecipeStepsAdapter.ListItemClickListener mOnClickListener;


    public interface ListItemClickListener{
        public void onListItemClick(int clickedItemIndex);
    }

    public RecipeStepsAdapter (List<Step> steps, Context context, RecipeStepsAdapter.ListItemClickListener listItemClickListener){
        this.Steps = steps;
        this.context = context;
        mOnClickListener = listItemClickListener;

    }


    @NonNull
    @Override
    public RecipeStepsAdapter.RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_recipe_steps,viewGroup,false );
        RecipeStepsAdapter.RecipeStepsViewHolder recipeViewHolder = new RecipeStepsAdapter.RecipeStepsViewHolder(layout);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter.RecipeStepsViewHolder viewHolder, int position) {


        Step r = Steps.get(position);

        viewHolder.mRecipeSteps.setText(r.getId() + ":  " + r.getShortDescription());

    }

    @Override
    public int getItemCount() {

        return Steps.size();
    }



    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mRecipeSteps;


        public RecipeStepsViewHolder(@NonNull View itemView){
            super(itemView);

            mRecipeSteps = (TextView) itemView.findViewById(R.id.tv_steps);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
