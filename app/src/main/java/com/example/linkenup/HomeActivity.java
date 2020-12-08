package com.example.linkenup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.linkenup.activities.NewClientActivity;
import com.example.linkenup.activities.NewContractActivity;
import com.example.linkenup.activities.NewSoftwareActivity;
import com.example.linkenup.activities.NewWorkerActivity;
import com.example.linkenup.activities.OpenClientActivity;
import com.example.linkenup.activities.OpenContractActivity;
import com.example.linkenup.activities.OpenDirectorActivity;
import com.example.linkenup.activities.OpenSoftwareActivity;
import com.example.linkenup.activities.OpenWorkerActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Director;
import com.example.linkenup.system.Software;
import com.example.linkenup.system.Worker;

public class HomeActivity extends AppCompatActivity {
    public static final String LAYOUT_PREFERENCE_NAME = "com.example.linkenup.HomeActivity.layout_preference";


    Toolbar toolbar;
    GridLayout gridLayout;
    LinearLayout mainLayout;
    ScrollView scrollView;
    View moreButtons;
    EditText smallImgEdit;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        gridLayout = (GridLayout) findViewById(R.id.home_layout_smallimg);
        mainLayout = (LinearLayout) findViewById(R.id.home_linear_main);
        scrollView = (ScrollView) findViewById(R.id.home_scroll_main);
        moreButtons = findViewById(R.id.home_more_buttons);
        smallImgEdit = (EditText) findViewById(R.id.home_edit_smallimg);

        SharedPreferences settings = getSharedPreferences(LAYOUT_PREFERENCE_NAME,0);
        boolean smallimg = settings.getBoolean("smallimg",false);
        gridLayout.setVisibility(smallimg?View.VISIBLE:View.GONE);


