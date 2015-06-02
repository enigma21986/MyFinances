package com.mokin.myfinances.app.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Map;

// TODO: remove this class?
public class CategorySpinnerAdapter extends ArrayAdapter {

    private Map<Integer, String> mObjects;

    public CategorySpinnerAdapter(Context context, int resource, Map<Integer, String> objects) {
        super(context, resource);
        mObjects = objects;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
