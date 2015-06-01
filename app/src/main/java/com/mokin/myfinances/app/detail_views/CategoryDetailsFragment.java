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
import android.support.v4.widget.SimpleCursorAdapter;
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

import java.util.ArrayList;


public class CategoryDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mEtCategoryName;
    private Spinner mTransactionTypeSpinner;
    private Spinner mParentCategorySpinner;

    private int mCategoryId;
    //private ArrayList<String> mCategoryList = new ArrayList<>();
    private Cursor mCategoryCursor;

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

        //ArrayAdapter<String> adapterC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mCategoryList);
        //adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mParentCategorySpinner.setAdapter(adapterC);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(), // context
                android.R.layout.simple_spinner_item, // layout file
                mCategoryCursor, // DB cursor
                new String[]{MyFinancesContract.Category.COLUMN_NAME}, // data to bind to the UI
                new int[]{android.R.id.text1}, // views that'll represent the data from "fromColumns"
                0
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParentCategorySpinner.setAdapter(adapter);

        TransactionType[] data = {TransactionType.Expense, TransactionType.Income, TransactionType.Transfer};
        ArrayAdapter<TransactionType> adapterTT = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
        adapterTT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTransactionTypeSpinner.setAdapter(adapterTT);

        // заголовок ??
        //mTransactionTypeSpinner.setPrompt("Select transaction type");

        /*
        // устанавливаем обработчик нажатия
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
            //category.putInt(MyFinancesContract.Category.COLUMN_PARENT_ID, mParentCategorySpinner.getSelectedItemPosition());

            category.putInt(MyFinancesContract.Category.COLUMN_TRANSACTION_TYPE_ID,
                    ((TransactionType) mTransactionTypeSpinner.getItemAtPosition(mTransactionTypeSpinner.getSelectedItemPosition())).getId());

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

        int id = -1; // id of category
        int parentId = -1; // id of parent category

        Intent intent = getActivity().getIntent();
        if (intent.getData() != null){
            id = (int) ContentUris.parseId(intent.getData());
        }

        if (cursor != null) {

            mCategoryCursor = cursor;

            while (cursor.moveToNext()) {

                //mCategoryList.add(cursor.getString(MyFinancesContract.Category.COL_NAME_IDX));

                if (id == cursor.getInt(MyFinancesContract.Category.COL_ID_IDX)) {
                    mCategoryId = cursor.getInt(MyFinancesContract.Category.COL_ID_IDX);
                    mEtCategoryName.setText(cursor.getString(MyFinancesContract.Category.COL_NAME_IDX));

                    int pos = ((ArrayAdapter<TransactionType>) mTransactionTypeSpinner.getAdapter()).getPosition(TransactionType.getTypeById(cursor.getInt(MyFinancesContract.Category.COL_TRANSACTION_TYPE_ID_IDX)));
                    mTransactionTypeSpinner.setSelection(pos);

                    parentId =  cursor.getInt(MyFinancesContract.Category.COL_PARENT_ID_IDX);
                    mParentCategorySpinner.setSelection(0); // stub
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
