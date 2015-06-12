package com.mokin.myfinances.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyCustomProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    private static final int CATEGORY = 100;
    private static final int CATEGORY_ID = 101;
    private static final int CATEGORY_SEARCH = 102;
    private static final int MARKET = 200;
    private static final int MARKET_ID = 201;
    private static final int ACCOUNT = 400;
    private static final int ACCOUNT_ID = 401;
    private static final int BUDGET = 500;
    private static final int BUDGET_ID = 501;
    private static final int TRANSACTION = 600;
    private static final int TRANSACTION_ID = 601;



    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FinContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, FinContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, FinContract.PATH_CATEGORY + "/#", CATEGORY_ID);
        matcher.addURI(authority, FinContract.PATH_CATEGORY + "/*", CATEGORY_SEARCH);

        matcher.addURI(authority, FinContract.PATH_MARKET, MARKET);
        matcher.addURI(authority, FinContract.PATH_MARKET + "/#", MARKET_ID);

        matcher.addURI(authority, FinContract.PATH_ACCOUNT, ACCOUNT);
        matcher.addURI(authority, FinContract.PATH_ACCOUNT + "/#", ACCOUNT_ID);

        matcher.addURI(authority, FinContract.PATH_BUDGET, BUDGET);
        matcher.addURI(authority, FinContract.PATH_BUDGET + "/#", BUDGET_ID);

        matcher.addURI(authority, FinContract.PATH_TRANSACTION, TRANSACTION);
        matcher.addURI(authority, FinContract.PATH_TRANSACTION + "/#", TRANSACTION_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri = null;
        long _id;

        switch (sUriMatcher.match(uri)) {
            case CATEGORY:
                _id = db.insert(FinContract.Category.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ContentUris.withAppendedId(FinContract.Category.CONTENT_URI, _id);
                break;
            case MARKET:
                _id = db.insert(FinContract.Market.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ContentUris.withAppendedId(FinContract.Market.CONTENT_URI, _id);
                break;
            case ACCOUNT:
                _id = db.insert(FinContract.Account.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ContentUris.withAppendedId(FinContract.Account.CONTENT_URI, _id);
                break;
            case BUDGET:
                _id = db.insert(FinContract.Budget.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ContentUris.withAppendedId(FinContract.Budget.CONTENT_URI, _id);
                break;
            case TRANSACTION:
                _id = db.insert(FinContract.Transactions.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ContentUris.withAppendedId(FinContract.Transactions.CONTENT_URI, _id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (_id <= 0) throw new android.database.SQLException("Failed to insert row into " + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case CATEGORY:
                rowsDeleted = db.delete(FinContract.Category.TABLE_NAME, selection, selectionArgs);
                break;
            case MARKET:
                rowsDeleted = db.delete(FinContract.Market.TABLE_NAME, selection, selectionArgs);
                break;
            case ACCOUNT:
                rowsDeleted = db.delete(FinContract.Account.TABLE_NAME, selection, selectionArgs);
                break;
            case BUDGET:
                rowsDeleted = db.delete(FinContract.Budget.TABLE_NAME, selection, selectionArgs);
                break;
            case TRANSACTION:
                rowsDeleted = db.delete(FinContract.Transactions.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            // "category"
            case CATEGORY:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Category.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "category/#"
            case CATEGORY_ID:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Category.TABLE_NAME,
                        projection,
                        FinContract.Category._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "category/*"
            case CATEGORY_SEARCH:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Category.TABLE_NAME,
                        projection,
                        FinContract.Category.COLUMN_NAME + " LIKE ?",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "market"
            case MARKET:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Market.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "market/#"
            case MARKET_ID:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Market.TABLE_NAME,
                        projection,
                        FinContract.Market._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "account"
            case ACCOUNT:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Account.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "account/#"
            case ACCOUNT_ID:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Account.TABLE_NAME,
                        projection,
                        FinContract.Account._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "budget"
            case BUDGET:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Budget.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "budget/#"
            case BUDGET_ID:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Budget.TABLE_NAME,
                        projection,
                        FinContract.Budget._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "transaction"
            case TRANSACTION:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Transactions.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "transaction/#"
            case TRANSACTION_ID:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        FinContract.Transactions.TABLE_NAME,
                        projection,
                        FinContract.Transactions._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case CATEGORY:
                rowsUpdated = db.update(FinContract.Category.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MARKET:
                rowsUpdated = db.update(FinContract.Market.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRANSACTION:
                rowsUpdated = db.update(FinContract.Transactions.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ACCOUNT:
                rowsUpdated = db.update(FinContract.Account.TABLE_NAME, values, selection, selectionArgs);
                break;
            case BUDGET:
                rowsUpdated = db.update(FinContract.Budget.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}


