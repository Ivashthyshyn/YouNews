package com.example.key.younews;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;




public class SettingsActivity extends AppCompatActivity {
    /**
     * the constant is a name preference field to save String values
     */
    final static String SAVED_TEXT = "saved_text";



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // the select date button
        Button button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // run dialog with DatePicker
                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }
    public static class DatePicker extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // use the calendar to obtain YEAR, MONTH, DAY_OF_MONTH
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // create DatePickerDialog and return it
            Dialog picker = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            picker.setTitle(getResources().getString(R.string.select_date));

            return picker;
        }
        @Override
        public void onStart() {
            super.onStart();
            // add button "Ready"
            Button nButton =  ((AlertDialog) getDialog())
                    .getButton(DialogInterface.BUTTON_POSITIVE);
            nButton.setText(getResources().getString(R.string.ready));

        }

        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int year,
                              int month, int day) {
            // create a text box and ask him text value derived from DatePicker
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            GregorianCalendar gc = new GregorianCalendar(year,month,day);
            Date hireDay = gc.getTime();
            String strDate = simpleDateFormat.format(hireDay);
            TextView dateTextView = (TextView)getActivity().findViewById(R.id.action_date);
            dateTextView.setText(strDate);
            // save date from DatePicker in specified format to SharedPreferences
            SharedPreferences datePref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            SharedPreferences.Editor editor = datePref.edit();
            editor.putString(SAVED_TEXT, strDate);
            editor.commit();
            // show Toast with date saved
            Toast.makeText(getContext(), "date saved ", Toast.LENGTH_SHORT).show();
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
