package com.example.shade_000.datahandler.users;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shade_000.datahandler.R;

import java.util.ArrayList;

import common.base.BaseActivity;
import common.constants.EnumConstants;
import util.ActivityUtils;
import util.NetworkUtils;

import com.example.shade_000.datahandler.data.models.eventBuss.UserErrorMessage;
import com.example.shade_000.datahandler.data.source.UserLoaderProvider;
import com.example.shade_000.datahandler.data.source.local.DatabaseContract;
import com.example.shade_000.datahandler.data.source.network.NetworkBackgroundProcessingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UserActivity extends BaseActivity {

    //region Fields
    private String[][] data = {{"Gray Watson", "Gray.Watson@gmail.com"}, {"Ravi Verma", "Ravi.Verma@gmail.com"}, {"Ashok Singhal", "Ashok.Singhal@gmail.com"},{"Amit Yadav", "Amit.Yadav@gmail.com"}, {"Jake Wharton", "Jake.Wharton@gmail.com"}};
    private UsersContract.Presenter userPresenter;
    //endregion

    //region Overrides
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UsersFragment tasksFragment =
                (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = UsersFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame,null);
        }

        userPresenter = new UsersPresenter(getSupportLoaderManager(),new UserLoaderProvider(getApplicationContext()),tasksFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_test_data) {
            addTestData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.activity_users;
    }

    @Override
    public int getTabLayoutId() {
        return R.id.toolbar;
    }
    //endregion

    //region Methods

    private void addTestData(){
        ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>();
        for (String[] aData : data) {
            ops.add(ContentProviderOperation.newInsert(DatabaseContract.User.buildTasksUri())
                    .withValue(DatabaseContract.User.COLUMN_NAME_NAME, aData[0])
                    .withValue(DatabaseContract.User.COLUMN_NAME_EMAIL, aData[1])
                    .build());
        }

        try {
            ContentResolver resolver = getContentResolver();
            resolver.applyBatch(DatabaseContract.CONTENT_AUTHORITY, ops);
        } catch (OperationApplicationException e) {
            Log.e("wemonit", "cannot apply batch: " + e.getLocalizedMessage(), e);
        } catch (RemoteException e) {
            Log.e("wemonit", "cannot apply batch: " + e.getLocalizedMessage(), e);
        }
    }

    //endregion

    //region Event Buss Methods

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onEvent(UserErrorMessage userErrorMessage){
        switch (userErrorMessage.getMessage()){
            case NetworkUtils.USER_NETWORK_ERROR:
                userPresenter.processError(userErrorMessage.getError());
                break;
        }

        EventBus.getDefault().removeStickyEvent(userErrorMessage);
    }

    //endregion
}
