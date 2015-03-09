package com.example.sajal.intentexample;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Sajal on 05-03-2015.
 */
public class globalStoreLatLng extends Application{

    private LatLng[] latLnStore;
    private List<LatLng> latLongForFetcher;


    public List<LatLng> getLatLongForFetcher() {
        return latLongForFetcher;
    }

    public void setLatLongForFetcher(List<LatLng> latLongForFetcher) {
        this.latLongForFetcher = latLongForFetcher;
    }

    public LatLng[] getLatLnStore() {
        return latLnStore;
    }

    public void setLatLnStore(LatLng[] latLnStore) {
        this.latLnStore = latLnStore;
    }


}
