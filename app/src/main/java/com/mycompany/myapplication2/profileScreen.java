package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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
public class profileScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        TextView username = (TextView) findViewById(R.id.profile_username);
        TextView phone = (TextView) findViewById(R.id.profile_phone);
        TextView email = (TextView) findViewById(R.id.profile_email);
        TextView gender = (TextView) findViewById(R.id.profile_gender);

        Intent i = getIntent();

        String id=i.getStringExtra("postid");

        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("activity_id", id));
        InputStream is = null;

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
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
                username.setText(json_data.getInt("username"));
                phone.setText(json_data.getInt("phone_number"));
                email.setText(json_data.getInt("email_address"));
                gender.setText(json_data.getInt("gender"));
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }



    }
}
