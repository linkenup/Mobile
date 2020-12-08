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

public class NewSoftwareActivity extends AppCompatActivity {

    public static List<Screen> SCREEN_LIST;
    EditText nameEdit, descrEdit, supportsEdit;
    boolean backSafe;
    TabLayout tabLayout;
    TabLayout.Tab screenTab,softwareTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editsoftware);


        SCREEN_LIST = new ArrayList<Screen>();

        nameEdit = (EditText) findViewById(R.id.newsoftware_edit_name);
        descrEdit = (EditText) findViewById(R.id.newsoftware_edit_description);
        supportsEdit = (EditText) findViewById(R.id.newsoftware_edit_supports);
        tabLayout = (TabLayout) findViewById(R.id.newsoftware_tablayout);
        screenTab = tabLayout.getTabAt(1);
        softwareTab = tabLayout.getTabAt(0);
        SCREEN_LIST = new ArrayList<Screen>();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                    intent.putExtra(ScreenActivity.EXTRA_MODE,ScreenActivity.NEWSOFTWARE_MODE);
                    startActivity(intent);
                }
            }
            //Annoying code:
            @Override public void onTabUnselected(TabLayout.Tab tab) { }@Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    protected void onResume() {
        softwareTab.select();
        super.onResume();
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
        if(!(SCREEN_LIST.size() >0))
        {
            Toast.makeText(this,getString(R.string.no_screen_insert_message),Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseHelper db = new DatabaseHelper(this);

        Software software = new Software(name,descr,supports);

        software.screenList = SCREEN_LIST;

        int id = db.insertSoftware(software);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCREEN_LIST = null;
    }
}
