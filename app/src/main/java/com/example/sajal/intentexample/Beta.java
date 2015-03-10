package com.example.sajal.intentexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Beta extends ActionBarActivity {

    final String TAG = "MyIntentExample";
    Location loc;
    String radioButton;
    static int[] i = new int[1];
    private String baseURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private String locationParameters;
    private String searchType;
    private String intermediateURL;
    private String finalURL;
    private HandleJSON obj;
    LatLng[] latLnStore=null;
    String[] names=null;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beta);
        registerReceiver(receiver,new IntentFilter(GetLocationService.BROADCAST_ACTION));
        Log.i(TAG,"We're inside Oncreate");

        Bundle AlphaData = getIntent().getExtras();
        if (AlphaData == null) {
            return;
        }
        Context context = getApplicationContext();
        String userName = AlphaData.getString("userName");
        String contactNum = AlphaData.getString("contactNum");
        radioButton = AlphaData.getString("radioButton");
        TextView betaText = (TextView) findViewById(R.id.betaText);
        betaText.setText("Hey " + userName + "\n\n" + "Contact Info : " + contactNum + "\n\n" + "Mode Selected : " + radioButton);
        Intent i= new Intent(context, GetLocationService.class);
        i.putExtra("KEY1", "Value to be used by the service");
        Log.i(TAG,"Service about to start");
        context.startService(i);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beta, menu);
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

    @Override
    protected void onPause() {



        super.onPause();
        unregisterReceiver(receiver);


    }

    public void onClickBeta(View view) {

        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context,"To be added soon",Toast.LENGTH_SHORT);
        toast.show();

        Intent i = new Intent(this,MapsActivity.class);

        startActivity(i);
    }

    //RIPPCORD FUCK YOU!!
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i(TAG,"We're inside ONRECV BETA ACTIVITY");

                Bundle extra = intent.getExtras();

                loc = (Location)extra.get("Location");

                TextView betaStartLocationLat = (TextView) findViewById(R.id.BetaStartLocationLat);
                TextView betaStartLocationLon = (TextView) findViewById(R.id.BetaStartLocationLong);
                TextView betaProvider = (TextView) findViewById(R.id.betaProvider);
                betaStartLocationLat.setText("Latitude : " + loc.getLatitude());
                betaStartLocationLon.setText("Longitude : " + loc.getLongitude());
                betaProvider.setText(loc.getProvider());
            if(i[0]==0){
                locationParameters=loc.getLatitude()+","+loc.getLongitude();
                if(radioButton.equals("Metro Station"))
                {
                    searchType="subway_station";
                }
                if(radioButton.equals("Bus Stop")){
                    searchType="bus_station";
                }
                if(radioButton.equals("Taxi Stand")){
                    searchType="taxi_stand";
                }
                intermediateURL = "&radius=500&types="+searchType+"&key=AIzaSyCxlMg0r_bb0R07g6D0lB2dBT9lijsTB-0";
                finalURL = baseURL+locationParameters+intermediateURL;


                obj = new HandleJSON(finalURL);

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        obj.fetchJSON();
                        long futureTime = System.currentTimeMillis()+6000;
                        while(System.currentTimeMillis()<futureTime){
                            synchronized (this){
                                try{
                                    wait(futureTime-System.currentTimeMillis());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }

                };
                Thread jsonThread = new Thread(r);
                jsonThread.start();
                i[0]++;

            }


        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Log.i("Gamma","waiting for parse");
            if(obj.parsingComplete){

                Log.i("Gamma","Parsing Complete");

                latLnStore = obj.getLatlnStore();

                names = obj.getNames();

            }
        }
    };
    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver,new IntentFilter(GetLocationService.BROADCAST_ACTION));
    }

    public void onClickPlaces(View view) {

        Intent i = new Intent(this,Gamma.class);
        if(latLnStore==null || names==null){
            Toast toast = Toast.makeText(getApplicationContext(),"Please wait while we fetch crap",Toast.LENGTH_SHORT);
            toast.show();
        }
        else {

            i.putExtra("names", names);
            globalStoreLatLng gs = (globalStoreLatLng) getApplication();
            gs.setLatLnStore(latLnStore);
            startActivity(i);
        }


    }



}
