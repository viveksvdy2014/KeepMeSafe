package com.speedyapps.keepyousafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class changePassword extends AppCompatActivity {

    SharedPreferences sp;
    EditText oldP,newP,confirmP;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldP=(EditText)findViewById(R.id.editText5);
        newP=(EditText)findViewById(R.id.editText6);
        confirmP=(EditText)findViewById(R.id.editText7);
        tv=(TextView)findViewById(R.id.textView15);
        sp=getSharedPreferences("settings",MODE_PRIVATE);
    }
    public void onPINClick(View view){
        if(oldP.getText().toString().equals(sp.getString("PIN","null"))){
            if(newP.getText().toString().equals(confirmP.getText().toString())){
                Toast.makeText(this, "Successfully Changed PIN", Toast.LENGTH_SHORT).show();
                sp.edit().putString("PIN", newP.getText().toString()).commit() ;
                Intent intent = new Intent(changePassword.this,Settings.class);
                startActivity(intent);
                finish();
            }
            else
            {
                tv.setText("Entered new Passwords Don't Match!");
            }

        }
        else{
            tv.setText("Old Password Wrong!");
        }
    }
}
