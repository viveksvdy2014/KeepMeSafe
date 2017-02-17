package com.speedyapps.keepyousafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    int choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
