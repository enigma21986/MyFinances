package com.mokin.myfinances.app.master_views;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.adapters.TransactionAdapter;
import com.mokin.myfinances.app.data.FinContract;
import com.mokin.myfinances.app.data.TransactionType;
import com.mokin.myfinances.app.detail_views.CategoryDetailsFragment;
import com.mokin.myfinances.app.detail_views.TransactionDetails;

import java.util.Calendar;


public class TransactionListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private TransactionAdapter mTransactionAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private View mSummaryHeader;
    private CoordinatorLayout mRootView;

    private static final String SELECTED_KEY = "selected_position";

    public static int DETAILS_REQUEST = 1;
    private static final int TRANSACTIONS_LOADER = 0;


    public TransactionListFragment() {
        setHasOptionsMenu(true);
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
        mRootView = (CoordinatorLayout) inflater.inflate(R.layout.general_list_layout, container, false);

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mTransactionAdapter = new TransactionAdapter(getActivity(), null, 0);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) mRootView.findViewById(R.id.listView);
        mListView.setEmptyView(mRootView.findViewById(R.id.emptyView));

        // Transactions summary
        mSummaryHeader = inflater.inflate(R.layout.transaction_list_summary, null, false);

        mListView.addHeaderView(mSummaryHeader, null, false);
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
                    bundle.putLong(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME, cursor.getLong(FinContract.Transactions.COL_TRANSACTION_DATETIME_IDX));
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

        FloatingActionButton btnFab = (FloatingActionButton) mRootView.findViewById(R.id.btnFloatingAction);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactionDetails(null);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return mRootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: change?
        inflater.inflate(R.menu.menu_account_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*switch (item.getItemId()) {
            case R.id.add_new:
                showTransactionDetails(null);
        }*/

        return super.onOptionsItemSelected(item);
    }


    private void showTransactionDetails(Bundle bundle) {
        Intent intentDetails = new Intent(getActivity(), TransactionDetails.class);
        //intentDetails.setData(uri);
        if (bundle != null) {
            intentDetails.putExtras(bundle);
        }
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
                    saveTransaction(data.getExtras());
                    break;

                case CategoryDetailsFragment.RESULT_DELETE:
                    int id = data.getIntExtra(FinContract.Transactions._ID, -1);
                    deleteTransaction(id, data.getExtras());
                    break;

                default:
                    break;
            }
        }
    }

    private void saveTransaction(Bundle bundle) {
        int rows, id;
        ContentValues cv;

        if (bundle.getInt(FinContract.Transactions._ID) > 0) {
            // update transaction
            cv = getContentValues(bundle);
            rows = getActivity().getContentResolver().update(FinContract.Transactions.CONTENT_URI, cv, "_id = " + bundle.getInt(FinContract.Transactions._ID), null);

            Toast.makeText(getActivity(), "Updated rows: " + rows, Toast.LENGTH_SHORT).show();

        } else {
            // add  new transaction
            cv = getContentValues(bundle);
            Uri uri = getActivity().getContentResolver().insert(FinContract.Transactions.CONTENT_URI, cv);

            id = Integer.valueOf(uri.getLastPathSegment());

            Toast.makeText(getActivity(), "New transaction added with ID = " + id, Toast.LENGTH_SHORT).show();
        }

        updateTransactionListView();
    }

    private void deleteTransaction(int id, final Bundle bundle) {

        int rowsNum = getActivity().getContentResolver().delete(FinContract.Transactions.CONTENT_URI, "_id = " + id, null);
        //Toast.makeText(getActivity(), "Deleted rows: " + rowsNum, Toast.LENGTH_SHORT).show();
        updateTransactionListView();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of transaction rollback - insert the row again
                ContentValues cv = getContentValues(bundle);
                getActivity().getContentResolver().insert(FinContract.Transactions.CONTENT_URI, cv);
                updateTransactionListView();
            }
        };

        // Show Snackbar allowing Undo transaction
        Snackbar.make(mRootView, "Undo deletion?", Snackbar.LENGTH_LONG)
                .setDuration(7000)
                .setAction("Undo", onClickListener)
                .show();
    }

    private void updateTransactionListView() {
        getLoaderManager().restartLoader(TRANSACTIONS_LOADER, null, this);
    }




    private ContentValues getContentValues(Bundle bundle) {
        ContentValues cv = new ContentValues();

        cv.put(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME, bundle.getLong(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME));
        cv.put(FinContract.Transactions.COLUMN_TRANSACTION_AMOUNT, bundle.getDouble(FinContract.Transactions.COLUMN_TRANSACTION_AMOUNT));
        cv.put(FinContract.Transactions.COLUMN_ACCOUNT_ID, bundle.getInt(FinContract.Transactions.COLUMN_ACCOUNT_ID));
        cv.put(FinContract.Transactions.COLUMN_TRANSACTION_TYPE_ID, bundle.getInt(FinContract.Transactions.COLUMN_TRANSACTION_TYPE_ID));
        cv.put(FinContract.Transactions.COLUMN_COMMENT, bundle.getString(FinContract.Transactions.COLUMN_COMMENT));

        if (bundle.getInt(FinContract.Transactions.COLUMN_CATEGORY_ID) != 0) {
            cv.put(FinContract.Transactions.COLUMN_CATEGORY_ID, bundle.getInt(FinContract.Transactions.COLUMN_CATEGORY_ID));
        }

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = FinContract.Transactions.COLUMN_TRANSACTION_DATETIME + " DESC";

        return new CursorLoader(getActivity(),
                FinContract.Transactions.CONTENT_URI_WITH_CATEGORY_NAME,
                FinContract.Transactions.TRANSACTION_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        MatrixCursor matrixCursor = new MatrixCursor(FinContract.Transactions.TRANSACTION_COLUMNS_2);
        int date_num = 0;
        long transaction_datetime;
        double income_sum = 0, expense_sum = 0;

        Calendar calendar = Calendar.getInstance();

        while (data.moveToNext()) {

            if (TransactionType.Expense.getId() == data.getInt(FinContract.Transactions.COL_TRANSACTION_TYPE_ID_IDX)) {
                expense_sum += data.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX);
            } else if (TransactionType.Income.getId() == data.getInt(FinContract.Transactions.COL_TRANSACTION_TYPE_ID_IDX)) {
                income_sum += data.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX);
            }

            transaction_datetime = data.getLong(FinContract.Transactions.COL_TRANSACTION_DATETIME_IDX);
            calendar.setTimeInMillis(transaction_datetime);

            // if new date separator
            if (date_num != calendar.get(Calendar.DATE))
            {
                matrixCursor.addRow(new Object[]{0, transaction_datetime, null, null, null, null, null, null, null, null, null});
                matrixCursor.addRow(new Object[]{
                        data.getInt(FinContract.Transactions.COL_ID_IDX),
                        transaction_datetime,
                        data.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX),
                        data.getInt(FinContract.Transactions.COL_ACCOUNT_ID_IDX),
                        data.getInt(FinContract.Transactions.COL_CATEGORY_ID_IDX),
                        data.getString(FinContract.Transactions.COL_CATEGORY_NAME_IDX),
                        data.getInt(FinContract.Transactions.COL_TRANSACTION_TYPE_ID_IDX),
                        data.getString(FinContract.Transactions.COL_COMMENT_IDX),
                        null, // COLUMN_MARKET_ID
                        null, // COLUMN_ACCOUNT_SOURCE
                        null // COLUMN_ACCOUNT_TARGET
                });

                date_num = calendar.get(Calendar.DATE);

            } else
            // the same date
            {
                matrixCursor.addRow(new Object[]{
                        data.getInt(FinContract.Transactions.COL_ID_IDX),
                        transaction_datetime,
                        data.getDouble(FinContract.Transactions.COL_TRANSACTION_AMOUNT_IDX),
                        data.getInt(FinContract.Transactions.COL_ACCOUNT_ID_IDX),
                        data.getInt(FinContract.Transactions.COL_CATEGORY_ID_IDX),
                        data.getString(FinContract.Transactions.COL_CATEGORY_NAME_IDX),
                        data.getInt(FinContract.Transactions.COL_TRANSACTION_TYPE_ID_IDX),
                        data.getString(FinContract.Transactions.COL_COMMENT_IDX),
                        null, // COLUMN_MARKET_ID
                        null, // COLUMN_ACCOUNT_SOURCE
                        null // COLUMN_ACCOUNT_TARGET
                });
            }
        }

        mTransactionAdapter.swapCursor(matrixCursor);

        // Set transactions summary in summary header
        ((TextView) mSummaryHeader.findViewById(R.id.expense_value)).setText(Double.toString(expense_sum));
        ((TextView) mSummaryHeader.findViewById(R.id.income_value)).setText(Double.toString(income_sum));
        ((TextView) mSummaryHeader.findViewById(R.id.summary_value)).setText(Double.toString(income_sum-expense_sum));

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
