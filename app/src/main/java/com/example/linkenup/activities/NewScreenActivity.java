package com.example.linkenup.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linkenup.R;
import com.example.linkenup.ScreenActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Screen;

public class NewScreenActivity extends AppCompatActivity {

    public static String EXTRA_SOFTWARE_ID;
    int softwareID;

    View clipButton;
    ImageView imageView;
    EditText nameEdit, functionsEdit;
    boolean backSafe;
    String imageURI;

    int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editscreen);

        Bundle extras = getIntent().getExtras();
        if(extras==null){
            onBackPressed();
            return;
        }

        mode = extras.getInt(ScreenActivity.EXTRA_MODE,0);

        if(mode==0)/********************************************canProceed**********************************************************************************/
        {
            Toast.makeText(this,getString(R.string.screen_modeextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;//lock !!!
        }

        if(mode==ScreenActivity.OLDSOFTWARE_MODE) softwareID = extras.getInt(EXTRA_SOFTWARE_ID);

        nameEdit = (EditText) findViewById(R.id.newscreen_edit_name);
        functionsEdit = (EditText) findViewById(R.id.newscreen_edit_functions);
        imageView = (ImageView) findViewById(R.id.newscreen_screen_image);
        clipButton = (View) findViewById(R.id.newscreen_floatbutton_image);

        ViewGroup.LayoutParams layout =  imageView.getLayoutParams();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            layout.height = metrics.heightPixels;
            layout.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            layout.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        imageView.setLayoutParams(layout);
        clipButton.setOnLongClickListener((View view)-> {
            imageURI = null;
            imageView.setImageURI(null);
            return true;
        });

    }

    public void onRegister(View view){

        String
                name = nameEdit.getText().toString(),
                functions = functionsEdit.getText().toString();



        if(name.length() < 2 || functions.length()<2)
        {
            return;
        }
        DatabaseHelper db = new DatabaseHelper(this);

        Screen screen = new Screen(name,functions,imageURI);

        if(mode==ScreenActivity.NEWSOFTWARE_MODE){
            NewSoftwareActivity.SCREEN_LIST.add(screen);
            backSafe = true;
            Toast.makeText(this,getString(R.string.screen_add_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(mode==ScreenActivity.OLDSOFTWARE_MODE) {
            screen.fkSoftware = softwareID;
            int id = db.insertScreen(screen);
            Toast.makeText(this,
                    getString(R.string.screen_insert_message)
                            .replace("%id", String.valueOf(id))
                            .replace("%name", screen.name)
                            .replace("%fk", String.valueOf(softwareID)),
                    Toast.LENGTH_LONG).show();

            NewScreenActivity context = this;
            new Handler().postDelayed(() -> {
                backSafe = true;
                context.onBackPressed();
            }, 1000);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("name",nameEdit.getText().toString());
        outState.putString("functions", functionsEdit.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameEdit.setText(savedInstanceState.getString("name"));
        functionsEdit.setText(savedInstanceState.getString("functions"));
    }

    @Override
    public void onBackPressed() {
        if(!backSafe)
        {
            Toast.makeText(this,getString(R.string.register_backsafe_message),Toast.LENGTH_SHORT).show();
            backSafe=true;
            new Handler().postDelayed(() -> {backSafe=false;},5000);
        }
        else super.onBackPressed();
    }

    public void requestImage(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Photo"),0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            imageURI = uri.toString();
            imageView.setImageURI(uri);
        }
        }
}
