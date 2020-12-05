package com.example.linkenup.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
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

import com.example.linkenup.HomeActivity;
import com.example.linkenup.R;
import com.example.linkenup.ScreenActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Screen;

public class EditScreenActivity extends AppCompatActivity {

    public static final String EXTRA_SCREEN_POSITION = "LinkenUp.EditScreen.EXTRA_SCREEN_POSITION";
    public static String EXTRA_SCREEN_ID = "LinkenUp.EditScreen.EXTRA_SCREEN_ID";
    public static String EXTRA_SCREEN_NOT_INSERTED = "LinkenUp.EditScreen.EXTRA_SCREEN_NOT_INSERTED";
    int softwareID;

    Screen screen;

    DatabaseHelper db;

    View clipButton;
    ImageView imageView;
    EditText nameEdit, functionsEdit;
    boolean backSafe;
    String imageURI;

    int mode, screenPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_editscreen);

        Bundle extras = getIntent().getExtras();
        if(extras==null){
            onBackPressed();
            return;
        }

        screenPosition = extras.getInt(EXTRA_SCREEN_POSITION);
        mode = extras.getInt(ScreenActivity.EXTRA_MODE,0);

        db = new DatabaseHelper(this);

        if(mode==0)/********************************************canProceed**********************************************************************************/
        {
            Toast.makeText(this,getString(R.string.screen_modeextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;//lock !!!
        }

        if(mode==ScreenActivity.OLDSOFTWARE_MODE) screen = db.getScreen(extras.getInt(EXTRA_SCREEN_ID));

        if(mode == ScreenActivity.NEWSOFTWARE_MODE) screen = (Screen) extras.getSerializable(EXTRA_SCREEN_NOT_INSERTED);
        nameEdit = (EditText) findViewById(R.id.newscreen_edit_name);
        functionsEdit = (EditText) findViewById(R.id.newscreen_edit_functions);
        imageView = (ImageView) findViewById(R.id.newscreen_screen_image);
        clipButton = findViewById(R.id.newscreen_floatbutton_image);

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

        nameEdit.setText(screen.name);
        functionsEdit.setText(screen.functions);

        if(screen.uri!=null)
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},11);
                }
                else imageView.setImageURI(Uri.parse(screen.uri));
            }
        }
        catch (Exception e){
            Toast.makeText(this,R.string.image_error_message,Toast.LENGTH_SHORT).show();
        }

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


        if(mode==ScreenActivity.NEWSOFTWARE_MODE){
            screen = new Screen(screen.fkSoftware,name,functions,imageURI);
            NewSoftwareActivity.SCREEN_LIST.set(screenPosition,screen);
            backSafe = true;
            Toast.makeText(this,getString(R.string.screen_add_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(mode==ScreenActivity.OLDSOFTWARE_MODE) {
            screen.fkSoftware = softwareID;
            if(!(db.updateScreen(screen)>0)){
                Toast.makeText(this,R.string.update_failed_message,Toast.LENGTH_SHORT).show();
                return;
            }
            else Toast.makeText(this,R.string.update_success_message,Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, HomeActivity.class));
            }, 500);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==11){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                imageView.setImageURI(Uri.parse(screen.uri));
        }
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
