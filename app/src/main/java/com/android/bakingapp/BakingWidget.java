package com.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static List<com.android.bakingapp.Ingredient> mIngredients = new ArrayList<com.android.bakingapp.Ingredient>();
    int[] appWidgetIds;
    static String mRecipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_bakingapp);

        if (mIngredients != null){

            String ingredientsList = "\n";
            for (com.android.bakingapp.Ingredient i : mIngredients) {

                ingredientsList += "\n(" + i.getQuantity() + " " + i.getMeasure() + ")     " + i.getIngredient();

            }

            views.setTextViewText(R.id.widget_ingredientsList, ingredientsList);
            views.setTextViewText(R.id.widget_recipeName, mRecipeName);
            views.setViewVisibility(R.id.widget_empty_tv, GONE);
            views.setViewVisibility(R.id.widget_ingredientsList, VISIBLE);
            views.setViewVisibility(R.id.widget_recipeName, VISIBLE);

        }

        if (mIngredients == null){

            views.setViewVisibility(R.id.widget_empty_tv, VISIBLE);
            views.setViewVisibility(R.id.widget_recipeName, GONE);
            views.setViewVisibility(R.id.widget_ingredientsList, GONE);
        }

        //updateAppWidget(context, appWidgetManager, appWidgetId);
        Intent intent = new Intent(context, com.android.bakingapp.BakingWidgetRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putParcelableArrayListExtra("INGRED", (ArrayList<? extends Parcelable>) mIngredients);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));


        //views.setRemoteAdapter(R.id.widget_list_view, intent);



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        mIngredients = intent.getParcelableArrayListExtra("FROM_ACTIVITY_INGREDIENTS_LIST");
        mRecipeName = intent.getStringExtra("RECIPENAME");
        appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        super.onReceive(context, intent);
        //onUpdate(context, appWidgetManager, appWidgetIds);

    }


}

