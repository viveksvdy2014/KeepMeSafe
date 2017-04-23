package com.speedyapps.keepyousafe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ContactSelection extends Activity {
    private static final int RESULT_PICK_CONTACT = 85500;
    public static String file1 = "MyPREFERENCES";
    public static String file2 = "PREFERENCES";
    public static String file3 = "COUNT";


    private TextView textView1;
    SharedPreferences sharedpreferences, sp, shared, get, no, get1;
    SharedPreferences.Editor ed, editor, num;
    private TextView textView2;
    ArrayList<String> a;
    ListView l;
    ArrayAdapter arr;
    public static int times = 0;
    int count, indexName, indexNumber;
    String n;
    String n1;
    String name;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);
        //textView1 = (TextView) findViewById(R.id.textView1);
        //textView2 = (TextView) findViewById(R.id.textView2);
        l = (ListView) findViewById(R.id.list);
        sharedpreferences = getSharedPreferences(file1, Context.MODE_PRIVATE);
        sp = getSharedPreferences(file2, Context.MODE_PRIVATE);
        shared = getSharedPreferences(file3, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        ed = sp.edit();
        num = shared.edit();


    }


    public void pickContact(View v) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }

    }


    private void contactPicked(Intent data) {
        Cursor cursor = null;
        times = shared.getInt("count", 0);
        Log.d("times", String.valueOf(times));


        // getData() method will have the Content Uri of the selected contact
        Uri uri = data.getData();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(uri, null, null, null, null);
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor names = getContentResolver().query(uri, projection, null, null, null);
        indexName = names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        indexNumber = names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        names.moveToLast();
        do {
            name = names.getString(indexName);

            number = names.getString(indexNumber);

            editor.putString(name, number);
            editor.apply();
            editor.commit();
            if (editor.commit()) {

                Log.d("subtimes1", Integer.toString(times));
                times++;
                Log.d("subtimes2", Integer.toString(times));
                num.putInt("count", times);
                num.apply();
                num.commit();
                Log.d("name", name);


                ed.putString("value" + times, name);
                ed.apply();
                ed.commit();


                Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "not entered", Toast.LENGTH_SHORT).show();
            }

        }
        while (names.moveToNext());

    }

    public void retrive(View v)
    {
        try {
            count = shared.getInt("count", 0);
            a = new ArrayList<>();
            arr = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, a);
            l.setAdapter(arr);
            Log.d("subcount", String.valueOf(count));

            for (int i = 1; i <= count; i++) {
                n1 = sp.getString("value" + i, "");
                n = sharedpreferences.getString(n1, "");
                Log.d("subn1", n1);
                Log.d("subn", n);

                try
                {
                    if(!(n1.isEmpty()&&n.isEmpty()))
                    {
                        a.add(n1 + "(" + n + ")");
                        arr.notifyDataSetChanged();
                        Log.d("sub", String.valueOf(a));
                    }
                }
                catch (Exception e){}

            }
        } catch (Exception e) {}

    }


    public void delete(View v)
    {
        try {
            int pos = l.getCheckedItemPosition();
            if (pos > -1) {
                try {
                    String b, b1 = null, b2;
                    b = a.get(pos).toString();

                    String s11 = b.split("\\(")[0];
                    Log.d("del", s11);


                    Iterator iter = sp.getAll().entrySet().iterator();

                    {
                        while (iter.hasNext()) {
                            Map.Entry pair = (Map.Entry) iter.next();
                            if (s11.equals(pair.getValue())) {
                                b1 = pair.getKey().toString();
                                Log.d("b1", b1);
                            }
                        }
                        // Check the value here

                        a.remove(pos);

                        arr.notifyDataSetChanged();

                        sp.edit().remove(b1).commit();
                        sharedpreferences.edit().remove(s11).commit();


                        Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }


            } else {
                Toast.makeText(this, "Not Deleted", Toast.LENGTH_SHORT).show();
            }
            arr.notifyDataSetChanged();
        }
        catch (Exception e){

        }
    }

}

