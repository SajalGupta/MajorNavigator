package com.example.sajal.intentexample;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sajal on 05-03-2015.
 */
public class globalStoreLatLng extends Application{

    private LatLng[] latLnStore;

    public LatLng[] getLatLnStore() {
        return latLnStore;
    }

    public void setLatLnStore(LatLng[] latLnStore) {
        this.latLnStore = latLnStore;
    }
}
