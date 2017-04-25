package com.speedyapps.keepyousafe;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.List;

public class confirmationScreen extends AppCompatActivity implements LocationListener {

    public static int SMS_SEND_INTERVAL=2*60*1000;
    LocationManager locationManager;
    TextView textView;
    EditText editText;
    Double latitude = null, longitude = null,newlat=null,newlong=null;
    Handler timehandler;
    SharedPreferences sharedpreferences, sp, shared,settings;
    String n;
    Intent alarmIntent;
    String n1;
    LocationProvider provider;
    int count, i;
    public static String file1 = "MyPREFERENCES";
    public static String file2 = "PREFERENCES";
    public static String file3 = "COUNT";
    Handler handler, handler2;
    Runnable run, run2,run3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

    }

    public void distressCall() {
        if(settings.getString("alarm","on").equals("on")) {
            startService(alarmIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = getLastKnownLocation();
        if(location!=null){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        count = shared.getInt("count", 0);
        Log.d("COUNT", String.valueOf(count));
        for (int i = 1; i <= count; i++) {
            n1 = sp.getString("value" + i, "");
            n = sharedpreferences.getString(n1, "");
            Log.d("sub", n);
            try {
                if (!(n.isEmpty())) {
                    sendSMS(n, "Help Me!!!!>" + latitude + "," + longitude);
                }
            } catch (Exception e) {
            }
        }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,1,this);
        run3 = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(confirmationScreen.this, "Old longitude :"+longitude+" New Longitude : "+newlong, Toast.LENGTH_SHORT).show();
                    }
                });
                if (longitude != newlong && newlong != null) {
                    count = shared.getInt("count", 0);
                    Log.d("COUNT", String.valueOf(count));
                    for (int i = 1; i <= count; i++) {
                        n1 = sp.getString("value" + i, "");
                        n = sharedpreferences.getString(n1, "");
                        Log.d("sub", n);
                        try {
                            if (!(n.isEmpty())) {
                                sendSMS(n, "Help Me!!!!>" + newlat + "," + newlong);
                            }
                        } catch (Exception e) {
                        }
                    }
                    latitude=newlat;
                    longitude=newlong;
                }
                timehandler.postDelayed(this, SMS_SEND_INTERVAL);
            }
        };
        timehandler.postDelayed(run3,SMS_SEND_INTERVAL);

    }

    public void sendSMS(String phoneNumber, String message)
    {

        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

// For when the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                     //   Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                     //   Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                     //   Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                      //  Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                      //  Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

// For when the SMS has been delivered
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                     //   Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                     //   Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED));

// Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS
        smsManager.sendTextMessage(phoneNumber, null, message, sentPendingIntent, deliveredPendingIntent);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Enter the PIN CODE in order to Go Back!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(this, "new Location : "+latitude+","+longitude, Toast.LENGTH_SHORT).show();
        newlat=location.getLatitude();
        newlong=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled Provider", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(run);
        handler2.removeCallbacks(run2);
        timehandler.removeCallbacks(run3);
    }

    @Override
    public void onProviderDisabled(String provider) {
            final AlertDialog.Builder builder =  new AlertDialog.Builder(this);
            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            final String message = "Please Enable GPS to continue !";

            builder.setMessage(message)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    startActivity(new Intent(action));
                                    d.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    d.cancel();
                                }
                            });
            builder.create().show();
        }
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            Log.d("last known location, provider: %s, location: %s", provider+l);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("found best last known location: %s", ""+l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        func();
    }

    public void func(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        settings=getSharedPreferences("settings",MODE_PRIVATE);
        if (!enabled) {
            Toast.makeText(this, "Please Enable GPS to use KeepMeSafe", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        else {
            provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
           // Toast.makeText(this, "Provider" + provider, Toast.LENGTH_SHORT).show();
            handler = new Handler();
            handler2 = new Handler();
            timehandler = new Handler();
            alarmIntent = new Intent(confirmationScreen.this, alarmService.class);
            textView = (TextView) findViewById(R.id.countDown);
            editText = (EditText) findViewById(R.id.editTextPIN);
            sharedpreferences = getSharedPreferences(file1, Context.MODE_PRIVATE);
            sp = getSharedPreferences(file2, Context.MODE_PRIVATE);
            shared = getSharedPreferences(file3, Context.MODE_PRIVATE);
            i = Integer.parseInt(settings.getString("timer", "10"));
            run = new Runnable() {
                @Override
                public void run() {
                    textView.setText("" + i);
                    if ((i) == 0) {
                        distressCall();
                        handler.removeCallbacks(this);
                    } else {
                        i--;
                        if (i <= 0)
                            i = 0;
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.post(run);
            run2 = new Runnable() {
                @Override
                public void run() {
                    if (editText.getText().toString().equals(settings.getString("PIN", "2123789564237572345278356834"))) {
                        stopService(alarmIntent);
                        handler.removeCallbacks(run);
                        handler2.removeCallbacks(this);
                        timehandler.removeCallbacks(run3);
                        Toast.makeText(confirmationScreen.this, "Distress Calls Cancelled!", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(confirmationScreen.this, MainActivity.class);
                        if(getIntent().getStringExtra("context").equals("1"))
                            finish();
                        else {
                            startActivity(main);
                            finish();
                        }
                    } else
                        handler2.postDelayed(this, 1000);


                }
            };
            handler2.post(run2);
        }


    }

}
