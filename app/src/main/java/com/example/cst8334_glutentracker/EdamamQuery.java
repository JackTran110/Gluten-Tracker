package com.example.cst8334_glutentracker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;

import com.example.cst8334_glutentracker.activity.CartActivity;
import com.example.cst8334_glutentracker.database.GlutenDatabase;
import com.example.cst8334_glutentracker.entity.Product;

/**
 * Class that will be performing API queries to the Edamam database.
 *
 * @link https://developer.edamam.com/food-database-api
 */
public class EdamamQuery extends AsyncTask<String, Long, String> {

    private Context context;
    boolean isGlutenFree;
    long upc;
    String appId = "90fb7f7d";
    String appKey = "ec9b27a10f3bb159f17fd932ac559526";
    String response;
    JSONObject jObject;
    String jProductLabel;
    Product prod;
    AlertDialog.Builder alertDialog;

    /**
     * Dialog listener will either add an item to the database and cart if the user clicks "Yes" or simply
     * add the item to the database if the user click "No" (only relevant for gluten items)
     */
    DialogInterface.OnClickListener dialogInterfaceListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                CartActivity.getProductsArrayList().add(prod);
                Toast.makeText(context, "successfully added " + prod.getProductName() + " to the cart", Toast.LENGTH_LONG).show();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(context, "successfully added " + prod.getProductName(), Toast.LENGTH_LONG).show();
                break;
        }
    };


    /**
     * Overloaded constructor that receives values from the ScanActivity class, will be used later
     * to perform API queries to Edamam.
     *
     * @param context This is used to pass the AlertDialog back to ScanActivity
     * @param upc Barcode value that will be used to query Edamam
     * @param isGluten Boolean passed from the checkbox, will be used to declare products gluten or gluten-free
     */
    public EdamamQuery(Context context, long upc, boolean isGluten){
        this.context = context;
        this.upc = upc;
        this.isGlutenFree = isGluten;
    }

    /**
     * Overridden method, attempts to perform an API query to Edamam. If it successfully finds
     * an item, it will parse the product name from the database which will be inserted in the product object
     * and finally will be added to the cart and database.
     */
    @Override
    public String doInBackground(String... strings){
            String ret = null;
            String queryURL = "https://api.edamam.com/api/food-database/v2/parser?upc=" + upc + "&app_id=" + appId + "&app_key=" + appKey;

        /**
         * Attempt a API query to Edamam, if the connection is successful and an item is retrieved from the barcode sent, it will parse it, retrieve the product name and add it to the database (and cart if it's a gluten item)
         *
         */
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
                jProductLabel = jObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getString("label");

            /**
             * Create dummy product object and assembled with parameters passed
             */
            prod = new Product(upc, jProductLabel,"",1.00, isGlutenFree);

            /**
             * If it is a gluten-free item, add the item to the cart
             */
                if (isGlutenFree){
                    CartActivity.getProductsArrayList().add(prod);
                }

                GlutenDatabase db = new GlutenDatabase(context);
                db.insertIntoProductsTable(prod);

                ret = jProductLabel;
            } catch (MalformedURLException mfe) {
            ret = "Malformed URL exception";
        } catch (IOException ioe) {
            ret = "Internet not available or product not found";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     *
     *
     * @param result Name of the product that is added to the database/cart
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        switch (result) {
            case "Malformed URL exception":
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                break;
            case "Internet not available or product not found":
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                break;
            default:
                if (isGlutenFree) {
                    Toast.makeText(context, "successfully added " + result + " to the cart", Toast.LENGTH_LONG).show();
                } else {
                    alertDialog = new AlertDialog.Builder(context);
                    this.setAlertDialog(alertDialog);
                    alertDialog.create().show();
                }
                break;
        }
    }

    /**
     * Setter for the AlertDialog builder, will be setting the parameters for the Alert box.
     *
     * @param alertDialog AlertDialog object that will be set with the title, message, etc.
     */
    public void setAlertDialog(AlertDialog.Builder alertDialog) {
        this.alertDialog = alertDialog.setTitle("Add gluten item to cart?")
            .setMessage("Would you like to add this gluten item to the cart?")
            .setPositiveButton("Yes", dialogInterfaceListener).setNegativeButton("No", dialogInterfaceListener);
    }

}
