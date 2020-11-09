package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String LAYOUT_PREFERENCE_NAME = "com.example.linkenup.HomeActivity.layout_preference";


    Toolbar toolbar;
    GridLayout gridLayout;
    RelativeLayout mainLayout;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        gridLayout = (GridLayout) findViewById(R.id.home_layout_smallimg);
        mainLayout = (RelativeLayout) findViewById(R.id.home_relative_main);
        scrollView = (ScrollView) findViewById(R.id.home_scroll_main);

        SharedPreferences settings = getSharedPreferences(LAYOUT_PREFERENCE_NAME,0);
        boolean smallimg = settings.getBoolean("smallimg",false);
        gridLayout.setVisibility(smallimg?View.VISIBLE:View.GONE);

        if(smallimg) scrollView.setVisibility(View.GONE); else {
            boolean img, btn;
            img = settings.getBoolean("img",true);
            btn = settings.getBoolean("btn",true);
            int imgVisibility = img ? View.VISIBLE : View.GONE;
            int btnVisibility = btn ? View.VISIBLE : View.GONE;
            int dividerVisibility = img && btn ? View.VISIBLE : View.GONE;

            int i = 1;
            while (i != -1) {
                try {
                    mainLayout.getChildAt(i * 3 - 3).setVisibility(imgVisibility);//imgs
                    mainLayout.getChildAt(i * 3 - 2).setVisibility(btnVisibility);//btns
                    mainLayout.getChildAt(i * 3 - 1).setVisibility(dividerVisibility);//dividers
                    i++;
                } catch (Exception ex) {
                    i = -1;
                }
            }
        }
        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
    }

    public void onClient(View view){
        startActivity(new Intent(this,ClientActivity.class));
    }

    public void onPreference(View view) { startActivity(new Intent(this, PreferenceActivity.class));}
    @Override
    public void onBackPressed() {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}