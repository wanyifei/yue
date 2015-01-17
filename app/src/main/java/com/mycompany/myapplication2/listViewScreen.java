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
public class listViewScreen extends Activity {

    ListView list;
    String[] memeTitles;
    String[] memeDescriptions;
    int[] images = {R.drawable.moon, R.drawable.cute_lion_cartoon, R.drawable.flower, R.drawable.dig10k_heart,
            R.drawable.penguin, R.drawable.dig10k_moon, R.drawable.dig10k_maples, R.drawable.img_thing, R.drawable.hepburn,
            R.drawable.weenie};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_screen);

        Resources res = getResources();
        memeTitles = res.getStringArray(R.array.titles);
        memeDescriptions = res.getStringArray(R.array.descriptions);

        list = (ListView) findViewById(R.id.listView);
        listView adapter = new listView(this, memeTitles, images, memeDescriptions);
        list.setAdapter(adapter);
        listViewOnclick onclickEvent = new listViewOnclick(adapter);
        list.setOnItemClickListener(onclickEvent);
    }

    public class listViewOnclick implements AdapterView.OnItemClickListener {
        private listView list;

        listViewOnclick(listView _list) {
            list=_list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapter, View v, int position, long a){

            System.out.println("HI!");
            System.out.println(a);

        }
    }

    class listView extends ArrayAdapter<String>
    {
        Context context;
        int[] images;
        String[] titleArray;
        String[] descriptionArray;

        listView(Context c, String[] titles, int[] imgs, String[] desc)
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
