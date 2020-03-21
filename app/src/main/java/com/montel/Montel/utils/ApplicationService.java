package com.montel.Montel.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.montel.Montel.MainActivity;
import com.montel.Montel.R;
import com.montel.Montel.ui.detailmarker.DetailActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ApplicationService extends Application {


    public ApplicationService(){

    }
    Context context;





    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        SharedPref.init(getApplicationContext());


        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("customer.db")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }


    public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler
    {
        @Override
        public void notificationOpened(OSNotificationOpenResult result)
        {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String activityToBeOpened;
            String activity;

            if (data != null)
            {
                activityToBeOpened = data.optString("activityToBeOpened", null);
                if (activityToBeOpened != null && activityToBeOpened.equals("ABC"))
                {
                    Log.i("OneSignal", "customkey set with value: " + activityToBeOpened);
                    Intent intent = new Intent(ApplicationService.this, DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } else if (activityToBeOpened != null && activityToBeOpened.equals("DEF"))
                {
                    Intent intent = new Intent(ApplicationService.this, DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        }
    }



}
