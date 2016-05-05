package com.example.shade_000.datahandler.userdetail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.shade_000.datahandler.data.source.UserLoaderProvider;

/**
 * Created by shade_000 on 03/05/2016.
 */
public class UserDetailPresenter implements LoaderManager.LoaderCallbacks<Cursor>,UserDetailContract.Presenter {
    //region Fields
    private final int USER_DETAIL_QUERY = 1;
    private final int userId;
    private final UserDetailContract.View userDetailView;
    private final LoaderManager loaderManager;
    private final UserLoaderProvider userLoaderProvider;
    //endregion

    //region Constructor

    public UserDetailPresenter(int userId, @NonNull LoaderManager loaderManager, @NonNull UserLoaderProvider userLoaderProvider, @NonNull UserDetailContract.View userDetailView) {
        this.userDetailView = userDetailView;
        this.loaderManager = loaderManager;
        this.userLoaderProvider = userLoaderProvider;
        this.userId = userId;

        userDetailView.setPresenter(this);
    }

    //endregion

    //region Presenter Overrides

    @Override
    public void start() {
        loaderManager.initLoader(USER_DETAIL_QUERY, null, this);
    }

    //endregion

    //region Loader Overrides

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(userId == 0){
            return null;
        }
        return userLoaderProvider.createLoader(userId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!=null && data.moveToLast()){
            showUserDetail(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //endregion

    //region Methods

    private void showUserDetail(Cursor data){
        userDetailView.showUserDetail(data);
        userDetailView.showNavigationUpIcon();
    }

    //endregion


}
