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

public class MainActivity extends AppCompatActivity {
    Intent intent;int choice,count;
    String n,n1;
    public static String file1 = "MyPREFERENCES";
    public static String file2 = "PREFERENCES";
    public static String file3 = "COUNT";
    SharedPreferences sharedpreferences,sp,shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(file1, Context.MODE_PRIVATE);
        sp = getSharedPreferences(file2, Context.MODE_PRIVATE);
        shared = getSharedPreferences(file3, Context.MODE_PRIVATE);
        intent = new Intent(MainActivity.this,alarmService.class);
        choice=0;
        Intent serviceIntent = new Intent(MainActivity.this,SMSReader.class);
        startService(serviceIntent);

    }
    public void onClick(View view){
        Log.i("zz",""+choice);
        switch(choice) {
            case 0:
                startService(intent);
                count = shared.getInt("count", 0);
                Log.d("COUNT", String.valueOf(count));
                for (int i = 1; i <= count; i++)
                {
                    n1 = sp.getString("value" + i, "");
                    n = sharedpreferences.getString(n1, "");
                    Log.d("sub",n);
                    try
                    {
                        if(!(n.isEmpty()))
                        {
                            sendSMS(n, "Help Me!!!!");
                        }
                    }
                    catch (Exception e){}
                    }



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
    public void sendSMS(String phoneNumber, String message)
    {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);

    }
}
