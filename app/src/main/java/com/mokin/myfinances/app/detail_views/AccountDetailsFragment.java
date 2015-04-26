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
import android.widget.EditText;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.MyFinancesContract;

/**
 * Created by Alexey on 18.04.2015.
 */
public class AccountDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mEtAccountName;
    private EditText mEtAccountComment;
    private long mAccountId;

    private static final int ACCOUNT_DETAIL_LOADER = 0;
    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;


    public AccountDetailsFragment() {
        // In order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_detail, container, false);

        mEtAccountName = (EditText) rootView.findViewById(R.id.et_account_name);
        mEtAccountComment = (EditText) rootView.findViewById(R.id.et_account_comment);

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
                builder.setMessage(R.string.confirm_delete);

                builder.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().setResult(RESULT_DELETE, getActivity().getIntent().putExtra("id", mAccountId));
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
            account.putLong(MyFinancesContract.Account._ID, mAccountId);
            account.putString(MyFinancesContract.Account.COLUMN_NAME, mEtAccountName.getText().toString());
            account.putString(MyFinancesContract.Account.COLUMN_COMMENT, mEtAccountComment.getText().toString());
            // TODO: replace this stub
            account.putLong(MyFinancesContract.Account.COLUMN_CURRENCY_ID, 1);

            getActivity().getIntent().putExtras(account);

            getActivity().setResult(RESULT_SAVE, getActivity().getIntent());
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        getActivity().finish();
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
                    MyFinancesContract.Account.ACCOUNT_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            mAccountId = data.getLong(MyFinancesContract.Account.COL_ID_IDX);
            mEtAccountName.setText(data.getString(MyFinancesContract.Account.COL_NAME_IDX));
            mEtAccountComment.setText(data.getString(MyFinancesContract.Account.COL_COMMENT_IDX));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
