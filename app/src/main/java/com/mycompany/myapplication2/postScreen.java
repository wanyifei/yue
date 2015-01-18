package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.format.Time;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class postScreen extends ActionBarActivity {

    //private ImageView image;
    private String[] categories;
    private Spinner spinner;
    //private TypedArray imgs;

    EditText title;
    //EditText depatureLocation;
    EditText date;
    EditText month;
    EditText hour;
    EditText minute;
    TextView description;
    ArrayList<String> destPoss=new ArrayList<String>();
    String dest;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();

        categories = getResources().getStringArray(R.array.category_list);
        //imgs = getResources().obtainTypedArray(R.array.countries_flag_list);

        //image = (ImageView) findViewById(R.id.country_image);
        spinner = (Spinner) findViewById(R.id.category_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        title = (EditText) findViewById(R.id.edit_title);
        //destination = (EditText) findViewById(R.id.edit_destination);
        //depatureLocation = (EditText) findViewById(R.id.edit_departure_location);
        date = (EditText) findViewById(R.id.edit_date);
        month = (EditText) findViewById(R.id.edit_month);
        hour = (EditText) findViewById(R.id.edit_hour);
        minute = (EditText) findViewById(R.id.edit_minute);
        description = (TextView) findViewById(R.id.editText);

        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.edit_destination);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        final AutoCompleteTextView depatureLocation = (AutoCompleteTextView) findViewById(R.id.edit_departure_location);
        depatureLocation.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));

        Button sendPost = (Button) findViewById(R.id.button);
        sendPost.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;

            @Override
            public void onClick(View v) {
//                if (date.getText() == null || month.getText() == null || hour.getText() == null || minute.getText() == null) {
//                    AlertDialog.Builder alert = new AlertDialog.Builder(postScreen.this);
//                    alert.setTitle("ERROR");
//                    alert.setMessage("Time invalid!");
//                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//                    alert.show();
//                } else if (Integer.getInteger(date.getText().toString()) <= 0 || Integer.getInteger(date.getText().toString()) >= 32
//                        || Integer.getInteger(month.getText().toString()) <= 0 ||
//                        Integer.getInteger(month.getText().toString()) >= 13 ||
//                        Integer.getInteger(hour.getText().toString()) < 0 ||
//                        Integer.getInteger(hour.getText().toString()) >= 25 ||
//                        Integer.getInteger(minute.getText().toString()) < 0 ||
//                        Integer.getInteger(minute.getText().toString()) >= 61) {
//                    AlertDialog.Builder alert = new AlertDialog.Builder(postScreen.this);
//                    alert.setTitle("ERROR");
//                    alert.setMessage("Time invalid!");
//                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//                    alert.show();
//                }


                double dest_lag=0, dest_lat=0, deaprt_lag=0, depart_lat=0;

                InputStream dest_is = null, depart_is=null;

                String result_1="", result_2="";

                String dest_addr = URLEncoder.encode(autoCompView.getText().toString());
                String depart_addr = URLEncoder.encode(depatureLocation.getText().toString());
                String dest_query = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        dest_addr + "&key=" + API_KEY;
                String depart_query = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        depart_addr + "&key=" + API_KEY;

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    HttpClient httpclient_1 = new DefaultHttpClient();
                    HttpPost httppost_1 = new HttpPost(dest_query);
                    HttpResponse response_1 = httpclient_1.execute(httppost_1);
                    HttpEntity entity_1 = response_1.getEntity();
                    dest_is = entity_1.getContent();
                    BufferedReader reader_1 = new BufferedReader(new InputStreamReader(dest_is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader_1.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    dest_is.close();
                    result_1 = sb.toString();
                    Log.e("log_tag", "string " + result_1);
//                    JSONArray jArray = new JSONArray(result_1);
//                    for (int i = 0; i < jArray.length(); i++) {
//                        JSONObject json_data = jArray.getJSONObject(i);
//                        System.out.println(json_data.toString());
//                        dest_lag = json_data.getDouble("longitude");
//                        dest_lat = json_data.getDouble("latitude");
                        JSONObject jsonObj = new JSONObject(result_1);

                        // Getting JSON Array node
                        JSONObject geometry = jsonObj.getJSONObject("geometry");

                        JSONObject location = geometry.getJSONObject("location");
                        dest_lat = location.getDouble("lat");
                        dest_lag = location.getDouble("lng");
//                    }
                    System.out.println(dest_lat + " " + dest_lag);


                    HttpClient httpclient_2 = new DefaultHttpClient();
                    HttpPost httppost_2 = new HttpPost(depart_query);
                    HttpResponse response_2 = httpclient_2.execute(httppost_1);
                    HttpEntity entity_2 = response_2.getEntity();
                    depart_is = entity_2.getContent();
                    BufferedReader reader_2 = new BufferedReader(new InputStreamReader(depart_is, "iso-8859-1"), 8);
                    StringBuilder sb_2 = new StringBuilder();
                    String line_2 = null;
                    while ((line_2 = reader_2.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    depart_is.close();
                    result_2 = sb_2.toString();
                    Log.e("log_tag", "string " + result_2);
                    JSONObject jsonObj_2 = new JSONObject(result_2);

                    // Getting JSON Array node
                    JSONObject geometry_2 = jsonObj_2.getJSONObject("geometry");

                    JSONObject location_2 = geometry_2.getJSONObject("location");
                    depart_lat = location_2.getDouble("lat");
                    deaprt_lag = location_2.getDouble("lng");
                    System.out.println(depart_lat + " " + deaprt_lag);
                }catch (IOException e) {

                }catch (JSONException e) {
                    Log.e("Log_Log", e.toString());
                }



                String result = "";
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("dest_addr", autoCompView.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("depart_addr", depatureLocation.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_day", date.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_month", month.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_hour", hour.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_minute", minute.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("description", description.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("category", type));
                nameValuePairs.add(new BasicNameValuePair("current_user_id", Integer.toString(MainActivity.user_id)));
                nameValuePairs.add(new BasicNameValuePair("depart_lat", Double.toString(depart_lat)));
                nameValuePairs.add(new BasicNameValuePair("depart_lgt", Double.toString(deaprt_lag)));
                nameValuePairs.add(new BasicNameValuePair("dest_lat", Double.toString(dest_lat)));
                nameValuePairs.add(new BasicNameValuePair("dest_lag", Double.toString(dest_lag)));

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/" +
                            "index.php/activity/post_activity");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result " + e.toString());
                }

                int finish = 0;
                try {
                    Log.e("log_tag", "string " + result);
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        finish = json_data.getInt("is_successful");
                    }
                } catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }

                if (finish == 1) {
                    Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                    startActivity(nextScreen);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(postScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Connection failed!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    alert.show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                nextScreen.putExtra("user_id", MainActivity.user_id);
                startActivity(nextScreen);

        }
        return super.onOptionsItemSelected(item);
    }

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBcxyuCrjMxAqQ9EHPih-3mNJOyG1THMHs";

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:uk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }




}
