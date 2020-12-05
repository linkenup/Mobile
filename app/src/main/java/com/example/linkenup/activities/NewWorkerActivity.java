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
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Worker;

public class NewWorkerActivity extends AppCompatActivity {

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

        nameEdit = (EditText) findViewById(R.id.newworker_edit_name);
        rgEdit = (EditText) findViewById(R.id.newworker_edit_rg);
        cpfEdit = (EditText) findViewById(R.id.newworker_edit_cpf);
        ctpsEdit = (EditText) findViewById(R.id.newworker_edit_ctps);
        professionEdit = (EditText) findViewById(R.id.newworker_edit_profession);
        nationalityEdit = (EditText) findViewById(R.id.newworker_edit_nationality);
        addressEdit = (EditText) findViewById(R.id.newworker_edit_address);
        civilStateEdit = (EditText) findViewById(R.id.newworker_edit_civilstate);



    }

    public void onRegister(View view){

        String
                name = nameEdit.getText().toString(),
                rg = rgEdit.getText().toString(),
                cpf = cpfEdit.getText().toString(),
                ctps = ctpsEdit.getText().toString(),
                profession = professionEdit.getText().toString(),
                nationality = nationalityEdit.getText().toString(),
                address = addressEdit.getText().toString(),
                civilState = civilStateEdit.getText().toString();

        /*if(name.length() < 2 || cnpj.length()!=14)
        {
            return;
        }*/

        DatabaseHelper db = new DatabaseHelper(this);

        if(db.findWorker(cpf,Worker.CPF)!=null)
        {
            Toast.makeText(this,getString(R.string.insert_cpf_notunique_message),Toast.LENGTH_LONG).show();
            return;
        }

        Worker worker = new Worker(name,rg,cpf,ctps,profession,nationality,address,civilState);

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
}
