package com.example.shade_000.datahandler.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import util.AbstractObservingLoader;
import com.example.shade_000.datahandler.data.source.local.DatabaseContract;

/**
 * Created by shade_000 on 3/29/2016.
 */
public class UserObservingLoader extends AbstractObservingLoader<Cursor> {
    //region Fields
    private final String TAG = getClass().getSimpleName();
    private final Uri mObservedUri;
    //endregion

    //region Constructor
    public UserObservingLoader(Context context,Uri uri) {
        super(context);
        mObservedUri = uri;
    }

    //endregion

    //region Overrides

    @Override
    public Cursor loadInBackground() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(mObservedUri, DatabaseContract.User.USER_COLUMNS, null, null, DatabaseContract.User.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
        }
        return cursor;
    }

    @Override
    protected void onNewDataDelivered(Cursor data) {
        super.onNewDataDelivered(data);
        data.registerContentObserver(mObserver);
    }

    @Override
    protected void registerObserver(ContentObserver observer) {
        getContext().getContentResolver().registerContentObserver(mObservedUri, true, observer);
        Log.i(TAG, "Content Observer Registered");
    }

    @Override
    protected void unregisterObserver(ContentObserver observer) {
        getContext().getContentResolver().unregisterContentObserver(observer);
        Log.i(TAG, "Content Observer UnRegistered");
    }

    @Override
    protected void releaseResources(Cursor result) {
        if (result != null && !result.isClosed()) {
            result.close();
        }
    }

    //endregion

    //region Methods



    //endregion
}
