package com.example.ivan.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class AddressBookActivity extends AppCompatActivity
{
    //<editor-fold desc="param">
    private static final String APP_PREFERENCES_Contact = "Contact";
    private static final String APP_PREFERENCES_CountContact = "CountContact";
    private static final int PERMISSION_REQUEST_CODE = 12;
    private final String LOG_TAG = "myLogs";
    private final String FILENAME = "file";

    private List<Contact> contacts = new ArrayList<>();
    private RVAdapter adapter;

    private String cardNumber = "";
    private String sum = "";
    private Integer q;
    private Integer many;
    //</editor-fold>

    //<editor-fold desc="onCreate">
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookaddres);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED)
            contacts = GetContact.getAll(AddressBookActivity.this);
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS
                    },
                    PERMISSION_REQUEST_CODE);

        adapter = new RVAdapter(contacts);
        rv.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
        cardNumber = preferences.getString(MainActivity.getAppPreferencesCartNumber(), "");
        sum = preferences.getString(MainActivity.getAPP_PREFERENCES_Sum(), "");
    }
    //</editor-fold>



    //<editor-fold desc="permition">
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                contacts = GetContact.getAll(this);
            }
        }
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //</editor-fold>

    public StringBuilder checkContacs()
    {
        StringBuilder str = new StringBuilder();
        contacts = adapter.getContacts();
        for (int i = 0; i < contacts.size(); i++)
            if (contacts.get(i).isCheck())
                str.append(contacts.get(i).get_name())
                        .append("!")
                        .append(contacts.get(i).get_phone())
                        .append("@");
        return str;
    }

    public int countContact()
    {
        contacts = adapter.getContacts();
        int j = 0;
        for (int i = 0; i < contacts.size(); i++)
            if (contacts.get(i).isCheck()) j++;
        return j;
    }

    public static String getAPP_PREFERENCES_Contact()
    {
        return APP_PREFERENCES_Contact;
    }

    public static String getAPP_PREFERENCES_CountContact()
    {
        return APP_PREFERENCES_CountContact;
    }

    public void goToSMS(View view)
    {
        String str = checkContacs().toString();
        contacts = ArrayListBild.mySplit(str);

        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_Contact, str);
        editor.commit();


        Intent intent = new Intent(this, DebtorsActivity.class);
        startActivity(intent);

        q = countContact();
        many = (int)Math.ceil(Integer.parseInt(sum) / q);

        String message = getString(R.string.message, many.toString(), cardNumber);
        StringBuilder numbers = new StringBuilder();

        for (int i = 0; i < contacts.size(); i++)
        {
            numbers.append("smsto:")
                    .append(contacts.get(i).get_phone())
                    .append(";");
        }

        Intent sms=new Intent(Intent.ACTION_SENDTO, Uri.parse(numbers.toString()));
        sms.putExtra("sms_body", message);
        startActivity(sms);
    }
}
