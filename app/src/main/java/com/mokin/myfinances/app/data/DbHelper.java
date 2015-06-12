package com.mokin.myfinances.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mokin.myfinances.app.data.FinContract.Account;
import com.mokin.myfinances.app.data.FinContract.Budget;
import com.mokin.myfinances.app.data.FinContract.Category;
import com.mokin.myfinances.app.data.FinContract.Market;
import com.mokin.myfinances.app.data.FinContract.Transactions;

import java.util.Calendar;

/**
 * Manages a local database for MyFinances data.
 */
public class DbHelper extends SQLiteOpenHelper {

    // DB constants.
    public static final String DB_NAME = "myfinances.db";
    // If you change the database schema, you must increment the database version.
    public static final int DB_VERSION = 13;


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    // The strings that define the SQL statements for creating the tables

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + Category.TABLE_NAME + " (" +
                Category._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Category.COLUMN_NAME + " NVARCHAR NOT NULL," +
                Category.COLUMN_TRANSACTION_TYPE_ID + " INTEGER REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ") ON DELETE SET NULL," +
                Category.COLUMN_PARENT_ID + " INTEGER);";

        final String SQL_CREATE_ACCOUNT_TABLE = "CREATE TABLE " + Account.TABLE_NAME + " (" +
                Account._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Account.COLUMN_NAME + " NVARCHAR NOT NULL," +
                Account.COLUMN_CURRENCY_CODE + " NVARCHAR(3) NOT NULL," +
                Account.COLUMN_COMMENT + " NVARCHAR);";

        final String SQL_CREATE_BUDGET_TABLE = "CREATE TABLE " + Budget.TABLE_NAME + " (" +
                Budget._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Budget.COLUMN_CATEGORY_ID + " INTEGER NOT NULL REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ") ON DELETE RESTRICT," +
                Budget.COLUMN_BUDGET_AMOUNT + " DOUBLE NOT NULL," +
                Budget.COLUMN_DATETIME_BEGIN + " BIGINT NOT NULL," +
                Budget.COLUMN_DATETIME_END + " BIGINT NOT NULL," +
                Budget.COLUMN_ACCOUNT_ID + " INTEGER REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT);";

        final String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + Transactions.TABLE_NAME + " (" +
                Transactions._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Transactions.COLUMN_TRANSACTION_DATETIME + " INTEGER NOT NULL," +
                Transactions.COLUMN_TRANSACTION_AMOUNT + " DOUBLE NOT NULL," +
                Transactions.COLUMN_ACCOUNT_ID + " INTEGER NOT NULL REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT," +
                Transactions.COLUMN_CATEGORY_ID + " INTEGER REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ") ON DELETE RESTRICT," +
                Transactions.COLUMN_TRANSACTION_TYPE_ID + " INTEGER NOT NULL," +
                Transactions.COLUMN_MARKET_ID + " INTEGER REFERENCES " + Market.TABLE_NAME + "(" + Market._ID + ") ON DELETE SET NULL," +
                Transactions.COLUMN_COMMENT + " NVARCHAR," +
                Transactions.COLUMN_ACCOUNT_SOURCE + " INTEGER REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT," +
                Transactions.COLUMN_ACCOUNT_TARGET + " INTEGER REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT);";

        final String SQL_CREATE_MARKET_TABLE = "CREATE TABLE " + Market.TABLE_NAME + " (" +
                Market._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Market.COLUMN_NAME + " NVARCHAR NOT NULL," +
                Market.COLUMN_COMMENT + " NVARCHAR);";

        // TODO: populate currency table with values from txt file ?!
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MARKET_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BUDGET_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTIONS_TABLE);

        ContentValues cv = new ContentValues();

/*
        cv.put(TransactionType.COLUMN_NAME, "Расход");
        sqLiteDatabase.insert(TransactionType.TABLE_NAME, null, cv);

        for (int i = 1; i <= 3; i++) {
            cv.put(Category.COLUMN_NAME, "яйцо " + i);
            cv.put(Category.COLUMN_TRANSACTION_TYPE_ID, 1);
            sqLiteDatabase.insert(Category.TABLE_NAME, null, cv);
        }*/


        for (int i = 1; i <= 3; i++) {
            cv.put(Account.COLUMN_NAME, "Счет " + i);
            cv.put(Account.COLUMN_CURRENCY_CODE, "RUB");
            cv.put(Account.COLUMN_COMMENT, "Комментарий " + i);
            sqLiteDatabase.insert(Account.TABLE_NAME, null, cv);
        }

        cv.clear();

        for (int i = 1; i <= 3; i++) {
            cv.put(Category.COLUMN_NAME, "Категория " + i);
            cv.put(Category.COLUMN_TRANSACTION_TYPE_ID, i);
            sqLiteDatabase.insert(Category.TABLE_NAME, null, cv);
        }


        cv.clear();

        Calendar c = Calendar.getInstance();
        int utcOffset = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
        Long utcMilliseconds = c.getTimeInMillis() + utcOffset;

        for (int i = 1; i <= 2; i++) {
            cv.put(Transactions.COLUMN_TRANSACTION_DATETIME, utcMilliseconds);
            cv.put(Transactions.COLUMN_TRANSACTION_AMOUNT, 100*i);
            cv.put(Transactions.COLUMN_ACCOUNT_ID, i);
            cv.put(Transactions.COLUMN_TRANSACTION_TYPE_ID, i);

            sqLiteDatabase.insert(Transactions.TABLE_NAME, null, cv);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Transactions.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Budget.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Account.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Market.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
