package zli.ld.absencetracker.Date;

import android.service.autofill.RegexValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtilities {

    public static boolean verifyEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
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
