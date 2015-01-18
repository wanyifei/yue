package com.mycompany.myapplication2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyifei on 1/17/15.
 */
public class signupScreen extends ActionBarActivity {

    TextView username;
    TextView password;
    TextView phone;
    TextView email;

    ImageButton uploadImage;

    private ImageView imageView;

    private String picPath = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (true) {
            Uri uri = data.getData();
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };
                System.out.println("1");
                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    System.out.println("2");
                    if (true) {
                        picPath = path;
                        Bitmap bitmapimg = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
//                        uploadImage.setImageBitmap(output);
                        uploadImage.setImageDrawable(putOverlay(BitmapFactory.decodeResource(getResources(),
                                R.drawable.default_user_photo), bitmapimg));
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public BitmapDrawable putOverlay(Bitmap bitmap, Bitmap overlay) {
        Bitmap b = Bitmap.createScaledBitmap(bitmap, overlay.getWidth()-50, overlay.getHeight()-50,true);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(overlay, 0, 0, paint);
        return new BitmapDrawable(b);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("ERROR")
                .setMessage("Image invalid!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }

    private String[] categories;
    private Spinner spinner;
    String gender;
    ImageButton sendSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        categories = getResources().getStringArray(R.array.signup_gender_array);
        //imgs = getResources().obtainTypedArray(R.array.countries_flag_list);

        //image = (ImageView) findViewById(R.id.country_image);
        spinner = (Spinner) findViewById(R.id.signup_gender);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        username = (TextView) findViewById(R.id.signup_username);
        password = (TextView) findViewById(R.id.signup_password);
        phone = (TextView) findViewById(R.id.signup_phone);
        email = (TextView) findViewById(R.id.signup_email);

        sendSignup = (ImageButton) findViewById(R.id.signup_button_signup);
        sendSignup.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;
            boolean received = false;

            @Override
            public void onClick(View v) {
                boolean valid = false;
                for (int i=0; i<email.getText().toString().length(); i++) {
                    if (email.getText().toString().charAt(i)=='@') {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(signupScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Email address invalid!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id) {
                            email.setText("");
                        }
                    });
                    alert.show();
                }

                String result = "";
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("phone_number", phone.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("gender", gender));

                try{
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws." +
                            "com/Hangout/index.php/user/sign_up");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.e("log_tag", nameValuePairs.toString());
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
                        MainActivity.user_id=json_data.getInt("current_user_id");
                    }
                }catch(JSONException e){
                    Log.e("log_tag", "Error parsing data "+e.toString());
                }

                if (finish == 1) {
                    System.out.println("Sign up successfully!");
                    Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                    nextScreen.putExtra("user_id", MainActivity.user_id);
                    startActivity(nextScreen);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(signupScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Duplicate username!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id) {
                            username.setText("");
                        }
                    });
                    alert.show();
                }
            }
        });

        uploadImage = (ImageButton) findViewById(R.id.signup_photo);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                System.out.println("about ot upload");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }
}
