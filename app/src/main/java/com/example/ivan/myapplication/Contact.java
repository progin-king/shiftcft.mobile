package com.example.ivan.myapplication;

import java.io.Serializable;

public class Contact implements Serializable
{
    private String _name = "";
    private String _phone = "";
    private int _my_id;
    private boolean check = false;

    public void setName(String name) {
        _name = name;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public void setMyId(int my_id) {
        _my_id = my_id;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public int get_my_id() {
        return _my_id;
    }

    public String get_name() {
        return _name;
    }

    public String get_phone() {
        return _phone;
    }

    public String getall()
    {
        return _my_id + _name + _phone;
    }
}
