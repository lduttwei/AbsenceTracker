package zli.ld.absencetracker.Notification;

import android.content.Context;
import android.content.Intent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import zli.ld.absencetracker.Date.ParseUtilities;
import zli.ld.absencetracker.Model.Absence;
import zli.ld.absencetracker.Persitance.Persistence;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    public BroadcastReceiver() {
    }

    private Intent selectNotification(Intent intent, LocalDate today, LocalDate expired, LocalDate notify){
        if ( today.isEqual(expired) || today.isAfter(expired)) {
            intent.putExtra("expired", true);
            return intent;
        }
        if ( today.isEqual(notify) || today.isAfter(notify)) {
            intent.putExtra("expired", false);
            return intent;
        }
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Absence> absences = Persistence.loadData(context);
        int ID = 0;
        for ( Absence absence : absences ) {
            if ( ParseUtilities.verifyDate(absence.getDate())) {
                LocalDate expired = ParseUtilities.parseDate(absence.getDate());
                LocalDate notify = ParseUtilities.parseDate(absence.getDate());
                notify = notify.minusDays(5);
                LocalDate today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
                Intent broad = new Intent(context, NotificationService.class);
                broad.putExtra("date", absence.getDate());
                broad.putExtra("id", ID++);
                selectNotification(broad, today, expired, notify);
                context.startService(broad);
            }
        }
    }
}
