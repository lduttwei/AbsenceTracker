package zli.ld.absencetracker;

import org.junit.Test;

import zli.ld.absencetracker.Date.ParseUtilities;

public class ParseUtilitiesTest {
    @Test
    public void parseDateTestCorrect(){
        String date = "12.12.2012";
        assert ParseUtilities.verifyDate(date);

        date = "31.11.2021";
        assert ParseUtilities.verifyDate(date);
    }

    @Test
    public void parseDateTestFalse(){
        String date = "Kein datum";
        assert !ParseUtilities.verifyDate(date);
    }
}
