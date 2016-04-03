package common.constants;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

/**
 * Created by shade_000 on 30/03/2016.
 */
public final class DatabaseContract {
    //region Database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DatabaseHandler.db";
    //endregion
    //region Types
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String OPENING_PARENTHESES = " (";
    private static final String CLOSING_PARENTHESES = " )";
    private static final String NUMERIC_MATCH_CHAR = "#";
    //endregion Types
    //region Separators
    private static final String COMMA_SEP = ",";
    public static final String SLASH = "/";
    //endregion
    //region Provider
    public static final String AUTHORITY = "com.example.shade_000.datahandler.access.provider";
    public static final String SCHEME = "content://";

    //endregion
    public static final class User implements BaseColumns {

        /**
         * Private Constructor in order to prevent instantiation
         */
        private User() {
        }

        public static final String TABLE_NAME = "users";
        //region Columns
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ALIAS = "alias";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PHONE = "phone";
        //endregion

        //region Provider URIs
        /**
         * The content style URI
         */
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);

        /**
         * The content URI base for a single row. An ID must be appended.
         */
        public static final Uri CONTENT_ID_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH + NUMERIC_MATCH_CHAR);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = _ID + " ASC";

        /**
         * URI ID for route: /users
         */
        public static final int CONTENT_URI_CODE = 1;

        /**
         * URI ID for route: /users/{ID}
         */
        public static final int CONTENT_ID_URI_CODE = 2;

        //endregion Provider URIS

        //region MIME type definitions
        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/vnd.com.example.shade_0000.user";
        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/vnd.com.example.shade_0000.user";

        //endregion

        //region Sql Queries
        /**
         * SQL statement to create the table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                OPENING_PARENTHESES +
                _ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ALIAS + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PHONE + TEXT_TYPE +
                CLOSING_PARENTHESES;
        /**
         * SQL statement to delete the table
         */
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //endregion

        public static final String[] PROJECTION = {
            _ID,
            COLUMN_NAME_NAME,
            COLUMN_NAME_ALIAS,
            COLUMN_NAME_PHONE,
            COLUMN_NAME_EMAIL
        };

    }
    //region Constructor

    /**
     * Private Constructor in order to prevent instantiation
     */
    private DatabaseContract() {
    }

    //endregion
}
