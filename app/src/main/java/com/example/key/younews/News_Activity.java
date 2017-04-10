package com.example.key.younews;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.key.younews.SettingsActivity.SAVED_TEXT;


public class News_Activity extends AppCompatActivity  implements LoaderCallbacks <List<News>>,SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = News_Activity.class.getSimpleName();

    private static final String USGS_REQUEST_URL =
            "http://content.guardianapis.com/search?from-date=2017-02-20&to-date=2017-02-24&page-size=30&q=news&api-key=cecf2795-0b87-4a86-a037-102de4826ab1";
    private static final int YOU_NEWS_LOADER_ID = 1;
    private RecyclerAdapter adapter;
    private TextView myEmptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_);
        // Create ListView which will display the NEws Class variables

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        myEmptyTextView = (TextView) findViewById(R.id.textView5);
        adapter = new RecyclerAdapter( new ArrayList<News>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        ConnectivityManager menedgerConnect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       // provides information about connecting to the Internet
        NetworkInfo netInfo = menedgerConnect.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
         //  if the connection is valid LoaderManager download information remote server
            LoaderManager myLoaderMenedger = getLoaderManager();
            myLoaderMenedger.initLoader(YOU_NEWS_LOADER_ID, null, this);
        } else {
            View lodigIndicator = findViewById(R.id.progressBar);
            lodigIndicator.setVisibility(View.GONE);
            myEmptyTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<News>> onCreateLoader ( int i, Bundle bundle){
        // use SharedPreferences for relevant news from the remote server depending on user preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // determine the current date
        Calendar c = Calendar.getInstance();
        Date date  = c.getTime();
        // converts a date in the appropriate format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = simpleDateFormat.format(date);
        // if SAVED_TEXT is null write  current date
        if (sharedPrefs.getString(SAVED_TEXT,"") == "" ) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(SAVED_TEXT, stringDate);
            editor.commit();
            Toast.makeText(this, "date saved ", Toast.LENGTH_SHORT).show();
        }
        // register category selection
        String nameCategoryNews = sharedPrefs.getString(
                getString(R.string.settings_category_order_by_key),
                getString(R.string.settings_category_order_by_default));
        // register country selection
        String nameCountry = sharedPrefs.getString(
                getString(R.string.settings_country_order_by_key),
                getString(R.string.settings_country_order_by_default)
        );
        // get a date with  SettingsActivity
       String mDate = sharedPrefs.getString(SAVED_TEXT,"");

        // create base Uri from constant URL
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        //uriBuilder use to create new Uri Custom
        uriBuilder.appendQueryParameter("from-date", mDate);
        uriBuilder.appendQueryParameter("to-date", mDate);
        uriBuilder.appendQueryParameter("category", nameCategoryNews);
        uriBuilder.appendQueryParameter("q", nameCountry);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished
            (Loader< List < News >> loader, List < News > newses){
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        myEmptyTextView.setText(R.string.no_find_news);
        adapter.swapList(null);

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newses != null && !newses.isEmpty()) {
            adapter.swapList(newses);
            myEmptyTextView.setVisibility(View.GONE);


        }
    }

    @Override
    public void onLoaderReset (Loader < List < News >> loader) {
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_category_order_by_key))
                || key.equals(getString(R.string.settings_category_order_by_key))){

            adapter.swapList(null);
            // Hide the empty state text view as the loading indicator will be displayed
            myEmptyTextView.setVisibility(View.GONE);

            View lodigIndicator = findViewById(R.id.progressBar);
            lodigIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(YOU_NEWS_LOADER_ID, null, this);

        }
    }
}
