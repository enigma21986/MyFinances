package com.mokin.myfinances.app.detail_views;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.FinContract;
import com.mokin.myfinances.app.data.TransactionType;
import com.mokin.myfinances.app.utility.DatePickerFragment;
import com.mokin.myfinances.app.utility.SpinnerData;
import com.mokin.myfinances.app.utility.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private int mTransactionId;
    private int mAccountId;
    private int mCategoryId;
    private long mTransactionDateTime;
    private ArrayList<SpinnerData> mAccountList;
    private ArrayAdapter<SpinnerData> mAccountAdapter;
    private ArrayList<SpinnerData> mCategoryList;
    private ArrayAdapter<SpinnerData> mCategoryAdapter;
    private ArrayAdapter<TransactionType> mTransactionTypeAdapter;

    private EditText mTransactionAmount;
    private EditText mTransactionComment;
    private Spinner mTransactionAccountSpinner;
    private Spinner mTransactionTypeSpinner;
    private Spinner mTransactionCategorySpinner;
    private Button mBtnTransactionDate;
    private Button mBtnTransactionTime;

    public static final int ACCOUNT_LOADER = 0;
    public static final int CATEGORY_LOADER = 1;

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_FORMAT = "HH:mm";

    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;


    public TransactionDetailsFragment() {
        // In order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ACCOUNT_LOADER, null, this);
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.transaction_detail_layout, container, false);

        mTransactionAmount = (EditText) rootView.findViewById(R.id.transaction_amount);
        mTransactionAccountSpinner = (Spinner) rootView.findViewById(R.id.transaction_account_spinner);
        mTransactionTypeSpinner = (Spinner) rootView.findViewById(R.id.transaction_type_spinner);
        mTransactionCategorySpinner = (Spinner) rootView.findViewById(R.id.transaction_category_spinner);
        mTransactionComment = (EditText) rootView.findViewById(R.id.transaction_comment);
        mBtnTransactionDate = (Button) rootView.findViewById(R.id.btn_transaction_date);
        mBtnTransactionTime = (Button) rootView.findViewById(R.id.btn_transaction_time);

        TransactionType[] data = {TransactionType.Expense, TransactionType.Income, TransactionType.Transfer};
        mTransactionTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
        mTransactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTransactionTypeSpinner.setAdapter(mTransactionTypeAdapter);

        // TODO: do localisation
        mTransactionTypeSpinner.setPrompt("Select transaction type");

        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle != null) {
            mTransactionId = bundle.getInt(FinContract.Transactions._ID);
            mAccountId = bundle.getInt(FinContract.Transactions.COLUMN_ACCOUNT_ID);
            mCategoryId = bundle.getInt(FinContract.Transactions.COLUMN_CATEGORY_ID);
            mTransactionDateTime = bundle.getLong(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME);

            mTransactionAmount.setText(String.valueOf(bundle.getDouble(FinContract.Transactions.COLUMN_TRANSACTION_AMOUNT)));
            mTransactionComment.setText(bundle.getString(FinContract.Transactions.COLUMN_COMMENT));

            int pos = mTransactionTypeAdapter.getPosition(TransactionType.getTypeById(bundle.getInt(FinContract.Transactions.COLUMN_TRANSACTION_TYPE_ID)));
            mTransactionTypeSpinner.setSelection(pos);

        } else {
            mTransactionDateTime = System.currentTimeMillis();
        }

        setTransactionDateAndTimeText(mTransactionDateTime);

        mBtnTransactionDate.setOnClickListener(this);
        mBtnTransactionTime.setOnClickListener(this);

        if (savedInstanceState == null){
            mCategoryList = new ArrayList<>();
            mCategoryList.add(new SpinnerData(0, "Choose category"));
            mAccountList = new ArrayList<>();
            mAccountList.add(new SpinnerData(0, "Choose account"));
        } else {
            mCategoryList = (ArrayList<SpinnerData>) savedInstanceState.getSerializable("mCategoryList");
            mAccountList = (ArrayList<SpinnerData>) savedInstanceState.getSerializable("mAccountList");
        }

        mCategoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mCategoryList);
        mCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTransactionCategorySpinner.setAdapter(mCategoryAdapter);

        mAccountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mAccountList);
        mAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTransactionAccountSpinner.setAdapter(mAccountAdapter);

        // TODO: do localisation
        mTransactionCategorySpinner.setPrompt("Select category");
        mTransactionAccountSpinner.setPrompt("Select account");

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: change it!
        inflater.inflate(R.menu.menu_account_details, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {

                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
                return true;
            }
            case R.id.save: {
                editTransaction();
                return true;
            }

            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.transaction_confirm_delete);

                builder.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().setResult(RESULT_DELETE, getActivity().getIntent().putExtra(FinContract.Transactions._ID, mTransactionId));
                                getActivity().finish();
                            }
                        });
                builder.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void editTransaction() {

        if (isValuesValid()) {
            Bundle transaction = new Bundle();
            transaction.putInt(FinContract.Transactions._ID, mTransactionId);
            transaction.putLong(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME, mTransactionDateTime);
            transaction.putDouble(FinContract.Transactions.COLUMN_TRANSACTION_AMOUNT, Double.parseDouble(mTransactionAmount.getText().toString()));

            int val = ((SpinnerData) mTransactionAccountSpinner.getItemAtPosition(mTransactionAccountSpinner.getSelectedItemPosition())).getKey();
            transaction.putInt(FinContract.Transactions.COLUMN_ACCOUNT_ID, val);

            val = ((SpinnerData) mTransactionCategorySpinner.getItemAtPosition(mTransactionCategorySpinner.getSelectedItemPosition())).getKey();
            transaction.putInt(FinContract.Transactions.COLUMN_CATEGORY_ID, val);

            val = ((TransactionType) mTransactionTypeSpinner.getItemAtPosition(mTransactionTypeSpinner.getSelectedItemPosition())).getId();
            transaction.putInt(FinContract.Transactions.COLUMN_TRANSACTION_TYPE_ID, val);

            transaction.putString(FinContract.Transactions.COLUMN_COMMENT, mTransactionComment.getText().toString());

            getActivity().getIntent().putExtras(transaction);
            getActivity().setResult(RESULT_SAVE, getActivity().getIntent());

            getActivity().finish();

        }
//        else {
//            getActivity().setResult(Activity.RESULT_CANCELED);
//        }

    }


    private boolean isValuesValid() {
        boolean result = true;

        if (TextUtils.isEmpty(mTransactionAmount.getText().toString())) {
            mTransactionAmount.setError("Amount is required!" );
            result = false;
        }

        if (mTransactionAccountSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Account is required!", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = null;

        switch (id) {
            case ACCOUNT_LOADER:
                loader = new CursorLoader(
                        getActivity(),
                        FinContract.Account.CONTENT_URI,
                        FinContract.Account.ACCOUNT_COLUMNS,
                        null,
                        null,
                        null
                );
                break;
            case CATEGORY_LOADER:
                loader = new CursorLoader(
                        getActivity(),
                        FinContract.Category.CONTENT_URI,
                        FinContract.Category.CATEGORY_COLUMNS,
                        null,
                        null,
                        null
                );
                break;
        }

        return loader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        switch (loader.getId()) {
            case ACCOUNT_LOADER:
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        mAccountList.add(new SpinnerData(cursor.getInt(FinContract.Account.COL_ID_IDX),
                                cursor.getString(FinContract.Account.COL_NAME_IDX)));
                    }
                }
                mAccountAdapter.notifyDataSetChanged();
                break;
            case CATEGORY_LOADER:
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        mCategoryList.add(new SpinnerData(cursor.getInt(FinContract.Category.COL_ID_IDX),
                                cursor.getString(FinContract.Category.COL_NAME_IDX)));
                    }
                }
                mCategoryAdapter.notifyDataSetChanged();
                break;
        }

        for (SpinnerData account : mAccountList) {
            if (mAccountId == account.getKey()){
                int pos = mAccountAdapter.getPosition(account);
                mTransactionAccountSpinner.setSelection(pos);
                break;
            }
        }

        for (SpinnerData category : mCategoryList) {
            if (mCategoryId == category.getKey()){
                int pos = mCategoryAdapter.getPosition(category);
                mTransactionCategorySpinner.setSelection(pos);
                break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mCategoryList", mCategoryList);
        outState.putSerializable("mAccountList", mAccountList);
    }


    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        bundle.putLong("transactionDateTime", mTransactionDateTime); // ???

        DialogFragment dialog;

        switch (v.getId()) {
            case R.id.btn_transaction_date:
                dialog = new DatePickerFragment();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), this.getClass().getSimpleName());

                break;
            case R.id.btn_transaction_time:
                dialog = new TimePickerFragment();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), this.getClass().getSimpleName());

        }

    }


    private void setTransactionDateAndTimeText(long milliSeconds) {
        Date date = new Date(milliSeconds);
        String dateStr = new SimpleDateFormat(DATE_FORMAT).format(date);
        String timeStr = new SimpleDateFormat(TIME_FORMAT).format(date);

        mBtnTransactionDate.setText(dateStr);
        mBtnTransactionTime.setText(timeStr);
    }


    public void setTransactionDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTransactionDateTime);
        calendar.set(year, month, day);
        mTransactionDateTime = calendar.getTimeInMillis();
        setTransactionDateAndTimeText(mTransactionDateTime);
    }


    public void setTransactionTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTransactionDateTime);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mTransactionDateTime = calendar.getTimeInMillis();
        setTransactionDateAndTimeText(mTransactionDateTime);
    }
}
