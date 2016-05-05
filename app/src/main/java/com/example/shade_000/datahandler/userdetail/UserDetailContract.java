package com.example.shade_000.datahandler.userdetail;

import android.database.Cursor;

import com.example.shade_000.datahandler.users.UsersContract;

import common.base.BasePresenter;
import common.base.BaseView;

/**
 * Created by shade_000 on 03/05/2016.
 */
public interface UserDetailContract {
    interface View extends BaseView<Presenter> {
        void showUserDetail(Cursor data);
        void showNavigationUpIcon();
    }

    interface Presenter extends BasePresenter {

    }
}
