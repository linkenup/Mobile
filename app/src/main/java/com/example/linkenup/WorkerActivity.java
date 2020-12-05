package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linkenup.activities.NewWorkerActivity;
import com.example.linkenup.code.ClientAdapter;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.code.WorkerAdapter;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Worker;

public class WorkerActivity extends AppCompatActivity {



    DatabaseHelper db;
    Toolbar toolbar;
    RecyclerView recyclerView;
    View addButton;
    boolean resulting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        Bundle extras = getIntent().getExtras();

        resulting = getCallingActivity()!=null;

        toolbar = (Toolbar) findViewById(R.id.worker_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.worker_recycler);
        addButton = findViewById(R.id.worker_floatbutton_new);

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
        WorkerActivity context = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });

        if(resulting)addButton.setVisibility(View.GONE);

        db = new DatabaseHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        Worker[] list;


            list = db.getAllWorker();

            if (list != null) {
                WorkerAdapter adapter = new WorkerAdapter(this, list, resulting);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                Toast.makeText(this, getString(R.string.no_workerfound_message), Toast.LENGTH_SHORT).show();
            }

    }

    public void onNew(View view) {
        startActivity(new Intent(this, NewWorkerActivity.class));
    }

    public void resultWorker(Worker worker){
        Intent data = new Intent();
        data.putExtra("workerID",worker.id);
        this.setResult(Activity.RESULT_OK,data);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(!resulting)super.onBackPressed();
        else{
            this.setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    public void onSearch(View view) {
        String stg = ((EditText)((ViewGroup)findViewById(R.id.worker_search_include)).getChildAt(0)).getText().toString();
        String row = null;

        stg = stg.replace(" ","%");

        if(stg.contains(Worker.NAME)){
            stg = stg.replace(Worker.NAME,"");
            row = Worker.NAME;
        }
        else if(stg.contains(Worker.ID)){
            stg = stg.replace(Worker.ID,"");
            row = Worker.ID;
        }
        else if(stg.contains(Worker.RG)){
            stg = stg.replace(Worker.RG,"");
            row = Worker.RG;
        }
        else if(stg.contains(Worker.CPF)){
            stg = stg.replace(Worker.CPF,"");
            row = Worker.CPF;
        }
        else if(stg.contains(Worker.CTPS)){
            stg = stg.replace(Worker.CTPS,"");
            row = Worker.CTPS;
        }
        else if(stg.contains(Worker.PROFESSION)){
            stg = stg.replace(Worker.PROFESSION,"");
            row = Worker.PROFESSION;
        }
        else if(stg.contains(Worker.NATIONALITY)){
            stg = stg.replace(Worker.NATIONALITY,"");
            row = Worker.NATIONALITY;
        }
        else if(stg.contains(Worker.ADDRESS)){
            stg = stg.replace(Worker.ADDRESS,"");
            row = Worker.ADDRESS;
        }
        else if(stg.contains(Worker.CIVIL_STATE)){
            stg = stg.replace(Worker.CIVIL_STATE,"");
            row = Worker.CIVIL_STATE;
        }

        Toast.makeText(this,stg,Toast.LENGTH_SHORT).show();

        if(stg!=null)if(stg.length()>0){
            ((WorkerAdapter)recyclerView.getAdapter()).workerList = db.findWorker(stg,row);
            ((WorkerAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }
}