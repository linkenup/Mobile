package com.example.linkenup.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkenup.ContractActivity;
import com.example.linkenup.R;
import com.example.linkenup.ScreenActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Software;
import com.google.android.material.tabs.TabLayout;

public class OpenSoftwareActivity extends AppCompatActivity {

    public static final String EXTRA_SOFTWARE_ID = "LinkenUp.OpenSoftware.EXTRA_SOFTWARE_ID";

    TextView idText, nameText, descrText, supportsText;
    TabLayout tabLayout;
    TabLayout.Tab screenTab,softwareTab;
    Software software;

    View changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensoftware);
        Bundle extras = getIntent().getExtras();

        extras = getIntent().getExtras();
        if(!(extras.getInt(EXTRA_SOFTWARE_ID,0)>0))
        {
            onBackPressed();
            Toast.makeText(this,getString(R.string.no_softwareextra_message),Toast.LENGTH_SHORT).show();
            return;
        }

        idText = (TextView) findViewById(R.id.opensoftware_text_id);
        nameText = (TextView) findViewById(R.id.opensoftware_text_name);
        descrText = (TextView) findViewById(R.id.opensoftware_text_description);
        supportsText = (TextView) findViewById(R.id.opensoftware_text_supports);

        changeButton=findViewById(R.id.opensoftware_button_change);

        tabLayout = (TabLayout) findViewById(R.id.newsoftware_tablayout);
        screenTab = tabLayout.getTabAt(1);
        softwareTab = tabLayout.getTabAt(0);


        DatabaseHelper db = new DatabaseHelper(this);
        software = db.getSoftware(extras.getInt(EXTRA_SOFTWARE_ID));

        idText.setText(getString(R.string.id)+": "+String.valueOf(software.id));
        nameText.setText(software.name);
        descrText.setText(getString(R.string.description)+":\n"+software.description);
        supportsText.setText(getString(R.string.supports)+":\n"+software.supports);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                    intent.putExtra(ScreenActivity.EXTRA_MODE,ScreenActivity.OLDSOFTWARE_MODE);
                    intent.putExtra(ScreenActivity.EXTRA_SOFTWARE_ID,software.id);
                    startActivity(intent);
                }
            }
            //Annoying code:
            @Override public void onTabUnselected(TabLayout.Tab tab) { }@Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        changeButton.setOnLongClickListener((View btn)->
        {
            DialogInterface.OnClickListener listener =  (DialogInterface dialog, int which)->{
                if(which==DialogInterface.BUTTON_POSITIVE)
                {
                    if(db.removeSoftware(software.id))Toast.makeText(this,R.string.remove_success_message,Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, R.string.remove_failed_message,Toast.LENGTH_SHORT).show();
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(getString(R.string.yes),listener)
                    .setNegativeButton(getString(R.string.no),listener)
                    .setTitle(getString(R.string.remove)+" "+getString(R.string.software)+"?")
                    .setMessage("ID: "+String.valueOf(software.id)+" "+software.name)
                    .show();
            return false;
        });
    }

    @Override
    protected void onResume() {
        softwareTab.select();
        super.onResume();
    }

    public void onUpdate(View view){
        Intent intent = new Intent(this,EditSoftwareActivity.class);
        intent.putExtra(EditSoftwareActivity.EXTRA_SOFTWARE_ID,software.id);
        startActivity(intent);
    }

    public void onContracts(View view){
        Intent intent = new Intent(this, ContractActivity.class);
        intent.putExtra(ContractActivity.EXTRA_SEARCH_VALUE,String.valueOf(software.id));
        intent.putExtra(ContractActivity.EXTRA_SEARCH_ROW, Contract.FK_SOFTWARE);
        startActivity(intent);
    }

}