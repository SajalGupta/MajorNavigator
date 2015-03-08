package com.example.sajal.intentexample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class Gamma extends ActionBarActivity {

    LatLng[] latLnStore;
    String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamma);

        Bundle BetaData = getIntent().getExtras();
        if (BetaData == null) {
            return;
        }


        globalStoreLatLng gs = (globalStoreLatLng) getApplication();
        latLnStore= gs.getLatLnStore();
        names = (String[])(BetaData.get("names"));
        ListAdapter mAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,names){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        ListView gammaListView = (ListView) findViewById(R.id.gammaListView);
        gammaListView.setAdapter(mAdapter);

        gammaListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LatLng chosenOne =  latLnStore[position];
                        Intent i = new Intent(Gamma.this,MapsActivity.class);
                        i.putExtra("destinationMarker",chosenOne);
                        startActivity(i);
                    }
                }
        );


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gamma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
