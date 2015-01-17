package com.mycompany.myapplication2;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
                //image.setImageResource(imgs.getResourceId(
                        //spinner.getSelectedItemPosition(), -1));
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
        description = (TextView) findViewById(R.id.description);

        Time now = new Time();
        now.setToNow();

        date.setText(now.monthDay);
        month.setText(now.month);
        hour.setText(now.hour);
        minute.setText(now.minute);

        Button sendPost = (Button) findViewById(R.id.button);
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("destination", destination.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("depatureLocation", depatureLocation.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("date", date.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("month", month.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("hour", hour.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("minute", minute.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("description", description.getText().toString()));

                try{
                    HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }
            }
        });
    }
}