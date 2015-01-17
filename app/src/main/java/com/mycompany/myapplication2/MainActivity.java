package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


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
        ImageButton toHomepgeScreen = (ImageButton) findViewById(R.id.welcome_button_login);

        toSingupScreen.setOnClickListener(new singupOnclick());
        toHomepgeScreen.setOnClickListener(new loginOnclick());
    }

    private class singupOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
            startActivity(nextScreen);
        }
    }

    private class loginOnclick implements View.OnClickListener {
        public void onClick(View v) {
            //Check the username and password with database

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("ERROR");
            alert.setMessage("Unrecognized username or\nIncorrect password!");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick (DialogInterface dialog, int id) {
                    Toast.makeText (MainActivity.this, "Success", Toast.LENGTH_SHORT) .show();
                }
            });
            alert.show();
            Log.e("n", inputName.getText() + "." + inputPassword.getText());
        }

    }
}
