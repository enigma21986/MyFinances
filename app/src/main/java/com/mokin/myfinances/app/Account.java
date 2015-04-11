package com.mokin.myfinances.app;

import java.io.Serializable;

/**
 * Created by Alexey on 07.04.2015.
 */
public class Account implements Serializable, Comparable<Account> {

    private long _id;
    private String name;
    private long currency_id;
    private String comment;
    private int index;

    public Account(long _id, String name, double initial_balance, String comment)
    {
        this._id = _id;
        this.name = name;
        this.comment = comment;
    }

    public Account() {
        index = -1;
    }


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(long currency_id) {
        this.currency_id = currency_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Account another) {
        return name.compareTo(another.getName());
    }
}
