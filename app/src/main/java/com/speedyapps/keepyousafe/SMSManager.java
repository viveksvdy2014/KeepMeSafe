package com.speedyapps.keepyousafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSManager extends BroadcastReceiver {
    private String TAG = SMSManager.class.getSimpleName();
    String latitudepart=null,longitudepart=null;
    Double latitude,longitude;
    SharedPreferences sp;
    public SMSManager() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
        sp=context.getSharedPreferences("locationinfo",Context.MODE_PRIVATE);
        sp.edit().putString("status","stoped").commit();
        String str = "",sender="";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                sender += msgs[i].getOriginatingAddress();
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                // Newline <img draggable="false" class="emoji" alt="🙂" src="https://s.w.org/images/core/emoji/72x72/1f642.png">
                str += "\n";
            }
            Toast.makeText(context, "`Sender : "+sender+" Message : "+str, Toast.LENGTH_SHORT).show();
        }

            if(str.contains("Help Me!!!!")){
                //myContext.startService(alarm);
                String[] coordinates = str.split(">");
                latitudepart=coordinates[1].split(",")[0];
                longitudepart=coordinates[1].split(",")[1];
                sp.edit().putString("latitude",latitudepart).putString("longitude",longitudepart).commit();
                Log.i("lat","lat"+latitudepart);
                Intent mapsIntent = new Intent(context, MapsActivity.class);
                mapsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //mapsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                if(!sp.getString("status","null").equals("running")) {
//                    context.startActivity(mapsIntent);
//                }
//                if(sp.getString("status","null").equals("paused")){
                    context.startActivity(mapsIntent);
                }
            }
        }

