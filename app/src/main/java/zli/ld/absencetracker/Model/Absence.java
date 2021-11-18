package zli.ld.absencetracker.Model;

import android.graphics.Bitmap;

public class Absence {
    private String reason;
    private String date;
    private String email;
    private Bitmap image;

    public Absence(String reason, String date, String email, Bitmap image) {
        this.reason = reason;
        this.date = date;
        this.email = email;
        this.image = image;
    }
    public Absence(String reason, String date, String email) {
        this.reason = reason;
        this.date = date;
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap imageLocation) {
        this.image = imageLocation;
    }
}
