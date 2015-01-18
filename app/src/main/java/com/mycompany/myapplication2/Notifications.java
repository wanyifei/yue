package com.mycompany.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by wangyifei on 1/16/15.
 */
public class Notifications extends Activity {

    ListView list;
    String[] titles;
    String[] names;
    String[] destinations;
    String[] times;
    int[] profile_pics = {R.drawable.cute_lion_cartoon, R.drawable.dig10k_heart, R.drawable.dig10k_maples,
            R.drawable.dig10k_moon, R.drawable.flower, R.drawable.hepburn, R.drawable.moon, R.drawable.penguin,
            R.drawable.img_thing, R.drawable.weenie};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        Resources res = getResources();
        titles = res.getStringArray(R.array.titles);
        names = res.getStringArray(R.array.names);
        destinations = res.getStringArray(R.array.destinations);
        times = res.getStringArray(R.array.times);

        list = (ListView) findViewById(R.id.listView3);
        listViewAdapter adapter = new listViewAdapter(this, titles, profile_pics, names,destinations,times);
        list.setAdapter(adapter);

        listViewOnclick onclickEvent = new listViewOnclick(adapter);
        list.setOnItemClickListener(onclickEvent);
    }

    public class listViewOnclick implements AdapterView.OnItemClickListener {
        private listViewAdapter list;

        listViewOnclick(listViewAdapter _list) {
            list=_list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapter, View v, int position, long a){

            System.out.println("HI!");
            System.out.println(a);

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
            View row = inflater.inflate(R.layout.notification_single_row, parent, false);

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
