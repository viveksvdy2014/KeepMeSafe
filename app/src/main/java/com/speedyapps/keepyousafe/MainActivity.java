package com.speedyapps.keepyousafe;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent intent;int choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this,alarmService.class);
        choice=0;
        Intent serviceIntent = new Intent(MainActivity.this,SMSReader.class);
        startService(serviceIntent);

    }
    public void onClick(View view){
        Log.i("zz",""+choice);
        switch(choice) {
            case 0:  startService(intent);
                sendSMS("8606670880","Help Me!!!!");
                break;
            case 1: stopService(intent);
                break;
        }

        choice=(choice+1)%2;
    }
    public void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
