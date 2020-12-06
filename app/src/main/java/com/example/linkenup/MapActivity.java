package com.example.linkenup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Scene;
import androidx.transition.SidePropagation;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.linkenup.code.ClientAdapter;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    MapView mapView;
    Scene showScene, hideScene;
    boolean trasitionBool;
    Transition transition;
    ViewGroup root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Uri uri = getIntent().getData();

        recyclerView = findViewById(R.id.map_include_recycler);

        root = findViewById(R.id.map_secondLayout);
        showScene = Scene.getSceneForLayout(root, R.layout.scene_map_nav_show, this);
        hideScene = Scene.getSceneForLayout(root, R.layout.scene_map_nav_hide, this);
        trasitionBool = false;

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        transition = new Slide(Gravity.RIGHT).addTarget(R.id.map_include_nav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        ClientAdapter adapter = new ClientAdapter(this, new DatabaseHelper(this).getAllClient(), ClientAdapter.LAYOUT_MAP);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        mapView.getMapAsync((GoogleMap googleMap) -> {/*******************************************************************************************/

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            else googleMap.setMyLocationEnabled(true);
            
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(@NonNull Transition transition) {
                    if(trasitionBool){
                        googleMap.getUiSettings().setAllGesturesEnabled(false);
                    }
                }
                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    if(!trasitionBool){
                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                    }
                }

                @Override public void onTransitionCancel(@NonNull Transition transition) { } @Override public void onTransitionPause(@NonNull Transition transition) { } @Override public void onTransitionResume(@NonNull Transition transition) { }
            });
        });

        

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


    public void changeNav(View view){
        if(trasitionBool=!trasitionBool) TransitionManager.go(showScene,transition);
        else TransitionManager.go(hideScene,transition);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
}