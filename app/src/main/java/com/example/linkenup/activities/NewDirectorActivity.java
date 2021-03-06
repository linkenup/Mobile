package com.example.linkenup.activities;

import android.app.Activity;
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

import com.example.linkenup.R;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.code.TextMask;
import com.example.linkenup.system.Director;

public class NewDirectorActivity extends AppCompatActivity {

    public static String EXTRA_CLIENT_ID = "LinkenUp.NewDriector.EXTRA_CLIENT_ID";

    DatabaseHelper db;

    Director director;

    EditText nameEdit, rgEdit, cpfEdit,professionEdit,addressEdit,nationalityEdit,civilStateEdit;
    boolean backSafe, resulting;
    int clientID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editdirector);

        Bundle extras = getIntent().getExtras();

        if(extras==null){
            Toast.makeText(this,getString(R.string.no_clientextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        clientID = extras.getInt(EXTRA_CLIENT_ID,0);
        if(!(clientID >0)){
            Toast.makeText(this,getString(R.string.no_clientextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        resulting = getCallingActivity()!=null;

        db = new DatabaseHelper(this);

        nameEdit = (EditText) findViewById(R.id.newdirector_edit_name);
        rgEdit = (EditText) findViewById(R.id.newdirector_edit_rg);
        cpfEdit = (EditText) findViewById(R.id.newdirector_edit_cpf);
        professionEdit = (EditText) findViewById(R.id.newdirector_edit_profession);
        addressEdit = (EditText) findViewById(R.id.newdirector_edit_address);
        nationalityEdit = (EditText) findViewById(R.id.newdirector_edit_nationality);
        civilStateEdit = (EditText) findViewById(R.id.newdirector_edit_civilstate);

        rgEdit.addTextChangedListener(TextMask.watch(rgEdit,TextMask.FORMAT_RG));
        cpfEdit.addTextChangedListener(TextMask.watch(cpfEdit,TextMask.FORMAT_CPF));
    }

    public void onRegister(View view){

        if(!validate())return;

        if(resulting)resultDirector(director);
        /*else {
            int id = db.insertDirector(director);
            Toast.makeText(this,
                    getString(R.string.director_insert_message)
                            .replace("%id", String.valueOf(id))
                            .replace("%name", director.name)
                            .replace("%cpf", director.cpf),
                    Toast.LENGTH_LONG).show();

            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }, 1000);
        }*/

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("name",nameEdit.getText().toString());
        outState.putString("cpf",cpfEdit.getText().toString());
        outState.putString("rg",rgEdit.getText().toString());
        outState.putString("profession",addressEdit.getText().toString());
        outState.putString("nationality",nationalityEdit.getText().toString());
        outState.putString("civilState",civilStateEdit.getText().toString());
        outState.putString("address",addressEdit.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameEdit.setText(savedInstanceState.getString("name"));
        cpfEdit.setText(savedInstanceState.getString("cpf"));
        rgEdit.setText(savedInstanceState.getString("rg"));
        professionEdit.setText(savedInstanceState.getString("profession"));
        nationalityEdit.setText(savedInstanceState.getString("nationality"));
        civilStateEdit.setText(savedInstanceState.getString("civilState"));
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

    public void resultDirector(Director director){
        Intent data = new Intent();
        data.putExtra("director",director);
        this.setResult(Activity.RESULT_OK,data);
        finish();
    }

    public boolean validate(){
        String
                name = nameEdit.getText().toString(),
                rg = rgEdit.getText().toString(),
                cpf = cpfEdit.getText().toString(),
                profession = professionEdit.getText().toString(),
                address = addressEdit.getText().toString(),
                nationality = nationalityEdit.getText().toString(),
                civilState = civilStateEdit.getText().toString();

        if(name.length() < 2 || rg.length()!=TextMask.FORMAT_RG.length() || cpf.length() != TextMask.FORMAT_CPF.length()||nationality.length()<2||civilState.length()<2)
        {
            Toast.makeText(this,R.string.insert_all_message,Toast.LENGTH_SHORT).show();
            return false;
        }

        if(db.findDirector(cpf,Director.CPF)!=null)
        {
            Toast.makeText(this,getString(R.string.insert_cpf_notunique_message),Toast.LENGTH_LONG).show();
            return false;
        }

        Address realAddress;
        try {
            realAddress = new Geocoder(this).getFromLocationName(address,1).get(0);
            if(address==null)throw new Exception();
            else if(realAddress.getPostalCode()==null)throw new Exception();
        }
        catch (Exception e) {
            Toast.makeText(this,R.string.invalid_directoraddress_message, Toast.LENGTH_LONG).show();
            return false;
        }

        director = new Director(clientID,name,rg,cpf,profession,address,nationality,civilState);

        return true;
    }
}
