package com.speedyapps.keepyousafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class confirmationScreen extends AppCompatActivity {
    TextView textView ;
    EditText editText ;
    SharedPreferences sharedpreferences,sp,shared;
    String n,n1;
    int count,i;
    public static String file1 = "MyPREFERENCES";
    public static String file2 = "PREFERENCES";
    public static String file3 = "COUNT";
    Handler handler;
    Intent alarmIntent ;
    Runnable run;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);
        handler=new Handler();
        alarmIntent = new Intent(confirmationScreen.this,alarmService.class);
        textView= (TextView)findViewById(R.id.countDown);
        editText=(EditText)findViewById(R.id.editTextPIN);
        sharedpreferences = getSharedPreferences(file1, Context.MODE_PRIVATE);
        sp = getSharedPreferences(file2, Context.MODE_PRIVATE);
        shared = getSharedPreferences(file3, Context.MODE_PRIVATE);
        i=10;
        run = new Runnable() {
            @Override
            public void run() {
                textView.setText(""+i);
                if((i--)<=0){
                    distressCall();
                    handler.removeCallbacks(this);
                }
                else
                handler.postDelayed(this,1000);
            }
        };
        handler.post(run);
    }

    public void onCancel(View view){
        if((!editText.getText().toString().equals("2020"))&&textView.getText().toString().equals("0"))
            distressCall();
        else if(editText.getText().toString().equals("2020")){
            handler.removeCallbacks(run);
            Toast.makeText(this, "Cancelled Distress Signals!", Toast.LENGTH_SHORT).show();
            Intent main = new Intent(confirmationScreen.this,MainActivity.class);
            stopService(alarmIntent);
            startActivity(main);
        }

    }
    public void distressCall(){
        startService(alarmIntent);
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
    }
    public void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);

    }
}
