package com.example.sajal.intentexample;

import android.location.Location;
import android.os.AsyncTask;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sajal on 08-03-2015.
 */
 public class DirectionsFetcher {

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private Location origin;
    private Location destination;
    private List<LatLng> latLngs = new ArrayList<LatLng>();

    public DirectionsFetcher(Location origin,Location destination) {
        this.origin.setLatitude(origin.getLatitude());
        this.origin.setLongitude(origin.getLongitude());
        this.destination.setLatitude(destination.getLatitude());
        this.destination.setLongitude(destination.getLongitude());
    }

    protected Void doInBackground() {
        Log.i("Directions Fetcher","Started!!");
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
            String startingUrl = "http://maps.googleapis.com/maps/api/directions/json";
            String origins = "&origin="+origin.getLatitude()+","+origin.getLongitude();
            String destinations = "&destination="+destination.getLatitude()+","+destination.getLongitude();
            String modes = "&mode=walking";
            String key = "&key=AIzaSyCxlMg0r_bb0R07g6D0lB2dBT9lijsTB-0";
            String finalURL = startingUrl+origins+destinations+modes+key;
            GenericUrl url = new GenericUrl(finalURL);
            Log.i("FINAL URL ",finalURL);


            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            DirectionsResult directionsResult = httpResponse.parseAs(DirectionsResult.class);

            String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;
            latLngs = PolyUtil.decode(encodedPoints);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

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