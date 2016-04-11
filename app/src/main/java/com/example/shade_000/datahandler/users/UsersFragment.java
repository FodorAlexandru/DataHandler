package com.example.shade_000.datahandler.users;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.shade_000.datahandler.R;

import common.base.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class UsersFragment extends BaseFragment implements UsersContract.View{

    //region Fields
    private UserAdapter mUserAdapter;
    private UsersContract.Presenter mUsersPresenter;
    //endregion

    //region Lifecycle Overrides

    @Override
    public int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        mUsersPresenter.start();
    }

    //endregion Lifecycle Overrides

    //region View Overrides

    @Override
    public void showUsers(Cursor users) {
        mUserAdapter.swapCursor(users);
    }

    @Override
    public void clearUsers() {
        mUserAdapter.swapCursor(null);
    }

    @Override
    public void setPresenter(UsersContract.Presenter presenter) {
        mUsersPresenter = presenter;
    }

    //endregion

    //region Methods

    private void initControls(View view){
        ListView listView = (ListView) view.findViewById(R.id.fragment_main_list_view);
        mUserAdapter = new UserAdapter(getContext(),null,false);
        listView.setAdapter(mUserAdapter);
    }

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    //endregion Methods
}
