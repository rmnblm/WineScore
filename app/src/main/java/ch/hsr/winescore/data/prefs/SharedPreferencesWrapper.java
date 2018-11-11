package ch.hsr.winescore.data.prefs;

import android.content.SharedPreferences;
import ch.hsr.winescore.WineScoreApplication;

public class SharedPreferencesWrapper implements IPreferences {

    private SharedPreferences sharedPreferences = WineScoreApplication.getSharedPreferences();

    @Override
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