        if(smallimg) {
            scrollView.setVisibility(View.GONE);
            smallImgEdit.setVisibility(View.VISIBLE);
        }
        else {
            boolean img, btn;
            img = settings.getBoolean("img",true);
            btn = settings.getBoolean("btn",true);
            int imgVisibility = img ? View.VISIBLE : View.GONE;
            int btnVisibility = btn ? View.VISIBLE : View.GONE;
            int dividerVisibility = img && btn ? View.VISIBLE : View.GONE;

            int i = 1;
            while (i != -1) {
                try {
                    mainLayout.getChildAt(i * 3 - 3).setVisibility(imgVisibility);//imgs
                    mainLayout.getChildAt(i * 3 - 2).setVisibility(btnVisibility);//btns
                    mainLayout.getChildAt(i * 3 - 1).setVisibility(dividerVisibility);//dividers
                    i++;
                } catch (Exception ex) {
                    i = -1;
                }
            }
            if(img && !btn)((ImageView)mainLayout.getChildAt(3 * 3 - 3)).setColorFilter(getResources().getColor(R.color.lowblue));
            if(btn && !img) moreButtons.setVisibility(View.VISIBLE);
        }
        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);

    }

    public void onPreference(View view) { startActivity(new Intent(this, PreferenceActivity.class));}
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void onClient(View view){
        startActivity(new Intent(this,ClientActivity.class));
    }

    public void onNewClient(View view) {
        startActivity(new Intent(this, NewClientActivity.class));
    }

    public void onWorker(View view) {startActivity(new Intent(this,WorkerActivity.class)); }

    public void onNewWorker(View view) {
        startActivity(new Intent(this, NewWorkerActivity.class));
    }

    public void onSoftware(View view) {startActivity(new Intent(this,SoftwareActivity.class));}

    public void onNewSoftware(View view) {
        startActivity(new Intent(this, NewSoftwareActivity.class));
    }

    public void onContract(View view) {startActivity(new Intent(this,ContractActivity.class)); }

    public void onNewContract(View view) {
        startActivity(new Intent(this, NewContractActivity.class));
    }

    public void onByID(View view) {
        try {
            int id = Integer.parseInt(smallImgEdit.getText().toString());
            String table, row;
            switch (view.getId()){
                case R.id.home_smallimg_clientID:
                    if(db.getClient(id)!=null){
                        Intent intent = new Intent(this, OpenClientActivity.class);
                        intent.putExtra(OpenClientActivity.EXTRA_CLIENT_ID,id);
                        startActivity(intent);
                        return;
                    }
                    break;
                case R.id.home_smallimg_contractID:
                    if(db.getContract(id)!=null){
                        Intent intent = new Intent(this, OpenContractActivity.class);
                        intent.putExtra(OpenContractActivity.EXTRA_CONTRACT_ID,id);
                        startActivity(intent);
                        return;
                    }
                    break;
                case R.id.home_smallimg_directorID:
                    if(db.getDirector(id)!=null){
                        Intent intent = new Intent(this, OpenDirectorActivity.class);
                        intent.putExtra(OpenDirectorActivity.EXTRA_DIRECTOR_ID,id);
                        startActivity(intent);
                        return;
                    }
                    break;
                case R.id.home_smallimg_softwareID:
                    if(db.getSoftware(id)!=null){
                        Intent intent = new Intent(this, OpenSoftwareActivity.class);
                        intent.putExtra(OpenSoftwareActivity.EXTRA_SOFTWARE_ID,id);
                        startActivity(intent);
                        return;
                    }
                    break;
                case R.id.home_smallimg_workerID:
                    if(db.getWorker(id)!=null){
                        Intent intent = new Intent(this, OpenWorkerActivity.class);
                        intent.putExtra(OpenWorkerActivity.EXTRA_WORKER_ID,id);
                        startActivity(intent);
                        return;
                    }
                    break;
            }
            throw new Exception();
        }
        catch (Exception e){
                   Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
        }
    }


    int removeID, directorFK;
    String removeTable;

    public void onRemove(View view) {
        try {
            int id = Integer.parseInt(smallImgEdit.getText().toString());
            String table = "";
            String stringField = "";
            boolean result = false;
            switch (view.getId()){
                case R.id.home_smallimg_removeclient:
                    Client client = db.getClient(id);
                    result = client != null;
                    table = DatabaseHelper.TABLE_CLIENT;
                    stringField = client.name;
                    break;
                case R.id.home_smallimg_removecontract:
                    Contract contract = db.getContract(id);
                    result = contract != null;
                    directorFK = contract.fkDirector;
                    table = DatabaseHelper.TABLE_CONTRACT;
                    stringField = db.getClient(contract.fkClient).name+" + "+db.getSoftware(contract.fkSoftware).name;
                    break;
                case R.id.home_smallimg_removesoftware:
                    Software software = db.getSoftware(id);
                    table = DatabaseHelper.TABLE_SOFTWARE;
                    stringField = software.name;
                    result = software  != null;
                    break;
                case R.id.home_smallimg_removeworker:
                    Worker worker = db.getWorker(id);
                    table = DatabaseHelper.TABLE_WORKER;
                    stringField = worker.name;
                    result = worker != null;
                    break;
            }

            if(!result)throw new Exception();
            removeID = id;
            removeTable = table;

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle(getString(R.string.remove)+" "+table+"?")
                    .setMessage(getString(R.string.id)+": "+String.valueOf(id)+"; "+stringField)
                    .setPositiveButton(R.string.yes,this::removeListener)
                    .setNegativeButton(R.string.no,this::removeListener)
                    .show();
        }
        catch (Exception e){
            Toast.makeText(this,R.string.invalid_id_message,Toast.LENGTH_SHORT).show();
        }
    }

    public void removeListener(DialogInterface dialog, int which){

        if(which == DialogInterface.BUTTON_POSITIVE){

            boolean result = false;

            switch (removeTable){
                case DatabaseHelper.TABLE_CLIENT :
                    result = db.removeClient(removeID);
                    break;
                case DatabaseHelper.TABLE_CONTRACT :

                    if(db.countDirectorContracts(directorFK)==1){

                        AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                                alert2.setTitle(getString(R.string.remove)+" "+getString(R.string.director)+"?")
                                .setMessage(getString(R.string.remove_contract_director).replace("%id",String.valueOf(directorFK)).replace("%name",db.getDirector(directorFK).name))
                                .setPositiveButton(R.string.yes,this::doubleRemove)
                                .setNegativeButton(R.string.no,this::doubleRemove)
                                .show();
                        return;
                    }
                    else result = db.removeContract(removeID);

                    break;
                case DatabaseHelper.TABLE_DIRECTOR :
                    result = db.removeDirector(removeID);
                    break;
                case DatabaseHelper.TABLE_SOFTWARE :
                    result = db.removeSoftware(removeID);
                    break;
                case DatabaseHelper.TABLE_WORKER :
                    result = db.removeWorker(removeID);
                    break;
            }
            Toast.makeText(this,result?R.string.remove_success_message:R.string.remove_failed_message,Toast.LENGTH_SHORT).show();
        }
    }

    public void doubleRemove(DialogInterface dialog, int which){

        if(which == DialogInterface.BUTTON_POSITIVE){
            if(db.removeContract(removeID))
                if(db.removeDirector(directorFK)){
                 Toast.makeText(this,R.string.remove_multiple_success, Toast.LENGTH_SHORT).show();
                 return;
                }
        }

        Toast.makeText(this,R.string.remove_failed_message,Toast.LENGTH_LONG).show();
    }


    public void onDirector(View view) {
        Intent intent = new Intent(this,DirectorActivity.class);
        intent.putExtra(DirectorActivity.EXTRA_CLIENT_ID,DirectorActivity.NO_CLIENT_ID);
        startActivity(intent);
    }

    public void onMap(View view){
        startActivity(new Intent(this,MapActivity.class));
    }
}


