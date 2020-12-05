package com.example.linkenup.activities;

import android.app.Activity;
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
import com.example.linkenup.system.Director;

public class EditDirectorActivity extends AppCompatActivity {

    public static final String
                                EXTRA_DIRECTOR_ID = "LinkenUp.EditDirector.EXTRA_DIRECTOR_ID",
                                EXTRA_DIRECTOR_NOT_INSERTED = "LinkenUp.EditDirector.EXTRA_DIRECTOR_NOT_INSERTED",
                                EXTRA_MODE = "LinkenUp.EditDirector.EXTRA_MODE";

    DatabaseHelper db;


    Director director;
    EditText nameEdit, rgEdit, cpfEdit,professionEdit,addressEdit,nationalityEdit,civilStateEdit;
    boolean backSafe;
    int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editdirector);

        Bundle extras = getIntent().getExtras();

        if(extras==null){
            Toast.makeText(this,getString(R.string.no_directorextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        mode = extras.getInt(EXTRA_MODE,0);
        if(mode == OpenDirectorActivity.OLDDIRECTOR_MODE) {


            int id = extras.getInt(EXTRA_DIRECTOR_ID, 0);
            if (!(id > 0) && extras.getSerializable(EXTRA_DIRECTOR_NOT_INSERTED) != null) {
                Toast.makeText(this, getString(R.string.no_directorextra_message), Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }

            DatabaseHelper db = new DatabaseHelper(this);

            director = db.getDirector(id);

        }
        else if (mode == OpenDirectorActivity.NEWDIRECTOR_MODE)
        {
            director = (Director) extras.getSerializable(EXTRA_DIRECTOR_NOT_INSERTED);
            if(director==null){
                Toast.makeText(this,getString(R.string.no_directorextra_message),Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }
        }
        else{
            Toast.makeText(this, getString(R.string.no_directorextra_message), Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }




        nameEdit = (EditText) findViewById(R.id.newdirector_edit_name);
        rgEdit = (EditText) findViewById(R.id.newdirector_edit_rg);
        cpfEdit = (EditText) findViewById(R.id.newdirector_edit_cpf);
        professionEdit = (EditText) findViewById(R.id.newdirector_edit_profession);
        addressEdit = (EditText) findViewById(R.id.newdirector_edit_address);
        nationalityEdit = (EditText) findViewById(R.id.newdirector_edit_nationality);
        civilStateEdit = (EditText) findViewById(R.id.newdirector_edit_civilstate);

        nameEdit.setText(director.name);
        cpfEdit.setText(director.cpf);
        rgEdit.setText(director.rg);
        addressEdit.setText(director.address);
        professionEdit.setText(director.profession);
        nationalityEdit.setText(director.nationality);
        civilStateEdit.setText(director.civilState);


    }

    public void onRegister(View view){

        String
                name = nameEdit.getText().toString(),
                rg = rgEdit.getText().toString(),
                cpf = cpfEdit.getText().toString(),
                profession = professionEdit.getText().toString(),
                address = addressEdit.getText().toString(),
                nationality = nationalityEdit.getText().toString(),
                civilState = civilStateEdit.getText().toString();

        if(name.length() < 2)
        {
            return;
        }

        if(db.findDirector(cpf,Director.CPF)!=null)
        {
            Toast.makeText(this,getString(R.string.insert_cpf_notunique_message),Toast.LENGTH_LONG).show();
            return;
        }

        if(mode == OpenDirectorActivity.OLDDIRECTOR_MODE) {
            director = new Director(director.id, director.fkClient, name, rg, cpf, profession, address, nationality, civilState);
            if(!(db.updateDirector(director)>0)){
                Toast.makeText(this,R.string.update_failed_message,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,R.string.update_success_message,Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(this, HomeActivity.class));
                }, 500);
            }

        }
        else if (mode == OpenDirectorActivity.NEWDIRECTOR_MODE) {
            director = new Director(director.fkClient, name, rg, cpf, profession, address, nationality, civilState);
            resultDirector(director);
        }

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





}
