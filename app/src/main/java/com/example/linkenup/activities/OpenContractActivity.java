package com.example.linkenup.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkenup.HomeActivity;
import com.example.linkenup.PreferenceActivity;
import com.example.linkenup.R;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.code.Us;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Director;
import com.example.linkenup.system.Screen;
import com.example.linkenup.system.Software;
import com.example.linkenup.system.Worker;

import java.io.IOException;
import java.util.ArrayList;

public class OpenContractActivity extends AppCompatActivity {

    public static final String EXTRA_CONTRACT_ID = "LinkenUp.OpenContract.EXTRA_CONTRACT_ID";

    DatabaseHelper db;
    Contract contract;
    Client client;
    Address clientAddress, directorAddress, workerDirectorAddress;
    Software software;
    Screen[] screenList;
    Director director;
    Worker workerDirector, workerConsultant;
    View changeButton;

    TextView titleText,
            clientText,
            softwareText,
            directorText,
            workerDirectorText,
            workerConsultantText,
            monthValueText,
            bankText,
            agencyText,
            accountText,
            daysText,
            hoursText,
            beginHourText,
            endHourText;

    boolean excluded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencontract);

        Bundle extras =  getIntent().getExtras();

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

        excluded = false;

        changeButton = findViewById(R.id.opencontract_button_change);
        titleText = (TextView) findViewById(R.id.contract_title);
        clientText = (TextView) findViewById(R.id.contract_text_client);
        softwareText = (TextView) findViewById(R.id.contract_text_software);
        directorText = (TextView) findViewById(R.id.contract_text_director);
        workerDirectorText = (TextView) findViewById(R.id.contract_text_workerDirector);
        workerConsultantText = (TextView) findViewById(R.id.contract_text_workerConsultant);
        monthValueText = (TextView) findViewById(R.id.contract_text_monthValue);
        bankText = (TextView) findViewById(R.id.contract_text_bank);
        agencyText = (TextView) findViewById(R.id.contract_text_agency);
        accountText = (TextView) findViewById(R.id.contract_text_account);
        hoursText = (TextView) findViewById(R.id.contract_text_hours);
        daysText = (TextView) findViewById(R.id.contract_text_days);
        beginHourText = (TextView) findViewById(R.id.contract_text_beginHour);
        endHourText = (TextView) findViewById(R.id.contract_text_endHour);


        db = new DatabaseHelper(this);
        contract =  db.getContract(extras.getInt(EXTRA_CONTRACT_ID));
        client = db.getClient(contract.fkClient);
        software = db.getSoftware(contract.fkSoftware);
        screenList = db.getAllScreen(contract.fkSoftware);
        director = db.getDirector(contract.fkDirector);
        workerDirector = db.getWorker(contract.fkWorkerDirector);
        workerConsultant = db.getWorker(contract.fkWorkerConsultant);

        titleText.setText("<"+titleText.getText().toString()+">");
        clientText.setText(getString(R.string.client)+":\n  "+client.name);
        softwareText.setText(getString(R.string.software)+":\n  "+software.name);
        directorText.setText(getString(R.string.director)+":\n  "+director.name);
        workerDirectorText.setText(getString(R.string.workerdirector)+":\n  "+workerDirector.name);
        workerConsultantText.setText(getString(R.string.consultant)+":\n  "+workerConsultant.name);
        monthValueText.setText(getString(R.string.monthValue)+":\n  "+String.valueOf(contract.monthValue));
        bankText.setText(getString(R.string.bank)+":\n  "+contract.bank);
        agencyText.setText(getString(R.string.agency)+":\n  "+contract.agency);
        accountText.setText(getString(R.string.account)+":\n  "+contract.account);
        hoursText.setText(hoursText.getText().toString()+" "+contract.hoursConsultant);
        daysText.setText(daysText.getText().toString()+" "+contract.daysConsultant);
        beginHourText.setText(beginHourText.getText().toString()+" "+contract.beginHour);
        endHourText.setText(endHourText.getText().toString()+" "+contract.endHour);

        changeButton.setOnLongClickListener((View btn)->
        {
            DialogInterface.OnClickListener listener =  (DialogInterface dialog, int which)->{
                if(which==DialogInterface.BUTTON_POSITIVE)
                {

                    if(db.countDirectorContracts(contract.fkDirector)==1){

                        AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                        alert2.setTitle(getString(R.string.remove)+" "+getString(R.string.director)+"?")
                                .setMessage(getString(R.string.remove_contract_director).replace("%id",String.valueOf(contract.fkDirector)).replace("%name",db.getDirector(contract.fkDirector).name))
                                .setPositiveButton(R.string.yes,this::doubleRemove)
                                .setNegativeButton(R.string.no,this::doubleRemove)
                                .show();
                        return;
                    }


                    if(db.removeContract(contract.id)){
                        Toast.makeText(this,R.string.remove_success_message,Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(()->{
            			startActivity(new Intent(this,HomeActivity.class));
            		},1000);
                        excluded = true;
                    }
                    else Toast.makeText(this, R.string.remove_failed_message,Toast.LENGTH_SHORT).show();



                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(getString(R.string.yes),listener)
                    .setNegativeButton(getString(R.string.no),listener)
                    .setTitle(getString(R.string.remove)+" "+getString(R.string.contract)+"?")
                    .setMessage("ID: "+String.valueOf(contract.id)+"\n"+String.valueOf(contract.fkClient)+" + "+String.valueOf(contract.fkSoftware))
                    .show();
            return false;
        });

            findViewById(R.id.float_home_button).setVisibility(
                    getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false)?
                            View.VISIBLE:
                            View.GONE);

        
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

            try {
                address = new Geocoder(this).getFromLocationName(workerDirector.address,1).get(0);
                if(address==null)throw new IOException();
                workerDirectorAddress = address;
            }
            catch (Exception e) {
                Toast.makeText(this,R.string.invalid_workerDirectoraddress_message, Toast.LENGTH_LONG).show();
            }

            try {
                address = new Geocoder(this).getFromLocationName(director.address,1).get(0);
                if(address==null)throw new IOException();
                directorAddress = address;
            }
            catch (Exception e) {
                Toast.makeText(this,R.string.invalid_directoraddress_message, Toast.LENGTH_LONG).show();
            }
        });


        if(screenList != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            }
        }
        if(NewContractActivity.REGISTERING){
            changeButton.setVisibility(View.GONE);
        }

    }



    public void onSend(View view){

        Address usAddress;
        try {
            usAddress = Us.getAddress(this);
        } catch (IOException e) {
            Toast.makeText(this, "Endereço de empresa incorreto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},11);
                return;
            }
        }
        DialogInterface.OnClickListener listener = (DialogInterface dialog, int which) -> {

            String screenListString = "";
            for(int i = 0; i < screenList.length; i++){
                screenListString +=" • "+screenList[i].name+"\n";
                screenListString +="    "+getString(R.string.functions)+":\n";
                screenListString +="    "+screenList[i].functions+"\n";
            }

            if(clientAddress==null||directorAddress==null||directorAddress==null||workerDirectorAddress==null)return;

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.contract_template)
                    .replace("%client.name", client.name)
                    .replace("%client.address(city)", clientAddress.getSubAdminArea()!=null?clientAddress.getSubAdminArea():getString(R.string.contract_template_notfound))
                    .replace("%client.address(street)", clientAddress.getThoroughfare()!=null?clientAddress.getThoroughfare():getString(R.string.contract_template_notfound))
                    .replace("%client.address(number)", clientAddress.getSubThoroughfare()!=null?clientAddress.getSubThoroughfare():getString(R.string.contract_template_notfound))
                        .replace("%client.address(neighborhood)",clientAddress.getLocality()!=null?clientAddress.getLocality():clientAddress.getSubLocality() )
                        .replace("%client.address(cep)", clientAddress.getPostalCode()!=null?clientAddress.getPostalCode():getString(R.string.contract_template_notfound))
                        .replace("%client.address(state)", clientAddress.getAdminArea()!=null?clientAddress.getAdminArea():getString(R.string.contract_template_notfound))
                        .replace("%client.cnpj", client.cnpj)
                        .replace("%client.ce", client.ce)

                        .replace("%director.name", director.name)
                        .replace("%director.nationality", director.nationality)
                        .replace("%director.civilState", director.civilState)
                        .replace("%director.rg", director.rg)
                        .replace("%director.cpf", director.cpf)
                        .replace("%director.profession", director.profession)
                        .replace("%director.address(street)", directorAddress.getThoroughfare()!=null?directorAddress.getThoroughfare():getString(R.string.contract_template_notfound))
                        .replace("%director.address(number)", directorAddress.getSubThoroughfare()!=null?directorAddress.getSubThoroughfare():getString(R.string.contract_template_notfound))
                        .replace("%director.address(neighborhood)", directorAddress.getLocality()!=null?directorAddress.getLocality():directorAddress.getSubLocality() )
                        .replace("%director.address(cep)", directorAddress.getPostalCode()!=null?directorAddress.getPostalCode():getString(R.string.contract_template_notfound))
                        .replace("%director.address(city)", directorAddress.getSubAdminArea()!=null?directorAddress.getSubAdminArea():getString(R.string.contract_template_notfound))
                        .replace("%director.address(state)", directorAddress.getAdminArea()!=null?directorAddress.getAdminArea():getString(R.string.contract_template_notfound))

                        .replace("%us.name", getString(R.string.app_name))
                        .replace("%us.address(city)", usAddress.getSubAdminArea())
                        .replace("%us.address(street)", usAddress.getAdminArea())
                        .replace("%us.address(number)", usAddress.getSubThoroughfare())
                        .replace("%us.address(neighborhood)", usAddress.getSubLocality())
                        .replace("%us.address(state)", usAddress.getAdminArea())
                        .replace("%us.address(cep)", usAddress.getPostalCode())
                        .replace("%us.cnpj", Us.CNPJ)
                        .replace("%us.ce", Us.CE)

                        .replace("%workerDirector.name", workerDirector.name)
                        .replace("%workerDirector.nationality", workerDirector.nationality)
                        .replace("%workerDirector.civilState", workerDirector.civilState)
                        .replace("%workerDirector.profession", workerDirector.profession)
                        .replace("%workerDirector.rg", workerDirector.rg)
                        .replace("%workerDirector.cpf", workerDirector.cpf)
                        .replace("%workerDirector.address(street)", workerDirectorAddress.getThoroughfare()!=null?workerDirectorAddress.getThoroughfare():getString(R.string.contract_template_notfound))
                        .replace("%workerDirector.address(neighborhood)", workerDirectorAddress.getSubLocality()!=null?workerDirectorAddress.getSubLocality():getString(R.string.contract_template_notfound))
                        .replace("%workerDirector.address(cep)", workerDirectorAddress.getPostalCode()!=null?workerDirectorAddress.getPostalCode():getString(R.string.contract_template_notfound))
                        .replace("%workerDirector.address(city)", workerDirectorAddress.getSubAdminArea()!=null?workerDirectorAddress.getSubAdminArea():getString(R.string.contract_template_notfound))
                        .replace("%workerDirector.address(state)", workerDirectorAddress.getAdminArea()!=null?workerDirectorAddress.getAdminArea():getString(R.string.contract_template_notfound))
                        .replace("%workerDirector.address(number)", workerDirectorAddress.getSubThoroughfare()!=null?workerDirectorAddress.getSubThoroughfare():getString(R.string.contract_template_notfound))

                        .replace("%software.name", software.name)
                        .replace(software.supports!=null?software.supports.length()>1?"%software.supports":", customização e ainda %software.supports":"e ainda %software.supports",software.supports!=null?software.supports.length()>1?software.supports:" e customização":" e customização")

                        .replace("%screensList", screenListString)

                        .replace("%workerConsultant.name", workerConsultant.name)
                        .replace("%workerConsultant.profession", workerConsultant.profession)
                        .replace("%workerConsultant.rg", workerConsultant.rg)
                        .replace("%workerConsultant.cpf", workerConsultant.cpf)
                        .replace("%workerConsultant.ctps", workerConsultant.ctps)
                        .replace("%workerConsultant.days", String.valueOf(contract.daysConsultant))
                        .replace("%workerConsultant.hours", String.valueOf(contract.hoursConsultant))
                        .replace("%workerConsultant.beginHour", String.valueOf(contract.beginHour))
                        .replace("%workerConsultant.endHour",  String.valueOf(contract.endHour))

                        .replace("%contract.monthValue", String.valueOf(contract.monthValue))
                        .replace("%contract.account",contract.account)
                        .replace("%contract.bank",contract.bank)
                        .replace("%contract.agency",contract.agency));

            switch(which){

             case DialogInterface.BUTTON_NEGATIVE:
                 intent.setAction(Intent.ACTION_SEND);
                 intent.setType("text/*");
             break;

             case DialogInterface.BUTTON_POSITIVE:
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                 intent.setType("*/*");
                    ArrayList<Parcelable> uriList = new ArrayList<Parcelable>();
                    for (Screen screenItem : screenList) {
                        try {
                            if(screenItem.uri!=null)uriList.add(Uri.parse(screenItem.uri));
                        }catch (Exception e){
                            Toast.makeText(this,"Screen Image Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uriList);
             break;
            }

            startActivity(Intent.createChooser(intent,null));

        };
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.attach_screens_question)
                .setPositiveButton(R.string.yes,listener)
                .setNegativeButton(R.string.no,listener)
                .show();

        //intent.putParcelableArrayListExtra("android.intent.extra.STREAM",uriList);

    }

    /*

    %client.name
    %client.address(city)
    %client.address(street)
    %client.address(number)
    %client.address(neighborhood)
    %client.address(cep)
    %client.address(state)
    %client.cnpj
    %client.ce

    %director.name
    %director.nationality
    %director.civilState
    %director.rg
    %director.cpf
    %director.profession
    %director.address(street)
    %director.address(number)
    %director.address(neighborhood)
    %director.address(cep)
    %director.address(city)
    %director.address(state)

    %us.name
    %us.address(city)
    %us.address(street)
    %us.address(number)
    %us.address(neighborhood)
    %us.address(state)
    %us.address(cep)
    %us.cnpj
    %us.ce

    %workerDirector.name
    %workerDirector.nationality
    %workerDirector.civilState
    %workerDirector.profession
    %workerDirector.rg
    %workerDirector.cpf
    %workerDirector.address(street)
    %workerDirector.address(neighborhood)
    %workerDirector.address(cep)
    %workerDirector.address(city)
    %workerDirector.address(state)

    %software.name
    %software.supports

    %screenList

    %workerConsultant.name
    %workerConsultant.profession
    %workerConsultant.rg
    %workerConsultant.cpf
    %workerConsultant.ctps
    %workerConsultant.days
    %workerConsultant.hours
    %workerConsultant.beginHour
    %workerConsultant.endHour
    R$%contract.monthValue.00
    (%contract.monthValue(text))
    %contract.account
    %contract.bank
    %contract.agency


    */
    public void onUpdate(View view){
        Intent intent = new Intent(this,EditContractActivity.class);
        intent.putExtra(EditContractActivity.EXTRA_CONTRACT_ID,contract.id);
        startActivity(intent);
    }

    public void doubleRemove(DialogInterface dialog, int which){

        if(which == DialogInterface.BUTTON_POSITIVE){
            if(db.removeContract(contract.id))
                if(db.removeDirector(contract.fkDirector)){
                    Toast.makeText(this,R.string.remove_multiple_success, Toast.LENGTH_SHORT).show();
                    return;
                }
        }

        Toast.makeText(this,R.string.remove_failed_message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if(excluded) startActivity(new Intent(this, HomeActivity.class));
        else super.onBackPressed();
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }
}
