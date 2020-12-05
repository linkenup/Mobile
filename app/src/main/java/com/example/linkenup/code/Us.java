package com.example.linkenup.code;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.example.linkenup.R;

import java.io.IOException;

public class Us {
    public static String ADDRESS = "Rua Guaipá, 678 - Vila Leopoldina, São Paulo - SP";
    public static String CNPJ = "23.465.332/0001-66";//XX. XXX. XXX/0001-XX
    public static  String CE = "06.236.765-6";

    public static Address getAddress(Context context) throws IOException {
        return new Geocoder(context).getFromLocationName(ADDRESS,1).get(0);
    }
}
