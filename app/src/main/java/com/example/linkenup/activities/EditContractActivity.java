package com.example.linkenup.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linkenup.ClientActivity;
import com.example.linkenup.DirectorActivity;
import com.example.linkenup.HomeActivity;
import com.example.linkenup.R;
import com.example.linkenup.SoftwareActivity;
import com.example.linkenup.WorkerActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Director;
import com.example.linkenup.system.Software;
import com.example.linkenup.system.Worker;

public class EditContractActivity extends AppCompatActivity {

    public static final String EXTRA_CONTRACT_ID = "LinkenUp.EditContract.EXTRA_CLIENT_ID";

    DatabaseHelper db;
    Contract contract;
    Client client;
    Software software;
    Director director;
    Worker workerDirector, workerConsultant;

    Spinner daysSpinner,hoursSpinner,beginHourSpinner,endHourSpinner;
    EditText clientEdit,softwareEdit, directorEdit, workerDirectorEdit, workerConsultantEdit, monthValueEdit, bankEdit, agencyEdit, accountEdit;
    View clientButton, softwareButton, directorButton, workerDirectorButton, workerConsultantButton;

    boolean backSafe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editcontract);

        ((TextView)findViewById(R.id.newcontract_title)).setText(R.string.edit_contract);


        Bundle extras = getIntent().getExtras();

        if(extras == null){
            Toast.makeText(this,R.string.no_contractextra_message,Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        if(!(extras.getInt(EXTRA_CONTRACT_ID,0)>0)){
            Toast.makeText(this,R.string.no_contractextra_message,Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        daysSpinner = (Spinner) findViewById(R.id.newcontract_spinner_days);
        hoursSpinner = (Spinner) findViewById(R.id.newcontract_spinner_hours);
        beginHourSpinner = (Spinner) findViewById(R.id.newcontract_spinner_beginHour);
        endHourSpinner = (Spinner) findViewById(R.id.newcontract_spinner_endHour);

        clientEdit = (EditText) findViewById(R.id.newcontract_edit_client);
        softwareEdit = (EditText) findViewById(R.id.newcontract_edit_software);
        directorEdit = (EditText) findViewById(R.id.newcontract_edit_director);
        workerDirectorEdit = (EditText) findViewById(R.id.newcontract_edit_workerDirector);
        workerConsultantEdit = (EditText) findViewById(R.id.newcontract_edit_workerConsultant);
        monthValueEdit = (EditText) findViewById(R.id.newcontract_edit_monthValue);
        bankEdit = (EditText) findViewById(R.id.newcontract_edit_bank);
        agencyEdit = (EditText) findViewById(R.id.newcontract_edit_agency);
        accountEdit = (EditText) findViewById(R.id.newcontract_edit_account);

        clientButton = findViewById(R.id.newcontract_floatbutton_client);
        softwareButton = findViewById(R.id.newcontract_floatbutton_software);
        directorButton = findViewById(R.id.newcontract_floatbutton_director);
        workerDirectorButton = findViewById(R.id.newcontract_floatbutton_workerDirector);
        workerConsultantButton = findViewById(R.id.newcontract_floatbutton_workerConsultant);

        db = new DatabaseHelper(this);

        contract =  db.getContract(extras.getInt(EXTRA_CONTRACT_ID));
        client = db.getClient(contract.fkClient);
        software = db.getSoftware(contract.fkSoftware);
        director = db.getDirector(contract.fkDirector);
        workerDirector = db.getWorker(contract.fkWorkerDirector);
        workerConsultant = db.getWorker(contract.fkWorkerConsultant);


        String stringArray[] = new String[24];

            for(int i = 0;i<stringArray.length; i++){
                stringArray[i] = String.valueOf(i)+":00";
            }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,stringArray);
        beginHourSpinner.setAdapter(adapter);
        beginHourSpinner.setSelection(contract.beginHour);
        beginHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contract.beginHour=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                contract.beginHour=null;
            }
        });
        endHourSpinner.setAdapter(adapter);
        endHourSpinner.setSelection(contract.endHour);
        endHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contract.endHour=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                contract.endHour=null;
            }
        });
        for(int i = 0;i<stringArray.length; i++){
            stringArray[i] = String.valueOf(i+1)+":00";
        }
        hoursSpinner.setAdapter(adapter);
        hoursSpinner.setSelection(contract.hoursConsultant - 1);
        hoursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contract.hoursConsultant=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                contract.hoursConsultant=null;
            }
        });


        stringArray = new String[7];
        for(int i = 0; i < stringArray.length; i++){
            stringArray[i] = String.valueOf(i+1);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,stringArray);
        daysSpinner.setAdapter(adapter);
        daysSpinner.setSelection(contract.daysConsultant-1);
        daysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contract.daysConsultant = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                contract.daysConsultant = null;
            }
        });

        clientEdit.setText(client.name);
        softwareEdit.setText(software.name);
        directorEdit.setText(director.name);
        workerDirectorEdit.setText(workerDirector.name);
        workerConsultantEdit.setText(workerConsultant.name);
        monthValueEdit.setText(String.valueOf(contract.monthValue));
        bankEdit.setText(contract.bank);
        agencyEdit.setText(contract.agency);
        accountEdit.setText(contract.account);


        clientEdit.setEnabled(false);
        softwareEdit.setEnabled(false);
        directorEdit.setEnabled(false);
        workerDirectorEdit.setEnabled(false);
        workerConsultantEdit.setEnabled(false);




    }

    public void onRegister(View view){
        String
                monthValue = monthValueEdit.getText().toString(),
                bank = bankEdit.getText().toString(),
                agency = agencyEdit.getText().toString(),
                account = accountEdit.getText().toString();
        int monthValueInt;
        try{
            monthValueInt = Integer.parseInt(monthValue);
        }
        catch (Exception e){
            return;
        }

        Contract contractResult = new Contract(
                contract.id,
                client.id,
                software.id,
                workerDirector.id,
                workerConsultant.id,
                director.id,
                monthValueInt,
                bank,
                agency,
                account,
                contract.daysConsultant,
                contract.hoursConsultant,
                contract.beginHour,
                contract.endHour
        );

        if(!(db.updateContract(contract)>0)){
            Toast.makeText(this,R.string.update_failed_message,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,R.string.update_success_message,Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this,HomeActivity.class));
            }, 500);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("client",clientEdit.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        clientEdit.setText(savedInstanceState.getString("client"));
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

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }
}
