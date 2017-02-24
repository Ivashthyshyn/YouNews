package com.example.key.younews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Key on 21.02.2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    public static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl=url;
    }
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<News> loadInBackground() {
        if (mUrl==null){
            return null;
        }
        List<News> newses = Utils.extractNews(mUrl);
        return newses;
    }
}
