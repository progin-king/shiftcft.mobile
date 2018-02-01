package com.example.ivan.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddresBookActivity extends AppCompatActivity
{
    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private static final int PERMISSION_REQUEST_CODE = 12;

    private List<Contact> contacts = new ArrayList<>();
    private RVAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookaddres);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED)
            contacts = getAll(AddresBookActivity.this);
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS,
                    },
                    PERMISSION_REQUEST_CODE);

        adapter = new RVAdapter(contacts);
        rv.setAdapter(adapter);

    }

    public static ArrayList<Contact> getAll(Context context)
    {
        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if(pCur != null)
        {
            if(pCur.getCount() > 0)
            {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));
                    ArrayList<String> curPhones = new ArrayList<>();
                    if (phones.containsKey(contactId))
                    {
                        curPhones = phones.get(contactId);
                    }
                    curPhones.add(pCur.getString(pCur.getColumnIndex(PHONE_CONTACT_ID)));
                    phones.put(contactId, curPhones);
                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,
                        DISPLAY_NAME + " ASC");
                if (cur != null)
                {
                    if (cur.getCount() > 0)
                    {
                        ArrayList<Contact> contacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                            if(phones.containsKey(id))
                            {
                                Contact con = new Contact();
                                con.setMyId(id);
                                con.setName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));
                                con.setPhone(TextUtils.join(",", phones.get(id).toArray()));
                                contacts.add(con);
                            }
                        }
                        return contacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ContactViewHolder>
    {
        public class ContactViewHolder extends RecyclerView.ViewHolder
        {
            CardView cv;
            TextView name;
            TextView phone;
            ContactViewHolder(View itemView)
            {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                name = (TextView)itemView.findViewById(R.id.contact_name);
                phone = (TextView)itemView.findViewById(R.id.contact_phone);
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
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                contacts = getAll(this);
            }
        }
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
