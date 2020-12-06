package com.example.linkenup.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkenup.ContractActivity;
import com.example.linkenup.DirectorActivity;
import com.example.linkenup.HomeActivity;
import com.example.linkenup.R;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Contract;

import java.io.IOException;

public class OpenClientActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENT_ID = "LinkenUp.OpenClient.EXTRA_CLIENT_ID";

    TextView idText, nameText, cnpjText, ceText, addressText;
    Client client;
    Address clientAddress;
    View changeButton;

    boolean excluded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openclient);
        Bundle extras = getIntent().getExtras();

        extras = getIntent().getExtras();
        if(!(extras.getInt(EXTRA_CLIENT_ID,0)>0))
        {
            Toast.makeText(this,getString(R.string.no_clientextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        excluded = false;

        changeButton = (View) findViewById(R.id.openclient_button_change);
        idText = (TextView) findViewById(R.id.openclient_text_id);
        nameText = (TextView) findViewById(R.id.openclient_text_name);
        cnpjText = (TextView) findViewById(R.id.openclient_text_cnpj);
        ceText = (TextView) findViewById(R.id.openclient_text_ce);
        addressText = (TextView) findViewById(R.id.openclient_text_address);

        DatabaseHelper db = new DatabaseHelper(this);
        client = db.getClient(extras.getInt(EXTRA_CLIENT_ID));

        idText.setText(getString(R.string.id)+": "+String.valueOf(client.id));
        nameText.setText(client.name);
        cnpjText.setText(getString(R.string.cnpj)+": "+client.cnpj);
        ceText.setText(getString(R.string.ce)+":\n"+client.ce);
        addressText.setText(client.address);

        changeButton.setOnLongClickListener((View btn)->
        {
            DialogInterface.OnClickListener listener =  (DialogInterface dialog, int which)->{
                if(which==DialogInterface.BUTTON_POSITIVE)
                {
                    if(db.removeClient(client.id)){
                        Toast.makeText(this,R.string.remove_success_message,Toast.LENGTH_SHORT).show();
                        excluded = true;
                    }
                    else Toast.makeText(this, R.string.remove_failed_message,Toast.LENGTH_SHORT).show();
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(getString(R.string.yes),listener)
                    .setNegativeButton(getString(R.string.no),listener)
                    .setMessage(getString(R.string.remove)+" "+getString(R.string.client)+"?\nID: "+String.valueOf(client.id)+"\n"+client.name)
            .show();
            return false;
        });

        new Handler().post(()->{
            Address address;
            try {
                address = new Geocoder(this).getFromLocationName(client.address,1).get(0);
                if(address==null)throw new IOException();
                clientAddress = address;
            }
            catch (Exception e) {
                Toast.makeText(this,R.string.invalid_clientaddress_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDirectors(View view){
        Intent intent = new Intent(this, DirectorActivity.class);
        intent.putExtra(DirectorActivity.EXTRA_CLIENT_ID,client.id);
        startActivity(intent);
    }

    public void onUpdate(View view){
        Intent intent = new Intent(this,EditClientActivity.class);
        intent.putExtra(EditClientActivity.EXTRA_CLIENT_ID,client.id);
        startActivity(intent);
    }

    public void onMap(View view) throws IOException {
        Uri location = Uri.parse("geo:" + clientAddress.getLatitude() + "," + clientAddress.getLongitude() + "?q=" + clientAddress.getAddressLine(0).replaceAll(" ", "+"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void onContracts(View view){
        Intent intent = new Intent(this, ContractActivity.class);
        intent.putExtra(ContractActivity.EXTRA_SEARCH_VALUE,String.valueOf(client.id));
        intent.putExtra(ContractActivity.EXTRA_SEARCH_ROW, Contract.FK_CLIENT);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(excluded) startActivity(new Intent(this, HomeActivity.class));
        else super.onBackPressed();
    }
}