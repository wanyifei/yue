package com.mycompany.myapplication2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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

/**
 * Created by wangyifei on 1/16/15.
 */
public class postedActivities extends ActionBarActivity {

    ListView list;
    String[] titles;
    String[] names;
    String[] destinations;
    String[] times;
    ArrayList<listViewScreen.Post> posted = new ArrayList<>();
    int[] profile_pics = {R.drawable.cute_lion_cartoon, R.drawable.dig10k_heart, R.drawable.dig10k_maples,
            R.drawable.dig10k_moon, R.drawable.flower, R.drawable.hepburn, R.drawable.moon, R.drawable.penguin,
            R.drawable.img_thing, R.drawable.weenie};
    Intent i=getIntent();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posted_activities);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("current_user_id", Integer.toString(MainActivity.user_id)));


        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-54-165-39-217.compute-1.amazonaws.com/Hangout/index.php" +
                    "/activity/get_post_activities");
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
                listViewScreen.Post newPost = new listViewScreen.Post();
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
                posted.add(newPost);
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }

        titles=null;
        names=null;
        destinations=null;
        times=null;
        titles = new String[posted.size()];
        names = new String[posted.size()];
        destinations = new String[posted.size()];
        times = new String[posted.size()];
        for (int i=0; i<posted.size(); i++) {
            titles[i]=posted.get(i).title;
            destinations[i]=posted.get(i).destinationLocation;
            names[i] = posted.get(i).departure;
            times[i]=posted.get(i).date+"/"+posted.get(i).month+" "+posted.get(i).hour+":"+posted.get(i).minute;
        }

        list = (ListView) findViewById(R.id.listView);
        listViewAdapter adapter = new listViewAdapter(this, titles, profile_pics, names,destinations,times);
        list.setAdapter(adapter);

        listViewOnclick onclickEvent = new listViewOnclick();
        list.setOnItemClickListener(onclickEvent);

        ImageButton button1=(ImageButton) findViewById(R.id.imageButton2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), participatedActivities.class);
                startActivity(nextScreen);
            }
        });
        ImageButton button2=(ImageButton) findViewById(R.id.imageButton3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Notifications.class);
                startActivity(nextScreen);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                nextScreen.putExtra("user_id", MainActivity.user_id);
                startActivity(nextScreen);
                break;
            case android.R.id.home:

                switch (i.getStringExtra("where")) {
                    case "activityDetail":
                        Intent next = new Intent(getApplicationContext(), activityScreen.class);
                        next.putExtra("where","activityDetail");
                        next.putExtra("postID", i.getStringExtra("postID"));;
                        next.putExtra("destinationLocation", i.getStringExtra("destinationLocation"));
                        next.putExtra("title",i.getStringExtra("title"));
                        next.putExtra("departureLocation", i.getStringExtra("depatureLocation"));
                        next.putExtra("time", i.getStringExtra("time"));
                        next.putExtra("remark",i.getStringExtra("remark"));
                        next.putExtra("type",i.getStringExtra("type"));
                        startActivity(next);
                        break;
                    case "activity":
                        Intent next1 = new Intent(getApplicationContext(), activityScreen.class);
                        startActivity(next1);
                        break;
                    case "listView":
                        Intent next2 = new Intent(getApplicationContext(), activityScreen.class);
                        next2.putExtra("type",i.getStringExtra("type"));
                        startActivity(next2);
                        break;
                    case "map":
                        Intent next3 = new Intent(getApplicationContext(), activityScreen.class);
                        startActivity(next3);
                        break;
                    case "post":
                        Intent next4 = new Intent(getApplicationContext(), activityScreen.class);
                        startActivity(next4);
                        break;
                    case "profile":
                        Intent next5 = new Intent(getApplicationContext(), activityScreen.class);
                        next5.putExtra("postid", i.getStringExtra("postid"));
                        startActivity(next5);
                        break;
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public class listViewOnclick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View v, int position, long a){

            final listViewScreen.Post selectPost = posted.get(position);

            System.out.println("text");
            Intent nextScreen = new Intent(getApplicationContext(), activityDetailScreen.class);
            nextScreen.putExtra("postID", Integer.toString(selectPost.id));
            nextScreen.putExtra("type", "posted");
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
