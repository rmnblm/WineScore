package ch.hsr.winescore.data.prefs;

import java.util.HashMap;
import java.util.Map;

public class PreferencesMock implements IPreferences {

    public final Map<String, String> map = new HashMap<>();

    @Override
    public String getString(String key, String defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public void putString(String key, String value) {
        map.put(key, value);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
