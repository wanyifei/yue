package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

/**
 * Created by wangyifei on 1/17/15.
 */
public class activityDetailScreen extends Activity {

    String postID;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();

        TextView name = (TextView) findViewById(R.id.fill_activity_name);
        TextView destination = (TextView) findViewById(R.id.fill_activity_destination);
        TextView title = (TextView) findViewById(R.id.fill_activity_title);
        TextView depatureLocation = (TextView) findViewById(R.id.fill_activity_depature_location);
        TextView depatureTime = (TextView) findViewById(R.id.fill_activity_depature_time);
        TextView remark = (TextView) findViewById(R.id.fill_activity_remark);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        postID=i.getStringExtra("postID");;
        destination.setText(i.getStringExtra("destinationLocation"));
        title.setText(i.getStringExtra("title"));
        depatureLocation.setText(i.getStringExtra("depatureLocation"));
        depatureTime.setText(String.format(i.getStringExtra("date")+"/"+i.getStringExtra("month")
        +"  "+i.getStringExtra("hour")+":"+i.getStringExtra("minute")));
        remark.setText(i.getStringExtra("remark"));
        type=i.getStringExtra("type");

        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("activity_id", postID));
        InputStream is = null;

        try{

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/index.php" +
                    "/user/get_user_info");
            Log.e("log_tag", nameValuePairs.toString());
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        String info="";
        int correct = 0;
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



        try{
            System.out.println("return" + " " + result);
            JSONArray jArray = new JSONArray(result);
            for(int j=0;j<jArray.length();j++){
                JSONObject json_data = jArray.getJSONObject(j);
                name.setText(json_data.getInt("username"));
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }

        ImageView button = (ImageView) findViewById(R.id.imageView2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), profileScreen.class);
                nextScreen.putExtra("postid", postID);
                startActivity(nextScreen);
            }
        });


        ImageButton sendJoin = (ImageButton) findViewById(R.id.imageButton);
        sendJoin.setOnClickListener(new joinOnclick());
    }

    private class joinOnclick implements View.OnClickListener {
        InputStream is = null;

        public void onClick(View v) {
            String result = "";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("activity_id",postID));

            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                 HttpClient httpclient = new DefaultHttpClient();
                 HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/" +
                         "index.php/activity/join_activity");
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
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    finish = json_data.getInt("is_successful");
                }
            }catch(JSONException e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }

            if (finish == 1 ) {
                Intent nextScreen = new Intent(getApplicationContext(), listViewScreen.class);
                nextScreen.putExtra("type", type);
                startActivity(nextScreen);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(activityDetailScreen.this);
                alert.setTitle("ERROR");
                alert.setMessage("Connection failed!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog, int id) {
                    }
                });
                alert.show();
            }
        }
    }
}
