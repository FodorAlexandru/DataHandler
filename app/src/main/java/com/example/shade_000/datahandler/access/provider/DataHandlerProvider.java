package com.example.shade_000.datahandler.access.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.shade_000.datahandler.data.database.DatabaseHelper;

import java.util.ArrayList;

import common.constants.DatabaseContract;

/**
 * Created by shade_000 on 3/29/2016.
 */
public class DataHandlerProvider extends ContentProvider {

    //region Fields
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<>();
    static {
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.User.TABLE_NAME, DatabaseContract.User.CONTENT_URI_CODE);
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.User.TABLE_NAME + "/#", DatabaseContract.User.CONTENT_ID_URI_CODE);
    }

    private DatabaseHelper dbhelper;
    //endregion Fields

    //region Overrides
    @Override
    public boolean onCreate() {
        dbhelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriMatch = sUriMatcher.match(uri);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (uriMatch) {
            case DatabaseContract.User.CONTENT_URI_CODE:
                builder.setTables(DatabaseContract.User.TABLE_NAME);
                break;
            case DatabaseContract.User.CONTENT_ID_URI_CODE:
                builder.setTables(DatabaseContract.User.TABLE_NAME);
                builder.appendWhere(DatabaseContract.User._ID + " = ");
                builder.appendWhere(uri.getLastPathSegment());
                break;
        }
        Cursor cursor =
                builder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
        cursor.setNotificationUri(
                getContext().getContentResolver(),
                DatabaseContract.User.CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DatabaseContract.User.CONTENT_URI_CODE:
                return DatabaseContract.User.CONTENT_TYPE;
            case DatabaseContract.User.CONTENT_ID_URI_CODE:
                return DatabaseContract.User.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        if (sUriMatcher.match(uri) == DatabaseContract.User.CONTENT_URI_CODE) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            long id =
                    db.insertWithOnConflict(
                            DatabaseContract.User.TABLE_NAME,
                            null,
                            contentValues,
                            SQLiteDatabase.CONFLICT_REPLACE);
            return getUriForId(id, uri);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        mIsInBatchMode.set(true);
        // the next line works because SQLiteDatabase
        // uses a thread local SQLiteSession object for
        // all manipulations
        db.beginTransaction();
        try {
            final ContentProviderResult[] retResult = super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(DatabaseContract.User.CONTENT_URI, null);
            return retResult;
        } finally {
            mIsInBatchMode.remove();
            db.endTransaction();
        }
    }

    //endregion Overrides

    //region Methods

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                // notify all listeners of changes:
                getContext().
                        getContentResolver().
                        notifyChange(itemUri, null);
            }
            return itemUri;
        }
        // s.th. went wrong:
        throw new SQLException(
                "Problem while inserting into uri: " + uri);
    }

    private boolean isInBatchMode() {
        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }

    //endregion
}
