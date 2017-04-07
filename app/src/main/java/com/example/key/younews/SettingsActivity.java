package com.example.key.younews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.R.attr.value;

public class SettingsActivity extends AppCompatActivity {
    /**
     * The selected calendar date
     * String tape format year-month-day
     * use to create uri
     */
   public static String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //create Calendar
       CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView1);
        Toast.makeText(SettingsActivity.this, "Select a date from the calendar for a list of your news ", Toast.LENGTH_SHORT).show();
        // use setOnDateChangeListener to select date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                // create correct date format
                if (mDay < 10 && mMonth <10){
                    selectedDate = new StringBuilder().append(mYear)
                            .append("-0").append(mMonth + 1).append("-0").append(mDay)
                            .toString();
                    Toast.makeText(SettingsActivity.this, "You have chosen " + selectedDate, Toast.LENGTH_SHORT).show();
                }else if (mDay < 10) {

                    selectedDate = new StringBuilder().append(mYear)
                            .append("-").append(mMonth + 1).append("-0").append(mDay)
                            .toString();
                    Toast.makeText(SettingsActivity.this, "You have chosen " + selectedDate, Toast.LENGTH_SHORT).show();
                }else if (mMonth <10){
                    selectedDate = new StringBuilder().append(mYear)
                            .append("-0").append(mMonth + 1).append("-").append(mDay)
                            .toString();
                    Toast.makeText(SettingsActivity.this, "You have chosen " + selectedDate, Toast.LENGTH_SHORT).show();

                }else{
                    selectedDate = new StringBuilder().append(mYear)
                            .append("-").append(mMonth + 1).append("-").append(mDay)
                            .toString();
                    Toast.makeText(SettingsActivity.this, "You have chosen " + selectedDate, Toast.LENGTH_SHORT).show();
                }
                          }
        });

    }
    public static class NewsPreferansFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            //create preference list category
            Preference orderByCategory = findPreference(getString(R.string.settings_category_order_by_key));
            bindPreferenceSummaryToValue(orderByCategory);
            //create preference list country
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
