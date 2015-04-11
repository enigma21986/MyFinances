package com.mokin.myfinances.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mokin.myfinances.app.data.MyFinancesContract.*;

/**
 * Manages a local database for MyFinances data.
 */
public class DbHelper extends SQLiteOpenHelper {

    // DB constants.
    public static final String DB_NAME = "myfinances.db";
    // If you change the database schema, you must increment the database version.
    public static final int DB_VERSION = 5;


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    // The strings that define the SQL statements for creating the tables

        final String SQL_CREATE_TRANSACTION_TYPE_TABLE = "CREATE TABLE " + TransactionType.TABLE_NAME + " (" +
                TransactionType._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                TransactionType.COLUMN_NAME + " NVARCHAR NOT NULL);";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + Category.TABLE_NAME + " (" +
                Category._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Category.COLUMN_NAME + " NVARCHAR NOT NULL," +
                Category.COLUMN_TRANSACTION_TYPE_ID + " INTEGER REFERENCES " + TransactionType.TABLE_NAME + "(" + TransactionType._ID + ") ON DELETE RESTRICT DEFAULT 1," +
                Category.COLUMN_PARENT_ID + " INTEGER);";

        final String SQL_CREATE_CURRENCY_TABLE = "CREATE TABLE " + Currency.TABLE_NAME + " (" +
                Currency._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Currency.COLUMN_NAME + " NVARCHAR NOT NULL," +
                Currency.COLUMN_CURRENCY_CODE + " NVARCHAR NOT NULL," +
                Currency.COLUMN_CURRENCY_SYMBOL + " NVARCHAR);";

        final String SQL_CREATE_ACCOUNT_TABLE = "CREATE TABLE " + Account.TABLE_NAME + " (" +
                Account._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Account.COLUMN_NAME + " NVARCHAR NOT NULL," +
                // TODO: uncomment it!!!
                //Account.COLUMN_CURRENCY_ID + " INTEGER NOT NULL REFERENCES " + Currency.TABLE_NAME + "(" + Currency._ID + ") ON DELETE RESTRICT," +
                Account.COLUMN_COMMENT + " NVARCHAR);";

        final String SQL_CREATE_BUDGET_TABLE = "CREATE TABLE " + Budget.TABLE_NAME + " (" +
                Budget._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Budget.COLUMN_CATEGORY_ID + " INTEGER NOT NULL REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ") ON DELETE RESTRICT," +
                Budget.COLUMN_BUDGET_AMOUNT + " DOUBLE NOT NULL," +
                Budget.COLUMN_DATETIME_BEGIN + " BIGINT NOT NULL," +
                Budget.COLUMN_DATETIME_END + " BIGINT NOT NULL," +
                Budget.COLUMN_ACCOUNT_ID + " INTEGER REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT);";

        final String SQL_CREATE_TRANSACTION_TABLE = "CREATE TABLE " + Transaction.TABLE_NAME + " (" +
                Transaction._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                Transaction.COLUMN_TRANSACTION_DATETIME + " INTEGER NOT NULL," +
                Transaction.COLUMN_TRANSACTION_AMOUNT + " DOUBLE NOT NULL," +
                Transaction.COLUMN_ACCOUNT_ID + " INTEGER NOT NULL REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT," +
                Transaction.COLUMN_CATEGORY_ID + " INTEGER REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ") ON DELETE RESTRICT," +
                Transaction.COLUMN_TRANSACTION_TYPE_ID + " INTEGER NOT NULL REFERENCES " + TransactionType.TABLE_NAME + "(" + TransactionType._ID + ") ON DELETE RESTRICT," +
                Transaction.COLUMN_COMMENT + " NVARCHAR," +
                Transaction.COLUMN_ACCOUNT_SOURCE + " INTEGER REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT," +
                Transaction.COLUMN_ACCOUNT_TARGET + " INTEGER REFERENCES " + Account.TABLE_NAME + "(" + Account._ID + ") ON DELETE RESTRICT);";

        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTION_TYPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        // TODO: populate currency table with values from txt file ?!
        sqLiteDatabase.execSQL(SQL_CREATE_CURRENCY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BUDGET_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTION_TABLE);

        ContentValues cv = new ContentValues();

/*        cv.put(TransactionType.COLUMN_NAME, "Доход");
        sqLiteDatabase.insert(TransactionType.TABLE_NAME, null, cv);

        cv.put(TransactionType.COLUMN_NAME, "Расход");
        sqLiteDatabase.insert(TransactionType.TABLE_NAME, null, cv);

        for (int i = 1; i <= 3; i++) {
            cv.put(Category.COLUMN_NAME, "яйцо " + i);
            cv.put(Category.COLUMN_TRANSACTION_TYPE_ID, 1);
            sqLiteDatabase.insert(Category.TABLE_NAME, null, cv);
        }*/

        for (int i = 1; i <= 3; i++) {
            cv.put(Account.COLUMN_NAME, "Счет " + i);
            cv.put(Account.COLUMN_COMMENT, "Комментарий " + i);
            sqLiteDatabase.insert(Account.TABLE_NAME, null, cv);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Transaction.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Budget.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Account.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Currency.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionType.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
