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
import com.mokin.myfinances.app.adapters.AccountAdapter;
import com.mokin.myfinances.app.data.FinContract;
import com.mokin.myfinances.app.detail_views.AccountDetails;
import com.mokin.myfinances.app.detail_views.AccountDetailsFragment;

public class AccountListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AccountAdapter mAccountAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    public static int ACCOUNT_DETAILS_REQUEST = 1;
    private static final int ACCOUNT_LOADER = 0;

    public AccountListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: To check whether it can be moved to constructor or not
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ACCOUNT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.general_list_layout, container, false);

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mAccountAdapter = new AccountAdapter(getActivity(), null, 0);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setEmptyView(rootView.findViewById(R.id.emptyView));
        mListView.setAdapter(mAccountAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Uri uri = ContentUris.withAppendedId(FinContract.Account.CONTENT_URI, cursor.getInt(FinContract.Account.COL_ID_IDX));
                    showAccountDetails(uri);
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
        inflater.inflate(R.menu.menu_account_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_new:
                showAccountDetails(null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAccountDetails(Uri uri) {
        Intent intentAccountDetails = new Intent(getActivity(), AccountDetails.class);
        intentAccountDetails.setData(uri);
        startActivityForResult(intentAccountDetails, ACCOUNT_DETAILS_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACCOUNT_DETAILS_REQUEST) {

            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    break;

                case AccountDetailsFragment.RESULT_SAVE:
                    Bundle bundle = data.getExtras();
                    saveAccount(bundle);
                    break;

                case AccountDetailsFragment.RESULT_DELETE:
                    int id = data.getIntExtra("id", -1);
                    deleteAccount(id);
                    break;

                default:
                    break;
            }
        }
    }


    private void saveAccount(Bundle bundle) {
        int rows;
        ContentValues cv = getAccountContentValues(bundle);

        if (bundle.getInt(FinContract.Account._ID) > 0) {
            // update account

            rows = getActivity().getContentResolver().update(FinContract.Account.CONTENT_URI, cv, "_id = " + bundle.getInt(FinContract.Account._ID), null);

            Toast.makeText(getActivity(), "Updated rows: " + rows, Toast.LENGTH_SHORT).show();

        } else {
            // add  new account

            Uri uri = getActivity().getContentResolver().insert(FinContract.Account.CONTENT_URI, cv);

            int id = Integer.valueOf(uri.getLastPathSegment());

            Toast.makeText(getActivity(), "New account added with ID = " + id, Toast.LENGTH_SHORT).show();
        }

    }


    private void deleteAccount(int id) {
        int rows = getActivity().getContentResolver().delete(FinContract.Account.CONTENT_URI, "_id = " + id, null);
        Toast.makeText(getActivity(), "Deleted rows: " + rows, Toast.LENGTH_SHORT).show();
    }


    private ContentValues getAccountContentValues(Bundle bundle) {
        ContentValues cv = new ContentValues();
/*        if (bundle.getLong(MyFinancesContract.Account._ID) > 0) {
            cv.put(MyFinancesContract.Account._ID, bundle.getLong(MyFinancesContract.Account._ID));
        }*/
        cv.put(FinContract.Account.COLUMN_NAME, bundle.getString(FinContract.Account.COLUMN_NAME));
        cv.put(FinContract.Account.COLUMN_COMMENT, bundle.getString(FinContract.Account.COLUMN_COMMENT));
        // TODO: replace this stub
        cv.put(FinContract.Account.COLUMN_CURRENCY_CODE, bundle.getString(FinContract.Account.COLUMN_CURRENCY_CODE));
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
        String sortOrder = FinContract.Account.COLUMN_NAME + " ASC";

        return new CursorLoader(getActivity(),
                FinContract.Account.CONTENT_URI,
                FinContract.Account.ACCOUNT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAccountAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    // ? Это зачем? или changeCursor() здесь лучше?
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAccountAdapter.swapCursor(null);
    }

}
