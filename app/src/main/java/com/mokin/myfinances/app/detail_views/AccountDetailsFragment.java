package com.mokin.myfinances.app.detail_views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;

import com.mokin.myfinances.app.CurrencyListDialogFragment;
import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.FinContract;

import java.util.Currency;


public class AccountDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mEtAccountName;
    private EditText mEtAccountComment;
    private Button mBtnAccountCurrency;
    private int mAccountId;
    private String mCurrencyCode;

    private static final int ACCOUNT_DETAIL_LOADER = 0;
    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;


    public AccountDetailsFragment() {
        // In order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.account_detail_layout, container, false);

        mEtAccountName = (EditText) rootView.findViewById(R.id.et_account_name);
        mEtAccountComment = (EditText) rootView.findViewById(R.id.et_account_comment);
        mBtnAccountCurrency = (Button) rootView.findViewById(R.id.btn_select_currency);

        mBtnAccountCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyListDialogFragment dialog = new CurrencyListDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putString("code", mCurrencyCode);

                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), this.getClass().getSimpleName());
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ACCOUNT_DETAIL_LOADER, null, this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_details, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {

/*                if (TextUtils.isEmpty(mEtAccountName.getText().toString())) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                } else {
                    editAccount();
                }*/
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
                return true;
            }
            case R.id.save: {
                editAccount();
                return true;
            }

            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.account_confirm_delete);

                builder.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().setResult(RESULT_DELETE, getActivity().getIntent().putExtra(FinContract.Account._ID, mAccountId));
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


    private void editAccount() {
        if (!TextUtils.isEmpty(mEtAccountName.getText().toString())) {
            Bundle account = new Bundle();
            account.putInt(FinContract.Account._ID, mAccountId);
            account.putString(FinContract.Account.COLUMN_NAME, mEtAccountName.getText().toString());
            account.putString(FinContract.Account.COLUMN_COMMENT, mEtAccountComment.getText().toString());
            // TODO: replace this stub
            account.putString(FinContract.Account.COLUMN_CURRENCY_CODE, mCurrencyCode);

            getActivity().getIntent().putExtras(account);

            getActivity().setResult(RESULT_SAVE, getActivity().getIntent());
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        getActivity().finish();
    }


    public void setCurrencyCode(String code) {
        mCurrencyCode = code;
        Currency currency = Currency.getInstance(code);
        mBtnAccountCurrency.setText(currency.getSymbol() + "(" + currency.getDisplayName() + ")");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getActivity().getIntent();

        if (intent.getData() != null) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    FinContract.Account.ACCOUNT_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null && cursor.moveToFirst()) {

            mAccountId = cursor.getInt(FinContract.Account.COL_ID_IDX);

            mEtAccountName.setText(cursor.getString(FinContract.Account.COL_NAME_IDX));
            mEtAccountComment.setText(cursor.getString(FinContract.Account.COL_COMMENT_IDX));

            setCurrencyCode(cursor.getString(FinContract.Account.COL_CURRENCY_CODE_IDX));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
