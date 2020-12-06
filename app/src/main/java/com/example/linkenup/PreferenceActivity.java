package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.example.linkenup.activities.OpenScreenActivity;
import com.example.linkenup.code.DatabaseHelper;

public class PreferenceActivity extends AppCompatActivity {
    public static final String THEME = "LinkenUp.theme";
    boolean
            home_imgVisible,home_btnVisible,home_smallimg,
            theme,
            isImageBorder;
    int imageBorderColor;

    Toolbar toolbar;
    CheckBox
            chkHomeLayout_img,chkHomeLayout_btn,chkHomeLayout_smallimg,
            chkImageBorderRed,chkImageBorderGreen,chkImageBorderBlue;
    Switch switchTheme, switchImageBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        toolbar = (Toolbar) findViewById(R.id.preference_toolbar);
        chkHomeLayout_img = (CheckBox) findViewById(R.id.preference_check_homelayout_img);
        chkHomeLayout_btn = (CheckBox) findViewById(R.id.preference_check_homelayout_btn);
        chkHomeLayout_smallimg = (CheckBox) findViewById(R.id.preference_check_homelayout_smallimg);
        switchTheme = (Switch) findViewById(R.id.preference_switch_theme);

        chkImageBorderRed = findViewById(R.id.preference_check_imageBorder_red);
        chkImageBorderGreen = findViewById(R.id.preference_check_imageBorder_green);
        chkImageBorderBlue = findViewById(R.id.preference_check_imageBorder_blue);
        switchImageBorder = (Switch) findViewById(R.id.preference_switch_imageBorder);

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

        isImageBorder = ((Switch) view).isChecked();

        int r =  chkImageBorderRed.isChecked()?1:0;
        int g =  chkImageBorderGreen.isChecked()?1:0;
        int b =  chkImageBorderBlue.isChecked()?1:0;

        imageBorderColor = Color.rgb(r,g,b);

        updatePreferences("imageBorder");

    }
}