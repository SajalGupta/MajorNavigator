package com.example.sajal.intentexample;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    static int[] i = new int[1];
    LatLng startLocForDirFetcher;
    LatLng destLocForDirFetcher;
    List<LatLng> polyUtil;
    DirectionFetcher myDirectionFetcher;
    boolean isBound = false;

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DirectionFetcher.MyLocalBinder binder = (DirectionFetcher.MyLocalBinder)service;
            myDirectionFetcher = binder.getDirectionFetcher();
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(receiver,new IntentFilter(GetLocationService.BROADCAST_ACTION));

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        Bundle GammaData = getIntent().getExtras();
        if (GammaData == null) {
            return;
        }
        LatLng destinationMarker = (LatLng) GammaData.get("destinationMarker");

        destLocForDirFetcher= new LatLng(destinationMarker.latitude,destinationMarker.longitude);

        mMap.addMarker(new MarkerOptions()
                .position(destinationMarker)

                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Destination"));



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Bundle extra = intent.getExtras();
            Location loc = (Location) extra.get("Location");
             putMeOnThatMap(loc);



        }
    };

    private void putMeOnThatMap(Location location){

        LatLng currentPosition = new LatLng(location.getLatitude(),
                location.getLongitude());


        // Zoom in the Google Map

        if(i[0]==0) {
            startLocForDirFetcher=new LatLng(currentPosition.latitude,currentPosition.longitude);
            if(destLocForDirFetcher!=null){
                Log.i("Direction Service","Sending Intent to service");
                Intent i = new Intent(this,DirectionFetcher.class);
                i.putExtra("origin",startLocForDirFetcher);
                i.putExtra("destination",destLocForDirFetcher);
               bindService(i,myServiceConnection,Context.BIND_AUTO_CREATE);



            }
            else
                Log.i("DirectionsFetcher","Dest Null!!");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    currentPosition).zoom(20).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .snippet(
                            "Lat:" + location.getLatitude() + "Lng:"
                                    + location.getLongitude())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Start Position"));
            i[0]++;
        }



    }

    @Override
    protected void onDestroy() {

        Intent i = new Intent(this,GetLocationService.class);
        stopService(i);
        super.onDestroy();
        unregisterReceiver(receiver);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }

    public void onClickGetRoutes(View view) {

        if(myDirectionFetcher.latLngs==null){
            Toast toast = Toast.makeText(this,"please wait while crap is being fetched",Toast.LENGTH_SHORT);
            toast.show();

        }
        else{
            polyUtil = myDirectionFetcher.latLngs;
            PolylineOptions options = new PolylineOptions();
            for(int i=0;i<polyUtil.size();i++)
                options.add(polyUtil.get(i));


            mMap.addPolyline(options);

            TextView mapText = (TextView)findViewById(R.id.mapText);
            mapText.setText("Total Distance : "+myDirectionFetcher.distance+"\nExpected Duration : "+myDirectionFetcher.expectedDuration+"\nStart Address : "+myDirectionFetcher.startAddress+"\nDestination : "+myDirectionFetcher.endAddress);

        }


    }
}
