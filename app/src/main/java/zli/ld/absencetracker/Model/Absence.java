package zli.ld.absencetracker.Model;

public class Absence {
    private String reason;
    private String date;
    private String email;
    private String imageLocation;

    public Absence(String reason, String date, String email, String imageLocation) {
        this.reason = reason;
        this.date = date;
        this.email = email;
        this.imageLocation = imageLocation;
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

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
