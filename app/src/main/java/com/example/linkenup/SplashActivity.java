package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Worker;

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


        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(db.getAllWorker()==null&&db.getAllClient()==null&&db.getAllSoftware()==null){

            Toast.makeText(this,R.string.insert_old_message,Toast.LENGTH_LONG).show();
            db.sqliteUpgrade();
            db.insertOLD();

        }
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}