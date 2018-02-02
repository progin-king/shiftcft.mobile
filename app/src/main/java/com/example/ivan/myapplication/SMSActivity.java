package com.example.ivan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class SMSActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);

        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
        String cardNumber = preferences.getString(MainActivity.getAppPreferencesCartNumber(), "");
        String sum = preferences.getString(MainActivity.getAPP_PREFERENCES_Sum(), "");
        Integer q = preferences.getInt(AddressBookActivity.getAPP_PREFERENCES_CountContact(), -1);

        Integer many = (int)Math.ceil(Integer.parseInt(sum) / q);


        Toast.makeText(this, many.toString(), Toast.LENGTH_SHORT).show();

        ((EditText)findViewById(R.id.message)).setText(getString(R.string.message, many.toString(), cardNumber));

        //Поделить сумму на количество выбранных людей!!!

    }
}
