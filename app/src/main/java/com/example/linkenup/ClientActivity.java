package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.linkenup.code.ClientAdapter;
import com.example.linkenup.system.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
    public static final String MANUAL_HOME = "com.example.linkenup.HomeActivity.MANUAL_HOME";
    public boolean manualHome;

    Toolbar toolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        try {
            manualHome = getIntent().getExtras().getBoolean(MANUAL_HOME);
        }catch (Exception e)
        {
            manualHome = false;
        }

        toolbar = (Toolbar) findViewById(R.id.client_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.client_recycler);

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        List<Cliente> list = new ArrayList<Cliente>(1);
        Cliente cliente = new Cliente();
        cliente.primaryKey = 1;
        list.add(cliente);
        ClientAdapter adapter = new ClientAdapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        if(manualHome)
        {
            startActivity(new Intent(this,HomeActivity.class));
            return;
        }
        super.onBackPressed();
    }
}