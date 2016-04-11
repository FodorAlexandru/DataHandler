package com.example.shade_000.datahandler.data.source;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by shade_000 on 09/04/2016.
 */
public class UserLoaderProvider {
    @NonNull
    private final Context mContext;

    public UserLoaderProvider(@NonNull Context context) {
        mContext = context;
    }

    public Loader<Cursor> createLoader() {
        return new UserObservingLoader(mContext);
    }
}
