package com.example.linkenup;

import androidx.annotation.Nullable;
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

import com.example.linkenup.activities.NewDirectorActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.code.DirectorAdapter;
import com.example.linkenup.system.Director;

public class DirectorActivity extends AppCompatActivity {

    public static String EXTRA_CLIENT_ID = "LinkenUp.Director.EXTRA_CLIENT_ID";
    public static int NO_CLIENT_ID = -3;

    Toolbar toolbar;
    RecyclerView recyclerView;
    View floatButton;

    DatabaseHelper db;

    int clientID;
    boolean resulting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director);
        Bundle extras = getIntent().getExtras();
        //if(getCallingActivity()!=null)
        if(extras==null)
        {
            Toast.makeText(this,getString(R.string.no_clientextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        clientID = extras.getInt(EXTRA_CLIENT_ID,0);
        if(!(clientID>0)&&clientID!=NO_CLIENT_ID) {
            Toast.makeText(this,getString(R.string.no_clientextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.director_toolbar);
        floatButton = findViewById(R.id.director_floatbutton_new);

        String newTitle = "<"+toolbar.getTitle()+">";
        toolbar.setTitle(newTitle);
        toolbar.setNavigationOnClickListener((View v)->{this.onBackPressed();});

        recyclerView = (RecyclerView) findViewById(R.id.director_recycler);

        db = new DatabaseHelper(this);
        Director[] list;

        if(clientID == NO_CLIENT_ID)list = db.getAllDirector();
        else list = db.getAllDirector(clientID);

        resulting = getCallingActivity()!=null;

        if(!resulting)floatButton.setVisibility(View.GONE);

        if(list!=null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            DirectorAdapter adapter = new DirectorAdapter(this, list,resulting);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }

        if(!resulting)findViewById(R.id.float_home_button).setVisibility(
                getSharedPreferences(PreferenceActivity.FLOAT_HOME,0).getBoolean("bool",false)?
                        View.VISIBLE:
                        View.GONE);
    }

    public void onNew(View view) {
        Intent intent = new Intent(this, NewDirectorActivity.class);
        intent.putExtra(NewDirectorActivity.EXTRA_CLIENT_ID,clientID);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK){
            Director director = (Director) data.getExtras().getSerializable("director");
            resultDirector(director);
        }
    }

    public void resultDirector(Director director){
        Intent data = new Intent();
        data.putExtra("director",director);
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
        String stg = ((EditText)((ViewGroup)findViewById(R.id.director_search_include)).getChildAt(0)).getText().toString();
        String row = null;
        stg = stg.replace(" ","%");

        if(stg.contains(Director.ID)){
            stg = stg.replace(Director.ID,"");
            row = Director.ID;
        }
        if(stg.contains(Director.NAME)){
            stg = stg.replace(Director.NAME,"");
            row = Director.NAME;
        }
        else if(stg.contains(Director.ADDRESS)){
            stg = stg.replace(Director.ADDRESS,"");
            row = Director.ADDRESS;
        }
        else if(stg.contains(Director.CIVIL_STATE)){
            stg = stg.replace(Director.CIVIL_STATE,"");
            row = Director.CIVIL_STATE;
        }
        else if(stg.contains(Director.CPF)){
            stg = stg.replace(Director.CPF,"");
            row = Director.CPF;
        }
        else if(stg.contains(Director.NATIONALITY)){
            stg = stg.replace(Director.NATIONALITY,"");
            row = Director.NATIONALITY;
        }
        else if(stg.contains(Director.PROFESSION)){
            stg = stg.replace(Director.PROFESSION,"");
            row = Director.PROFESSION;
        }
        else if(stg.contains(Director.RG)) {
            stg = stg.replace(Director.RG,"");
            row = Director.RG;
        }

        Toast.makeText(this,stg,Toast.LENGTH_SHORT).show();

        if(stg!=null)if(stg.length()>0){
            ((DirectorAdapter)recyclerView.getAdapter()).directorList = db.findDirector(stg,row);
            ((DirectorAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    public void onHome(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }

}