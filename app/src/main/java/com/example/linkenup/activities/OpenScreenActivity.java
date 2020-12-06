package com.example.linkenup.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.linkenup.R;
import com.example.linkenup.ScreenActivity;
import com.example.linkenup.code.DatabaseHelper;
import com.example.linkenup.system.Screen;

public class OpenScreenActivity extends AppCompatActivity {
    public static final String 
                                EXTRA_SCREEN_ID = "LinkenUp.OpenScreen.EXTRA_SCREEN_ID",
                                EXTRA_SCREEN_NOT_INSERTED = "LinkenUp.OpenScreen.EXTRA_SCREEN_NOT_INSERTED",
                                EXTRA_SCREEN_POSITION = "LinkenUp.OpenScreen.EXTRA_SCREEN_POSITION";
    public static final String IMAGE_BORDER_PREFERENCE = "LinkenUp.OpenScreen.IMAGE_BORDER_PREFERENCE";
    Screen screen;
    ImageView imageView;
    TextView
            idText,
            nameText,
            functionsText,
            softwareNameText;
    View changeButton;
    int mode,screenPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openscreen);

        Bundle extras = getIntent().getExtras();
        if(extras==null){
            onBackPressed();
            return;
        }

        mode = extras.getInt(ScreenActivity.EXTRA_MODE,0);

        screenPosition = extras.getInt(EXTRA_SCREEN_POSITION);

        if(mode==0)/********************************************canProceed**********************************************************************************/
        {
            Toast.makeText(this,getString(R.string.screen_modeextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;//lock !!!
        }

        if(!(extras.getInt(EXTRA_SCREEN_ID,0)>0)&& extras.getSerializable(EXTRA_SCREEN_NOT_INSERTED)==null)
        {
            Toast.makeText(this,getString(R.string.no_screenextra_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        imageView = (ImageView) findViewById(R.id.openscreen_image);
        idText = (TextView) findViewById(R.id.openscreen_text_id);
        nameText = (TextView) findViewById(R.id.openscreen_text_name);
        functionsText = (TextView) findViewById(R.id.openscreen_text_functions);
        softwareNameText = (TextView) findViewById(R.id.openscreen_text_software_name);
        changeButton = findViewById(R.id.openscreen_button_change);

        SharedPreferences imageBorder = getSharedPreferences(OpenScreenActivity.IMAGE_BORDER_PREFERENCE,0);

        boolean isColor =  imageBorder.getBoolean("bool",false);
        int color = imageBorder.getInt("color", Color.rgb(0,0,0));

        if(isColor){
            float padding = getResources().getDimension(R.dimen.screen_imageBorder_size);
            imageView.setPadding((int)padding,(int)padding,(int)padding,(int)padding);
            imageView.setBackgroundColor(color);
        }

        DatabaseHelper db = new DatabaseHelper(this);
        if(mode == ScreenActivity.OLDSOFTWARE_MODE) {
            screen = db.getScreen(extras.getInt(EXTRA_SCREEN_ID));
            softwareNameText.setText(db.getSoftware(screen.fkSoftware).name);
            idText.setText(getString(R.string.id)+": "+String.valueOf(screen.id));
        }
        else if(mode == ScreenActivity.NEWSOFTWARE_MODE){
            screen = (Screen) extras.getSerializable(EXTRA_SCREEN_NOT_INSERTED);
            softwareNameText.setVisibility(View.GONE);
            idText.setVisibility(View.GONE);
        }

        nameText.setText(screen.name);
        functionsText.setText(getString(R.string.functions)+":\n"+screen.functions);

        if(screen.uri != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},11);
            }
            else if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                    imageView.setImageURI(Uri.parse(screen.uri));

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

        }

        changeButton.setOnLongClickListener((View btn)->
        {
            if(mode == ScreenActivity.OLDSOFTWARE_MODE)if(db.countScreen(screen.fkSoftware)>1) {
                DialogInterface.OnClickListener listener = (DialogInterface dialog, int which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        if (db.removeScreen(screen.id))
                            Toast.makeText(this, R.string.remove_success_message, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this, R.string.remove_failed_message, Toast.LENGTH_SHORT).show();
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(getString(R.string.yes), listener)
                        .setNegativeButton(getString(R.string.no), listener)
                        .setTitle(getString(R.string.remove) + " " + getString(R.string.client) + "?")
                        .setMessage("ID: " + String.valueOf(screen.id) +" "+ screen.name)
                        .show();
                return true;
            }
            if(mode == ScreenActivity.NEWSOFTWARE_MODE){
                DialogInterface.OnClickListener listener = (DialogInterface dialog, int which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        NewSoftwareActivity.SCREEN_LIST.remove(screenPosition);
                        onBackPressed();
                        return;
                    }
                    Toast.makeText(this, R.string.remove_success_message, Toast.LENGTH_SHORT).show();
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(getString(R.string.yes), listener)
                        .setNegativeButton(getString(R.string.no), listener)
                        .setTitle(getString(R.string.remove) + " " + getString(R.string.screen) + "?")
                        .setMessage("ID: " + String.valueOf(screen.id) +" "+ screen.name)
                        .show();
                return true;
            }
            return false;
        });
    }

    public void onUpdate(View view){
        Intent intent = new Intent(this,EditScreenActivity.class);
        intent.putExtra(ScreenActivity.EXTRA_MODE,mode);

        if(mode == ScreenActivity.OLDSOFTWARE_MODE){
            intent.putExtra(EditScreenActivity.EXTRA_SCREEN_ID,screen.id);
            startActivity(intent);
        }
        if(mode == ScreenActivity.NEWSOFTWARE_MODE){
            intent.putExtra(EditScreenActivity.EXTRA_SCREEN_NOT_INSERTED,screen);
            intent.putExtra(EditScreenActivity.EXTRA_SCREEN_POSITION,screenPosition);
            startActivity(intent);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==11){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                imageView.setImageURI(Uri.parse(screen.uri));
        }
    }
}
