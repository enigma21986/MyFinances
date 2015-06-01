package com.mokin.myfinances.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.utility.MyCurrency;

import java.util.List;


public class CurrencyAdapter extends ArrayAdapter<MyCurrency> {

    private String selectedCode;

    public CurrencyAdapter(Context context, List<MyCurrency> currencyList, String selectedCode) {
        super(context, R.layout.currency_listview_row, currencyList);

        currencyList.add(new MyCurrency("RUB"));
        currencyList.add(new MyCurrency("USD"));
        currencyList.add(new MyCurrency("EUR"));

        this.selectedCode = selectedCode;

        //currencyList.addAll(Currency.getAvailableCurrencies());
    }


    static class ViewHolder {
        final TextView currencyName;
        final TextView currencySymbol;
        final ImageView currencyFlag;
        final CheckBox currencySelected;

        public ViewHolder(View view) {
            currencyName = (TextView) view.findViewById(R.id.currency_name);
            currencySymbol = (TextView) view.findViewById(R.id.currency_symbol);
            currencyFlag = (ImageView) view.findViewById(R.id.currency_flag);
            currencySelected = (CheckBox) view.findViewById(R.id.currency_selected);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.currency_listview_row, parent, false);

            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        MyCurrency currency = getItem(position);
        //Currency currency = (Currency) getItem(position);

        viewHolder.currencyName.setText(currency.getName());
        viewHolder.currencySymbol.setText(currency.getSymbol());
        viewHolder.currencyFlag.setImageResource(currency.getFlag());

        if ((selectedCode != null) && (selectedCode.equals(currency.getCode()))) {
            viewHolder.currencySelected.setVisibility(View.VISIBLE);
            viewHolder.currencySelected.setChecked(true);
        } else {
            viewHolder.currencySelected.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }
}
