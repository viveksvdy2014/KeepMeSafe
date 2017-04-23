package com.speedyapps.keepyousafe;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReader extends Service {
    public SMSReader() {
    }
    private static Context myContext;
    @Override
    public void onCreate() {
        Toast.makeText(this, "Created Service", Toast.LENGTH_SHORT).show();
        super.onCreate();
        myContext = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public  class readSMS extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            final Intent alarm = new Intent(myContext,alarmService.class);
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            Toast.makeText(context, "Received Message", Toast.LENGTH_SHORT).show();
                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage rcvdmsg =null;
                        String message = null;
                        for(int p=0;p<pdusObj.length;p++){
                            rcvdmsg=SmsMessage.createFromPdu((byte[])pdusObj[p]);
                        }
                        byte[] data=null;
                        data=rcvdmsg.getUserData();
                        if(data!=null){
                            for(int index=0;index<data.length;index++){
                                message+=Character.toString((char)data[index]);
                            }
                        }
                        int count=0;
                        Handler handler = new Handler();
                        if(message.contains("Help Me!!!!")){
                            //myContext.startService(alarm);
                            String[] coordinates = message.split(">");
                            String latitudepart=coordinates[1].split(",")[0];
                            String longitudepart=coordinates[1].split(",")[1];
                            Log.i("lat","lat"+latitudepart);
                            Intent mapsIntent = new Intent(myContext,MapsActivity.class);
                            mapsIntent.putExtra("latitude",Double.parseDouble(latitudepart));
                            mapsIntent.putExtra("longitude",Double.parseDouble(longitudepart));
                            startActivity(mapsIntent);
                        }

                        // Show alert
                        int duration = Toast.LENGTH_LONG;
                    } // end for loop
                } // bundle is null
            }


        }
    }
