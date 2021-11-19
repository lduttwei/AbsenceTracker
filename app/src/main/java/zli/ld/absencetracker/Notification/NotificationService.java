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
    private final int notifyID = 1;
    private final String CHANNEL_ID = "my_channel_01";// The id of the channel.
    private final int importance = NotificationManager.IMPORTANCE_HIGH;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extra = intent.getExtras();
        boolean expired = extra.getBoolean("expired");
        String date = extra.getString("date");
        Notification notification;
        if ( expired ) {
            notification = buildExpiredNotification(date);
        } else {
            notification = buildNotification(date);
        }
        CharSequence name = getString(R.string.app_name);
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager.notify(notifyID , notification);
    }

    private Notification buildExpiredNotification(String date) {
        return new Notification.Builder(NotificationService.this)
                .setContentTitle("Expired Absences!")
                .setContentText("Your absence for " + date + " is expired. Send it now.")
                .setSmallIcon(R.drawable.img)
                .setChannelId(CHANNEL_ID)
                .build();
    }

    private Notification buildNotification(String date){
        return new Notification.Builder(NotificationService.this)
                .setContentTitle("Open Absences")
                .setContentText("You still have an open Absence for: ")
                .setSmallIcon(R.drawable.img)
                .setChannelId(CHANNEL_ID)
                .build();
    }
}
