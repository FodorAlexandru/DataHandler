package com.example.shade_000.datahandler.users;

import android.content.Context;
import android.database.Cursor;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import common.base.BasePresenter;
import common.base.BaseView;

/**
 * Created by shade_000 on 09/04/2016.
 */
public interface UsersContract {
    interface View extends BaseView<Presenter>{
        void showUsers(Cursor users);
        void clearUsers();
        void setLoadingIndicator(boolean active);
        void showLoadingUsersError();
        void showUserDetailUi(int userId);
        void showNavigationUpIcon();
    }

    interface Presenter extends BasePresenter{
        void loadUsers(Context context);
        void processError(VolleyError volleyError);
        void openUserDetails(int userId);
    }
}
