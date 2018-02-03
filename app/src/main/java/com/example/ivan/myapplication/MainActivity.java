package com.example.ivan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    private static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_CART_NUMBER = "Card Number";
    private static final String APP_PREFERENCES_Sum = "Sum";

    private static final int MAX_LENGHT = 4 ;

    private EditText editTextCard1;
    private EditText editTextCard2;
    private EditText editTextCard3;
    private EditText editTextCard4;
    private EditText editTextSum;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCard1 = (EditText)findViewById(R.id.editText4);
        editTextCard1.requestFocus();
        editTextCard1.setFocusableInTouchMode(true);
        editTextCard2 = (EditText)findViewById(R.id.editText3);
        editTextCard3 = (EditText)findViewById(R.id.editText5);
        editTextCard4 = (EditText)findViewById(R.id.editText6);
        editTextSum = (EditText)findViewById(R.id.editText);

        button = (Button)findViewById(R.id.button);
        button.setEnabled(false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (checkButton())
                    button.setEnabled(true);
                else
                    button.setEnabled(false);

                if (editTextCard1.getText().length() == MAX_LENGHT)
                {
                    editTextCard2.requestFocus();
                    editTextCard2.setFocusableInTouchMode(true);
                    if (editTextCard2.getText().length() == MAX_LENGHT)
                    {
                        editTextCard3.requestFocus();
                        editTextCard3.setFocusableInTouchMode(true);
                        if (editTextCard3.getText().length() == MAX_LENGHT)
                        {
                            editTextCard4.requestFocus();
                            editTextCard4.setFocusableInTouchMode(true);
                            if (editTextCard4.getText().length() == MAX_LENGHT)
                            {
                                editTextSum.requestFocus();
                                editTextSum.setFocusableInTouchMode(true);
                            }
                        }
                    }
                }
            }
        };

        editTextCard1.addTextChangedListener(watcher);
        editTextCard2.addTextChangedListener(watcher);
        editTextCard3.addTextChangedListener(watcher);
        editTextCard4.addTextChangedListener(watcher);
        editTextSum.addTextChangedListener(watcher);

    }
    public void goToAddresBook(View view)
    {
        String str = editTextCard1.getText().toString() + " " + editTextCard2.getText().toString()
                + " " + editTextCard3.getText().toString() + " " + editTextCard4.getText().toString();

        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_CART_NUMBER, str);
        editor.putString(APP_PREFERENCES_Sum, editTextSum.getText().toString());
        if (editor.commit())
            Log.i("1234", "1234");


        Intent intent = new Intent(this, AddressBookActivity.class);
        startActivity(intent);

    }

    public static String getAppPreferences() {
        return APP_PREFERENCES;
    }

    public static String getAppPreferencesCartNumber() {
        return APP_PREFERENCES_CART_NUMBER;
    }

    public static String getAPP_PREFERENCES_Sum() {
        return APP_PREFERENCES_Sum;
    }

    public boolean checkButton()
    {
        return (editTextCard1.getText().length() == MAX_LENGHT && editTextCard2.getText().length() == MAX_LENGHT
                && editTextCard2.getText().length() == MAX_LENGHT && editTextCard2.getText().length() == MAX_LENGHT
                && editTextSum.getText().length() != 0) ? true : false;
    }
}
