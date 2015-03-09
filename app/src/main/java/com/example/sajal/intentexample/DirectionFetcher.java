package com.example.sajal.intentexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class DirectionFetcher extends Service {

    public class MyLocalBinder extends Binder {
        DirectionFetcher getDirectionFetcher (){
            return DirectionFetcher.this;
        }
    }

    final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final String BROADCAST_ACTION_DIRECTION = "Hello World";

    LatLng param1;
    LatLng param2;
   public static List<LatLng> latLngs;
    final IBinder mServiceBinder = new MyLocalBinder();
    public DirectionFetcher() {
    }




    public int datMagic(final LatLng param1, final LatLng param2) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.i("DirectionFetcher","Service Thread Started");



                HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });


                Log.i("DirectionService","inside thread");
                String startingUrl = "https://maps.googleapis.com/maps/api/directions/json?";
                String origins = "&origin=" + param1.latitude + "," + param1.longitude;
                String destinations = "&destination=" + param2.latitude + "," + param2.longitude;
                String modes = "&mode=walking";
                String key = "&key=AIzaSyCxlMg0r_bb0R07g6D0lB2dBT9lijsTB-0";
                String finalURL = startingUrl + origins + destinations + modes + key;
                GenericUrl url = new GenericUrl(finalURL);
                Log.i("FINAL URL ", finalURL);
                try {
                    HttpRequest request = requestFactory.buildGetRequest(url);
                    HttpResponse httpResponse = request.execute();
                    DirectionsResult directionsResult = httpResponse.parseAs(DirectionsResult.class);
                    String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;
                     latLngs = PolyUtil.decode(encodedPoints);
                    Log.i("DirectionFetcher","Thread Finished no Exceptions");
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }

        };
        Thread mThread = new Thread(r);
        mThread.start();
        return 0;
    }

    public static List<LatLng> getLatLngs() {
        return latLngs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("DirectionFetcher","in OnBind");
        Bundle data = intent.getExtras();
        if(data!=null) {
            Log.i("DirectionFetcher","got data in bundle :D");
            param1 = (LatLng) data.get("origin");
            param2 = (LatLng) data.get("destination");
            datMagic(param1,param2);
        }

        return mServiceBinder;
    }





    public static class DirectionsResult {

        @Key("routes")
        public List<Route> routes;

    }

    public static class Route {
        @Key("overview_polyline")
        public OverviewPolyLine overviewPolyLine;

    }

    public static class OverviewPolyLine {
        @Key("points")
        public String points;

    }

}
