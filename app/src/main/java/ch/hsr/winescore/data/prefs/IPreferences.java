package ch.hsr.winescore.data.prefs;

public interface IPreferences {
    String getString(String key, String defaultValue);
    void putString(String key, String value);
    void clear();
}
