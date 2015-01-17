package com.mycompany.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Date;

/**
 * Created by wangyifei on 1/16/15.
 */
public class listViewScreen extends Activity {

    class Post {
        public int id;
        public String postName;
        public String title;
        public String destinationLocation;
        public String destinationTime;
        public String description;
    }

    ListView list;
    ArrayList<Post> posts=new ArrayList<Post>();
    String type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_screen);

        Intent i = getIntent();
        type = i.getStringExtra("type");

        String result = "";
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("type", type));

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e){
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
                newPost.postName = json_data.getString("postName");
                newPost.title = json_data.getString("title");
                newPost.destinationLocation = json_data.getString("destinationLocation");
                newPost.destinationTime = json_data.getString("destinationTime");
                newPost.description = json_data.getString("description");
                posts.add(newPost);
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
        }



        list = (ListView) findViewById(R.id.listView);
        listView adapter = new listView(this);
        list.setAdapter(adapter);
        listViewOnclick onclickEvent = new listViewOnclick();
        list.setOnItemClickListener(onclickEvent);
    }

    public class listViewOnclick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View v, int position, long a){
            Intent nextScreen = new Intent(getApplicationContext(), activityDetailScreen.class);
            Post selectPost = posts.get(position);
            nextScreen.putExtra("postID", selectPost.id);
            nextScreen.putExtra("postName", selectPost.postName);
            nextScreen.putExtra("destinationLocation", selectPost.destinationLocation);
            nextScreen.putExtra("destinationTime", selectPost.destinationTime);
            nextScreen.putExtra("title", selectPost.title);
            nextScreen.putExtra("description", selectPost.description);
            startActivity(nextScreen);
        }
    }

    class listView extends ArrayAdapter<String>
    {
        Context context;
        int[] images;
        String[] title;
        String[] postName;
        String[] destinationLocation;
        String[] destinationTime;

        listView(Context c, String[] titles)
        {
            super(c, R.layout.single_row, R.id.textView, titles);
            this.context = c;
            this.images = imgs;
            this.titleArray = titles;
            this.descriptionArray = desc;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.single_row, parent, false);

            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            TextView myTitle = (TextView) row.findViewById(R.id.textView);
            TextView myDescription = (TextView) row.findViewById(R.id.textView2);

            myImage.setImageResource(images[position]);
            myTitle.setText(titleArray[position]);
            myDescription.setText(descriptionArray[position]);

            return row;
        }


    }
}
