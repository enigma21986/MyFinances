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

public class TransactionAdapter extends CursorAdapter {


    public TransactionAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View rowView = LayoutInflater.from(context).inflate(R.layout.transaction_listview_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        rowView.setTag(viewHolder);

        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read data from cursor
        String category = cursor.getString(FinContract.Transactions.COL_CATEGORY_NAME_IDX);
        String comment = cursor.getString(FinContract.Transactions.COL_COMMENT_IDX);
        String amount = String.valueOf(cursor.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX));

        // Find TextView and set value on it
        viewHolder.category.setText(category);
        viewHolder.comment.setText(comment);
        viewHolder.amount.setText(amount);
    }


    /**
     * Cache of the children views for an category list item.
     */
    static class ViewHolder {
        final TextView category;
        final TextView comment;
        final TextView amount;

        public ViewHolder(View view) {
            category = (TextView) view.findViewById(R.id.transaction_category_name);
            comment = (TextView) view.findViewById(R.id.transaction_comment);
            amount = (TextView) view.findViewById(R.id.transaction_amount);
        }
    }
}
