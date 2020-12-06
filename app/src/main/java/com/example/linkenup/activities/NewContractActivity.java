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

public class NewContractActivity extends AppCompatActivity {

    private static final int
                            REQUEST_CLIENT = 6,
                            REQUEST_SOFTWARE = 7,
                            REQUEST_DIRECTOR = 8,
                            REQUEST_WORKER_DIRECTOR = 9,
                            REQUEST_WORKER_CONSULTANT = 10;

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

        db = new DatabaseHelper(this);

        contract = new Contract();
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

        String stringArray[] = new String[24];

            for(int i = 0;i<stringArray.length; i++){
                stringArray[i] = String.valueOf(i)+":00";
            }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,stringArray);
        beginHourSpinner.setAdapter(adapter);
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

        clientButton.setOnClickListener(this::requestClient);
        clientButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(client != null){
                    Intent intent = new Intent(getApplicationContext(),OpenClientActivity.class);
                    intent.putExtra(OpenClientActivity.EXTRA_CLIENT_ID,client.id);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        clientEdit.setOnFocusChangeListener((View view, boolean bool)->{
            if(!bool && client != null)clientEdit.setText(client.name);
        });

        softwareButton.setOnClickListener(this::requestSoftware);
        softwareButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(software != null){
                    Intent intent = new Intent(getApplicationContext(),OpenSoftwareActivity.class);
                    intent.putExtra(OpenSoftwareActivity.EXTRA_SOFTWARE_ID,software.id);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        softwareEdit.setOnFocusChangeListener((View view, boolean bool)->{
            if(!bool && software != null)softwareEdit.setText(software.name);
        });

        directorButton.setOnClickListener(this::requestDirector);
        directorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OpenDirectorActivity.class);
                if(director!= null){
                    if(director.id!=null){
                    intent.putExtra(OpenDirectorActivity.EXTRA_DIRECTOR_ID,director.id);
                    startActivity(intent);
                    return true;
                    }
                    else
                    {
                        intent.putExtra(OpenDirectorActivity.EXTRA_DIRECTOR_NOT_INSERTED,director);
                        startActivityForResult(intent,REQUEST_DIRECTOR);
                        return true;
                    }
                }
                return false;
            }
        });
        directorEdit.setOnFocusChangeListener((View view, boolean bool)->{
            if(!bool && director != null)directorEdit.setText(director.name);
        });

        workerDirectorButton.setOnClickListener(this::requestWorkerDirector);
        workerDirectorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OpenWorkerActivity.class);
                if(workerDirector!= null)if(workerDirector.id!=null){
                    intent.putExtra(OpenWorkerActivity.EXTRA_WORKER_ID,workerDirector.id);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        workerDirectorEdit.setOnFocusChangeListener((View view, boolean bool)->{
            if(!bool && workerDirector != null)workerDirectorEdit.setText(workerDirector.name);
        });

        workerConsultantButton.setOnClickListener(this::requestWorkerConsultant);
        workerConsultantButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OpenWorkerActivity.class);
                if(workerConsultant!= null)if(workerConsultant.id!=null){
                    intent.putExtra(OpenWorkerActivity.EXTRA_WORKER_ID,workerConsultant.id);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        workerConsultantEdit.setOnFocusChangeListener((View view, boolean bool)->{
            if(!bool && workerConsultant != null)workerConsultantEdit.setText(workerConsultant.name);
        });
    }

    public void onRegister(View view){
        String
                monthValue = monthValueEdit.getText().toString(),
                bank = bankEdit.getText().toString(),
                agency = agencyEdit.getText().toString(),
                account = accountEdit.getText().toString();

        if(client == null || software == null || director == null || workerDirector == null || workerConsultant == null || contract.daysConsultant == null || contract.hoursConsultant == null || contract.beginHour == null || contract.endHour == null || !(monthValue.length()>0) || !(agency.length()>0) || !(bank.length()>0) || !(account.length()>0))
        {
            Toast.makeText(this,R.string.insert_all_message,Toast.LENGTH_SHORT).show();
            return;
        }

        int monthValueInt;
        try{
            monthValueInt = Integer.parseInt(monthValue);
        }
        catch (Exception e){
            return;
        }


                db = new DatabaseHelper(this);

        if(director.id == null) director.id = db.insertDirector(director);
        else if(director.id>0)director = db.getDirector(director.id);


        Contract contractResult = new Contract(
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

        int id = db.insertContract(contractResult);
        Toast.makeText(this,
                getString(R.string.contract_insert_message)
                        .replace("%id",String.valueOf(id))
                        .replace("%client",client.name)
                        .replace("%software",software.name),
                Toast.LENGTH_LONG).show();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        },1000);
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

    public void requestClient(View view){
        String clientText = clientEdit.getText().toString();
        if(clientText.length() > 0 && !clientText.equals(client!=null?client.name:"")){
            try{
                int id = Integer.parseInt(clientText);
                director = null;
                directorEdit.setText("");
                client = db.getClient(id);
                if(client == null)throw new Exception();
                else {
                    clientEdit.setText(client.name);
                    Toast.makeText(this,R.string.contract_linked_client,Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                clientEdit.setText("");
                Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        director = null;
        directorEdit.setText("");
        startActivityForResult(new Intent(this, ClientActivity.class),REQUEST_CLIENT);
    }

    public void requestSoftware(View view){
        String softwareText = softwareEdit.getText().toString();
        if(softwareText.length() > 0 && !softwareText.equals(software!=null?software.name:"")){
            try{
                int id = Integer.parseInt(softwareText);
                software = db.getSoftware(id);
                if(software == null)throw new Exception();
                else {
                    softwareEdit.setText(software.name);
                    Toast.makeText(this,R.string.contract_linked_software,Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                softwareEdit.setText("");
                Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        startActivityForResult(new Intent(this, SoftwareActivity.class),REQUEST_SOFTWARE);
    }

    public void requestDirector(View view){
        if(client==null){
            Toast.makeText(this,R.string.contract_director_nullclient_message,Toast.LENGTH_LONG).show();
            return;
        }

        String directorText = directorEdit.getText().toString();
        if(directorText.length() > 0 && !directorText.equals(director!=null?director.name:"")){
            try{
                int id = Integer.parseInt(directorText);
                director = db.getDirector(id);
                if(director == null)throw new Exception();
                else if(director.fkClient != client.id)throw new Exception();
                else {
                    directorEdit.setText(director.name);
                    Toast.makeText(this,R.string.contract_linked_director,Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                directorEdit.setText("");
                Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        Intent intent = new Intent(this, DirectorActivity.class);
        intent.putExtra(DirectorActivity.EXTRA_CLIENT_ID,client.id);
        startActivityForResult(intent,REQUEST_DIRECTOR);
    }

    public void requestWorkerDirector(View view){
        String workerText = workerDirectorEdit.getText().toString();
        if(workerText.length() > 0 && !workerText.equals(workerDirector!=null?workerDirector.name:"")){
            try{
                int id = Integer.parseInt(workerText);
                workerDirector = db.getWorker(id);
                if(workerDirector == null)throw new Exception();
                else {
                    workerDirectorEdit.setText(workerDirector.name);
                    Toast.makeText(this,R.string.contract_linked_director,Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                workerDirectorEdit.setText("");
                Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        startActivityForResult(new Intent(this, WorkerActivity.class),REQUEST_WORKER_DIRECTOR);
    }

    public void requestWorkerConsultant(View view){
        String workerText = workerConsultantEdit.getText().toString();
        if(workerText.length() > 0 && !workerText.equals(workerConsultant!=null?workerConsultant.name:"")){
            try{
                int id = Integer.parseInt(workerText);
                workerConsultant = db.getWorker(id);
                if(workerConsultant == null)throw new Exception();
                else {
                    workerConsultantEdit.setText(workerConsultant.name);
                    Toast.makeText(this,R.string.contract_linked_director,Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                workerConsultantEdit.setText("");
                Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        startActivityForResult(new Intent(this,WorkerActivity.class),REQUEST_WORKER_CONSULTANT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CLIENT && resultCode == Activity.RESULT_OK)
        {
            int id =data.getExtras().getInt("clientID",0);
            client = db.getClient(id);

            if(client==null)Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            else{
                clientEdit.setText(client.name);
                Toast.makeText(this,getString(R.string.contract_linked_client)+"\nID: "+String.valueOf(client.id),Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==REQUEST_SOFTWARE && resultCode == Activity.RESULT_OK)
        {
            int id =data.getExtras().getInt("softwareID",0);
            software = db.getSoftware(id);

            if(software==null)Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
            else{
                softwareEdit.setText(software.name);
                Toast.makeText(this,getString(R.string.contract_linked_software)+"\nID: "+String.valueOf(software.id),Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==REQUEST_DIRECTOR && resultCode == Activity.RESULT_OK)
        {
            director = (Director) data.getExtras().getSerializable("director");

            if(director!=null){
                if(director.fkClient.equals(client.id)) {
                    directorEdit.setText(director.name);
                    Toast.makeText(this, getString(R.string.contract_linked_director) + "\nID: " + String.valueOf(director.id), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
        }

        if(requestCode==REQUEST_WORKER_DIRECTOR && resultCode == Activity.RESULT_OK)
        {
            workerDirector = db.getWorker(data.getExtras().getInt("workerID"));

            if(workerDirector!=null){
                workerDirectorEdit.setText(workerDirector.name);
                Toast.makeText(this, getString(R.string.contract_linked_director) + "\nID: " + String.valueOf(workerDirector.id), Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
        }

        if(requestCode==REQUEST_WORKER_CONSULTANT && resultCode == Activity.RESULT_OK)
        {
            workerConsultant = db.getWorker(data.getExtras().getInt("workerID"));

            if(workerConsultant!=null){
                workerConsultantEdit.setText(workerConsultant.name);
                Toast.makeText(this, getString(R.string.contract_linked_consultant) + "\nID: " + String.valueOf(workerConsultant.id), Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
        }
    }
}
