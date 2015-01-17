package com.mycompany.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
    int responderID;

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
        depatureTime.setText(i.getStringExtra("depatureTime"));
        remark.setText(i.getStringExtra("remark"));

        ImageButton sendJoin = (ImageButton) findViewById(R.id.imageButton);
        sendJoin.setOnClickListener(new joinOnclick());
    }

    private class joinOnclick implements View.OnClickListener {
        public void onClick(View v) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("postID",Integer.toString(postID)));

            try{
                HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
        }
    }
}
