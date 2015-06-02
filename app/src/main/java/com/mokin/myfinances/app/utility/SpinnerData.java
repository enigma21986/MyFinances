package com.mokin.myfinances.app.utility;


public class SpinnerData {

    private int key;
    private String value;

    public SpinnerData(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
