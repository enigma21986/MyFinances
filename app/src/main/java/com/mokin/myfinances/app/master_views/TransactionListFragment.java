package com.mokin.myfinances.app.master_views;

import android.app.Activity;
import android.content.ContentUris;
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
import com.mokin.myfinances.app.adapters.TransactionAdapter;
import com.mokin.myfinances.app.data.FinContract;
import com.mokin.myfinances.app.detail_views.CategoryDetailsFragment;
import com.mokin.myfinances.app.detail_views.TransactionDetails;


public class TransactionListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private TransactionAdapter mTransactionAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    public static int DETAILS_REQUEST = 1;
    private static final int TRANSACTIONS_LOADER = 0;


    public TransactionListFragment() {
        //setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(TRANSACTIONS_LOADER, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.general_list_layout, container, false);

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mTransactionAdapter = new TransactionAdapter(getActivity(), null, 0);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setEmptyView(rootView.findViewById(R.id.emptyView));
        mListView.setAdapter(mTransactionAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Uri uri = ContentUris.withAppendedId(FinContract.Transactions.CONTENT_URI, cursor.getInt(FinContract.Transactions.COL_ID_IDX));
                    //showTransactionDetails(uri);

                    Bundle bundle = new Bundle();
                    bundle.putInt(FinContract.Transactions._ID, cursor.getInt(FinContract.Transactions.COL_ID_IDX));
                    bundle.putString(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME, cursor.getString(FinContract.Transactions.COL_TRANSACTION_DATETIME_IDX));
                    bundle.putDouble(FinContract.Transactions.COLUMN_TRANSACTION_AMOUNT, cursor.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX));
                    bundle.putInt(FinContract.Transactions.COLUMN_ACCOUNT_ID, cursor.getInt(FinContract.Transactions.COL_ACCOUNT_ID_IDX));
                    bundle.putInt(FinContract.Transactions.COLUMN_TRANSACTION_TYPE_ID, cursor.getInt(FinContract.Transactions.COL_TRANSACTION_TYPE_ID_IDX));
                    bundle.putInt(FinContract.Transactions.COLUMN_CATEGORY_ID, cursor.getInt(FinContract.Transactions.COL_CATEGORY_ID_IDX));
                    bundle.putString(FinContract.Transactions.COLUMN_COMMENT, cursor.getString(FinContract.Transactions.COL_COMMENT_IDX));

                    showTransactionDetails(bundle);
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
                showTransactionDetails(null);
        }

        return super.onOptionsItemSelected(item);
    }


    private void showTransactionDetails(Bundle bundle) {
        Intent intentDetails = new Intent(getActivity(), TransactionDetails.class);
        //intentDetails.setData(uri);
        intentDetails.putExtras(bundle);
        startActivityForResult(intentDetails, DETAILS_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DETAILS_REQUEST) {

            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    break;

                case CategoryDetailsFragment.RESULT_SAVE:
                    Bundle bundle = data.getExtras();
                    saveTransaction(bundle);
                    break;

                case CategoryDetailsFragment.RESULT_DELETE:
                    int id = data.getIntExtra("id", -1);
                    deleteTransaction(id);
                    break;

                default:
                    break;
            }
        }
    }

    private void saveTransaction(Bundle bundle) {

    }

    private void deleteTransaction(int id) {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = FinContract.Transactions.COLUMN_TRANSACTION_DATETIME + " ASC";

        return new CursorLoader(getActivity(),
                FinContract.Transactions.CONTENT_URI,
                FinContract.Transactions.TRANSACTION_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTransactionAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTransactionAdapter.swapCursor(null);
    }
}
