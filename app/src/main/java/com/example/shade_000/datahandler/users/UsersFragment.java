package com.example.shade_000.datahandler.users;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shade_000.datahandler.R;
import com.example.shade_000.datahandler.data.models.eventBuss.UserErrorMessage;
import com.example.shade_000.datahandler.data.source.UserLoaderProvider;
import com.example.shade_000.datahandler.userdetail.UserDetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import common.base.BaseFragment;
import util.ActivityUtils;
import util.NetworkUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class UsersFragment extends BaseFragment implements UsersContract.View{

    //region Fields
    private final static String ARG_LIST_STATE = "LIST_STATE";
    private UserAdapter mUserAdapter;
    private ListView listView;
    private Parcelable mListState = null;
    private UsersContract.Presenter mUsersPresenter;
    private SwipeRefreshLayout swipeRefreshContainer;
    //endregion

    //region Lifecycle Overrides

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            restoreControlsState(savedInstanceState);
        }
        new UsersPresenter(getActivity().getSupportLoaderManager(),new UserLoaderProvider(getActivity().getApplicationContext()),this);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_LIST_STATE,listView.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

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
    public void showNavigationUpIcon() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }

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

    @Override
    public void showUserDetailUi(int userId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentById(R.id.contentFrame);
        UserDetailFragment newFragment = UserDetailFragment.newInstance(userId);
        ActivityUtils.replaceFragmentFromActivity(fragmentManager,oldFragment,newFragment,R.id.contentFrame,true,null);
    }

    //endregion

    //region Methods

    private void initControls(View view){
        listView = (ListView) view.findViewById(R.id.fragment_main_list_view);
        mUserAdapter = new UserAdapter(getContext(),null,false);
        listView.setAdapter(mUserAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(i);
                mUsersPresenter.openUserDetails(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
            }
        });
        if(mListState!=null)
            listView.onRestoreInstanceState(mListState);

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

    private void restoreControlsState(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            mListState = savedInstanceState.getParcelable(ARG_LIST_STATE);
        }
    }

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    //endregion Methods

    //region Event Buss Methods

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onEvent(UserErrorMessage userErrorMessage){
        switch (userErrorMessage.getMessage()){
            case NetworkUtils.USER_NETWORK_ERROR:
                mUsersPresenter.processError(userErrorMessage.getError());
                break;
        }

        EventBus.getDefault().removeStickyEvent(userErrorMessage);
    }

    //endregion
}
