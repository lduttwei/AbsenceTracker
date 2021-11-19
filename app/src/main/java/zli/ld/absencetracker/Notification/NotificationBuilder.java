package zli.ld.absencetracker.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import zli.ld.absencetracker.R;

public class NotificationBuilder {
    public static final String CHANNEL_ID = "my_channel_01";
    public static final int importance = NotificationManager.IMPORTANCE_HIGH;

    public static Notification buildExpiredNotification(String date, Context context) {
        return new Notification.Builder(context)
                .setContentTitle("Expired Absences!")
                .setContentText("Your absence for " + date + " is expired. Send it now.")
                .setSmallIcon(R.drawable.img)
                .setChannelId(CHANNEL_ID)
                .build();
    }

    public static Notification buildNotification(String date, Context context){
        return new Notification.Builder(context)
                .setContentTitle("Open Absences")
                .setContentText("You still have an open Absence for: " + date)
                .setSmallIcon(R.drawable.img)
                .setChannelId(CHANNEL_ID)
                .build();
    }
}
