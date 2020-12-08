package com.example.linkenup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.linkenup.activities.OpenClientActivity;
import com.example.linkenup.code.ClientAdapter;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.code.Us;
import com.example.linkenup.system.Client;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MapActivity extends AppCompatActivity {

    MapView mapView;
    Scene showScene, hideScene;
    boolean transitionBool;
    public Transition transition;
    ViewGroup root;
    public Double lastLatitude,lastLongitude;

    FusedLocationProviderClient fusedLocationClient;

    Uri uri;
    Client[] clients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        uri = getIntent().getData();

        queryClients();

        root = findViewById(R.id.map_secondLayout);
        showScene = Scene.getSceneForLayout(root, R.layout.scene_map_nav_show, this);
        hideScene = Scene.getSceneForLayout(root, R.layout.scene_map_nav_hide, this);
        transitionBool = false;

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        RecyclerView recyclerView = findViewById(R.id.map_include_recycler);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        ClientAdapter adapter = new ClientAdapter(this,new DatabaseHelper(this).getAllClient(),ClientAdapter.LAYOUT_MAP);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.getAdapter().notifyDataSetChanged();


        transition = new Slide().addTarget(R.id.map_include_nav);//.addTarget(R.id.map_include_recycler);//.addTarget("com.example.linkenup.code.MapFragment");
        ((Slide)transition).setSlideEdge(Gravity.RIGHT);
        boolean floatHomeBool = getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false);
        transition.addListener(new Transition.TransitionListener() {

            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                if(floatHomeBool)findViewById(R.id.float_home_button).setVisibility(View.VISIBLE);
                queryClients();
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                if(transitionBool)applyClients();
            }
            @Override
            public void onTransitionCancel(@NonNull Transition transition) { }
            @Override
            public void onTransitionPause(@NonNull Transition transition) {}
            @Override
            public void onTransitionResume(@NonNull Transition transition) {}
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                123);
        }
        else {
            fusedLocationClient.getLastLocation().addOnSuccessListener((Location location) ->{
                if(location!=null) {
                    lastLatitude = location.getLatitude();
                    lastLongitude = location.getLongitude();
                }
            });
            asyncMap();
        }

    }




    public void changeNav(View view){
        if(transitionBool =!transitionBool) TransitionManager.go(showScene,transition);
        else TransitionManager.go(hideScene,transition);
    }

    public void changeNav() {
        if (transitionBool = !transitionBool) TransitionManager.go(showScene, transition);
        else TransitionManager.go(hideScene, transition);
    }

    @Override
    public void onBackPressed() {
        if(!(transitionBool=!transitionBool))TransitionManager.go(hideScene, transition);
        else super.onBackPressed();
    }

    private class ClientDistanceComparator implements Comparator<Client>{
        double latitude,
                longitude;

        ClientDistanceComparator(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
        @Override
        public int compare(Client o1, Client o2) {
            Float distance1 = o1.getLastDistance()!=null?
                                o1.getLastDistance():o1.getDistance(getApplicationContext(),latitude,longitude);
            Float distance2 = o2.getLastDistance()!=null?
                                o2.getLastDistance():o2.getDistance(getApplicationContext(),latitude,longitude);
            if(distance1==null)return 1;
            if(distance2==null)return -1;
            if(distance1==distance2){
                return 0;
            }
            else if(distance1>distance2){
                return 1;
            }
            return -1;
        }
    }

    public void queryClients(){
        if(clients==null){
            List<Client> clientList = Arrays.asList(new DatabaseHelper(this).getAllClient());
            if (lastLatitude != null && lastLongitude != null) {
                Comparator<Client> comparator = new ClientDistanceComparator(lastLatitude, lastLongitude);
                for (Client client : clientList){
                    client.getDistance(this, lastLatitude, lastLongitude);
                    client.getLatLng(this);
                }
                List<Client> listParse = Arrays.asList(clients);
                Collections.sort(listParse, comparator);
                clients = listParse.toArray(new Client[0]);
            }
            clients = clientList.toArray(new Client[0]);
        }
        else if(clients[0].getLastDistance()==null)
        {
            if (lastLatitude != null && lastLongitude != null) {
                Comparator<Client> comparator = new ClientDistanceComparator(lastLatitude, lastLongitude);
                for (Client client : clients) {
                    client.getDistance(this, lastLatitude, lastLongitude);
                    client.getLatLng(this);
                }
                List<Client> listParse = Arrays.asList(clients);
                Collections.sort(listParse, comparator);
                clients = listParse.toArray(new Client[0]);
            }
        }

    }

    public void applyClients(){
        if(clients==null)return;

        RecyclerView recyclerView = findViewById(R.id.map_include_recycler);

        if(recyclerView.getAdapter()!=null){
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        else {
            LinearLayoutManager llm= new LinearLayoutManager(this);
            llm.setOrientation(RecyclerView.VERTICAL);
            ClientAdapter adapter = new ClientAdapter(this,clients,ClientAdapter.LAYOUT_MAP);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
        }
    }

    public void clickClient(Client client){
        changeNav();
        LatLng latlng = client.getLatLng(this);
        if(latlng!=null)
        mapView.getMapAsync((GoogleMap googleMap)->{
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,14.6f));
        });
    }

    private void asyncMap(){
        mapView.getMapAsync((GoogleMap googleMap) -> {/*******************************************************************************************/

            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                googleMap.setMyLocationEnabled(true);
            }
                if(uri!=null) {
                    LatLng latlng;
                    Scanner scanner = new Scanner(uri.toString());
                        try {
                            List<Address> addresses;// = new Geocoder(getApplicationContext()).getFromLocation(scanner.nextDouble(),scanner.nextDouble(),1);
                            addresses = new Geocoder(getApplicationContext()).getFromLocationName(uri.getQuery().replace("q="," "),1);
                            if(!(addresses.size()>0)){
                                Toast.makeText(getApplicationContext(),R.string.cannot_decode_address,Toast.LENGTH_SHORT).show();
                                return;
                            }
                            latlng = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,14.6f));
                        } catch (IOException e) {
                            e.printStackTrace();
                        };
                }

                for(Client client : clients){
                    if(client.getLatLng(getApplicationContext())!=null)
                        googleMap.addMarker(new MarkerOptions()
                            .position(client.getLatLng(getApplicationContext()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).setTag(client.id);
                }

            try {
                Address usAddress = Us.getAddress(getApplicationContext());
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_marker))
                        .position(new LatLng(usAddress.getLatitude()+0.0003f,usAddress.getLongitude()+0.0005))).setTag("LinkenUp");
            } catch (IOException e) { }


            googleMap.setOnMarkerClickListener(this::markerListener);

                transition.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(@NonNull Transition transition) {
                        if(transitionBool){
                            googleMap.getUiSettings().setAllGesturesEnabled(false);
                        }
                    }
                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        if(!transitionBool){
                            googleMap.getUiSettings().setAllGesturesEnabled(true);
                        }
                    }

                    @Override public void onTransitionCancel(@NonNull Transition transition) { } @Override public void onTransitionPause(@NonNull Transition transition) { } @Override public void onTransitionResume(@NonNull Transition transition) { }
                });
        });
    }


    private Object focusedTag;
    public boolean markerListener(Marker marker){
        if(focusedTag!=null){
            if(marker.getTag() == focusedTag){
                if(marker.getTag() == "LinkenUp")startActivity(new Intent(this,UsActivity.class));
                else {
                    Intent intent = new Intent(this, OpenClientActivity.class);
                    intent.putExtra(OpenClientActivity.EXTRA_CLIENT_ID,(Integer)marker.getTag());
                    startActivity(intent);
                    focusedTag = null;
                }
                return true;
            }
        }

        Client client ;

        if(marker.getTag() != "LinkenUp"){
            client = new DatabaseHelper(this).getClient((Integer)marker.getTag());

            Toast.makeText(this, client.name+"\n"+
                    getString(R.string.cnpj)+": "+client.cnpj,Toast.LENGTH_SHORT).show();

            focusedTag = marker.getTag();
            new Handler().postDelayed(()->{
                focusedTag = null;
            },4000);

            mapView.getMapAsync((GoogleMap map)-> {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),14.6f));
            });
        }
        else {
            focusedTag = "LinkenUp";

            new Handler().postDelayed(()->{
                focusedTag = null;
            },4000);

            mapView.getMapAsync((GoogleMap map)-> {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),19.6f));
            });
        }
        return true;
    }





    /*public void {
        try {
            address = new Geocoder(this).getFromLocationName(director.address,1).get(0);
            if(address==null)throw new IOException();
            directorAddress = address;
        }
        catch (Exception e) {
            Toast.makeText(this,R.string.invalid_directoraddress_message, Toast.LENGTH_LONG).show();
        }
    }*/

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123) {
            asyncMap();
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                fusedLocationClient.getLastLocation().addOnSuccessListener((Location location) ->{
                    if(location != null) {
                        lastLatitude = location.getLatitude();
                        lastLongitude = location.getLongitude();
                    }
                });
            }
        }
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
}