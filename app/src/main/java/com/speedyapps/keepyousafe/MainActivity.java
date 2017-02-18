package com.speedyapps.keepyousafe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    int choice;
    SharedPreferences firsttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                onClick();
                return true;
            }

        });

    }
    public void onClick(){
            Log.i("zz",""+choice);
            switch(choice) {
                case 0:
                    startActivity(intent);
                    break;
                case 1: stopService(intent);
                    break;
            }

            choice=(choice+1)%2;
        }

    public void contact(View v)
    {

        Intent i=new Intent(this,ContactSelection.class);
        startActivity(i);

    }

}
