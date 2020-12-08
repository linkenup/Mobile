package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.example.linkenup.activities.OpenScreenActivity;
import com.example.linkenup.code.DatabaseHelper;

public class PreferenceActivity extends AppCompatActivity {
    public static final String THEME = "LinkenUp.theme";
    public static final String FLOAT_HOME = "LinkenUp.floatHome";

    boolean
            home_imgVisible,home_btnVisible,home_smallimg,
            theme,
            isImageBorder,
            floatHome_bool;
    int imageBorderColor;



    ViewGroup floatRoot;
    View floatHome_button;
    Toolbar toolbar;
    CheckBox
            chkHomeLayout_img,chkHomeLayout_btn,chkHomeLayout_smallimg,
            chkImageBorderRed,chkImageBorderGreen,chkImageBorderBlue;
    Switch switchTheme, switchImageBorder, swicthFloatHome;

    Transition floatHomeTransition;
    Scene floatHome_visible,floatHome_gone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        floatRoot = (ViewGroup) findViewById(R.id.preference_root_floatHome);

        toolbar = (Toolbar) findViewById(R.id.preference_toolbar);
        chkHomeLayout_img = (CheckBox) findViewById(R.id.preference_check_homelayout_img);
        chkHomeLayout_btn = (CheckBox) findViewById(R.id.preference_check_homelayout_btn);
        chkHomeLayout_smallimg = (CheckBox) findViewById(R.id.preference_check_homelayout_smallimg);
        switchTheme = (Switch) findViewById(R.id.preference_switch_theme);

        chkImageBorderRed = findViewById(R.id.preference_check_imageBorder_red);
        chkImageBorderGreen = findViewById(R.id.preference_check_imageBorder_green);
        chkImageBorderBlue = findViewById(R.id.preference_check_imageBorder_blue);
        switchImageBorder = (Switch) findViewById(R.id.preference_switch_imageBorder);

        swicthFloatHome = (Switch) findViewById(R.id.preference_switch_floatHome);
        floatHome_button = findViewById(R.id.float_home_button);

        imageBorderColor = 0;

        SharedPreferences homeLayout = getSharedPreferences(HomeActivity.LAYOUT_PREFERENCE_NAME,0);
        SharedPreferences imageBorder = getSharedPreferences(OpenScreenActivity.IMAGE_BORDER_PREFERENCE,0);


        boolean img, btn, smallimg, isColor;

        int color;

        img = homeLayout.getBoolean("img",true);
        btn = homeLayout.getBoolean("btn",true);
        smallimg =homeLayout.getBoolean("smallimg",false);

        isColor =  imageBorder.getBoolean("bool",false);
        color = imageBorder.getInt("color",Color.rgb(0,0,0));

        chkHomeLayout_img.setChecked(img);
        chkHomeLayout_btn.setChecked(btn);
        chkHomeLayout_smallimg.setChecked(smallimg);

        home_imgVisible = img;
        home_btnVisible = btn;
        home_smallimg = smallimg;


        chkImageBorderRed.setChecked(Color.red(color) > 0);
        chkImageBorderGreen.setChecked(Color.green(color) > 0);
        chkImageBorderBlue.setChecked(Color.blue(color) > 0);

        switchImageBorder.setChecked(isColor);

        SharedPreferences themePreference = getSharedPreferences(THEME,0);

        theme = themePreference.getBoolean("theme",false);
        switchTheme.setChecked(theme);

        sqliteSafe=false;
        toolbar.setTitle("<"+toolbar.getTitle()+">");
        toolbar.setNavigationOnClickListener((View v)-> {
            onBackPressed();
        });

        floatHome_bool = getSharedPreferences(FLOAT_HOME,0).getBoolean("bool",false);

        swicthFloatHome.setChecked(floatHome_bool);


        findViewById(R.id.float_home_button).setVisibility(
                getSharedPreferences(FLOAT_HOME,0).getBoolean("bool",false)?
                        View.VISIBLE:
                        View.GONE);

