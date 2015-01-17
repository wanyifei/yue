package com.mycompany.myapplication2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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

    }
}
