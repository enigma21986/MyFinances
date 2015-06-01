package com.mokin.myfinances.app.detail_views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Button;

import com.mokin.myfinances.app.CurrencyListDialogFragment;
import com.mokin.myfinances.app.R;

import java.util.Currency;

public class AccountDetails extends ActionBarActivity implements CurrencyListDialogFragment.MyCurrencySelectedCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_detail_fragment);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.account_detail_container, new AccountDetailsFragment())
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_account_details, menu);
        return true;
    }


    @Override
    public void onCurrencySelected(String code) {

        AccountDetailsFragment fragment = (AccountDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.account_detail_container);

        fragment.setCurrencyCode(code);
        Button btnSelectCurrency = (Button) fragment.getView().findViewById(R.id.btn_select_currency);

        Currency currency = Currency.getInstance(code);
        btnSelectCurrency.setText(currency.getSymbol() + "(" + currency.getDisplayName() + ")");
    }

}
