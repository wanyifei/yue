package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
public class profileScreen extends ActionBarActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView pic=(ImageView) findViewById(R.id.profile_id_photo);
        pic.setImageResource(MainActivity.user_image);

        TextView username = (TextView) findViewById(R.id.profile_username);
        TextView phone = (TextView) findViewById(R.id.profile_phone);
        TextView email = (TextView) findViewById(R.id.profile_email);
        TextView gender = (TextView) findViewById(R.id.profile_gender);

        Intent i = getIntent();

        id = i.getStringExtra("postid");

        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("activity_id", id));
        nameValuePairs.add(new BasicNameValuePair("current_user_id", Integer.toString(MainActivity.user_id)));
        InputStream is = null;

        try {
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
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        String info = "";
        int correct = 0;
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


        try {
            System.out.println("return" + " " + result);
            JSONArray jArray = new JSONArray(result);
            for (int j = 0; j < jArray.length(); j++) {
                JSONObject json_data = jArray.getJSONObject(j);
                username.setText(json_data.getString("username"));
                phone.setText(json_data.getString("phone_number"));
                email.setText(json_data.getString("email_address"));
                int der = json_data.getInt("gender");
                switch (der) {
                    case 0:
                        gender.setText("Male");
                        break;
                    case 1:
                        gender.setText("Female");
                        break;
                    case 2:
                        gender.setText("Other");
                        break;

                }
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }


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
                break;
            case R.id.action_user:
                Intent next = new Intent(getApplicationContext(), Notifications.class);
                next.putExtra("where","profile");
                next.putExtra("postid", id);
                startActivity(next);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}