package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linkenup.activities.NewContractActivity;
import com.example.linkenup.code.ClientAdapter;
import com.example.linkenup.code.ContractAdapter;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Contract;

public class ContractActivity extends AppCompatActivity {

    public static final String
            EXTRA_SEARCH_VALUE = "LinkenUp.Contract.EXTRA_SEARCH_VALUE",
            EXTRA_SEARCH_ROW = "LinkenUp.Contract.EXTRA_SEARCH_ROW";

    DatabaseHelper db;
    Toolbar toolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        toolbar = (Toolbar) findViewById(R.id.contract_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.contract_recycler);

        Bundle extras = getIntent().getExtras();

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
        ContractActivity context = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });

        db = new DatabaseHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        Contract[] list;
        String valueSearch = "&@*!";
        if(extras!= null)valueSearch= extras.getString(EXTRA_SEARCH_VALUE,"&@*!");
        if(valueSearch!="&@*!"){
            String row = extras.getString(EXTRA_SEARCH_ROW,"&@*!");
            if(row!="&@*!"){
                ((EditText)((ViewGroup)findViewById(R.id.contract_search_include)).getChildAt(0)).setText(row+" "+valueSearch);
                list = db.findContract(valueSearch,row);
            }
            else{
                ((EditText)((ViewGroup)findViewById(R.id.contract_search_include)).getChildAt(0)).setText(valueSearch);
                list = db.findContract(valueSearch,null);
            }
        }else
            list = db.getAllContract();

            if(list!=null) {
                ContractAdapter adapter = new ContractAdapter(this, list);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }

    }

    public void onNew(View view) {
        startActivity(new Intent(this, NewContractActivity.class));
    }

    public void onSearch(View view) {
        String stg = ((EditText)((ViewGroup)findViewById(R.id.contract_search_include)).getChildAt(0)).getText().toString();
        String row = null;
        stg = stg.replace(" ","%");

        if(stg.contains(Contract.ID)){
            stg = stg.replace(Contract.ID,"");
            row = Contract.ID;
        }
        else if(stg.contains(Contract.FK_CLIENT)){
            stg = stg.replace(Contract.FK_CLIENT,"");
            row = Contract.FK_CLIENT;
        }
        else if(stg.contains(Contract.FK_SOFTWARE)){
            stg = stg.replace(Contract.FK_SOFTWARE,"");
            row = Contract.FK_SOFTWARE;
        }
        else if(stg.contains(Contract.FK_WORKER_CONSULTANT)){
            stg = stg.replace(Contract.FK_WORKER_CONSULTANT,"");
            row = Contract.FK_WORKER_CONSULTANT;
        }
        else if(stg.contains(Contract.FK_WORKER_DIRECTOR)){
            stg = stg.replace(Contract.FK_WORKER_DIRECTOR,"");
            row = Contract.FK_WORKER_DIRECTOR;
        }
        else if(stg.contains(Contract.MONTH_VALUE)){
            stg = stg.replace(Contract.MONTH_VALUE,"");
            row = Contract.MONTH_VALUE;
        }
        else if(stg.contains(Contract.BANK)){
            stg = stg.replace(Contract.BANK,"");
            row = Contract.BANK;
        }
        else if(stg.contains(Contract.AGENCY)){
            stg = stg.replace(Contract.AGENCY,"");
            row = Contract.AGENCY;
        }
        else if(stg.contains(Contract.ACCOUNT)){
            stg = stg.replace(Contract.ACCOUNT,"");
            row = Contract.ACCOUNT;
        }
        else if(stg.contains(Contract.DAYS_CONSULTANT)){
            stg = stg.replace(Contract.DAYS_CONSULTANT,"");
            row = Contract.DAYS_CONSULTANT;
        }
        else if(stg.contains(Contract.HOURS_CONSULTANT)){
            stg = stg.replace(Contract.HOURS_CONSULTANT,"");
            row = Contract.HOURS_CONSULTANT;
        }
        else if(stg.contains(Contract.BEGIN_HOUR)){
            stg = stg.replace(Contract.BEGIN_HOUR,"");
            row = Contract.BEGIN_HOUR;
        }
        else if(stg.contains(Contract.END_HOUR)){
            stg = stg.replace(Contract.END_HOUR,"");
            row = Contract.END_HOUR;
        }

        Toast.makeText(this,stg,Toast.LENGTH_SHORT).show();

        if(stg!=null)if(stg.length()>0){

            ((ContractAdapter)recyclerView.getAdapter()).contractList = db.findContract(stg,row);
            ((ContractAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }
}