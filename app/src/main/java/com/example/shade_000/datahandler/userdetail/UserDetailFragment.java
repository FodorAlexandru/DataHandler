package com.example.shade_000.datahandler.userdetail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.shade_000.datahandler.R;
import com.example.shade_000.datahandler.data.source.UserLoaderProvider;
import com.example.shade_000.datahandler.data.source.local.DatabaseContract;

import common.base.BaseFragment;
import common.constants.Extras;

/**
 * Created by shade_000 on 03/05/2016.
 */
public class UserDetailFragment extends BaseFragment implements UserDetailContract.View {
    //region Fields
    private UserDetailContract.Presenter presenter;
    private TextView name;
    private TextView alias;
    private TextView email;
    private TextView phone;
    private int userId;
    //endregion

    //region Overrides

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            userId = getArguments().getInt(Extras.FRAGMENT_USER_DETAIL_ARGUMENT_USER_ID);
        }


        new UserDetailPresenter(userId,getActivity().getSupportLoaderManager(),new UserLoaderProvider(getContext()),this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls(view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_detail;
    }

    //endregion

    //region Lifecycle Overrides

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    //endregion

    //region View Overrides

    @Override
    public void setPresenter(UserDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUserDetail(Cursor data) {
        name.setText(data.getString(data.getColumnIndex(DatabaseContract.User.COLUMN_NAME_NAME)));
        alias.setText(data.getString(data.getColumnIndex(DatabaseContract.User.COLUMN_NAME_ALIAS)));
        phone.setText(data.getString(data.getColumnIndex(DatabaseContract.User.COLUMN_NAME_PHONE)));
        email.setText(data.getString(data.getColumnIndex(DatabaseContract.User.COLUMN_NAME_EMAIL)));
    }

    @Override
    public void showNavigationUpIcon() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    //endregion

    //region Methods

    private void initControls(View view){
        name = (TextView) view.findViewById(R.id.fragment_task_detail_name);
        alias = (TextView) view.findViewById(R.id.fragment_task_detail_alias);
        phone = (TextView) view.findViewById(R.id.fragment_task_detail_phone);
        email = (TextView) view.findViewById(R.id.fragment_task_detail_email);
    }

    public static UserDetailFragment newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(Extras.FRAGMENT_USER_DETAIL_ARGUMENT_USER_ID,userId);
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //endregion
}
