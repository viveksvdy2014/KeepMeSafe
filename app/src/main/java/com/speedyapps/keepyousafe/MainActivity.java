package com.speedyapps.keepyousafe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    int choice;
    SharedPreferences firsttime;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 291;
    int backCount=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissionsApp();
        firsttime = this.getSharedPreferences("firsttimecheck",MODE_PRIVATE);
        SharedPreferences.Editor editor = firsttime.edit();
        String checkString = firsttime.getString("status","false");
        if(!checkString.equals("true")){
            editor.putString("status","true");
            editor.apply();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.alert_dark_frame)
                    .setTitle("Instructions!")
                    .setMessage("How To Use : - \nStep 1 : - Add Trusted Contacts from your Existing Android Contacts to whom Distress Messages are to be Sent! \nStep 2 : - In Case of an Emergency, Open The App and Long Press The HELP Button! \nPlease Also Do the Following to Ensure Proper Functioning of the Application! \n1)If you have a Dual SIM phonw, Please set any one of them as default for sending SMS from settings > SIM cards! \n2) Add this app as an exception in any Task Killer apps if you are using any! \n3) Setup a PIN Code in the Next Screen for Security ! ")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("zz","OK");
                        }
                    }).show();
        }
        intent = new Intent(MainActivity.this,confirmationScreen.class);
        choice=0;
        Intent serviceIntent = new Intent(MainActivity.this,SMSReader.class);
        startService(serviceIntent);
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




    public void contact(View v)
    {
        backCount=0;
        Intent i=new Intent(this,ContactSelection.class);
        startActivity(i);

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
            permissionsNeeded.add("Send SMS");
            permissionsNeeded.add("Receive SMS");
            permissionsNeeded.add("Read SMS");
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
}
