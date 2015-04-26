package com.mokin.myfinances.app.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines tables and columns for "MyFinances" database.
 */
public class MyFinancesContract {

    // The "Content authority" is a name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.mokin.myfinances.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_ACCOUNT = "account";
    public static final String PATH_BUDGET = "budget";
    public static final String PATH_CURRENCY = "currency";
    public static final String PATH_TRANSACTION = "transactions";
    public static final String PATH_MARKET = "market";


    public static final class Category implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_CATEGORY;

        // Table fields
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PARENT_ID = "parent_id";
        public static final String COLUMN_TRANSACTION_TYPE_ID = "transaction_type_id";

        public static final String[] CATEGORY_COLUMNS = {
                TABLE_NAME + "." + _ID,
                COLUMN_NAME,
                COLUMN_PARENT_ID,
                COLUMN_TRANSACTION_TYPE_ID
        };

        // Indexes related to CATEGORY_COLUMNS. If CATEGORY_COLUMNS changes, these must be changed
        public static final int COL_ID_IDX = 0;
        public static final int COL_NAME_IDX = 1;
        public static final int COL_PARENT_ID_IDX = 2;
        public static final int COL_TRANSACTION_TYPE_ID_IDX = 3;

        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        // MIME types
        public static final String MIME_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String MIME_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
    }


    public static final class Account implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_ACCOUNT;
        // Table fields
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CURRENCY_ID = "currency_id";
        public static final String COLUMN_COMMENT = "comment";

        public static final String[] ACCOUNT_COLUMNS = {
                TABLE_NAME + "." + _ID,
                COLUMN_NAME,
                COLUMN_CURRENCY_ID,
                COLUMN_COMMENT
        };

        // Indexes related to ACCOUNT_COLUMNS. If ACCOUNT_COLUMNS changes, these must be changed
        public static final int COL_ID_IDX = 0;
        public static final int COL_NAME_IDX = 1;
        public static final int COL_CURRENCY_ID_IDX = 2;
        public static final int COL_COMMENT_IDX = 3;

        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build();

        // MIME types
        public static final String MIME_TYPE =
                "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH_ACCOUNT;
        public static final String MIME_ITEM_TYPE =
                "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH_ACCOUNT;
    }


    public static final class Budget implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_BUDGET;

        // Table fields
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_BUDGET_AMOUNT = "budget_amount";
        public static final String COLUMN_DATETIME_BEGIN = "datetime_begin";
        public static final String COLUMN_DATETIME_END = "datetime_end";
        public static final String COLUMN_ACCOUNT_ID = "account_id";

        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUDGET).build();

        // MIME types
        public static final String MIME_TYPE =
                "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH_BUDGET;
        public static final String MIME_ITEM_TYPE =
                "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH_BUDGET;
    }

    public static final class Currency implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_CURRENCY;

        // Table fields
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CURRENCY_CODE = "currency_code";
        public static final String COLUMN_CURRENCY_SYMBOL = "currency_symbol";


        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CURRENCY).build();

        // MIME types
        public static final String MIME_TYPE =
                "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH_CURRENCY;
        public static final String MIME_ITEM_TYPE =
                "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH_CURRENCY;
    }


    public static final class Transactions implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_TRANSACTION;

        // Table fields
        public static final String COLUMN_TRANSACTION_DATETIME = "transaction_datetime";
        public static final String COLUMN_TRANSACTION_AMOUNT = "transaction_amount";
        public static final String COLUMN_ACCOUNT_ID = "account_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_TRANSACTION_TYPE_ID = "transaction_type_id";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_MARKET_ID = "market_id";
        public static final String COLUMN_ACCOUNT_SOURCE = "account_source";
        public static final String COLUMN_ACCOUNT_TARGET = "account_target";


        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build();

        // MIME types
        public static final String MIME_TYPE =
                "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH_TRANSACTION;
        public static final String MIME_ITEM_TYPE =
                "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH_TRANSACTION;
    }

    public static final class Market implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_MARKET;

        // Table fields
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMMENT = "comment";

        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MARKET).build();

        // MIME types
        public static final String MIME_TYPE =
                "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH_MARKET;
        public static final String MIME_ITEM_TYPE =
                "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH_MARKET;
    }

}
