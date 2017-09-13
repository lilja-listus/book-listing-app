package com.example.lilja.booklistingapp;

/**
 * Created by lilja on 7/10/17.
 */


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
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to make reaqest and receive data
 */

public final class QueryUtils {
    // for log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // empty constructor
    private QueryUtils() {
    }

    /**
     * Methods to make query the dataset and return the list of objects
     */

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        // HTTP request and JSON response on it
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // extracting needed fields from JSON response and making a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    /**
     * @param stringUrl
     * @return URL url object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Making HTTP request to the given URL
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000/*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        String description = "Description Not Available";
        String authors = "Authors Not Available";
        String imageUrl = "http://via.placeholder.com/150x170?text=Thumbnail+N/A";
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            if (baseJsonResponse.has("items")) {
                JSONArray bookArray = baseJsonResponse.getJSONArray("items");
                for (int i = 0; i < bookArray.length(); i++) {
                    // Get a single book at position i within the list of books
                    JSONObject currentBook = bookArray.getJSONObject(i);
                    JSONObject items = currentBook.getJSONObject("volumeInfo");
                    String title = items.getString("title");
                    if (items.has("description")) {
                        description = items.getString("description");
                    }
                    String url = items.getString("infoLink");
                    StringBuilder author = new StringBuilder();
                    if (items.has("authors")) {
                        JSONArray authorArray = items.getJSONArray("authors");

                        // For each author in the authorArray, append its value to authors StringBuilder
                        for (int j = 0; j < authorArray.length(); j++) {
                            author.append(authorArray.getString(j)).append(", ");
                        }
                        //remove comma from the end of the string
                        author.setLength(author.length() - 2);
                        authors = author.toString();
                    }

                    if (items.has("imageLinks")) {
                        JSONObject imageResponse = items.getJSONObject("imageLinks");
                        imageUrl = imageResponse.getString("smallThumbnail");
                    }
                    Book book = new Book(authors, title, description, url, imageUrl);
                    books.add(book);

                }
            }
        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        return books;
    }
}

