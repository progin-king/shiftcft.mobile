package com.example.ivan.myapplication;

import java.util.ArrayList;

public class ArrayListBild
{
    public static ArrayList<Contact> mySplit(String str)
    {
        ArrayList<Contact> list = new ArrayList<>();
        String firstName = str.substring(0, str.indexOf("!"));
        String[] s = str.split("!");
        list.add(new Contact());
        list.get(0).setName(firstName);
        for (int i = 1; i < s.length - 1; i++)
        {
            String phone = s[i].substring(0,s[i].indexOf("@"));
            list.get(i - 1).setPhone(phone);
            String name = s[i].substring(s[i].indexOf("@") + 1, s[i].length());
            list.add(new Contact());
            list.get(i).setName(name);
        }
        String lastPhone = s[s.length - 1].substring(0, s[s.length - 1].indexOf("@"));
        list.get(list.size() - 1).setPhone(lastPhone);
        return list;
    }
}

