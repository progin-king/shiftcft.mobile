package com.example.ivan.myapplication;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

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
