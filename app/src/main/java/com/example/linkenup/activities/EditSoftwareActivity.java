package com.example.linkenup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linkenup.HomeActivity;
import com.example.linkenup.R;
import com.example.linkenup.ScreenActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Screen;
import com.example.linkenup.system.Software;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class EditSoftwareActivity extends AppCompatActivity {

    public static final String EXTRA_SOFTWARE_ID = "LinkenUp.EditSoftware.EXTRA_SOFTWARE_ID";

    DatabaseHelper db;

    Software software;

    EditText nameEdit, descrEdit, supportsEdit;
    boolean backSafe;
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editsoftware);

        Bundle extras = getIntent().getExtras();
        if(!(extras.getInt(EXTRA_SOFTWARE_ID,0)>0))
        {
            onBackPressed();
            Toast.makeText(this,getString(R.string.no_softwareextra_message),Toast.LENGTH_SHORT).show();
            return;
        }
        db = new DatabaseHelper(this);
        software = db.getSoftware(extras.getInt(EXTRA_SOFTWARE_ID));

        nameEdit = (EditText) findViewById(R.id.newsoftware_edit_name);
        descrEdit = (EditText) findViewById(R.id.newsoftware_edit_description);
        supportsEdit = (EditText) findViewById(R.id.newsoftware_edit_supports);
        tabLayout = (TabLayout) findViewById(R.id.newsoftware_tablayout);

        nameEdit.setText(software.name);
        descrEdit.setText(software.description);
        supportsEdit.setText(software.supports);
        tabLayout.setVisibility(View.GONE);
    }

    public void onRegister(View view){

        String
                name = nameEdit.getText().toString(),
                descr = descrEdit.getText().toString(),
                supports = supportsEdit.getText().toString();

        if(name.length() < 2 || descr.length()<4)
        {
            return;
        }
        DatabaseHelper db = new DatabaseHelper(this);

        software = new Software(software.id,name,descr,supports);

        int id = db.updateSoftware(software);
        Toast.makeText(this,
                getString(R.string.software_insert_message)
                        .replace("%id",String.valueOf(id))
                        .replace("%name",software.name)
                , Toast.LENGTH_LONG).show();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        },1000);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("name",nameEdit.getText().toString());
        outState.putString("descr", descrEdit.getText().toString());
        outState.putString("supports", supportsEdit.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameEdit.setText(savedInstanceState.getString("name"));
        descrEdit.setText(savedInstanceState.getString("cnpj"));
        supportsEdit.setText(savedInstanceState.getString("ce"));
    }

    @Override
    public void onBackPressed() {
        if(!backSafe)
        {
            Toast.makeText(this,getString(R.string.register_backsafe_message),Toast.LENGTH_SHORT).show();
            backSafe=true;
            new Handler().postDelayed(() -> {backSafe=false;},5000);
        }
        else super.onBackPressed();
    }

}
