package com.example.sajal.intentexample;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sajal on 02-03-2015.
 */
public class HandleJSON {


    private String[] names=null;
    private String lat="lat";
    private String lon="lon";
    private String urlString = null;
    public volatile boolean parsingComplete = false;
    private LatLng[] latlnStore = null;
    public HandleJSON(String url){
        this.urlString = url;
    }

    public String[] getNames() {
        return names;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public LatLng[] getLatlnStore() {
        return latlnStore;
    }

    public void fetchJSON(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    String data = convertStreamToString(stream);
                    Log.i("JSON",data);
                    readAndParseJSON(data);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {


        try {
            JSONObject reader = new JSONObject(in);

            JSONArray sys  = reader.getJSONArray("results");
            JSONObject sys2;
            latlnStore= new LatLng[sys.length()];
            names = new String[sys.length()];
            for(int i=0;i<sys.length();i++) {
                 sys2 = sys.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                Log.i("JSON","Latitude Parsed :"+sys2.getString("lat"));
                Log.i("JSON","Longitude Parsed :"+sys2.getString("lng"));
                latlnStore[i] =  new LatLng(Double.parseDouble(sys2.getString("lat")),Double.parseDouble(sys2.getString("lng")));
                names[i] = sys.getJSONObject(i).getString("name");

            }


            parsingComplete = true;



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
