package zli.ld.absencetracker.Notification;

import android.content.Context;
import android.content.Intent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import zli.ld.absencetracker.Date.ParseUtilities;
import zli.ld.absencetracker.Model.Absence;
import zli.ld.absencetracker.Persitance.Persistence;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    public BroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("NUTS");
        ArrayList<Absence> absences = Persistence.loadData(context);
        for ( Absence absence : absences ) {
            if ( ParseUtilities.verifyDate(absence.getDate())) {
                LocalDate expired = ParseUtilities.parseDate(absence.getDate());
                LocalDate notify = ParseUtilities.parseDate(absence.getDate());
                notify.minusDays(5);
                LocalDate today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
                Intent broad = new Intent(context, NotificationService.class);
                broad.putExtra("date", absence.getDate());

                if ( today.isEqual(expired) || today.isAfter(expired)) {
                    broad.putExtra("expired", true);
                    context.startService(broad);
                    return;
                }
                if ( today.isEqual(notify) || today.isAfter(notify)) {
                    broad.putExtra("expired", false);
                    context.startService(broad);
                    return;
                }
            }
        }
    }
}
