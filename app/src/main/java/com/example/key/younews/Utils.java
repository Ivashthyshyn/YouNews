package com.example.key.younews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.data;

/**
 * Created by Key on 21.02.2017.
 */

public class Utils  {
    public static final String LOG_TAG = Utils.class.getSimpleName();
private ArrayList myList;

    private Utils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<News> extractNews(String requestUrl) {
        //get url for String data
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List <News> newses = extractFeatureFromJson(jsonResponse);

        // Return the list of earthquakes
        return newses;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**

     * about the first earthquake from the input earthquakeJSON string.
     */
    private static ArrayList <News> extractFeatureFromJson(String earthquakeJSON) {
        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> newses = new ArrayList<>();


        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            // Extract the JSONArray associated with the key called "response",
            // which represents a list of response
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray featureArray = response.getJSONArray("results");


            if (featureArray.length() > 0) {
                // Get a single news at position i within the list of news
                for (int i = 0; i < featureArray.length(); i++) {

                    JSONObject section = featureArray.getJSONObject(i);
                    // Extract the value for the key called "webTitle"
                    String Title = section.getString("webTitle");
                    // Extract the value for the key called "sectionName"
                    String sectionName = section.getString("sectionName");
                    // Extract the value for the key called "url"
                    String webUrl = section.getString("webUrl");
                    // Extract the value for the key called "time"
                    String Time = section.getString("webPublicationDate");

                    // Create a new {@link News} object with the title, time, webUrl,
                    //and sectionName from the JSON response.
                    News resurseNews = new News(Title,Time, webUrl, sectionName);
                    // Add the new {@link News} to the list of news.
                    newses.add(resurseNews);


                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        // Return the list of
        return newses;
    }
}
