package com.mokin.myfinances.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.MyFinancesContract;

/**
 * Created by Alexey on 08.04.2015.
 * почему ниже именно так ArrayAdapter<Account>, а не ArrayAdapter ???
 */
public class AccountAdapter extends CursorAdapter {

    public AccountAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.account_listview_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read name from cursor
        String accountName = cursor.getString(MyFinancesContract.Account.COL_NAME_IDX);
        // Find TextView and set value on it
        viewHolder.accountName.setText(accountName);

    }

    /**
     * Cache of the children views for an account list item.
     */
    public static class ViewHolder {
        public final TextView accountName;
        //public final TextView accountComment;

        public ViewHolder(View view) {
            accountName = (TextView) view.findViewById(R.id.account_name);
            //accountComment = (TextView) view.findViewById(R.id.account_balance);
        }
    }
}
