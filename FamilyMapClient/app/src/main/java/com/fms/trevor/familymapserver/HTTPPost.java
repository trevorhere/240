package com.fms.trevor.familymapserver;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


//NOTES: HTTP Client, need to combine all other HTTP Clients into this file,
// not enough time though, might be a good task if continuing app build
public class HTTPPost  extends AsyncTask<String, Void, JSONObject>
{
    JSONObject postData;
    String urlString;

    public HTTPPost (Map<String, String> postData) {


        if (postData != null)
        {
            this.postData = new JSONObject(postData);
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {

        try {

            System.setProperty("http.proxyHost", "proxy.example.com");
            System.setProperty("http.proxyPort", "8080");

            urlString = params[0];
            URL url = new URL(urlString + "/" + postData.getString( "route"));
            Log.d(TAG, "doInBackground: " + urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", postData.getString("authToken"));
            if (this.postData != null) {
                OutputStream os = urlConnection.getOutputStream();   //   new OutputStreamWriter(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData.toString());
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();
            }



            int statusCode = urlConnection.getResponseCode();
            if (statusCode ==  200) {
                InputStream inputStream = new
                        BufferedInputStream(urlConnection.getInputStream());

                String response = stringify(inputStream);
                Log.d(TAG, "doInBackground: " + response);
                JSONObject json = new JSONObject(response);


                return json;

            }
            else
            {
                Log.d(TAG, "error in httpPostAsyncTask do in Background");
            }

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }

        return null;
    }

    protected void onPostExecute(JSONObject json) { }


    private  String stringify(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}