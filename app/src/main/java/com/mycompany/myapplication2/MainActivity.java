package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class MainActivity extends Activity {

    EditText inputName;
    EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        inputName = (EditText) findViewById(R.id.welcome_button_username);
        inputPassword = (EditText) findViewById(R.id.welcome_button_password);
        ImageButton toSingupScreen = (ImageButton) findViewById(R.id.welcome_button_signup);
        ImageButton toHomepageScreen = (ImageButton) findViewById(R.id.welcome_button_login);

        toSingupScreen.setOnClickListener(new singupOnclick());
        toHomepageScreen.setOnClickListener(new loginOnclick());
    }

    private class singupOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), signupScreen.class);
            startActivity(nextScreen);
        }
    }

    private class loginOnclick implements View.OnClickListener {
        InputStream is = null;
        boolean received = false;

        @Override
        public void onClick(View v) {
            Log.e("n", inputName.getText() + "." + inputPassword.getText());

            String result = "";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", inputName.toString()));
            nameValuePairs.add(new BasicNameValuePair("password",inputPassword.toString()));

            try{
                final HttpClient httpclient = new DefaultHttpClient();
                final HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        try {
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            is = entity.getContent();
                        } catch(Exception e) {
                            Log.e("log_tag", "Error in http connection " + e.toString());
                        }
                        if (is!=null) {
                            received = true;
                            cancel();
                        }
                    }
                    public void onFinish() {
                    }
                }.start();
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

            int correct = 0;
            try{
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    correct = json_data.getInt("correct");
                }
            }catch(JSONException e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }

            if (!received) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("ERROR");
                alert.setMessage("Connection failed!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog, int id) {
                        Toast.makeText (MainActivity.this, "Success", Toast.LENGTH_SHORT) .show();
                    }
                });
                alert.show();
            }

            if (correct == 1) {
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                startActivity(nextScreen);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("ERROR");
                alert.setMessage("Unrecognized username or\nIncorrect password!");
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
