package com.example.shade_000.datahandler.users;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.shade_000.datahandler.data.models.User;
import com.example.shade_000.datahandler.data.models.eventBuss.UserErrorMessage;
import com.example.shade_000.datahandler.data.models.volley.GsonRequest;
import com.example.shade_000.datahandler.data.source.UserLoaderProvider;
import com.example.shade_000.datahandler.data.source.local.DatabaseContract;
import com.example.shade_000.datahandler.data.source.network.NetworkBackgroundProcessingService;
import com.example.shade_000.datahandler.data.source.network.VolleyHandler;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;

import common.constants.EnumConstants;
import util.NetworkUtils;

/**
 * Created by shade_000 on 09/04/2016.
 */
public class UsersPresenter implements UsersContract.Presenter,LoaderManager.LoaderCallbacks<Cursor>{

    //region Fields
    private final int USERS_QUERY = 1;
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

    @Override
    public void loadUsers(final Context context) {
        mUserView.setLoadingIndicator(true);
        Intent intent = new Intent(context, NetworkBackgroundProcessingService.class);
        intent.putExtra(NetworkUtils.OPERATION_KEY, EnumConstants.NetworkOperations.Get_Users.getId());
        context.startService(intent);
    }

    @Override
    public void processError(VolleyError volleyError) {
        mUserView.setLoadingIndicator(false);
        mUserView.showLoadingUsersError();
    }

    @Override
    public void openUserDetails(int userId) {
        mUserView.showUserDetailUi(userId);
    }

    //endregion

    //region Loader Overrides

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mUserView.setLoadingIndicator(true);
        return mUserLoaderProvider.createLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUserView.clearUsers();
    }

    //endregion

    //region Methods

    private void showData(Cursor data){
        mUserView.setLoadingIndicator(false);
        mUserView.showUsers(data);
        mUserView.showNavigationUpIcon();
    }

    //endregion
}
