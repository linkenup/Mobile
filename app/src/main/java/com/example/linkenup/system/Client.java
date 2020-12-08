package com.example.linkenup.system;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.example.linkenup.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class Client {

    public static final String //Database Row references
            ID = "id",
            NAME = "name",
            CNPJ = "cnpj",
            CE = "ce",
            ADDRESS = "address";

    public Integer id;
    public String name;
    public String cnpj;
    public String ce;
    public String address;

    private Float lastDistance;
    private LatLng lastLatLng;

    public Client(){ }

    public Client(String name,String cnpj,String ce,String address){
        this.name = name;
        this.cnpj = cnpj;
        this.ce = ce;
        this.address = address;
    }

    public Client(int id,String name,String cnpj,String ce,String address){
        this.name = name;
        this.cnpj = cnpj;
        this.ce = ce;
        this.address = address;
        this.id = id;
    }


    public LatLng getLatLng(Context context){
        if(lastLatLng!=null)return lastLatLng;
        LatLng latlng;
        try {
            Address addressInstance;
            addressInstance = new Geocoder(context).getFromLocationName(address,1).get(0);
            latlng = new LatLng(addressInstance.getLatitude(),addressInstance.getLongitude());
            lastLatLng = latlng;
            return latlng;
        }
        catch (Exception e) {
            return null;
        }
    }

    public Float getLastDistance() {
        return lastDistance;
    }

    public Float getDistance(Context context, double latitude, double longitude){
        double clientLatitude, clientLongitude;
        try {
            Address addressInstance;
            addressInstance = new Geocoder(context).getFromLocationName(address,1).get(0);
            clientLatitude = addressInstance.getLatitude();
            clientLongitude = addressInstance.getLongitude();
            float[] floatArray = new float[1];
            Location.distanceBetween(latitude,longitude,clientLatitude,clientLongitude,floatArray);
            lastDistance = floatArray[0];
            return floatArray[0];
        }
        catch (Exception e) {
            return null;
        }
    }

}
