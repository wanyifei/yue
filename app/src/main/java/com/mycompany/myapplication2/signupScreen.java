package com.mycompany.myapplication2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
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

    private WebView wv;
    //make HTML upload button work in Webview
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);

        username = (TextView) findViewById(R.id.signup_text_username);
        password = (TextView) findViewById(R.id.signup_text_username);
        phone = (TextView) findViewById(R.id.signup_text_username);
        email = (TextView) findViewById(R.id.signup_text_username);

        ImageButton sendSignup = (ImageButton) findViewById(R.id.signup_button_signup);
        sendSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("title", username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("destination", password.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("depatureLocation", phone.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("date", email.getText().toString()));

                try {
                    HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }
            }
        });

        ImageButton uploadImage = (ImageButton) findViewById(R.id.signup_button_signup);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.setWebChromeClient(new WebChromeClient() {
                    private Uri imageUri;

                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
                        // Create the storage directory if it does not exist
                        if (!imageStorageDir.exists()) {
                            imageStorageDir.mkdirs();
                        }
                        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                        imageUri = Uri.fromFile(file);

                        final List<Intent> cameraIntents = new ArrayList<Intent>();
                        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        final PackageManager packageManager = getPackageManager();
                        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                        for (ResolveInfo res : listCam) {
                            final String packageName = res.activityInfo.packageName;
                            final Intent i = new Intent(captureIntent);
                            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                            i.setPackage(packageName);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            cameraIntents.add(i);

                        }


                        mUploadMessage = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("image/*");
                        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
                        signupScreen.this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                    }
                });
            }
        });
    }
}
