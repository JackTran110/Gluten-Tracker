package com.example.cst8334_glutentracker;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

import com.example.entity.Product;

public class EdamamQuery extends AsyncTask<String, Long, String> {

    long upc;
    String appId = "90fb7f7d";
    String appKey = "ec9b27a10f3bb159f17fd932ac559526";
    String response;
    JSONObject jObject;
    String jProductLabel;

    public EdamamQuery(String response, long upc){
        this.response = response;
        this.upc = upc;
    }

    @Override
    public String doInBackground(String... strings){
            String ret = null;
            String queryURL = "https://api.edamam.com/api/food-database/v2/parser?upc=" + upc + "&app_id=" + appId + "&app_key=" + appKey;

            try {
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                jObject = new JSONObject(result);
                //String jUPC = jObject.getString("upc");
                jProductLabel = jObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getString("label");

                CartActivity.getProductsArrayList().add(new Product( upc,jProductLabel,"",String.valueOf(upc),1,false));

            } catch (MalformedURLException mfe) {
            ret = "Malformed URL exception";
        } catch (IOException ioe) {
            ret = "IO Exception. Is the Wifi connected?";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }
}
