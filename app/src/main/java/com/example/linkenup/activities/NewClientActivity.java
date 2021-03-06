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
import com.example.linkenup.code.TextMask;
import com.example.linkenup.system.Client;

public class NewClientActivity extends AppCompatActivity {

    EditText nameEdit, cnpjEdit, ceEdit, addressEdit;
    Client client;
    boolean backSafe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editclient);

        nameEdit = (EditText) findViewById(R.id.newclient_edit_name);
        cnpjEdit = (EditText) findViewById(R.id.newclient_edit_cnpj);
        ceEdit = (EditText) findViewById(R.id.newclient_edit_ce);
        addressEdit = (EditText) findViewById(R.id.newclient_edit_address);

        cnpjEdit.addTextChangedListener(TextMask.watch(cnpjEdit,TextMask.FORMAT_CNPJ));
        ceEdit.addTextChangedListener(TextMask.watch(ceEdit,TextMask.FORMAT_CE));
    }

    public void onRegister(View view){

        if(!validate())return;

        DatabaseHelper db = new DatabaseHelper(this);

        int id = db.insertClient(client);
        Toast.makeText(this,
                getString(R.string.client_insert_message)
                        .replace("%id",String.valueOf(id))
                        .replace("%name",client.name)
                        .replace("%cnpj",client.cnpj),
                Toast.LENGTH_LONG).show();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        },1000);
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

        if(name.length() < 2 || cnpj.length()!= TextMask.FORMAT_CNPJ.length() || ce.length()!=TextMask.FORMAT_CE.length())
        {
            Toast.makeText(this,R.string.insert_all_message,Toast.LENGTH_SHORT).show();
            return false;
        }

        DatabaseHelper db = new DatabaseHelper(this);

        if(db.findClient(cnpj,Client.CNPJ)!=null)
        {
            Toast.makeText(this,getString(R.string.clientinsert_cnpj_notunique_message),Toast.LENGTH_LONG).show();
            return false;
        }

            Address address;
            try {
                address = new Geocoder(this).getFromLocationName(addressString,1).get(0);
                if(address==null)throw new Exception();
                else {
                    if(address.getAdminArea()==null || address.getPostalCode()==null)throw new Exception();
                    client = new Client(name,cnpj,ce,addressString);
                    return true;
                }
            }
            catch (Exception e) {
                Toast.makeText(this,R.string.invalid_clientaddress_message, Toast.LENGTH_LONG).show();
            return false;
            }
    }
}
