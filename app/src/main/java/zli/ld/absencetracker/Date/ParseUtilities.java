package zli.ld.absencetracker.Date;

import android.service.autofill.RegexValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ParseUtilities {

    //Todo: Implement
    public static boolean verifyEmail(String email) {
        return true;
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDateTime.parse(date, formatter);
    }

    public static boolean verifyDate(String date) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date d = format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
