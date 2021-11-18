package zli.ld.absencetracker.Model;

import android.graphics.Bitmap;

public class Absence {
    private String reason;
    private String date;
    private String email;
    private String image;

    public Absence(String reason, String date, String email, String image) {
        this.reason = reason;
        this.date = date;
        this.email = email;
        this.image = image;
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

    public String getImagePath() {
        return image;
    }

    public void setImage(String imageLocation) {
        this.image = imageLocation;
    }
}
