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
    public static class readSMS extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            final SmsManager sms = SmsManager.getDefault();
            final Intent alarm = new Intent(myContext,alarmService.class);
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        Log.d("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                        int count=0;
                        Handler handler = new Handler();
                        if(message.equals("Help Me!!!!")){
                            myContext.startService(alarm);
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    myContext.stopService(alarm);

                                }
                            },5000);
                        }

                        // Show alert
                        int duration = Toast.LENGTH_LONG;
                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }


        }
    }
}
