package com.example.linkenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.linkenup.activities.NewScreenActivity;
import com.example.linkenup.activities.NewSoftwareActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Screen;
import com.example.linkenup.code.ScreenAdapter;
import com.google.android.material.tabs.TabLayout;

public class ScreenActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "LinkenUp.Screen.EXTRA_MODE";
    public static final int NEWSOFTWARE_MODE = 1;
    public static final int OLDSOFTWARE_MODE = 2;


    public static final String EXTRA_SOFTWARE_ID = "LinkenUp.Screen.EXTRA_SCREEN_ID";

    int softwareID;
    int mode;

    Screen[] screenList;

    DatabaseHelper db;
    RecyclerView recyclerView;
    TabLayout.Tab softwareTab;
    TabLayout.Tab screenTab;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        ScreenActivity context = this;


        recyclerView = (RecyclerView) findViewById(R.id.screen_recycler);

        Bundle extras = getIntent().getExtras();
        if(extras==null){
            onBackPressed();
            return;
        }

        mode = extras.getInt(EXTRA_MODE,0);

        if(mode==0)/********************************************canProceed**********************************************************************************/
        {
            Toast.makeText(this,getString(R.string.screen_modeextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;//lock !!!
        }


        tabLayout = (TabLayout) findViewById(R.id.screen_tablayout);
        softwareTab = tabLayout.getTabAt(0);
        screenTab = tabLayout.getTabAt(1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)context.onBackPressed();
            }

            //Ignore
            @Override public void onTabUnselected(TabLayout.Tab tab) { } @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        if(mode==ScreenActivity.NEWSOFTWARE_MODE){
            screenList =  NewSoftwareActivity.SCREEN_LIST.toArray(new Screen[]{});
        }

        if(mode==ScreenActivity.OLDSOFTWARE_MODE) {
            softwareID = extras.getInt(EXTRA_SOFTWARE_ID, 0);
            if (!(softwareID > 0)) {
                Toast.makeText(this, getString(R.string.no_softwareextra_message), Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }

            db = new DatabaseHelper(this);
            screenList = db.getAllScreen(softwareID);

        }


        if (screenList != null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            ScreenAdapter adapter = new ScreenAdapter(this, screenList,mode);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        else
        {
            Toast.makeText(this,R.string.no_screenfound_message,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        screenTab.select();
        if(mode==ScreenActivity.NEWSOFTWARE_MODE){
            screenList = NewSoftwareActivity.SCREEN_LIST.toArray(new Screen[]{});
        if(recyclerView.getAdapter()!=null){
            if(screenList!=((ScreenAdapter)recyclerView.getAdapter()).screenList) {
                ((ScreenAdapter)recyclerView.getAdapter()).screenList = screenList;
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }}
        if(mode == ScreenActivity.OLDSOFTWARE_MODE){
            screenList = NewSoftwareActivity.SCREEN_LIST.toArray(new Screen[]{});
            if(recyclerView.getAdapter()!=null){
                if(screenList!=((ScreenAdapter)recyclerView.getAdapter()).screenList) {
                    ((ScreenAdapter)recyclerView.getAdapter()).screenList = screenList;
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }

    public void onNew(View view) {
        Intent intent = new Intent(this, NewScreenActivity.class);
        intent.putExtra(EXTRA_MODE,mode);
        if(mode == OLDSOFTWARE_MODE) intent.putExtra(NewScreenActivity.EXTRA_SOFTWARE_ID,softwareID);
        startActivity(intent);
    }


    public void onBack(View view) { onBackPressed(); }
}