        floatHome_gone = Scene.getSceneForLayout(floatRoot,R.layout.include_home_floatbutton,this);
        floatHome_visible = Scene.getSceneForLayout(floatRoot,R.layout.scene_home_float_show,this);
        floatHomeTransition = new Slide().addTarget(R.id.float_home_button);
        ((Slide)floatHomeTransition).setSlideEdge(Gravity.LEFT);

    }

    public void updatePreferences(String preference)
    {
    SharedPreferences.Editor setting;
    switch (preference)
    {
        case "homelayout":
            setting = getSharedPreferences(HomeActivity.LAYOUT_PREFERENCE_NAME,0).edit();
            setting.putBoolean("img",home_imgVisible);
            setting.putBoolean("btn",home_btnVisible);
            setting.putBoolean("smallimg",home_smallimg);
            setting.commit();
        case "theme":
            setting = getSharedPreferences(THEME,0).edit();
            setting.putBoolean("theme",theme);
            setting.commit();
            break;
        case "imageBorder":
            setting = getSharedPreferences(OpenScreenActivity.IMAGE_BORDER_PREFERENCE,0).edit();
            setting.putBoolean("bool",isImageBorder);
            setting.putInt("color",imageBorderColor);
            setting.commit();
            break;
        case "floatHome":
            setting = getSharedPreferences(FLOAT_HOME,0).edit();
            setting.putBoolean("bool",floatHome_bool);
            setting.commit();
            break;

    }
    }

    public void onHomeLayout(View view){
        int id = view.getId();
        if(id == R.id.preference_check_homelayout_img) {
            if (home_imgVisible = ((CheckBox) view).isChecked())
                home_smallimg = false;
            else if (!home_btnVisible)
                home_smallimg = true;
        }

        if(id == R.id.preference_check_homelayout_btn) {
            if (home_btnVisible = ((CheckBox) view).isChecked()) {
                home_smallimg = false;
            } else if (!home_imgVisible) {
                home_smallimg = true;
            }
        }

        if(id == R.id.preference_check_homelayout_smallimg){
            if(home_smallimg = ((CheckBox) view).isChecked())
            {
                home_imgVisible = false;
                home_btnVisible = false;
            }else
            if(!home_btnVisible && !home_imgVisible)
            {
                home_imgVisible = true;
                home_btnVisible = true;
            }
        }

        chkHomeLayout_img.setChecked(home_imgVisible);
        chkHomeLayout_btn.setChecked(home_btnVisible);
        chkHomeLayout_smallimg.setChecked(home_smallimg);


        updatePreferences("homelayout");
    }

    public void onTheme(View view)
    {
        theme = ((Switch)view).isChecked();
        if(theme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        updatePreferences("theme");
    }

    private boolean sqliteSafe;
    public void onUpgradeSQLite(View view){
        if(sqliteSafe) {
            DatabaseHelper db = new DatabaseHelper(this);
            db.sqliteUpgrade();
            sqliteSafe = false;
            return;
        }
        sqliteSafe=true;
        Toast.makeText(this,R.string.sqlitesafe_message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void onImageBorder(View view) {

        isImageBorder = switchImageBorder.isChecked();

        int r =  chkImageBorderRed.isChecked()?177:0;
        int g =  chkImageBorderGreen.isChecked()?177:0;
        int b =  chkImageBorderBlue.isChecked()?177:0;

        imageBorderColor = Color.rgb(r,g,b);

        updatePreferences("imageBorder");

    }

    public void onSwicthHome(View view){
        floatHome_bool = swicthFloatHome.isChecked();
        if(floatHome_bool) TransitionManager.go(floatHome_visible,floatHomeTransition);
        else TransitionManager.go(floatHome_gone,floatHomeTransition);
        updatePreferences("floatHome");
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void onMap(View view){
        startActivity(new Intent(this,MapActivity.class));
    }

    public void onAboutUs(View view){startActivity(new Intent(this,UsActivity.class));}
}