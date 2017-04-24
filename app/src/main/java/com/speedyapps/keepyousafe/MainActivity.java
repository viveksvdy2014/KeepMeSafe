package com.speedyapps.keepyousafe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    int choice;
    MapsActivity maps ;
    SharedPreferences firsttime,settings;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 291;
    int backCount=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maps=new MapsActivity();
        firsttime = this.getSharedPreferences("firsttimecheck",MODE_PRIVATE);
        SharedPreferences.Editor editor = firsttime.edit();
        settings=getSharedPreferences("settings",MODE_PRIVATE);
        String checkString = firsttime.getString("status","false");
        if(!checkString.equals("true")){
            editor.putString("status","true");
            editor.apply();
            editor.commit();
            settings.edit().putString("PIN","2020").commit();
            settings.edit().putString("alarm","on");
            Intent welcome = new Intent(MainActivity.this,Welcome_Activity.class);
            startActivity(welcome);
            finish();
       }
        requestPermissionsApp();
        intent = new Intent(MainActivity.this,confirmationScreen.class);
        choice=0;
        //Intent serviceIntent = new Intent(MainActivity.this,SMSReader.class);
        //startService(serviceIntent);
        ImageButton help = (ImageButton)findViewById(R.id.helpButton);
        help.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                backCount=0;
                startActivity(intent);
                finish();
                return true;
            }

        });
     }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press Back Button One More Time To Exit Application!!", Toast.LENGTH_SHORT).show();
        if(backCount==1){
            finish();
        }
        backCount=(backCount+1)%2;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestPermissionsApp(){
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        permissionsList.clear();
        permissionsNeeded.clear();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_CONTACTS);
            permissionsNeeded.add("Read Contacts");
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.SEND_SMS);
            permissionsList.add(Manifest.permission.RECEIVE_SMS);
            permissionsList.add(Manifest.permission.READ_SMS);
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionsNeeded.add("Send SMS");
            permissionsNeeded.add("Receive SMS");
            permissionsNeeded.add("Read SMS");
            permissionsList.add("Get GPS");
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

      //  requestPermissionsApp();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.settings:
                //your code here
                Intent tempSettings= new Intent(this,Settings.class);
                startActivity(tempSettings);
                finish();
                return true;
            case R.id.contacts:
                backCount=0;
                Intent i=new Intent(this,ContactSelection.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
