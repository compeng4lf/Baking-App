package com.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class BakingWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<com.android.bakingapp.Ingredient> mIngredientList = new ArrayList<>();
        private com.android.bakingapp.Ingredient mIngredient;
        private Intent intent;
        private int appWidgetId;

        public ListViewRemoteViewsFactory (Context applicationContext, Intent intent){
            mContext = applicationContext;
            this.intent = intent;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID );
        }


        @Override
        public void onCreate() {
            Log.i("Widget Created", "List View Remote View Factory");
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

            mIngredientList = intent.getParcelableExtra("INGRED");
            mIngredient = mIngredientList.get(position);

            String Ingredients = mIngredient.getIngredient() + " " + mIngredient.getMeasure() + " " + mIngredient.getQuantity();
            Log.i("Widget", Ingredients);

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
            return true;
        }


    }

}
