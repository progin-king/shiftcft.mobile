package com.example.ivan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class SMSActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);

        Intent intent = getIntent();
        //Поделить сумму на количество выбранных людей!!!

        String text = getString(R.string.message, intent.getStringExtra("Sum"),intent.getStringExtra("Cart Number"));
        final EditText editText = (EditText)findViewById(R.id.message);
        editText.setText(text);

    }
}
