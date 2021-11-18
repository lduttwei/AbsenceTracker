package zli.ld.absencetracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import zli.ld.absencetracker.Date.ParseUtilities;

public class AbsenceActivity extends AppCompatActivity {

    private Bitmap photo;

    public static final int DELETE = 1000;
    public static final int CREATE = 100;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1888;

    String getTextField(int key) {
        EditText text = findViewById(key);
        try {
            return text.getText().toString();
        } catch (NullPointerException exception) {
            return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        Bundle extras = getIntent().getExtras();
        setField(extras, R.id.reason, "reason", "Reason");
        setField(extras, R.id.date, "date", "Date");
        setField(extras, R.id.email, "email", "Email");
        setImage(extras, R.id.image, "image");

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            setResult(DELETE);
            finish();
        });
        Button camera = findViewById(R.id.picture);
        camera.setOnClickListener(v -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ImageView image = findViewById(R.id.image);
            image.setImageBitmap(photo);
        }
    }

    @Override
    public void onBackPressed() {
        String date = getTextField(R.id.date);
        TextView text = findViewById(R.id.error);
        if ( date.isEmpty() ) {
            text.setText("You need a Date to create the Absence.\nOtherwise delete It.");
        } else {
            if (!ParseUtilities.verifyDate(date)) {
                text.setText("Please enter correct Date format (mm.dd.yyyy).");
            } else {
                Intent intent = new Intent();
                intent.putExtra("email", getTextField(R.id.email));
                intent.putExtra("reason", getTextField(R.id.reason));
                intent.putExtra("date", date);
                intent.putExtra("image", photo);
                setResult(CREATE, intent);
                super.onBackPressed();
            }
        }
    }

    void setField(Bundle extras, int key, String strKey, String defaultStr) {
        String str = extras.getString(strKey);
        if ( str.isEmpty() ) { str = defaultStr; }
        EditText field = findViewById(key);
        field.setText(str);
    }

    void setImage(Bundle extras, int key, String keyStr) {
        photo = (Bitmap) extras.getParcelable(keyStr);
        ImageView image = findViewById(key);
        image.setImageBitmap(photo);
    }
}