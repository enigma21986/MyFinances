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
 * Created by Alexey on 26.04.2015.
 */
public class CategoryAdapter extends CursorAdapter {

    public CategoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_listview_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read data from cursor
        String categoryName = cursor.getString(MyFinancesContract.Category.COL_NAME_IDX);

        // Find TextView and set value on it
        viewHolder.categoryName.setText(categoryName);

    }

    /**
     * Cache of the children views for an category list item.
     */
    public static class ViewHolder {
        public final TextView categoryName;

        public ViewHolder(View view) {
            categoryName = (TextView) view.findViewById(R.id.category_name);
        }
    }
}
