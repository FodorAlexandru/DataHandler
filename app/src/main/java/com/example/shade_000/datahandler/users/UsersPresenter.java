package com.example.shade_000.datahandler.users;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.shade_000.datahandler.data.source.UserLoaderProvider;
import com.example.shade_000.datahandler.data.source.UserObservingLoader;

/**
 * Created by shade_000 on 09/04/2016.
 */
public class UsersPresenter implements UsersContract.Presenter,LoaderManager.LoaderCallbacks<Cursor>{

    //region Fields
    private final static int USERS_QUERY = 1;
    private final UsersContract.View mUserView;
    private final LoaderManager mLoaderManager;
    private final UserLoaderProvider mUserLoaderProvider;
    //endregion

    //region Constructor

    public UsersPresenter(@NonNull LoaderManager loaderManager,@NonNull UserLoaderProvider userLoaderProvider,@NonNull UsersContract.View usersView){
        mLoaderManager = loaderManager;
        mUserView = usersView;
        mUserLoaderProvider = userLoaderProvider;

        usersView.setPresenter(this);
    }

    //endregion

    //region Presenter Overrides
    @Override
    public void start() {
        mLoaderManager.initLoader(USERS_QUERY, null, this);
    }

    //endregion

    //region Loader Overrides

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mUserLoaderProvider.createLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mUserView.showUsers(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUserView.clearUsers();
    }

    //endregion
}
