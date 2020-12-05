package com.example.linkenup.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;

public class EditClientActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENT_ID = "LinkenUp.EditClient.EXTRA_CLIET_ID";

    EditText nameEdit, cnpjEdit, ceEdit, addressEdit;
    Client client;
    boolean backSafe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editclient);

        Bundle extras = getIntent().getExtras();

        if(!(extras.getInt(EXTRA_CLIENT_ID,0)>0))
        {
            Toast.makeText(this,getString(R.string.no_clientextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        nameEdit = (EditText) findViewById(R.id.newclient_edit_name);
        cnpjEdit = (EditText) findViewById(R.id.newclient_edit_cnpj);
        ceEdit = (EditText) findViewById(R.id.newclient_edit_ce);
        addressEdit = (EditText) findViewById(R.id.newclient_edit_address);

        DatabaseHelper db = new DatabaseHelper(this);
        client = db.getClient(extras.getInt(EXTRA_CLIENT_ID));

        nameEdit.setText(client.name);
        cnpjEdit.setText(client.cnpj);
        ceEdit.setText(client.ce);
        addressEdit.setText(client.address);

        registerSafe = false;
    }

    private boolean registerSafe;
    public void onRegister(View view){
        if(registerSafe)return;
        registerSafe = true;

        if(!validate())return;

        DatabaseHelper db = new DatabaseHelper(this);

        if(!(db.updateClient(client)>0)){
            Toast.makeText(this,R.string.update_failed_message,Toast.LENGTH_SHORT).show();
            registerSafe = false;
        }
        else {
            Toast.makeText(this,R.string.update_success_message,Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this,HomeActivity.class));
            }, 500);
            registerSafe = false;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("name",nameEdit.getText().toString());
        outState.putString("cnpj",cnpjEdit.getText().toString());
        outState.putString("ce",ceEdit.getText().toString());
        outState.putString("address",addressEdit.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameEdit.setText(savedInstanceState.getString("name"));
        cnpjEdit.setText(savedInstanceState.getString("cnpj"));
        ceEdit.setText(savedInstanceState.getString("ce"));
        addressEdit.setText(savedInstanceState.getString("address"));
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

    public boolean validate(){

        String
                name = nameEdit.getText().toString(),
                cnpj = cnpjEdit.getText().toString(),
                ce = ceEdit.getText().toString(),
                addressString = addressEdit.getText().toString();

        if(name.length() < 2 || cnpj.length()!=14)
        {
            return false;
        }

        DatabaseHelper db = new DatabaseHelper(this);

        Address address;
        try {
            address = new Geocoder(this).getFromLocationName(addressString,1).get(0);
            if(address==null)throw new Exception();
            else {
                if(address.getAdminArea()==null || address.getPostalCode()==null)throw new Exception();
                client = new Client(client.id,name,cnpj,ce,addressString);
                return true;
            }
        }
        catch (Exception e) {
            Toast.makeText(this,R.string.invalid_clientaddress_message, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

