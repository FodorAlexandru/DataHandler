package com.example.shade_000.datahandler.users;

import android.database.Cursor;

import common.base.BasePresenter;
import common.base.BaseView;

/**
 * Created by shade_000 on 09/04/2016.
 */
public interface UsersContract {
    interface View extends BaseView<Presenter>{
        void showUsers(Cursor users);
        void clearUsers();
    }

    interface Presenter extends BasePresenter{

    }
}
