package zli.ld.absencetracker.Persitance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import zli.ld.absencetracker.Model.Absence;

public class Persistence {

    private static final Gson gson = new Gson();

    /**
     * Saves the Absence Objects to the locale storage by JSON.
     */
    public static void saveData(ArrayList<Absence> absences, Context context) {
        SharedPreferences memory = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = memory.edit();
        prefsEditor.putInt("absences", absences.size());
        for (int i = 0; i < absences.size(); i++) {
            String json = gson.toJson(absences.get(i));
            prefsEditor.putString(String.valueOf(i), json);
        }
        prefsEditor.commit();
    }

    /**
     * Loads the objects from the local storage. Deserializes them from JSON.
     */
    public static ArrayList<Absence> loadData(Context context) {
        ArrayList<Absence> absences = new ArrayList<>();
        SharedPreferences memory = PreferenceManager.getDefaultSharedPreferences(context);
        int size = memory.getInt("absences", 0);
        for (int i = 0; i < size; i++) {
            String json = memory.getString(String.valueOf(i), "");
            absences.add(gson.fromJson(json, Absence.class));
        }
        return absences;
    }
}
