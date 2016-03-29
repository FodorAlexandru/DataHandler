package com.example.shade_000.datahandler.access.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.shade_000.datahandler.data.database.DatabaseHelper;

import java.sql.SQLException;

import common.constants.UriConstants;

/**
 * Created by shade_000 on 3/29/2016.
 */
public class UserContentProvider extends ContentProvider {

    //region Fields
    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = UriConstants.CONTENT_AUTHORITY;
    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, UriConstants.USER_BASE_PATH, UriConstants.ROUTE_USERS);
        sUriMatcher.addURI(AUTHORITY, UriConstants.USER_BASE_PATH + "/#", UriConstants.ROUTE_USERS_ID);
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
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        int uriMatch = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (uriMatch) {
            case UriConstants.ROUTE_USERS:

            case UriConstants.ROUTE_USERS_ID:


        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UriConstants.ROUTE_USERS:
                return UriConstants.USER_CONTENT_TYPE;
            case UriConstants.ROUTE_USERS_ID:
                return UriConstants.USER_CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
    //endregion Overrides
}
