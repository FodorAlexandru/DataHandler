package com.example.shade_000.datahandler.data;

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
import android.text.TextUtils;

import com.example.shade_000.datahandler.data.source.local.DatabaseHelper;

import java.util.ArrayList;

import com.example.shade_000.datahandler.data.source.local.DatabaseContract;

/**
 * Created by shade_000 on 3/29/2016.
 */
public class DataHandlerProvider extends ContentProvider {

    //region Fields
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<>();
    private DatabaseHelper dbHelper;
    private static final int USER = 100;
    private static final int USER_ITEM = 101;
    //endregion Fields

    //region Overrides
    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriMatch = sUriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (uriMatch) {
            case USER:
                builder.setTables(DatabaseContract.User.TABLE_NAME);
                break;
            case USER_ITEM:
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
                DatabaseContract.User.CONTENT_USER_URI);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USER:
                return DatabaseContract.User.CONTENT_USER_TYPE;
            case USER_ITEM:
                return DatabaseContract.User.CONTENT_USER_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        if (sUriMatcher.match(uri) == USER) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriMatch = sUriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRowsCount = 0;

        switch (uriMatch){
            case USER:
                deletedRowsCount =
                        db.delete(
                                DatabaseContract.User.TABLE_NAME,
                                selection,
                                selectionArgs);
                break;
            case USER_ITEM:
                String idStr = uri.getLastPathSegment();
                String where = DatabaseContract.User._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                deletedRowsCount = db.delete(
                        DatabaseContract.User.TABLE_NAME,
                        where,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deletedRowsCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRowsCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int uriMatch = sUriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateCount = 0;

        switch (uriMatch){
            case USER:
                updateCount =
                        db.update(
                                DatabaseContract.User.TABLE_NAME,
                                contentValues,
                                selection,
                                selectionArgs);
                break;
            case USER_ITEM:
                String idStr = uri.getLastPathSegment();
                String where = DatabaseContract.User._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                updateCount = db.update(
                        DatabaseContract.User.TABLE_NAME,
                        contentValues,
                        where,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updateCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mIsInBatchMode.set(true);
        // the next line works because SQLiteDatabase
        // uses a thread local SQLiteSession object for
        // all manipulations
        db.beginTransaction();
        try {
            final ContentProviderResult[] retResult = super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(DatabaseContract.User.buildTasksUri(), null);
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

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, DatabaseContract.User.TABLE_NAME, USER);
        matcher.addURI(authority, DatabaseContract.User.TABLE_NAME + "/*", USER_ITEM);
        return matcher;
    }

    //endregion
}
