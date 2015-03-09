package com.example.sajal.intentexample;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sajal on 09-03-2015.
 */
public class DataWrapper implements Serializable {

    private List<LatLng> myData;


    public DataWrapper(List<LatLng> data) {
        this.myData = data;
    }

    public List<LatLng> getUserData() {
        return this.myData;
    }
}
