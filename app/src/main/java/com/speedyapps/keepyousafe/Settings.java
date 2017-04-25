package com.speedyapps.keepyousafe;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    String value;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    Switch sw;
    SharedPreferences sp;
    Runnable runnable;
    String spinnerString;
    EditText et;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinner=(Spinner)findViewById(R.id.spinner);
        arrayList=new ArrayList<>();
        arrayList.add("NORMAL");
        arrayList.add("SATELITE");
        arrayList.add("TERRAIN");
        arrayList.add("HYBRID");
        sw=(Switch)findViewById(R.id.switch1);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);
        sp=getSharedPreferences("settings",MODE_PRIVATE);
        if(sp.getString("alarm","on").equals("on")){
            sw.setChecked(true);
            sp.edit().putString("alarm","on").commit();
        }
        else
            sw.setChecked(false);

        et = (EditText)findViewById(R.id.editText2);
        et.setHint(sp.getString("timer","10").toString());
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp.edit().putString("alarm","on").commit();
                }
                else
                    sp.edit().putString("alarm","off").commit();
            }

        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                spinnerString= spinner.getItemAtPosition(i).toString();
                sp.edit().putString("maptype",spinnerString).commit();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });

    }

    public void changePIN(View view){
        Intent intent = new Intent(Settings.this,changePassword.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(!et.getText().toString().isEmpty()){
            sp.edit().putString("timer",et.getText().toString()).commit();
        }
        Intent main = new Intent(Settings.this,MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
