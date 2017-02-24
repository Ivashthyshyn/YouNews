package com.example.key.younews;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class News_Activity extends AppCompatActivity  implements LoaderCallbacks <List<News>> {
    private static final String LOG_TAG = News_Activity.class.getSimpleName();
    private static final String USGS_REQUEST_URL ="http://content.guardianapis.com/search?from-date=2017-02-20&to-date=2017-02-24&page-size=30&q=news&api-key=cecf2795-0b87-4a86-a037-102de4826ab1";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private News_Adapter adapter;
    private TextView myEmptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_);
        // Create ListView which will display the NEws Class variables

        ListView newsList = (ListView)findViewById(R.id.list);
        myEmptyTextView=(TextView)findViewById(R.id.textView5);
        adapter = new News_Adapter(this, new ArrayList<News>());
        newsList.setEmptyView(myEmptyTextView);
        newsList.setAdapter(adapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News kurEar = adapter.getItem(position);
                Uri newsUri = Uri.parse(kurEar.getUrl());
                Intent webSuitInt = new Intent(Intent.ACTION_VIEW,newsUri);
                startActivity(webSuitInt);
            }
        });

        LoaderManager loaderManager = getLoaderManager();


        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader ( int i, Bundle bundle){
        return new NewsLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished
            (Loader< List < News >> loader, List < News > earthquakes){
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset (Loader < List < News >> loader) {
        adapter.clear();
    }

}
