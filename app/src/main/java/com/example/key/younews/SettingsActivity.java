package com.example.key.younews;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.R.attr.value;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }
    public static class NewsPreferansFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);//

            Preference orderByCategory = findPreference(getString(R.string.settings_category_order_by_key));
            bindPreferenceSummaryToValue(orderByCategory);

            Preference orderByCountry = findPreference(getString(R.string.settings_country_order_by_key));
            bindPreferenceSummaryToValue(orderByCountry);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();
            preference.setSummary(stringValue);

            return true;
        }
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }

}
