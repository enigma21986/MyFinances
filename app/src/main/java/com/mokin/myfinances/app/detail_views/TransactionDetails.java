package com.mokin.myfinances.app.detail_views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mokin.myfinances.app.R;


public class TransactionDetails extends ActionBarActivity {

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
}
