package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class postScreen extends Activity {

    //private ImageView image;
    private String[] categories;
    private Spinner spinner;
    //private TypedArray imgs;

    EditText title;
    EditText destination;
    EditText depatureLocation;
    EditText date;
    EditText month;
    EditText hour;
    EditText minute;
    TextView description;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);

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
        destination = (EditText) findViewById(R.id.edit_destination);
        depatureLocation = (EditText) findViewById(R.id.edit_departure_location);
        date = (EditText) findViewById(R.id.edit_date);
        month = (EditText) findViewById(R.id.edit_month);
        hour = (EditText) findViewById(R.id.edit_hour);
        minute = (EditText) findViewById(R.id.edit_minute);
        description = (TextView) findViewById(R.id.editText);




        Button sendPost = (Button) findViewById(R.id.button);
        sendPost.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;

            @Override
            public void onClick(View v) {
                if (date.getText()==null || month.getText()==null || hour.getText()==null || minute.getText()==null) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(postScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Time invalid!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id) {
                        }
                    });
                    alert.show();
                }
                else if (Integer.getInteger(date.getText().toString())<=0 || Integer.getInteger(date.getText().toString())>=32
                    || Integer.getInteger(month.getText().toString())<=0 ||
                        Integer.getInteger(month.getText().toString())>=13 ||
                        Integer.getInteger(hour.getText().toString())<0 ||
                        Integer.getInteger(hour.getText().toString())>=25 ||
                        Integer.getInteger(minute.getText().toString())<0 ||
                        Integer.getInteger(minute.getText().toString())>=61) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(postScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Time invalid!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id) {
                        }
                    });
                    alert.show();
                }

                String result = "";
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("dest_addr", destination.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("depart_addr", depatureLocation.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_day", date.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_month", month.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_hour", hour.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("time_minute", minute.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("description", description.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("category", type));

                try{
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                     HttpClient httpclient = new DefaultHttpClient();
                     HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/" +
                             "index.php/activity/post_activity");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity entity = response.getEntity();
                                is = entity.getContent();
                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }

                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result=sb.toString();
                }catch(Exception e){
                    Log.e("log_tag", "Error converting result "+e.toString());
                }

                int finish = 0;
                try{
                    Log.e("log_tag", "string "+result);
                    JSONArray jArray = new JSONArray(result);
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        finish = json_data.getInt("is_successful");
                    }
                }catch(JSONException e){
                    Log.e("log_tag", "Error parsing data "+e.toString());
                }

                if (finish == 1) {
                    Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                    startActivity(nextScreen);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(postScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Connection failed!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id) {
                        }
                    });
                    alert.show();
                }
            }
        });
    }
}