package com.newsblur.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.newsblur.R;
import com.newsblur.activity.FeedReading;
import com.newsblur.activity.Reading;
import com.newsblur.database.BlurDatabaseHelper;
import com.newsblur.util.FeedSet;
import com.newsblur.util.FeedUtils;
import com.newsblur.util.Log;
import com.newsblur.util.PrefsUtils;
import com.newsblur.util.UIUtils;

public class NewsBlurWidgetProvider extends AppWidgetProvider {
    public static String ACTION_OPEN_STORY = "ACTION_OPEN_STORY";
    public static String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static String EXTRA_FEED_ID = "EXTRA_FEED_ID";

    private static String TAG = "NewsBlurWidgetProvider";
    // Called when the BroadcastReceiver receives an Intent broadcast.
    // Checks to see whether the intent's action is TOAST_ACTION. If it is, the app widget
    // displays a Toast message for the current item.
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ACTION_OPEN_STORY)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String storyHash = intent.getStringExtra(EXTRA_ITEM_ID);
            String feedId = intent.getStringExtra(EXTRA_FEED_ID);
            FeedSet fs = FeedSet.singleFeed(feedId);
            Intent i = new Intent(context, FeedReading.class);
            i.putExtra(Reading.EXTRA_FEEDSET, fs);
            i.putExtra(Reading.EXTRA_STORY_HASH, storyHash);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(i);

        }
        super.onReceive(context, intent);
    }
    /**
     * Called to update at regular interval
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the app widgets with the remote adapter
        Log.d(TAG, "onUpdate");
        for (int i = 0; i < appWidgetIds.length; ++i) {

            Log.d(TAG, "onUpdate iteration #" + i);
            // Set up the intent that starts the BlurWidgetRemoteViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, BlurWidgetRemoteViewsService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));


            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.newsblur_widget);
            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            rv.setRemoteAdapter(R.id.widget_list, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            rv.setEmptyView(R.id.widget_list, R.id.empty_view);

            rv.setTextViewText(R.id.txt_feed_name,
                    PrefsUtils.getWidgetFeedName(context, appWidgetIds[i]));

            //
            // Do additional processing specific to this app widget...
            //
            // This section makes it possible for items to have individualized behavior.
            // It does this by setting up a pending intent template. Individuals items of a collection
            // cannot set up their own pending intents. Instead, the collection as a whole sets
            // up a pending intent template, and the individual items set a fillInIntent
            // to create unique behavior on an item-by-item basis.
            Intent touchIntent = new Intent(context, NewsBlurWidgetProvider.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting TOAST_ACTION.
            touchIntent.setAction(NewsBlurWidgetProvider.ACTION_OPEN_STORY);
            touchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent touchIntentTemplate = PendingIntent.getBroadcast(context, 0, touchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list, touchIntentTemplate);


            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }


        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


}
