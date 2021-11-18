package zli.ld.absencetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Absence absence = new Absence("sick", "21.12.2012", "linus.duttweiler@lernende.bbw.ch", "");
        absences.add(absence);
        absence = new Absence("deez", "22.12.2012", "linus.duttweiler@lernende.bbw.ch", "");
        absences.add(absence);
        updateAbsencesView();

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            openAbsenceCreate();
        });
    }

    void openAbsenceCreate() {
        Intent intent = new Intent(MainActivity.this, AbsenceActivity.class);
        intent.putExtra("edit", false);
        startActivityForResult(intent, CREATE_ACTIVITY);
    }

    void openAbsenceEdit(int pos) {
        Intent intent = new Intent(MainActivity.this, AbsenceActivity.class);
        Absence absence = absences.get(pos);
        String date = absence.getDate();
        String reason = absence.getReason();
        String image = absence.getImageLocation();
        String email = absence.getEmail();
        intent.putExtra("edit", true);
        intent.putExtra("date", date);
        intent.putExtra("reason", reason);
        intent.putExtra("image", image);
        intent.putExtra("email", email);
        intent.putExtra("position", pos);
        position = pos;
        startActivityForResult(intent, EDIT_ACTIVITY);
    }

    private void updateAbsencesView() {
        ListView view = findViewById(R.id.list);
        ArrayAdapter<String> arr = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, generateAbsencesView());
        view.setAdapter(arr);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(absences.get(position).getDate());
                openAbsenceEdit(position);
            }
        });
    }

    @Override
    public void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == CREATE_ACTIVITY) {
            handleCreate(result, data);
        }
        if (request == EDIT_ACTIVITY) {
            handleEdit(result, data);
        }
        updateAbsencesView();
    }

    private void handleCreate(int result, Intent data) {
        Bundle extras = data.getExtras();
        String reason = getString(extras, "reason");
        String date = getString(extras, "date");
        String email = getString(extras, "email");
        String image = getString(extras, "image");
        absences.add(new Absence(reason, date, email, image));
    }

    String getString(Bundle extra, String key) {
        try {
            return extra.getString(key);
        } catch (NullPointerException exception) {
            return "";
        }

    }

    private void handleEdit(int result, Intent data) {
        if (result == DELETE_ACTIVITY) {
            absences.remove(position);
        }
    }

    ArrayList<String> generateAbsencesView() {
        ArrayList<String> list = new ArrayList<>();
        for (Absence a : absences) {
            list.add(a.getDate());
        }
        return list;
    }

}