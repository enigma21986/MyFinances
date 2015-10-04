package com.mokin.myfinances.app.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines tables and columns for "MyFinances" database.
 */
public class FinContract {

    // The "Content authority" is a name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.mokin.myfinances.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_ACCOUNT = "account";
    public static final String PATH_BUDGET = "budget";
    public static final String PATH_CURRENCY = "currency";
    public static final String PATH_TRANSACTION = "transactions";
    public static final String PATH_TRANSACTION_WITH_CATEGORY_NAME = "transactions_with_category_name";
    public static final String PATH_MARKET = "market";


    public static final class Category implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_CATEGORY;

        // Table fields
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PARENT_ID = "parent_id";
        public static final String COLUMN_TRANSACTION_TYPE_ID = "transaction_type";

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
        public static final String COLUMN_CURRENCY_CODE = "currency_code";
        public static final String COLUMN_COMMENT = "comment";

        public static final String[] ACCOUNT_COLUMNS = {
                TABLE_NAME + "." + _ID,
                COLUMN_NAME,
                COLUMN_CURRENCY_CODE,
                COLUMN_COMMENT
        };

        // Indexes related to ACCOUNT_COLUMNS. If ACCOUNT_COLUMNS changes, these must be changed
        public static final int COL_ID_IDX = 0;
        public static final int COL_NAME_IDX = 1;
        public static final int COL_CURRENCY_CODE_IDX = 2;
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


    public static final class Transactions implements BaseColumns {
        // Table name (path)
        public static final String TABLE_NAME = PATH_TRANSACTION;

        // Table fields
        public static final String COLUMN_TRANSACTION_DATETIME = "transaction_datetime";
        public static final String COLUMN_TRANSACTION_AMOUNT = "transaction_amount";
        public static final String COLUMN_ACCOUNT_ID = "account_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_CATEGORY_NAME = "name";
        public static final String COLUMN_TRANSACTION_TYPE_ID = "transaction_type_id";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_MARKET_ID = "market_id";
        public static final String COLUMN_ACCOUNT_SOURCE = "account_source";
        public static final String COLUMN_ACCOUNT_TARGET = "account_target";

        public static final String[] TRANSACTION_COLUMNS = {
                TABLE_NAME + "." + _ID,
                COLUMN_TRANSACTION_DATETIME,
                COLUMN_TRANSACTION_AMOUNT,
                COLUMN_ACCOUNT_ID,
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_NAME,
                COLUMN_TRANSACTION_TYPE_ID,
                COLUMN_COMMENT,
                COLUMN_MARKET_ID,
                COLUMN_ACCOUNT_SOURCE,
                COLUMN_ACCOUNT_TARGET
        };

        public static final String[] TRANSACTION_COLUMNS_2 = {
                _ID,
                COLUMN_TRANSACTION_DATETIME,
                COLUMN_TRANSACTION_AMOUNT,
                COLUMN_ACCOUNT_ID,
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_NAME,
                COLUMN_TRANSACTION_TYPE_ID,
                COLUMN_COMMENT,
                COLUMN_MARKET_ID,
                COLUMN_ACCOUNT_SOURCE,
                COLUMN_ACCOUNT_TARGET
        };

        // Indexes related to TRANSACTION_COLUMNS. If TRANSACTION_COLUMNS changes, these must be changed
        public static final int COL_ID_IDX = 0;
        public static final int COL_TRANSACTION_DATETIME_IDX = 1;
        public static final int COL_TRANSACTION_AMOUNT_IDX = 2;
        public static final int COL_ACCOUNT_ID_IDX = 3;
        public static final int COL_CATEGORY_ID_IDX = 4;
        public static final int COL_CATEGORY_NAME_IDX = 5;
        public static final int COL_TRANSACTION_TYPE_ID_IDX = 6;
        public static final int COL_COMMENT_IDX = 7;
        public static final int COL_MARKET_ID_IDX = 8;
        public static final int COL_ACCOUNT_SOURCE_IDX = 9;
        public static final int COL_ACCOUNT_TARGET_IDX = 10;

        // URI that identifies data in the provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build();
        public static final Uri CONTENT_URI_WITH_CATEGORY_NAME = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION_WITH_CATEGORY_NAME).build();

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
