package com.mokin.myfinances.app.master_views;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.adapters.CategoryAdapter;
import com.mokin.myfinances.app.data.MyFinancesContract;
import com.mokin.myfinances.app.detail_views.AccountDetails;
import com.mokin.myfinances.app.detail_views.AccountDetailsFragment;

/**
 * Created by Alexey on 26.04.2015.
 */
public class CategoryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CategoryAdapter mCategoryAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    public static int CATEGORY_DETAILS_REQUEST = 1;
    private static final int CATEGORY_LOADER = 0;


    public CategoryListFragment() {
        setHasOptionsMenu(true);
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: To check whether it can be moved to constructor or not
        setHasOptionsMenu(true);
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_list, container, false);

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mCategoryAdapter = new CategoryAdapter(getActivity(), null, 0);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setEmptyView(rootView.findViewById(R.id.emptyView));
        mListView.setAdapter(mCategoryAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Uri uri = ContentUris.withAppendedId(MyFinancesContract.Category.CONTENT_URI, cursor.getLong(MyFinancesContract.Category.COL_ID_IDX));
                    showCategoryDetails(uri);
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: change?
        inflater.inflate(R.menu.menu_account_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_new:
                showCategoryDetails(null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCategoryDetails(Uri uri) {
        Intent intentAccountDetails = new Intent(getActivity(), AccountDetails.class);
        intentAccountDetails.setData(uri);
        startActivityForResult(intentAccountDetails, CATEGORY_DETAILS_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CATEGORY_DETAILS_REQUEST) {

            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    break;

                case AccountDetailsFragment.RESULT_SAVE:
                    Bundle bundle = data.getExtras();
                    saveAccount(bundle);
                    break;

                case AccountDetailsFragment.RESULT_DELETE:
                    long id = data.getLongExtra("id", -1);
                    deleteAccount(id);
                    break;

                default:
                    break;
            }
        }
    }


    private void saveAccount(Bundle bundle) {
        int rows;
        ContentValues cv;

        if (bundle.getLong(MyFinancesContract.Account._ID) > 0) {
            // update account

            cv = getAccountContentValues(bundle);
            rows = getActivity().getContentResolver().update(MyFinancesContract.Account.CONTENT_URI, cv, "_id = " + bundle.getLong(MyFinancesContract.Account._ID), null);

            Toast.makeText(getActivity(), "Updated rows: " + rows, Toast.LENGTH_SHORT).show();

        } else {
            // add  new account
            //Log.d("MainActivity", "Here goes add new account...");

            cv = getAccountContentValues(bundle);
            Uri uri = getActivity().getContentResolver().insert(MyFinancesContract.Account.CONTENT_URI, cv);

            long id = Long.valueOf(uri.getLastPathSegment());

            Toast.makeText(getActivity(), "New account added with ID = " + id, Toast.LENGTH_SHORT).show();
        }

    }


    private void deleteAccount(long id) {
        int rows = getActivity().getContentResolver().delete(MyFinancesContract.Account.CONTENT_URI, "_id = " + id, null);
        Toast.makeText(getActivity(), "Deleted rows: " + rows, Toast.LENGTH_SHORT).show();
    }


    private ContentValues getAccountContentValues(Bundle bundle) {
        ContentValues cv = new ContentValues();
/*        if (bundle.getLong(MyFinancesContract.Account._ID) > 0) {
            cv.put(MyFinancesContract.Account._ID, bundle.getLong(MyFinancesContract.Account._ID));
        }*/
        cv.put(MyFinancesContract.Account.COLUMN_NAME, bundle.getString(MyFinancesContract.Account.COLUMN_NAME));
        cv.put(MyFinancesContract.Account.COLUMN_COMMENT, bundle.getString(MyFinancesContract.Account.COLUMN_COMMENT));
        // TODO: replace this stub
        cv.put(MyFinancesContract.Account.COLUMN_CURRENCY_ID, bundle.getLong(MyFinancesContract.Account.COLUMN_CURRENCY_ID));
        return cv;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // Sort order:  Ascending
        String sortOrder = MyFinancesContract.Account.COLUMN_NAME + " ASC";

        return new CursorLoader(getActivity(),
                MyFinancesContract.Account.CONTENT_URI,
                MyFinancesContract.Account.ACCOUNT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCategoryAdapter.swapCursor(null);
    }

}
