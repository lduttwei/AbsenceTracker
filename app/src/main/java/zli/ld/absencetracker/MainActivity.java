package zli.ld.absencetracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import zli.ld.absencetracker.Model.Absence;

public class MainActivity extends AppCompatActivity {

    private final Gson gson = new Gson();
    private ArrayList<Absence> absences = new ArrayList<>();
    private int position;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private SharedPreferences memory;

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    private void saveData() {
        SharedPreferences.Editor prefsEditor = memory.edit();
        prefsEditor.putInt("absences", absences.size());
        for (int i = 0; i < absences.size(); i++) {
            String json = gson.toJson(absences.get(i));
            prefsEditor.putString(String.valueOf(i), json);
        }
        prefsEditor.commit();
    }

    private void loadData() {
        int size = memory.getInt("absences", 0);
        for (int i = 0; i < size; i++) {
            String json = memory.getString(String.valueOf(i), "");
            absences.add(gson.fromJson(json, Absence.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memory = getPreferences(MODE_PRIVATE);
        loadData();
        updateAbsencesView();

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            absences.add(new Absence("", "", "", ""));
            position = absences.size() - 1;
            openAbsence(position);
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
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
                    }
                });
    }

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

    ArrayList<String> generateAbsencesView() {
        ArrayList<String> list = new ArrayList<>();
        for (Absence a : absences) {
            list.add(a.getDate());
        }
        return list;
    }

}