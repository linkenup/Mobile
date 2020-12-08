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

import com.example.linkenup.activities.NewClientActivity;
import com.example.linkenup.code.ClientAdapter;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Client;

public class ClientActivity extends AppCompatActivity {

    DatabaseHelper db;
    Toolbar toolbar;
    RecyclerView recyclerView;
    View addButton;
    boolean resulting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Bundle extras = getIntent().getExtras();

        resulting = getCallingActivity()!=null;

        toolbar = (Toolbar) findViewById(R.id.client_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.client_recycler);

        addButton = findViewById(R.id.client_floatbutton_new);

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
        ClientActivity context = this;
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
        Client[] list;

        list = db.getAllClient();

        if(list!=null) {
            ClientAdapter adapter = !resulting?
                    new ClientAdapter(this, list):
                    new ClientAdapter(this,list,resulting);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        else
        {
            Toast.makeText(this,getString(R.string.no_clientfound_message),Toast.LENGTH_SHORT).show();
        }

        if(!resulting)findViewById(R.id.float_home_button).setVisibility(
                getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false)?
                        View.VISIBLE:
                        View.GONE);
    }

    public void onNew(View view) {
        startActivity(new Intent(this, NewClientActivity.class));
    }

    public void resultClient(Client client){
        Intent data = new Intent();
        data.putExtra("clientID",client.id);
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

        String stg = ((EditText)((ViewGroup)findViewById(R.id.client_search_include)).getChildAt(0)).getText().toString();
        String row = null;
        stg = stg.replace(" ","%");


        if(stg.contains(Client.NAME)){
            stg = stg.replace(Client.NAME,"");
            row = Client.NAME;
        }
        else if(stg.contains(Client.ADDRESS)){
            stg = stg.replace(Client.ADDRESS,"");
            row = Client.ADDRESS;
        }
        else if(stg.contains(Client.CE)){
            stg = stg.replace(Client.CE,"");
            row = Client.CE;
        }
        else if(stg.contains(Client.CNPJ)){
            stg = stg.replace(Client.CNPJ,"");
            row = Client.CNPJ;
        }
        else if(stg.contains(Client.ID)){
            stg = stg.replace(Client.ID,"");
            row = Client.ID;
        }

        Toast.makeText(this,stg,Toast.LENGTH_SHORT).show();

        if(stg!=null)if(stg.length()>0){
            ((ClientAdapter)recyclerView.getAdapter()).clientList = db.findClient(stg,row);
            ((ClientAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }
}