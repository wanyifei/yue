package com.mycompany.myapplication2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.Map;

/**
 * Created by wangyifei on 1/17/15.
 */
public class mapViewScreen extends ActionBarActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private GoogleMap map;

    @Override
    public void onConnectionSuspended(int a){

    }

    @Override
    public void onConnectionFailed(ConnectionResult c){

    }

    GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_new);



        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        for (listViewScreen.Post p: listViewScreen.posts) {
            Marker p1 = map.addMarker(new MarkerOptions().position(new LatLng(p.latitude, p.longitude))
                    .title(p.title));

        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker p1) {
                TextView title = (TextView) findViewById(R.id.list_activity_title);
                TextView depature = (TextView) findViewById(R.id.list_activity_name);
                TextView time = (TextView) findViewById(R.id.list_activity_time);
                TextView destination = (TextView) findViewById(R.id.list_activity_destination);
                title.setText(p1.getTitle());
                for (listViewScreen.Post p: listViewScreen.posts) {
                    if (p.title.equals(p1.getTitle())) {
                        depature.setText(p.departure);
                        time.setText(p.date+"/"+p.month+" "+p.hour+":"+p.minute);
                        destination.setText(p.destinationLocation);

                        final listViewScreen.Post ps=p;

                        title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent nextScreen = new Intent(getApplicationContext(), activityDetailScreen.class);
//                                nextScreen.putExtra("type", type);
                                nextScreen.putExtra("destinationLocation", ps.destinationLocation);
                                nextScreen.putExtra("depatureLocation", ps.departure);
                                nextScreen.putExtra("date", Integer.toString(ps.date));
                                nextScreen.putExtra("month", Integer.toString(ps.month));
                                nextScreen.putExtra("hour", Integer.toString(ps.hour));
                                nextScreen.putExtra("minute", Integer.toString(ps.minute));
                                nextScreen.putExtra("title", ps.title);
                                nextScreen.putExtra("description", ps.description);
                                startActivity(nextScreen);
                            }
                        });

                        break;
                    }

                }
                return true;
            }
        });


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.2827,
                -83.7486), 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    Location mLastLocation;

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent nextScreen = new Intent(getApplicationContext(), activityScreen.class);
                nextScreen.putExtra("user_id", MainActivity.user_id);
                startActivity(nextScreen);
                break;
            case R.id.action_user:
                Intent next = new Intent(getApplicationContext(), Notifications.class);
                next.putExtra("where", "map");
                startActivity(next);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
