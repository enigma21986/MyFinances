package com.mokin.myfinances.app.detail_views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.mokin.myfinances.app.R;
import com.mokin.myfinances.app.data.MyFinancesContract;
import com.mokin.myfinances.app.data.TransactionType;
import com.mokin.myfinances.app.utility.SpinnerData;

import java.util.ArrayList;


public class CategoryDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mEtCategoryName;
    private Spinner mTransactionTypeSpinner;
    private Spinner mParentCategorySpinner;

    private int mCategoryId;
    private ArrayList<SpinnerData> mCategoryList;
    private ArrayAdapter<SpinnerData> mCategoryAdapter;
    private ArrayAdapter<TransactionType> mTransactionTypeAdapter;

    private static final int CATEGORY_DETAIL_LOADER = 0;
    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;


    public CategoryDetailsFragment() {
        // In order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.category_detail_layout, container, false);

        mEtCategoryName = (EditText) rootView.findViewById(R.id.et_category_name);
        mParentCategorySpinner = (Spinner) rootView.findViewById(R.id.parent_category_spinner);
        mTransactionTypeSpinner = (Spinner) rootView.findViewById(R.id.transaction_type_spinner);

        if (mCategoryList == null) {
            if (savedInstanceState == null){
                mCategoryList = new ArrayList<>();
                mCategoryList.add(new SpinnerData(0, "Root category"));
            } else {
                mCategoryList = (ArrayList<SpinnerData>) savedInstanceState.getSerializable("mCategoryList");
            }

        }

        if (mCategoryAdapter == null) {
            mCategoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mCategoryList);
            mCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mParentCategorySpinner.setAdapter(mCategoryAdapter);
        }
        // TODO: do localisation
        mParentCategorySpinner.setPrompt("Select parent category");


        TransactionType[] data = {TransactionType.Expense, TransactionType.Income, TransactionType.Transfer};

        if (mTransactionTypeAdapter == null) {
            mTransactionTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
            mTransactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTransactionTypeSpinner.setAdapter(mTransactionTypeAdapter);
        }
        // TODO: do localisation
        mTransactionTypeSpinner.setPrompt("Select transaction type");

        /*
        // ������������� ���������� �������
        mTransactionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        */

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CATEGORY_DETAIL_LOADER, null, this);
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
                editCategory();
                return true;
            }

            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.category_confirm_delete);

                builder.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().setResult(RESULT_DELETE, getActivity().getIntent().putExtra("id", mCategoryId));
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


    private void editCategory() {
        if (!TextUtils.isEmpty(mEtCategoryName.getText().toString())) {
            Bundle category = new Bundle();
            category.putInt(MyFinancesContract.Category._ID, mCategoryId);
            category.putString(MyFinancesContract.Category.COLUMN_NAME, mEtCategoryName.getText().toString());

            int val = ((SpinnerData) mParentCategorySpinner.getItemAtPosition(mParentCategorySpinner.getSelectedItemPosition())).getKey();
            category.putInt(MyFinancesContract.Category.COLUMN_PARENT_ID, val);

            val = ((TransactionType) mTransactionTypeSpinner.getItemAtPosition(mTransactionTypeSpinner.getSelectedItemPosition())).getId();
            category.putInt(MyFinancesContract.Category.COLUMN_TRANSACTION_TYPE_ID, val);

            getActivity().getIntent().putExtras(category);

            getActivity().setResult(RESULT_SAVE, getActivity().getIntent());
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        getActivity().finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //Intent intent = getActivity().getIntent();
        //if (intent.getData() != null) {

            return new CursorLoader(
                    getActivity(),
                    MyFinancesContract.Category.CONTENT_URI,
                    //intent.getData(), // Uri
                    MyFinancesContract.Category.CATEGORY_COLUMNS,
                    null,
                    null,
                    null
            );
       // }
        //return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int id = 0; // id of category
        int parentId = 0; // id of parent category
        int pos = 0;

        Intent intent = getActivity().getIntent();
        if (intent.getData() != null){
            id = (int) ContentUris.parseId(intent.getData());
        }

        if (cursor != null) {

            while (cursor.moveToNext()) {

                if (id == cursor.getInt(MyFinancesContract.Category.COL_ID_IDX)) {
                    mCategoryId = cursor.getInt(MyFinancesContract.Category.COL_ID_IDX);
                    mEtCategoryName.setText(cursor.getString(MyFinancesContract.Category.COL_NAME_IDX));

                    pos = mTransactionTypeAdapter.getPosition(TransactionType.getTypeById(cursor.getInt(MyFinancesContract.Category.COL_TRANSACTION_TYPE_ID_IDX)));
                    mTransactionTypeSpinner.setSelection(pos);

                    parentId =  cursor.getInt(MyFinancesContract.Category.COL_PARENT_ID_IDX);

                } else {
                    mCategoryList.add(new SpinnerData(cursor.getInt(MyFinancesContract.Category.COL_ID_IDX), cursor.getString(MyFinancesContract.Category.COL_NAME_IDX)));
                }

            }

            mCategoryAdapter.notifyDataSetChanged();

            for (SpinnerData category : mCategoryList) {
                if (parentId == category.getKey()){
                    pos = mCategoryAdapter.getPosition(category);
                    mParentCategorySpinner.setSelection(pos);
                    break;
                }
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
    }




}
