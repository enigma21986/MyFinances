package com.mokin.myfinances.app.detail_views;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.FinContract;

public class TransactionDetailsFragment extends Fragment {

    private int mTransactionId;

    private EditText mTransactionDate;
    private EditText mTransactionAmount;
    private EditText mTransactionComment;
    private Spinner mTransactionAccountSpinner;
    private Spinner mTransactionTypeSpinner;
    private Spinner mTransactionCategorySpinner;

    public static final int RESULT_DELETE = 101;


    public TransactionDetailsFragment() {
        // In order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getLoaderManager().initLoader(CATEGORY_DETAIL_LOADER, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.transaction_detail_layout, container, false);

        mTransactionDate = (EditText) rootView.findViewById(R.id.transaction_date);
        mTransactionAmount = (EditText) rootView.findViewById(R.id.transaction_amount);
        mTransactionAccountSpinner = (Spinner) rootView.findViewById(R.id.transaction_account_spinner);
        mTransactionTypeSpinner = (Spinner) rootView.findViewById(R.id.transaction_type_spinner);
        mTransactionCategorySpinner = (Spinner) rootView.findViewById(R.id.transaction_category_spinner);
        mTransactionComment = (EditText) rootView.findViewById(R.id.transaction_comment);

//        Intent intent = getActivity().getIntent();
//        if (intent.getData() != null){
//            mTransactionId = (int) ContentUris.parseId(intent.getData());
//        }

        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle != null) {
            mTransactionId = bundle.getInt(FinContract.Transactions._ID);

            mTransactionDate.setText(bundle.getString(FinContract.Transactions.COLUMN_TRANSACTION_DATETIME));
            mTransactionAmount.setText(String.valueOf(bundle.getDouble(FinContract.Transactions.COLUMN_TRANSACTION_AMOUNT)));

            mTransactionComment.setText(bundle.getString(FinContract.Transactions.COLUMN_COMMENT));

        }

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
                                getActivity().setResult(RESULT_DELETE, getActivity().getIntent().putExtra("id", mTransactionId));
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

        getActivity().finish();
    }


}
