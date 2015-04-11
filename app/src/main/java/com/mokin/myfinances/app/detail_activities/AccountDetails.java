package com.mokin.myfinances.app.detail_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mokin.myfinances.app.Account;
import com.mokin.myfinances.app.MainActivity;
import com.mokin.myfinances.app.R;

/**
 * Created by Alexey on 10.04.2015.
 */
public class AccountDetails extends ActionBarActivity {

    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;

    private EditText etAccountName;
    private EditText etAccountComment;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_details);

        etAccountName = (EditText) findViewById(R.id.et_account_name);
        etAccountComment = (EditText) findViewById(R.id.et_account_comment);

        account = (Account) getIntent().getSerializableExtra(MainActivity.ACCOUNT_ENTRY);

        // TODO: Проверка на null или пустое значение ?
        //setTitle(account.getName());
        etAccountName.setText(account.getName());
        etAccountComment.setText(account.getComment());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {

                if (TextUtils.isEmpty(etAccountName.getText().toString())) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    editAccount();
                }
                return true;
            }
            case R.id.save: {
                editAccount();
                return true;
            }

            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_delete);

                builder.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setResult(RESULT_DELETE, getIntent());
                                finish();
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
        if (!TextUtils.isEmpty(etAccountName.getText().toString())) {
            account.setName(etAccountName.getText().toString());
            account.setComment(etAccountComment.getText().toString());
            setResult(RESULT_SAVE, getIntent());
        } else {
            setResult(RESULT_CANCELED, getIntent());
        }
        finish();
    }
}
