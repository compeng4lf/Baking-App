package com.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;
import static com.android.bakingapp.BakingWidget.updateAppWidget;

public class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    ArrayList<com.android.bakingapp.Ingredient> mIngredientList = new ArrayList<>();
    com.android.bakingapp.Ingredient mIngredient;
    Intent intent;

    public ListViewRemoteViewsFactory (Context applicationContext, Intent intent){
        this.mContext = applicationContext;
        this.intent = intent;
    }


    @Override
    public void onCreate() {
        //Log.i("Widget Created", "List View Remote View Factory");

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        if (mIngredientList.isEmpty()){
            return 0;
        }

        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {


        mIngredientList = intent.getParcelableExtra("FROM_ACTIVITY_INGREDIENTS_LIST");
        mIngredient = mIngredientList.get(position);

        String Ingredients = mIngredient.getIngredient() + " " + mIngredient.getMeasure() + " " + mIngredient.getQuantity();


        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        rv.setTextViewText(R.id.widget_list_item_tv, Ingredients);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
