package com.example.linkenup;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.linkenup.activities.NewContractActivity;
import com.example.linkenup.code.Us;

import java.io.IOException;

public class UsActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us);
            findViewById(R.id.float_home_button).setVisibility(
                    getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false)?
                            View.VISIBLE:
                            View.GONE);

        toolbar = (Toolbar) findViewById(R.id.us_toolbar);
        toolbar.setNavigationOnClickListener((View v)->{
            this.onBackPressed();
        });
    }

    public void onMap(View view) throws IOException {
        Address us =Us.getAddress(this);
        Uri location = Uri.parse("geo:" + us.getLatitude() + "," + us.getLongitude() + "?q=" + us.getAddressLine(0).replaceAll(" ", "+"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }

}
