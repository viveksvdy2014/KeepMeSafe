package com.speedyapps.keepyousafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class power_receiver extends BroadcastReceiver {
    int count=0;
    SharedPreferences sp;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent main = new Intent(context,confirmationScreen.class);
        main.putExtra("context","1");
        sp=context.getSharedPreferences("counter", MODE_PRIVATE);

        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sp.edit().putString("count",""+(count++)).commit();
        if((Integer.parseInt(sp.getString("count","0"))==3)){
            sp.edit().putString("count","0").commit();
            count=0;
            context.startActivity(main);
        }
        if(!sp.getString("count","0").equals("0")){
            new CountDownTimer(5000, 1000) {
                public void onTick(long millisUntilFinished) {
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    //
                    if(!sp.getString("count","0").equals("0")){
                        sp.edit().putString("count","0").commit();
                        count=0;
                    }
                }
            }.start();
        }
        }
    }





