package com.example.ivan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity
{
    private EditText editTextCard1;
    private EditText editTextCard2;
    private EditText editTextCard3;
    private EditText editTextCard4;
    private EditText editTextSum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCard1 = (EditText)findViewById(R.id.editText4);
        editTextCard2 = (EditText)findViewById(R.id.editText3);
        editTextCard3 = (EditText)findViewById(R.id.editText5);
        editTextCard4 = (EditText)findViewById(R.id.editText6);
        editTextSum = (EditText)findViewById(R.id.editText);
    }
    public void goToAddresBook(View view)
    {
        String str = editTextCard1.getText().toString() + " " + editTextCard2.getText().toString()
                + " " + editTextCard3.getText().toString() + " " + editTextCard4.getText().toString();
        Intent intent = new Intent(this, SMSActivity.class);
        intent.putExtra("Cart Number", str);
        intent.putExtra("Sum", editTextSum.getText().toString());
        Intent intentForSMS = new Intent(this, SMSActivity.class);
        startActivity(intent);
    }
}
