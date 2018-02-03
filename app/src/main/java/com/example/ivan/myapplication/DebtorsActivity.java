package com.example.ivan.myapplication;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DebtorsActivity extends AppCompatActivity
{
    private List<Contact> contacts = new ArrayList<>();
    private RVAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debtors_activity);

        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), MODE_PRIVATE);
        String str = preferences.getString(AddressBookActivity.getAPP_PREFERENCES_Contact(), "");

        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        contacts = ArrayListBild.mySplit(str);

        adapter = new RVAdapter(contacts);
        rv.setAdapter(adapter);
    }

}
