package com.mokin.myfinances.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mokin.myfinances.app.adapters.AccountAdapter;
import com.mokin.myfinances.app.data.MyFinancesContract;
import com.mokin.myfinances.app.detail_activities.AccountDetails;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private AccountAdapter mAccountAdapter;

    public static String ACCOUNT_ENTRY = "com.mokin.myfinances.app.Account";
    public static int ACCOUNT_DETAILS_REQUEST = 1;

    private static final int ACCOUNT_LOADER = 0;


    //ArrayList<Account> accountArrayList = new ArrayList<Account>();
    //private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mAccountAdapter = new AccountAdapter(this, null, 0);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyView));
        listView.setAdapter(mAccountAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(MyFinancesContract.Account._ID, cursor.getLong(MyFinancesContract.Account.COL_ID_IDX));
                    bundle.putString(MyFinancesContract.Account.COLUMN_NAME, cursor.getString(MyFinancesContract.Account.COL_NAME_IDX));
                    bundle.putLong(MyFinancesContract.Account.COLUMN_CURRENCY_ID, cursor.getLong(MyFinancesContract.Account.COL_CURRENCY_ID_IDX));
                    bundle.putString(MyFinancesContract.Account.COLUMN_COMMENT, cursor.getString(MyFinancesContract.Account.COL_COMMENT_IDX));

                    showAccountDetails(bundle);
                }

            }
        };

        Cursor cursor = getContentResolver().query(MyFinancesContract.Account.CONTENT_URI, null, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                Account account = new Account(cursor.getLong(cursor.getColumnIndex(MyFinancesContract.Account._ID)),
                                              cursor.getString(cursor.getColumnIndex(MyFinancesContract.Account.COLUMN_NAME)),
                                              cursor.getDouble(cursor.getColumnIndex(MyFinancesContract.Account.COLUMN_INITIAL_BALANCE)),
                                              cursor.getString(cursor.getColumnIndex(MyFinancesContract.Account.COLUMN_COMMENT)));
                accountArrayList.add(account);
            }
        }

        accountAdapter = new AccountAdapter(this, accountArrayList);




        getLoaderManager().initLoader(ACCOUNT_LOADER, null, this);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


/*    class ListViewClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Account account = (Account) parent.getAdapter().getItem(position);
            account.setIndex(position);

            showAccountDetails(account);
        }
    }*/


    private void showAccountDetails(Bundle bundle) {
        Intent intentAccountDetails = new Intent(MainActivity.this, AccountDetails.class);
        intentAccountDetails.putExtra(ACCOUNT_ENTRY, bundle);
        startActivityForResult(intentAccountDetails, ACCOUNT_DETAILS_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACCOUNT_DETAILS_REQUEST) {

            Account account;
            switch (resultCode) {
                case RESULT_CANCELED:
                    Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
                    break;

                case AccountDetails.RESULT_SAVE:
                    account = (Account) data.getSerializableExtra(ACCOUNT_ENTRY);
                    saveAccount(account);
                    break;

                case AccountDetails.RESULT_DELETE:
                    account = (Account) data.getSerializableExtra(ACCOUNT_ENTRY);
                    deleteAccount(account);
                    break;

                default:
                    break;
            }
        }
    }


    private void saveAccount(Account account) {
        int rows;
        ContentValues cv;

        if (account.getIndex() >= 0) {
        // update account

            cv = getAccountContentValues(account);
            rows = getContentResolver().update(MyFinancesContract.Account.CONTENT_URI, cv, "_id = " + account.get_id(), null);
            accountArrayList.set(account.getIndex(), account);
            Toast.makeText(this, "Updated rows: " + rows, Toast.LENGTH_SHORT).show();

        } else {
        // add  new account
            //Log.d("MainActivity", "Here goes add new account...");

            cv = getAccountContentValues(account);
            Uri uri = getContentResolver().insert(MyFinancesContract.Account.CONTENT_URI, cv);
            account.set_id(Long.valueOf(uri.getLastPathSegment()));
            accountArrayList.add(account);

            Toast.makeText(this, "New account added with ID = " + account.get_id(), Toast.LENGTH_SHORT).show();
        }

        Collections.sort(accountArrayList);
        accountAdapter.notifyDataSetChanged();
    }

    private void deleteAccount(Account account) {
        int rows = getContentResolver().delete(MyFinancesContract.Account.CONTENT_URI, "_id = " + account.get_id(), null);
        Toast.makeText(this, "Deleted rows: " + rows, Toast.LENGTH_SHORT).show();
        accountArrayList.remove(account.getIndex()); // ???
        accountAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // TODO: (amokin) as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.add_new:
                showAccountDetails(new Account());
        }

        return super.onOptionsItemSelected(item);
    }

    private ContentValues getAccountContentValues(Account account) {
        ContentValues cv = new ContentValues();
        if (account.get_id() > 0) {
            cv.put(MyFinancesContract.Account._ID, account.get_id());
        }
        cv.put(MyFinancesContract.Account.COLUMN_NAME, account.getName());
        cv.put(MyFinancesContract.Account.COLUMN_COMMENT, account.getComment());
        return cv;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // Sort order:  Ascending
        String sortOrder = MyFinancesContract.Account.COLUMN_NAME + " ASC";

        return new CursorLoader(this,
                MyFinancesContract.Account.CONTENT_URI,
                MyFinancesContract.Account.ACCOUNT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAccountAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAccountAdapter.swapCursor(null);
    }
}
