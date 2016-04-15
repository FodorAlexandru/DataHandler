package com.example.shade_000.datahandler.users;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
    private SwipeRefreshLayout swipeRefreshContainer;
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

    @Override
    public void setLoadingIndicator(final boolean active) {
        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshContainer.setRefreshing(active);
            }
        });
    }

    @Override
    public void showLoadingUsersError() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getContext().getString(R.string.default_error_tittle))
                .setMessage(getContext().getString(R.string.default_error_message))
                .setCancelable(true)
                .setPositiveButton(getContext().getString(R.string.ok_button), null)
                .create().show();
    }

    //endregion

    //region Methods

    private void initControls(View view){
        ListView listView = (ListView) view.findViewById(R.id.fragment_main_list_view);
        mUserAdapter = new UserAdapter(getContext(),null,false);
        listView.setAdapter(mUserAdapter);

        swipeRefreshContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUsersPresenter.loadUsers(getActivity());
            }
        });
        swipeRefreshContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        FloatingActionButton floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.fab_add_users);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsersPresenter.loadUsers(getActivity());
            }
        });

    }

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    //endregion Methods
}
