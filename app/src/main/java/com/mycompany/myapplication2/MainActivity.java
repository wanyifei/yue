package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;


public class MainActivity extends ActionBarActivity {

    EditText inputName;
    EditText inputPassword;

    static public int user_id;
    static public int user_image;

    Menu menu;

    public static int[] profile_pics = {R.drawable.cute_lion_cartoon, R.drawable.dig10k_heart, R.drawable.dig10k_maples,
            R.drawable.dig10k_moon, R.drawable.flower, R.drawable.hepburn, R.drawable.moon, R.drawable.penguin,
            R.drawable.img_thing, R.drawable.weenie};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputName = (EditText) findViewById(R.id.welcome_button_username);
        inputPassword = (EditText) findViewById(R.id.welcome_button_password);
        ImageButton toSingupScreen = (ImageButton) findViewById(R.id.welcome_button_signup);
        ImageButton toHomepageScreen = (ImageButton) findViewById(R.id.welcome_button_login);


        toSingupScreen.setOnClickListener(new singupOnclick());
        toHomepageScreen.setOnClickListener(new loginOnclick());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu=menu;
        return true;
    }










    private class singupOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), signupScreen.class);
            startActivity(nextScreen);
        }
    }

    private class loginOnclick implements View.OnClickListener {
        InputStream is = null;

        @Override
        public void onClick(View v) {
            Log.e("n", inputName.getText() + "." + inputPassword.getText());

            String result = "";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", inputName.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password",inputPassword.getText().toString()));

            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                 HttpClient httpclient = new DefaultHttpClient();
                 HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/index.php/" +
                        "user/log_in");
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
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        correct = json_data.getInt("is_successful");
                        if (correct==0) info = json_data.getString("fail_reason");
                        if (correct==1) user_id=json_data.getInt("current_user_id");
                    }
                }catch(JSONException e){
                    Log.e("log_tag", "Error parsing data "+e.toString());
                }



            if (correct == 1) {
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                nextScreen.putExtra("user_id", user_id);
                Random random=new Random();
//                MainActivity.user_image=profile_pics[random.nextInt()%10];
//                Resources res = getResources();
//                menu.findItem(R.menu.menu_user).setIcon(MainActivity.user_image);
                startActivity(nextScreen);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("ERROR");
                alert.setMessage(info);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog, int id) {
                        Toast.makeText (MainActivity.this, "Success", Toast.LENGTH_SHORT) .show();
                    }
                });
                alert.show();
            }
        }
    }
}
