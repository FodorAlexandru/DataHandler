package com.example.shade_000.datahandler.business.loaders;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import common.base.AbstractObservingLoader;
import common.base.DisableableContentObserver;

/**
 * Created by shade_000 on 3/29/2016.
 */
public class UserObservingLoader extends AbstractObservingLoader<Cursor> {
    //region Fields
    private final DisableableContentObserver mObserver;
    private Cursor mCursor;
    //endregion

    //region Constructor
    public UserObservingLoader(Context context) {
        super(context);
        mObserver = new DisableableContentObserver(new ForceLoadContentObserver());
    }
    //endregion

    //region Overrides

    @Override
    public Cursor loadInBackground() {


        return mCursor;
    }

    @Override
    protected void onNewDataDelivered(Cursor data) {
        super.onNewDataDelivered(data);
        data.registerContentObserver(mObserver);
    }

    @Override
    protected void registerObserver(ContentObserver observer) {
        mCursor.registerContentObserver(observer);
    }

    @Override
    protected void unregisterObserver(ContentObserver observer) {
        mCursor.unregisterContentObserver(observer);
    }

    @Override
    protected void releaseResources(Cursor result) {
        if(result!=null && !result.isClosed()){
            result.close();
        }
    }

    //endregion
}
