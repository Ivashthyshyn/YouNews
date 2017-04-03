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
import java.util.ArrayList;
import java.util.List;


public class News_Activity extends AppCompatActivity  implements LoaderCallbacks <List<News>>,SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = News_Activity.class.getSimpleName();

    private static final String USGS_REQUEST_URL ="http://content.guardianapis.com/search?from-date=2017-02-20&to-date=2017-02-24&page-size=30&q=news&api-key=cecf2795-0b87-4a86-a037-102de4826ab1";
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
        // створюємо NetworkInfo який надаватиме інформацію про підключення чи не підключення пристрою
        NetworkInfo netInfo = menedgerConnect.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            // якщо є зєднання то запускається LoaderManager який буде завантажувати інформацію з сервера
            LoaderManager myLoaderMenedger = getLoaderManager();
            myLoaderMenedger.initLoader(YOU_NEWS_LOADER_ID, null, this);
        } else {//якщо ні то на пустому екрані зявиться повідомлення про відсутність зєднання з інтернетом
            View lodigIndicator = findViewById(R.id.progressBar);
            lodigIndicator.setVisibility(View.GONE);
            myEmptyTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<News>> onCreateLoader ( int i, Bundle bundle){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nameCategoryNews = sharedPrefs.getString(
                getString(R.string.settings_category_order_by_key),
                getString(R.string.settings_category_order_by_default));

        String nameCountry = sharedPrefs.getString(
                getString(R.string.settings_country_order_by_key),
                getString(R.string.settings_country_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();



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
