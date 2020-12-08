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
import com.example.linkenup.system.Worker;

public class NewWorkerActivity extends AppCompatActivity {

    DatabaseHelper db;

    Worker worker;

    EditText
            nameEdit,
            rgEdit,
            cpfEdit,
            ctpsEdit,
            professionEdit,
            nationalityEdit,
            addressEdit,
            civilStateEdit;

    boolean backSafe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editworker);

        db = new DatabaseHelper(this);

        nameEdit = (EditText) findViewById(R.id.newworker_edit_name);
        rgEdit = (EditText) findViewById(R.id.newworker_edit_rg);
        cpfEdit = (EditText) findViewById(R.id.newworker_edit_cpf);
        ctpsEdit = (EditText) findViewById(R.id.newworker_edit_ctps);
        professionEdit = (EditText) findViewById(R.id.newworker_edit_profession);
        nationalityEdit = (EditText) findViewById(R.id.newworker_edit_nationality);
        addressEdit = (EditText) findViewById(R.id.newworker_edit_address);
        civilStateEdit = (EditText) findViewById(R.id.newworker_edit_civilstate);

        rgEdit.addTextChangedListener(TextMask.watch(rgEdit,TextMask.FORMAT_RG));
        cpfEdit.addTextChangedListener(TextMask.watch(cpfEdit,TextMask.FORMAT_CPF));
        ctpsEdit.addTextChangedListener(TextMask.watch(ctpsEdit,TextMask.FORMAT_CTPS));

    }

    public void onRegister(View view){

        if(!validate())return;


        int id = db.insertWorker(worker);
        Toast.makeText(this,
                getString(R.string.worker_insert_message)
                        .replace("%id",String.valueOf(id))
                        .replace("%name",worker.name)
                        .replace("%cpf",worker.cpf),
                Toast.LENGTH_LONG).show();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        },1000);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("name",nameEdit.getText().toString());
        outState.putString("rg",rgEdit.getText().toString());
        outState.putString("cpf",cpfEdit.getText().toString());
        outState.putString("ctps",ctpsEdit.getText().toString());
        outState.putString("profession",professionEdit.getText().toString());
        outState.putString("nationality",nationalityEdit.getText().toString());
        outState.putString("address",addressEdit.getText().toString());
        outState.putString("civilState",civilStateEdit.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameEdit.setText(savedInstanceState.getString("name"));
        cpfEdit.setText(savedInstanceState.getString("cpf"));
        rgEdit.setText(savedInstanceState.getString("rg"));
        ctpsEdit.setText(savedInstanceState.getString("ctps"));
        nationalityEdit.setText(savedInstanceState.getString("nationality"));
        addressEdit.setText(savedInstanceState.getString("address"));
        civilStateEdit.setText(savedInstanceState.getString("civilState"));
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
                rg = rgEdit.getText().toString(),
                cpf = cpfEdit.getText().toString(),
                ctps = ctpsEdit.getText().toString(),
                profession = professionEdit.getText().toString(),
                nationality = nationalityEdit.getText().toString(),
                address = addressEdit.getText().toString(),
                civilState = civilStateEdit.getText().toString();

        if(name.length() < 2 || rg.length()!=TextMask.FORMAT_RG.length()||cpf.length()!=TextMask.FORMAT_CPF.length()||ctps.length()!=TextMask.FORMAT_CTPS.length())
        {
            Toast.makeText(this,R.string.insert_all_message,Toast.LENGTH_SHORT).show();
            return false;
        }

        if(db.findWorker(cpf,Worker.CPF)!=null&&cpf!=worker.cpf)
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
            Toast.makeText(this,R.string.invalid_workeraddress_message, Toast.LENGTH_LONG).show();
            return false;
        }

        worker = new Worker(name,rg,cpf,ctps,profession,nationality,address,civilState);

        return  true;
    }

}
