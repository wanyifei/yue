package com.mycompany.myapplication2;

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
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyifei on 1/17/15.
 */
public class signupScreen extends Activity {

    TextView username;
    TextView password;
    TextView phone;
    TextView email;

    private ImageView imageView;

    private String picPath = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));

                        int x = bitmap.getWidth();
                        int y = bitmap.getHeight();
                        Bitmap output = Bitmap.createBitmap(x,
                                y, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(output);
                        final int color = 0xff424242;
                        final Paint paint = new Paint();
                        final Rect rect = new Rect(0, 0, x, y);
                        paint.setAntiAlias(true);
                        paint.setColor(color);
                        canvas.drawCircle(x/2, x/2, x/2-5, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(bitmap, rect, rect, paint);

                        imageView.setImageBitmap(output);
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

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("ERROR")
                .setMessage("Image Invalid!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = (TextView) findViewById(R.id.signup_text_username);
        password = (TextView) findViewById(R.id.signup_text_username);
        phone = (TextView) findViewById(R.id.signup_text_username);
        email = (TextView) findViewById(R.id.signup_text_username);

        ImageButton sendSignup = (ImageButton) findViewById(R.id.signup_button_signup);
        sendSignup.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;
            boolean received = false;

            @Override
            public void onClick(View v) {
                String result = "";
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("title", username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("destination", password.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("depatureLocation", phone.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("date", email.getText().toString()));

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

                if (finish == 1 && received) {
                    Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                    startActivity(nextScreen);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(signupScreen.this);
                    alert.setTitle("ERROR");
                    alert.setMessage("Connect failed!");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id) {
                            Toast.makeText(signupScreen.this, "Success", Toast.LENGTH_SHORT) .show();
                        }
                    });
                    alert.show();
                }
            }
        });

        ImageButton uploadImage = (ImageButton) findViewById(R.id.signup_button_signup);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }
}
