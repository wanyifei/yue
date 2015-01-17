package com.mycompany.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by wangyifei on 1/17/15.
 */
public class activityDetailScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView name = (TextView) findViewById(R.id.fill_activity_name);
        TextView destination = (TextView) findViewById(R.id.fill_activity_destination);
        TextView depatureLocation = (TextView) findViewById(R.id.fill_activity_depature_location);
        TextView depatureTime = (TextView) findViewById(R.id.fill_activity_depature_time);
        TextView remark = (TextView) findViewById(R.id.fill_activity_remark);

        //request for post info

        ImageButton sendJoin = (ImageButton) findViewById(R.id.imageButton);
        sendJoin.setOnClickListener(new joinOnclick());
    }

    private class joinOnclick implements View.OnClickListener {
        public void onClick(View v) {
            //Send join request to server
        }
    }
}
