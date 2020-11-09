package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class ClientActivity extends AppCompatActivity {
    public static final String MANUAL_HOME = "com.example.linkenup.HomeActivity.MANUAL_HOME";
    public boolean manualHome;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        try {
            manualHome = getIntent().getExtras().getBoolean(MANUAL_HOME);
        }catch (Exception e)
        {
            manualHome = false;
        }

        toolbar = (Toolbar) findViewById(R.id.client_toolbar);

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
    }

    @Override
    public void onBackPressed() {
        if(manualHome)
        {
            startActivity(new Intent(this,HomeActivity.class));
            return;
        }
        super.onBackPressed();
    }
}