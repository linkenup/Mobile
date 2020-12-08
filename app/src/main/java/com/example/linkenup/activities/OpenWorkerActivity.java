package com.example.linkenup.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkenup.ContractActivity;
import com.example.linkenup.HomeActivity;
import com.example.linkenup.PreferenceActivity;
import com.example.linkenup.R;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Worker;

public class OpenWorkerActivity extends AppCompatActivity {
        public static final String EXTRA_WORKER_ID = "LinkenUp.OpenWorker.EXTRA_WORKER_ID";

        TextView
                idText,
                nameText,
                cpfText,
                rgText,
                ctpsText,
                professionText,
                nationalityText,
                addressText,
                civilStateText;

        Worker worker;

        View changeButton;

        boolean excluded;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_openworker);

            Bundle extras = getIntent().getExtras();
            if(!(extras.getInt(EXTRA_WORKER_ID,0)>0))
            {
                Toast.makeText(this,getString(R.string.no_workerextra_message),Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }

            excluded = true;

            idText = (TextView) findViewById(R.id.openworker_text_id);
            nameText = (TextView) findViewById(R.id.openworker_text_name);
            cpfText = (TextView) findViewById(R.id.openworker_text_cpf);
            ctpsText = (TextView) findViewById(R.id.openworker_text_ctps);
            nationalityText = (TextView) findViewById(R.id.openworker_text_nationality);
            addressText = (TextView) findViewById(R.id.openworker_text_address);
            professionText = (TextView) findViewById(R.id.openworker_text_profession);
            civilStateText = (TextView) findViewById(R.id.openworker_text_civilState);
            rgText = (TextView) findViewById(R.id.openworker_text_rg);

            changeButton = findViewById(R.id.openworker_button_change);

            DatabaseHelper db = new DatabaseHelper(this);
            worker = db.getWorker(extras.getInt(EXTRA_WORKER_ID));

            idText.setText(getString(R.string.id)+": "+String.valueOf(worker.id));
            nameText.setText(worker.name);
            cpfText.setText(getString(R.string.cpf)+": "+worker.cpf);
            rgText.setText(getString(R.string.rg)+":\n"+worker.rg);
            ctpsText.setText(getString(R.string.ctps)+": "+worker.ctps);
            professionText.setText(worker.profession);
            addressText.setText(worker.address);
            nationalityText.setText(worker.nationality);
            civilStateText.setText(worker.civilState);

            changeButton.setOnLongClickListener((View btn)->
            {
                DialogInterface.OnClickListener listener =  (DialogInterface dialog, int which)->{
                    if(which==DialogInterface.BUTTON_POSITIVE)
                    {
                        if(db.removeWorker(worker.id)){
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
                        .setTitle(getString(R.string.remove)+" "+getString(R.string.worker)+"?")
                        .setMessage("ID: "+String.valueOf(worker.id)+" "+worker.name)
                        .show();
                return false;
            });

            if(!NewContractActivity.REGISTERING){
                findViewById(R.id.float_home_button).setVisibility(
                        getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false)?
                                View.VISIBLE:
                                View.GONE);
            }

            if(NewContractActivity.REGISTERING){
                changeButton.setVisibility(View.GONE);
            }
        }

        public void onUpdate(View view){
            Intent intent = new Intent(this,EditWorkerActivity.class);
            intent.putExtra(EditWorkerActivity.EXTRA_WORKER_ID,worker.id);
            startActivity(intent);
        }

    public void onContracts(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.search_worker_as_message)
        .setPositiveButton(R.string.consultant,this::contractsListener)
        .setNegativeButton(R.string.director,this::contractsListener)
        .show();
    }

    public void contractsListener(DialogInterface dialog, int which){
        Intent intent = new Intent(this, ContractActivity.class);
        intent.putExtra(ContractActivity.EXTRA_SEARCH_VALUE,String.valueOf(worker.id));
            if(which == DialogInterface.BUTTON_POSITIVE){
                intent.putExtra(ContractActivity.EXTRA_SEARCH_ROW, Contract.FK_WORKER_CONSULTANT);
            }
            else if(which == DialogInterface.BUTTON_NEGATIVE){
                intent.putExtra(ContractActivity.EXTRA_SEARCH_ROW, Contract.FK_WORKER_DIRECTOR);
            }
        startActivity(intent);
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