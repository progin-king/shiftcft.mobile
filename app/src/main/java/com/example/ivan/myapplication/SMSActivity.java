package com.example.ivan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMSActivity extends AppCompatActivity
{
    private List<Contact> contacts = new ArrayList<>();
    private EditText message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);

//        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
//        String cardNumber = preferences.getString(MainActivity.getAppPreferencesCartNumber(), "");
//        String sum = preferences.getString(MainActivity.getAPP_PREFERENCES_Sum(), "");
//        Integer q = preferences.getInt(AddressBookActivity.getAPP_PREFERENCES_CountContact(), -1);
//
//        Integer many = (int)Math.ceil(Integer.parseInt(sum) / q);
//
//
//        ((EditText)findViewById(R.id.message)).setText(getString(R.string.message, many.toString(), cardNumber));
    }
    public void goToAny(View view)
    {
        Intent intent = new Intent(this, DebtorsActivity.class);
        startActivity(intent);

        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
        String str = preferences.getString(AddressBookActivity.getAPP_PREFERENCES_Contact(), "");

        contacts = ArrayListBild.mySplit(str);

        message = findViewById(R.id.message);
        String messageText= message.getText().toString();

        StringBuilder numbers = new StringBuilder();

        for (int i = 0; i < contacts.size(); i++)
        {
            numbers.append("smsto:")
                    .append(contacts.get(i).get_phone())
                    .append(";");
        }

        Intent sms=new Intent(Intent.ACTION_SENDTO, Uri.parse(numbers.toString()));
        sms.putExtra("sms_body", messageText);
        startActivity(sms);
    }
}
