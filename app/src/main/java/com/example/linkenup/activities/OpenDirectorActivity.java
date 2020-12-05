package com.example.linkenup.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkenup.ContractActivity;
import com.example.linkenup.R;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Director;

public class OpenDirectorActivity extends AppCompatActivity {

    public static final int
                            OLDDIRECTOR_MODE = 66,
                            NEWDIRECTOR_MODE = 55;

    public static final String EXTRA_DIRECTOR_ID = "LinkenUp.OpenDirector.EXTRA_DIRECTOR_ID";
    public static final String EXTRA_DIRECTOR_NOT_INSERTED = "LinkenUp.OpenDirector.EXTRA_DIRECTOR_NOT_INSERTED";

    TextView idText, rgText, nameText, cpfText, professionText,addressText,nationalityText,civilStateText;
    Director director;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opendirector);
        Bundle extras = getIntent().getExtras();

        if(extras==null){
            Toast.makeText(this,getString(R.string.no_directorextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        if(!(extras.getInt(EXTRA_DIRECTOR_ID,0)>0)
          && extras.getSerializable(EXTRA_DIRECTOR_NOT_INSERTED)==null)
        {
            Toast.makeText(this,getString(R.string.no_directorextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        updated = false;

        idText = (TextView) findViewById(R.id.opendirector_text_id);
        nameText = (TextView) findViewById(R.id.opendirector_text_name);
        cpfText = (TextView) findViewById(R.id.opendirector_text_cpf);
        rgText = (TextView) findViewById(R.id.opendirector_text_rg);
        professionText = (TextView) findViewById(R.id.opendirector_text_profession);
        nationalityText = (TextView) findViewById(R.id.opendirector_text_nationality);
        civilStateText = (TextView) findViewById(R.id.opendirector_text_civilState);
        addressText = (TextView) findViewById(R.id.opendirector_text_address);

        DatabaseHelper db = new DatabaseHelper(this);

        int id = extras.getInt(EXTRA_DIRECTOR_ID,0);

        if(id>0){
            mode = OLDDIRECTOR_MODE;
            director = db.getDirector(id);
        }
        else{
            mode = NEWDIRECTOR_MODE;
            director = (Director) extras.getSerializable(EXTRA_DIRECTOR_NOT_INSERTED);
        }

        if(director == null){
            Toast.makeText(this,getString(R.string.no_directorextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        idText.setText(getString(R.string.id)+": "+String.valueOf(director.id!=null?director.id:0));
        nameText.setText(director.name);
        cpfText.setText(getString(R.string.cpf)+": "+director.cpf);
        rgText.setText(getString(R.string.rg)+": "+director.rg);
        addressText.setText(director.address);
        professionText.setText(director.profession);
        nationalityText.setText(director.nationality);
        civilStateText.setText(director.civilState);

    }


    public void onUpdate(View view){
        Intent intent = new Intent(this,EditDirectorActivity.class);
        if(mode == OLDDIRECTOR_MODE){
            intent.putExtra(EditDirectorActivity.EXTRA_DIRECTOR_ID,director.id);
            updated = true;
        }
        else if (mode == NEWDIRECTOR_MODE)
            intent.putExtra(EditDirectorActivity.EXTRA_DIRECTOR_NOT_INSERTED,director);
        intent.putExtra(EditDirectorActivity.EXTRA_MODE,mode);
        startActivityForResult(intent,78);
    }

    private boolean updated;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 78 && resultCode == Activity.RESULT_OK)
        {
            director = (Director) data.getExtras().getSerializable("director");
            resultDirector(director);
        }
    }

    public void resultDirector(Director director){
        Intent data = new Intent();
        data.putExtra("director",director);
        this.setResult(Activity.RESULT_OK,data);
        finish();
    }

    public void onContracts(View view){
        Intent intent = new Intent(this, ContractActivity.class);
        intent.putExtra(ContractActivity.EXTRA_SEARCH_VALUE,String.valueOf(director.id));
        intent.putExtra(ContractActivity.EXTRA_SEARCH_ROW, Contract.FK_DIRECTOR);
        startActivity(intent);
    }
}