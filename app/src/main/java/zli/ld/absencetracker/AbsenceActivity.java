package zli.ld.absencetracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import zli.ld.absencetracker.Date.ParseUtilities;

public class AbsenceActivity extends AppCompatActivity {

    private Bitmap photo;
    private String photoPath;
    private int position;

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
        position = extras.getInt("position");
        photoPath = extras.getString("image", "");
        String reason = setField(extras, R.id.reason, "reason", "Reason");
        String date = setField(extras, R.id.date, "date", "Date");
        String email = setField(extras, R.id.email, "email", "Email");
        setImage(R.id.image);

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

        Button send = findViewById(R.id.send);
        send.setOnClickListener(v -> {
            composeEmail();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            photoPath = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Absence - " + position, "Picture for absence application");
            System.out.println("PATH:" + photoPath);
            ImageView image = findViewById(R.id.image);
            image.setImageBitmap(photo);
        }
    }

    @Override
    public void onBackPressed() {
        String date = getTextField(R.id.date);
        TextView text = findViewById(R.id.error);
        if (date.isEmpty()) {
            text.setText("You need a Date to create the Absence.\nOtherwise delete It.");
        } else {
            if (!ParseUtilities.verifyDate(date)) {
                text.setText("Please enter correct Date format (mm.dd.yyyy).");
                return;
            }
            if (!ParseUtilities.verifyEmail(getTextField(R.id.email)) ) {
                text.setText("Please enter a valid email.");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("email", getTextField(R.id.email));
            intent.putExtra("reason", getTextField(R.id.reason));
            intent.putExtra("date", date);
            intent.putExtra("image", photoPath);
            setResult(CREATE, intent);
            super.onBackPressed();
        }
    }

    String setField(Bundle extras, int key, String strKey, String defaultStr) {
        String str = extras.getString(strKey);
        if (str.isEmpty()) {
            str = defaultStr;
        }
        EditText field = findViewById(key);
        field.setText(str);
        return str;
    }

    void setImage(int key) {
        if (!photoPath.isEmpty()) {
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photoPath));
            } catch (FileNotFoundException e) {
                TextView error = findViewById(R.id.error);
                error.setText("Problem locating your image");
            } catch (IOException e) {
                System.out.println("IOEXCEPTION");
            }
            ImageView image = findViewById(key);
            image.setImageBitmap(photo);
        }
    }

    boolean checkFile(String path) {
        try {
            MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photoPath));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private void composeEmail() {
        String reason = ((EditText) findViewById(R.id.reason)).getText().toString();
        String date = ((EditText) findViewById(R.id.date)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        TextView error = findViewById(R.id.error);
        if (email.isEmpty() || !ParseUtilities.verifyEmail(email)) {
            error.setText("Please enter a valid email.");
            return;
        }
        if (!ParseUtilities.verifyDate(date)) {
            error.setText("Please enter a valid date.");
            return;
        }
        if (reason.isEmpty()) {
            error.setText("Please enter a reason.");
            return;
        }
        if (!checkFile(photoPath)) {
            error.setText("Create an image first.");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Absenz vom " + date);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(photoPath));
        intent.putExtra(Intent.EXTRA_TEXT, "Hier ist die Absenz vom " + date + " unterschrieben zurück.\n");
        startActivity(intent);
    }
}