package ch.hsr.winescore.ui.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.winescore.R;

public class FilterBottomDialogFragment extends BottomSheetDialogFragment {


    public FilterBottomDialogFragment() {
        // empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false);
    }

    public static class FilterSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_filter_wines);
        }

        @Override
        public void onResume() {
            super.onResume();
            Preference vintagePref = findPreference("pref_vintage");
            vintagePref.setSummary(getPreferenceScreen().getSharedPreferences().getString(vintagePref.getKey(), ""));
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);
            if (preference instanceof EditTextPreference) {
                preference.setSummary(String.valueOf(sharedPreferences.getString(key, "")));
            }
        }
    }

}
