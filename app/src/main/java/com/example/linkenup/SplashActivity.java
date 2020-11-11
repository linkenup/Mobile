package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_LinkenUp_Splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences theme = getSharedPreferences(PreferenceActivity.THEME,0);
        if(theme.getBoolean("theme",false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new Handler().postDelayed(() -> startActivity(new Intent(getApplicationContext(),HomeActivity.class)),2000);
    }
}