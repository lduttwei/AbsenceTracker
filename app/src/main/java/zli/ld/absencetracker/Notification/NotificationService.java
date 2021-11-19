package zli.ld.absencetracker.Notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import zli.ld.absencetracker.MainActivity;
import zli.ld.absencetracker.R;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extra = intent.getExtras();
        boolean expired = extra.getBoolean("expired");
        String date = extra.getString("date");
        int notifyID = extra.getInt("id");
        Notification notification;
        if ( expired ) {
            notification = NotificationBuilder.buildExpiredNotification(date, NotificationService.this);
        } else {
            notification = NotificationBuilder.buildNotification(date, NotificationService.this);
        }
        CharSequence name = getString(R.string.app_name);
        NotificationChannel mChannel = new NotificationChannel(NotificationBuilder.CHANNEL_ID, name, NotificationBuilder.importance);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager.notify(notifyID , notification);
    }

}
