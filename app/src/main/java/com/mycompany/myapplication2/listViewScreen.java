package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by wangyifei on 1/16/15.
 */
public class listViewScreen extends ActionBarActivity {

    public static class Post {
        public int id;
        public int date;
        public int month;
        public int hour;
        public int minute;
        public double longitude;
        public double latitude;
        public String title;
        public String destinationLocation;
        public String description;
        public String departure;
    }

    String[] titles;
    String[] names;
    String[] destinations;
    String[] times;
    int[] profile_pics = {R.drawable.cute_lion_cartoon, R.drawable.dig10k_heart, R.drawable.dig10k_maples,
            R.drawable.dig10k_moon, R.drawable.flower, R.drawable.hepburn, R.drawable.moon, R.drawable.penguin,
            R.drawable.img_thing, R.drawable.weenie};


    LocationManager locationManager;
    static Location currentLocation;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                nextScreen.putExtra("user_id", MainActivity.user_id);
                startActivity(nextScreen);

        }
        return super.onOptionsItemSelected(item);
    }


    static class postSort1 implements Comparator {
        public int compare(Object o1,Object o2){
            Post p1 = (Post)o1;
            Post p2 = (Post)o2;
            if (p1.month == p2.month) {
                if (p1.date == p2.date) {
                    if (p1.hour == p2.hour) {
                        return p1.minute > p2.minute ? 1 : 0;
                    }
                    return p1.hour > p2.hour ? 1 : 0;
                }
                return p1.date > p2.date ? 1 : 0;
            }
            return p1.month > p2.month ? 1 : 0;
        }
    }

    static class postSort0 implements Comparator {
        public int compare(Object o1,Object o2){
            Post p1 = (Post)o1;
            Post p2 = (Post)o2;
            double dis1 = (p1.latitude - 0)*(p1.latitude - 0) +
                    (p1.longitude - 0)*(p1.longitude - 0);
            double dis2 = (p2.latitude - 0)*(p2.latitude - 0) +
                    (p2.longitude - 0)*(p2.longitude - 0);
            return dis1 > dis2 ? 1 : 0;
        }
    }

    ListView list;
    public static ArrayList<Post> posts=new ArrayList<Post>();
    String type;

    InputStream is = null;
    boolean received = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent i = getIntent();
        type = i.getStringExtra("type");

        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("category", type));
        nameValuePairs.add(new BasicNameValuePair("current_user_id", Integer.toString(MainActivity.user_id)));

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/index.php" +
                     "/activity/get_activities");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

        try{
            JSONArray jArray = new JSONArray(result);
            for(int j=0;j<jArray.length();j++){
                JSONObject json_data = jArray.getJSONObject(j);
                Post newPost = new Post();
                newPost.id = json_data.getInt("id");
                newPost.title = json_data.getString("title");
                newPost.destinationLocation = json_data.getString("dest_addr");
                newPost.date = json_data.getInt("time_day");
                newPost.month = json_data.getInt("time_month");
                newPost.hour = json_data.getInt("time_hour");
                newPost.minute = json_data.getInt("time_minute");
                newPost.description = json_data.getString("description");
                newPost.latitude = json_data.getDouble("depart_lat");
                newPost.longitude = json_data.getDouble("depart_lgt");
                newPost.departure=json_data.getString("depart_addr");
                posts.add(newPost);
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }

        sort(1);
//
//        MyAdapter myAdapter = new MyAdapter();
//        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(myAdapter);
//        animationAdapter.setAbsListView(mListView);
//        mListView.setAdapter(animationAdapter);

        list = (ListView) findViewById(R.id.listView);
        listViewAdapter adapter = new listViewAdapter(this, titles, profile_pics, names,destinations,times);
        list.setAdapter(adapter);

        listViewOnclick onclickEvent = new listViewOnclick();
        list.setOnItemClickListener(onclickEvent);
    }

    private void sort(int sortby) {
        if (sortby == 0) {
           // currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Collections.sort(posts, new postSort0());
        } else if (sortby == 1) {
            Collections.sort(posts, new postSort1());
        }
        titles = new String[posts.size()];
        names = new String[posts.size()];
        destinations = new String[posts.size()];
        times = new String[posts.size()];
        for (int i=0; i<posts.size(); i++) {
            titles[i]=posts.get(i).title;
            destinations[i]=posts.get(i).destinationLocation;
            names[i] = posts.get(i).departure;
            times[i]=posts.get(i).date+"/"+posts.get(i).month+" "+posts.get(i).hour+":"+posts.get(i).minute;
        }
    }

    private class listViewOnclick implements AdapterView.OnItemClickListener {
        boolean isOnImg=false;
        @Override
        public void onItemClick(AdapterView<?> adapter, View v, int position, long a){
            final Post selectPost = posts.get(position);

                System.out.println("text");
                Intent nextScreen = new Intent(getApplicationContext(), activityDetailScreen.class);
                nextScreen.putExtra("postID", Integer.toString(selectPost.id));
            nextScreen.putExtra("user_id", MainActivity.user_id);
            nextScreen.putExtra("type", type);
                nextScreen.putExtra("destinationLocation", selectPost.destinationLocation);
                nextScreen.putExtra("depatureLocation", selectPost.departure);
                nextScreen.putExtra("date", Integer.toString(selectPost.date));
                nextScreen.putExtra("month", Integer.toString(selectPost.month));
                nextScreen.putExtra("hour", Integer.toString(selectPost.hour));
                nextScreen.putExtra("minute", Integer.toString(selectPost.minute));
                nextScreen.putExtra("title", selectPost.title);
                nextScreen.putExtra("description", selectPost.description);
                startActivity(nextScreen);

        }
    }

    class listViewAdapter extends ArrayAdapter<String>
    {
        Context context;
        int[] images;
        String[] titleArray;
        String[] nameArray;
        String[] destinationArray;
        String[] timeArray;

        listViewAdapter(Context c, String[] titles, int[] imgs, String[] names, String[] dest, String[] times)
        {
            super(c, R.layout.single_row, R.id.list_activity_title, titles);
            this.context = c;
            this.images = imgs;
            this.titleArray = titles;
            this.nameArray = names;
            this.destinationArray = dest;
            this.timeArray = times;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.single_row, parent, false);

            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            TextView myTitle = (TextView) row.findViewById(R.id.list_activity_title);
            TextView myName = (TextView) row.findViewById(R.id.list_activity_name);
            TextView myDestination = (TextView) row.findViewById(R.id.list_activity_destination);
            TextView myTime = (TextView) row.findViewById(R.id.list_activity_time);

            myImage.setImageResource(images[position]);
            myTitle.setText(titleArray[position]);
            myName.setText(nameArray[position]);
            myDestination.setText(destinationArray[position]);
            myTime.setText(timeArray[position]);

            return row;
        }


    }
}
