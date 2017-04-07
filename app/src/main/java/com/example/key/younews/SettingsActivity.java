package com.example.key.younews;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

   public static int year;
   public static int month;
   public static int day;
   public static TextView dateTextView;
   public static String strDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });

        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        dateTextView = (TextView) findViewById(R.id.dateText);
        Date date  = c.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        strDate = simpleDateFormat.format(date);

        dateTextView.setText(strDate);
    }
    public static class DatePicker extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {



            // создаем DatePickerDialog и возвращаем его
            Dialog picker = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            picker.setTitle(getResources().getString(R.string.select_date));

            return picker;
        }
        @Override
        public void onStart() {
            super.onStart();
            // добавляем кастомный текст для кнопки
            Button nButton =  ((AlertDialog) getDialog())
                    .getButton(DialogInterface.BUTTON_POSITIVE);
            nButton.setText(getResources().getString(R.string.ready));

        }

        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int year,
                              int month, int day) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsingDate;
            try {
                parsingDate = simpleDateFormat.parse(year + "-" + month + "-" +day );
                strDate = simpleDateFormat.format(parsingDate);
                dateTextView.setText(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
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
