package com.mycompany.myapplication2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by wangyifei on 1/17/15.
 */
public class activityScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();

        ImageButton toFoodScreen = (ImageButton) findViewById(R.id.homepage_food);
        ImageButton toMovieScreen = (ImageButton) findViewById(R.id.homepage_movie);
        ImageButton toSportScreen = (ImageButton) findViewById(R.id.homepage_sport);
        ImageButton toStudyScreen = (ImageButton) findViewById(R.id.homepage_study);
        ImageButton toOtherScreen = (ImageButton) findViewById(R.id.homepage_other);
        ImageButton toPostScreen = (ImageButton) findViewById(R.id.homepage_post);

        toFoodScreen.setOnClickListener(new foodOnclick());
        toMovieScreen.setOnClickListener(new movieOnclick());
        toSportScreen.setOnClickListener(new sportOnclick());
        toStudyScreen.setOnClickListener(new studyOnclick());
        toOtherScreen.setOnClickListener(new otherOnclick());
        toPostScreen.setOnClickListener(new postOnclick());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);
                break;
            case R.id.action_user:
                Intent next = new Intent(getApplicationContext(), Notifications.class);
                next.putExtra("where","activity");
                startActivity(next);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class foodOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), listViewScreen.class);
            nextScreen.putExtra("type", "food");
            startActivity(nextScreen);
        }
    }

    private class movieOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), listViewScreen.class);
            nextScreen.putExtra("type", "movie");
            startActivity(nextScreen);
        }
    }

    private class sportOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), listViewScreen.class);
            nextScreen.putExtra("type", "sport");
            startActivity(nextScreen);
        }
    }

    private class studyOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), listViewScreen.class);
            nextScreen.putExtra("type", "study");
            startActivity(nextScreen);
        }
    }

    private class otherOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), listViewScreen.class);
            nextScreen.putExtra("type", "other");
            startActivity(nextScreen);
        }
    }

    private class postOnclick implements View.OnClickListener {
        public void onClick(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), postScreen.class);
            startActivity(nextScreen);
        }
    }
}
