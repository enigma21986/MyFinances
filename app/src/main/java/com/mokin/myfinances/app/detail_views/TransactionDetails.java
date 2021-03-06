package com.mokin.myfinances.app.detail_views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.utility.DatePickerFragment;
import com.mokin.myfinances.app.utility.TimePickerFragment;


public class TransactionDetails extends ActionBarActivity implements DatePickerFragment.MyDateSetCallbacks, TimePickerFragment.MyTimeSetCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_detail_layout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new TransactionDetailsFragment())
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onDateSet(int year, int month, int day) {
        TransactionDetailsFragment fragment = (TransactionDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detail_container);
        fragment.setTransactionDate(year, month, day);
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        TransactionDetailsFragment fragment = (TransactionDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detail_container);
        fragment.setTransactionTime(hourOfDay, minute);
    }
}
