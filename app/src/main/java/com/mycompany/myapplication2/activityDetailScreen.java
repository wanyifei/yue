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

    int postID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView name = (TextView) findViewById(R.id.fill_activity_name);
        TextView destination = (TextView) findViewById(R.id.fill_activity_destination);
        TextView title = (TextView) findViewById(R.id.fill_activity_title);
        TextView depatureLocation = (TextView) findViewById(R.id.fill_activity_depature_location);
        TextView depatureTime = (TextView) findViewById(R.id.fill_activity_depature_time);
        TextView remark = (TextView) findViewById(R.id.fill_activity_remark);

        Intent i = getIntent();
        postID=Integer.getInteger(i.getStringExtra("postID"));
        name.setText(i.getStringExtra("postName"));
        destination.setText(i.getStringExtra("destination"));
        title.setText(i.getStringExtra("title"));
        depatureLocation.setText(i.getStringExtra("depatureLocation"));
        depatureTime.setText(String.format(i.getStringExtra("date")+"/"+i.getStringExtra("month")
        +"  "+i.getStringExtra("hour")+":"+i.getStringExtra("minute")));
        remark.setText(i.getStringExtra("remark"));

        ImageButton sendJoin = (ImageButton) findViewById(R.id.imageButton);
        sendJoin.setOnClickListener(new joinOnclick());
    }

    private class joinOnclick implements View.OnClickListener {
        InputStream is = null;

        public void onClick(View v) {
            String result = "";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("postID",Integer.toString(postID)));

            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                 HttpClient httpclient = new DefaultHttpClient();
                 HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
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
                    finish = json_data.getInt("finish");
                }
            }catch(JSONException e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }

            if (finish == 1 ) {
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                startActivity(nextScreen);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(activityDetailScreen.this);
                alert.setTitle("ERROR");
                alert.setMessage("Connection failed!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog, int id) {
                        Toast.makeText(activityDetailScreen.this, "Success", Toast.LENGTH_SHORT) .show();
                    }
                });
                alert.show();
            }
        }
    }
}
