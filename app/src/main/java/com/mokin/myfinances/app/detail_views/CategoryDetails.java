package com.mokin.myfinances.app.detail_views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mokin.myfinances.app.R;

public class CategoryDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail_fragment);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.category_detail_container, new CategoryDetailsFragment())
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
