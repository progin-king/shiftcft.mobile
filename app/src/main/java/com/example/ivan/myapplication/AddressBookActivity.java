package com.example.ivan.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddressBookActivity extends AppCompatActivity
{
    //<editor-fold desc="param">


    private static final String APP_PREFERENCES_Contact = "Contact";
    private static final String APP_PREFERENCES_CountContact = "CountContact";
    private static final int PERMISSION_REQUEST_CODE = 12;

    private List<Contact> contacts = new ArrayList<>();
    private RVAdapter adapter;
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
                            Manifest.permission.READ_CONTACTS,
                    },
                    PERMISSION_REQUEST_CODE);

        adapter = new RVAdapter(contacts);
        rv.setAdapter(adapter);

    }
    //</editor-fold>

    //<editor-fold desc="adapter">
    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ContactViewHolder>
    {
        public class ContactViewHolder extends RecyclerView.ViewHolder
        {
            CardView cv;
            TextView name;
            TextView phone;
            private CheckBox checkBox;


            ContactViewHolder(final View itemView)
            {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                name = (TextView)itemView.findViewById(R.id.contact_name);
                phone = (TextView)itemView.findViewById(R.id.contact_phone);
                checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);

                cv.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        checkBox.setChecked(!checkBox.isChecked());
                        if (checkBox.isChecked())
                            contacts.get(getPosition()).setCheck(true);
                        else
                            contacts.get(getPosition()).setCheck(false);
                    }
                });
            }
        }

        private List<Contact> contacts;
        RVAdapter(List<Contact> contacts)
        {
            this.contacts = contacts;
        }

        @Override
        public int getItemCount()
        {
            return contacts.size();
        }
        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
            ContactViewHolder pvh = new ContactViewHolder(v);
            return pvh;
        }
        @Override
        public void onBindViewHolder(ContactViewHolder contactViewHolder, int i)
        {
            contactViewHolder.name.setText(contacts.get(i).get_name());
            contactViewHolder.phone.setText(contacts.get(i).get_phone());

        }

        public List<Contact> getContacts() {
            return contacts;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }
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
                        .append(" ")
                        .append(contacts.get(i).get_phone())
                        .append("#");

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
        SharedPreferences preferences = getSharedPreferences(MainActivity.getAppPreferences(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_Contact, checkContacs().toString());
        editor.putInt(APP_PREFERENCES_CountContact, countContact());
        editor.commit();

        Intent intent = new Intent(this, SMSActivity.class);
        startActivity(intent);
    }
}
