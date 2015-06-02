package com.mokin.myfinances.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mokin.myfinances.app.adapters.CurrencyAdapter;
import com.mokin.myfinances.app.utility.MyCurrency;

import java.util.ArrayList;
import java.util.List;


public class CurrencyListDialogFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(R.string.select_currency);

        View view = inflater.inflate(R.layout.currency_list_layout, container, false);

        ListView listView = (ListView) view.findViewById(R.id.currency_list);

        List<MyCurrency> currencyList = new ArrayList<>();

        String code = getArguments().getString("code");

        CurrencyAdapter adapter = new CurrencyAdapter(getActivity(), currencyList, code);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyCurrency currency = (MyCurrency) parent.getItemAtPosition(position);
                MyCurrencySelectedCallbacks activity = (MyCurrencySelectedCallbacks) getActivity();
                activity.onCurrencySelected(currency.getCode());

                getDialog().dismiss();
            }
        });

        return view;
    }


    public interface MyCurrencySelectedCallbacks {
        void onCurrencySelected(String code);
    }

}
