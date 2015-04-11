package com.mokin.myfinances.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mokin.myfinances.app.Account;
import com.mokin.myfinances.app.R;

import java.util.List;

/**
 * Created by Alexey on 08.04.2015.
 * почему ниже именно так ArrayAdapter<Account>, а не ArrayAdapter ???
 */
public class AccountAdapter extends ArrayAdapter {

    private List<Account> list;

    public AccountAdapter(Context context, List<Account> list) {
        super(context, R.layout.account_listview_row, list);
        this.list = list;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.account_listview_row, parent, false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.accountName = (TextView) convertView.findViewById(R.id.account_name);
            viewHolder.accountBalance = (TextView) convertView.findViewById(R.id.account_balance);

            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        Account account = list.get(position);

        holder.accountName.setText(account.getName());
        // TODO: возможно лучше убрать этот initial balance
        holder.accountBalance.setText(Double.toString(account.getInitial_balance()));

        return convertView;
    }

    static class ViewHolder {
        public TextView accountName;
        public TextView accountBalance;
    }    
    
    
}
