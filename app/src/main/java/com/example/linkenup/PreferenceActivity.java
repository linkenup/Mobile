package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class PreferenceActivity extends AppCompatActivity {
    boolean home_imgVisible,home_btnVisible,home_smallimg;

    CheckBox chkHomeLayout_img,chkHomeLayout_btn,chkHomeLayout_smallimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        chkHomeLayout_img = (CheckBox) findViewById(R.id.preference_check_homelayout_img);
        chkHomeLayout_btn = (CheckBox) findViewById(R.id.preference_check_homelayout_btn);
        chkHomeLayout_smallimg = (CheckBox) findViewById(R.id.preference_check_homelayout_smallimg);

        SharedPreferences homeLayout = getSharedPreferences(HomeActivity.LAYOUT_PREFERENCE_NAME,0);

        boolean img, btn, smallimg;
        img = homeLayout.getBoolean("img",true);
        btn = homeLayout.getBoolean("btn",true);
        smallimg =homeLayout.getBoolean("smallimg",false);

        chkHomeLayout_img.setChecked(img);
        chkHomeLayout_btn.setChecked(btn);
        chkHomeLayout_smallimg.setChecked(smallimg);
        home_imgVisible = img;
        home_btnVisible = btn;
        home_smallimg = smallimg;

    }

    public void updatePreferences(String preference)
    {
    switch (preference)
    {
        case "homelayout":
            SharedPreferences.Editor setting = getSharedPreferences(HomeActivity.LAYOUT_PREFERENCE_NAME,0).edit();
            setting.putBoolean("img",home_imgVisible);
            setting.putBoolean("btn",home_btnVisible);
            setting.putBoolean("smallimg",home_smallimg);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }
}