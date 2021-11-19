package zli.ld.absencetracker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import zli.ld.absencetracker.Model.Absence;
import zli.ld.absencetracker.Notification.BroadcastReceiver;
import zli.ld.absencetracker.Persitance.Persistence;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Absence> absences = new ArrayList<>();
    private int position;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private SharedPreferences memory;

    @Override
    protected void onStop() {
        Persistence.saveData(absences, getApplicationContext());
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memory = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //memory = getPreferences(MODE_PRIVATE);
        absences = Persistence.loadData(getApplicationContext());
        updateAbsencesView();

        Intent notifyIntent = new Intent(this, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 10, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 1000, pendingIntent);

        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis() + 5 * 1000);
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(v -> {

            absences.add(new Absence("", "", "", ""));
            position = absences.size() - 1;
            openAbsence(position);
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    System.out.println("Result: " + result.getResultCode());
                    if (result.getResultCode() == AbsenceActivity.DELETE) {
                        absences.remove(position);
                    }
                    if (result.getResultCode() == AbsenceActivity.CREATE) {
                        Intent resultData = result.getData();
                        Bundle data = resultData.getExtras();
                        Absence absence = absences.get(position);
                        absence.setDate(data.getString("date"));
                        absence.setEmail(data.getString("email"));
                        absence.setReason(data.getString("reason"));
                        absence.setImage(data.getString("image"));
                        absences.set(position, absence);
                    }
                    updateAbsencesView();
                });


    }

    /**
     * Opens a AbsenceActivity by Intent with all the important extras.
     *
     * @param position
     */
    void openAbsence(int position) {
        Absence absence = absences.get(position);
        Intent intent = new Intent(MainActivity.this, AbsenceActivity.class);
        String date = absence.getDate();
        String reason = absence.getReason();
        String image = absence.getImagePath();
        String email = absence.getEmail();
        intent.putExtra("position", position);
        intent.putExtra("date", date);
        intent.putExtra("reason", reason);
        intent.putExtra("image", image);
        intent.putExtra("email", email);
        activityResultLauncher.launch(intent);
    }

    /**
     * Updated den Inhalt der View mit den Absenzen.
     */
    private void updateAbsencesView() {
        ListView view = findViewById(R.id.list);
        ArrayAdapter<String> arr = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, generateAbsencesView());
        view.setAdapter(arr);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                System.out.println(absences.get(pos).getDate());
                position = pos;
                openAbsence(position);
            }
        });
    }

    /**
     * Erstellt die View mit allen Absenzen darin.
     *
     * @return
     */
    ArrayList<String> generateAbsencesView() {
        ArrayList<String> list = new ArrayList<>();
        for (Absence a : absences) {
            list.add(a.getDate());
        }
        return list;
    }

}