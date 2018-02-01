package com.example.ivan.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressBookActivity extends AppCompatActivity
{
    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private static final int PERMISSION_REQUEST_CODE = 12;

    private List<Contact> contacts = new ArrayList<>();
    private RVAdapter adapter;


    //<editor-fold desc="onCreate">
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
            contacts = getAll(AddressBookActivity.this);
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

    //<editor-fold desc="getAll">
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
                while (pCur.moveToNext())
                {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));

                    ArrayList<String> curPhones = new ArrayList<>();

                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);

                    }
                    curPhones.add(pCur.getString(0));

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
    //</editor-fold>

    //<editor-fold desc="adapter">
    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ContactViewHolder>
    {
        private CheckBox checkBox;
        private int lastCheckedPosition = -1;

        public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            CardView cv;
            TextView name;
            TextView phone;
            CheckBox checkBox;


            ContactViewHolder(final View itemView)
            {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                name = (TextView)itemView.findViewById(R.id.contact_name);
                phone = (TextView)itemView.findViewById(R.id.contact_phone);

//                checkBox.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        lastCheckedPosition = getAdapterPosition();
//                        //because of this blinking problem occurs so
//                        //i have a suggestion to add notifyDataSetChanged();
//                        //   notifyItemRangeChanged(0, list.length);//blink list problem
//                        notifyDataSetChanged();
//
//                    }
//                });


            }
            @Override
            public void onClick(View v)
            {
                String contact = name.getText() + " " + phone.getText();
                String _name = name.getText().toString();
                String _phone = phone.getText().toString();
                Toast.makeText(AddressBookActivity.this, contact, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddressBookActivity.this, SMSActivity.class);
                intent.putExtra("contact", contact);
                intent.putExtra("_name", _name);
                intent.putExtra("_phone", _phone);
                startActivity(intent);
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

//            contactViewHolder.checkBox.setChecked(i == lastCheckedPosition);
//            if (contactViewHolder.checkBox.isChecked())
        }
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }

//        private int getAdapterPosition()
//        {
//            for (int i = 0; i < contacts.size(); i++)
//            {
//
//            }
//            return -1;
//        }
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
                contacts = getAll(this);
            }
        }
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //</editor-fold>

    public void goToSMS(View view)
    {
        Intent intent = new Intent(this, SMSActivity.class);
        startActivity(intent);
    }

    public void createContacs()
    {

    }
}
