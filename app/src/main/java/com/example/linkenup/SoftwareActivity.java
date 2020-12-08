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

import com.example.linkenup.activities.NewSoftwareActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.code.SoftwareAdapter;
import com.example.linkenup.system.Software;

public class SoftwareActivity extends AppCompatActivity {

    DatabaseHelper db;
    Toolbar toolbar;
    RecyclerView recyclerView;
    View addButton;

    boolean resulting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        SoftwareActivity context = this;

        toolbar = (Toolbar) findViewById(R.id.software_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.software_recycler);
        addButton = findViewById(R.id.software_floatbutton_new);


        resulting = getCallingActivity()!=null;
        if(resulting)addButton.setVisibility(View.GONE);

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });

        db = new DatabaseHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        Software[] list = db.getAllSoftware();
        if(list!=null) {
            SoftwareAdapter adapter = new SoftwareAdapter(this,list,resulting);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        if(!resulting)findViewById(R.id.float_home_button).setVisibility(
                getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false)?
                        View.VISIBLE:
                        View.GONE);
    }

    public void resultSoftware(Software software){
        Intent data = new Intent();
        data.putExtra("softwareID",software.id);
        this.setResult(Activity.RESULT_OK,data);
        finish();
    }

    public void onNew(View view) {
        startActivity(new Intent(this, NewSoftwareActivity.class));
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
        String stg = ((EditText)((ViewGroup)findViewById(R.id.software_search_include)).getChildAt(0)).getText().toString();
        String row = null;
        stg = stg.replace(" ","%");

        if(stg.contains(Software.NAME)){
            stg = stg.replace(Software.NAME,"");
            row = Software.NAME;
        }
        else if(stg.contains(Software.SUPPORTS)){
            stg = stg.replace(Software.SUPPORTS,"");
            row = Software.SUPPORTS;
        }
        else if(stg.contains(Software.DESCRIPTION)){
            stg = stg.replace(Software.DESCRIPTION,"");
            row = Software.DESCRIPTION;
        }
        else if(stg.contains(Software.ID)){
            stg = stg.replace(Software.ID,"");
            row = Software.ID;
        }


        Toast.makeText(this,stg,Toast.LENGTH_SHORT).show();

        if(stg!=null)if(stg.length()>0){
            ((SoftwareAdapter)recyclerView.getAdapter()).softwareList = db.findSoftware(stg,row);
            ((SoftwareAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }
}