package com.example.ivan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.time.Instant;

public class SplashScreenActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
        String str = preferences.getString(AddressBookActivity.getAPP_PREFERENCES_Contact(), "");

        if (str.equals(""))
        {
            Intent instant = new Intent(this, MainActivity.class);
            startActivity(instant);
            finish();
        }
        else
        {
            Intent instant = new Intent(this, DebtorsActivity.class);
            startActivity(instant);
            finish();
        }
    }
}
