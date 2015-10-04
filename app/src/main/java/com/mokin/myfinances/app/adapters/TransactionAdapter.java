package com.mokin.myfinances.app.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.FinContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_DATE_SEPARATOR = 0;
    private static final int VIEW_TYPE_TRANSACTION = 1;
    public static final String DATE_FORMAT = "dd.MM.yyyy";


    public TransactionAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Choose the layout type
        int viewType = getItemViewType(cursor);

        boolean isSeparator = false;
        int layoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_DATE_SEPARATOR: {
                layoutId = R.layout.transaction_listview_row_date;
                isSeparator = true;
                break;
            }
            case VIEW_TYPE_TRANSACTION: {
                layoutId = R.layout.transaction_listview_row;
                break;
            }
        }

        View rowView = LayoutInflater.from(context).inflate(layoutId, parent, false);

        if (isSeparator) {
            rowView.setOnClickListener(null);
        }

        ViewHolder viewHolder = new ViewHolder(rowView);
        rowView.setTag(viewHolder);

        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor);

        switch (viewType) {
            case VIEW_TYPE_DATE_SEPARATOR: {

                long transaction_datetime = cursor.getLong(FinContract.Transactions.COL_TRANSACTION_DATETIME_IDX);

                Date date = new Date(transaction_datetime);
                String dateStr = new SimpleDateFormat(DATE_FORMAT).format(date);

                viewHolder.date.setText(dateStr);
                break;
            }
            case VIEW_TYPE_TRANSACTION: {
                // Read data from cursor
                String category = cursor.getString(FinContract.Transactions.COL_CATEGORY_NAME_IDX);
                String comment = cursor.getString(FinContract.Transactions.COL_COMMENT_IDX);
                String amount = String.valueOf(cursor.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX));

                // Find TextView and set value on it
                viewHolder.category.setText(category);
                viewHolder.comment.setText(comment);
                viewHolder.amount.setText(amount);
                break;
            }
        }

    }


    public int getItemViewType(Cursor cursor) {
        return (cursor.getInt(FinContract.Transactions.COL_ID_IDX) > 0) ? VIEW_TYPE_TRANSACTION : VIEW_TYPE_DATE_SEPARATOR;
    }


    /**
     * Cache of the children views for transaction list item.
     */
    static class ViewHolder {
        final TextView category;
        final TextView comment;
        final TextView amount;
        final TextView date;

        public ViewHolder(View view) {
            category = (TextView) view.findViewById(R.id.transaction_category_name);
            comment = (TextView) view.findViewById(R.id.transaction_comment);
            amount = (TextView) view.findViewById(R.id.transaction_amount);

            date = (TextView) view.findViewById(R.id.transaction_date);
        }
    }


}
