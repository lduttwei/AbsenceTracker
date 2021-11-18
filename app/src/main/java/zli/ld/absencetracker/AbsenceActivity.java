package zli.ld.absencetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AbsenceActivity extends AppCompatActivity {
    @Override
    protected void onStop() {
        super.onStop();
        EditText text = findViewById(R.id.reason);
        System.out.println(text.getText());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        Bundle extras = getIntent().getExtras();
        boolean edit = extras.getBoolean("edit");
        if ( edit ) {
            setField(extras, R.id.reason, "reason");
            setField(extras, R.id.date, "date");
            setField(extras, R.id.email, "email");
        }

        Button button = findViewById(R.id.delete);
        button.setOnClickListener(v -> {
            setResult(1);
            finish();
        });
    }

    void setField(Bundle extras, int key, String strKey)
    {
        String str = extras.getString(strKey);
        EditText field = findViewById(key);
        field.setText(str);

    }
}