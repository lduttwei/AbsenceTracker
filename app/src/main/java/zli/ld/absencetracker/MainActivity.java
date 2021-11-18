package zli.ld.absencetracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import zli.ld.absencetracker.Model.Absence;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Absence> absences = new ArrayList<>();
    private int position;
    private final int CREATE_ACTIVITY = 0;
    private final int EDIT_ACTIVITY = 10;
    private final int DELETE_ACTIVITY = 1;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Absence absence = new Absence("sick", "21.12.2012", "linus.duttweiler@lernende.bbw.ch");
        absences.add(absence);
        absence = new Absence("deez", "22.12.2012", "linus.duttweiler@lernende.bbw.ch");
        absences.add(absence);
        updateAbsencesView();

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            absences.add(new Absence("", "", ""));
            position = absences.size() - 1;
            openAbsence(absences.get(position));
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println("Result: " + result.getResultCode());
                        if ( result.getResultCode() == AbsenceActivity.DELETE) {
                            absences.remove(position);
                        }
                        if ( result.getResultCode() == AbsenceActivity.CREATE) {
                            Intent resultData = result.getData();
                            Bundle data = resultData.getExtras();
                            Absence absence = absences.get(position);
                            absence.setDate(data.getString("date"));
                            absence.setEmail(data.getString("email"));
                            absence.setReason(data.getString("reason"));
                            absence.setImage(data.getParcelable("image"));
                        }
                        updateAbsencesView();
                    }
                });
    }

    void openAbsence(Absence absence) {
        Intent intent = new Intent(MainActivity.this, AbsenceActivity.class);
        String date = absence.getDate();
        String reason = absence.getReason();
        Bitmap image = absence.getImage();
        String email = absence.getEmail();
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
                openAbsence(absences.get(position));
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