package com.example.shade_000.datahandler.users;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shade_000.datahandler.R;

import java.util.ArrayList;

import common.base.BaseActivity;
import util.ActivityUtils;

import com.example.shade_000.datahandler.data.source.local.DatabaseContract;

public class UserActivity extends BaseActivity {

    //region Fields
    private ActionBar actionBar;
    private String[][] data = {{"Gray Watson", "Gray.Watson@gmail.com"}, {"Ravi Verma", "Ravi.Verma@gmail.com"}, {"Ashok Singhal", "Ashok.Singhal@gmail.com"},{"Amit Yadav", "Amit.Yadav@gmail.com"}, {"Jake Wharton", "Jake.Wharton@gmail.com"}};
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

        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if(savedInstanceState != null)
            return;

        UsersFragment userFragment =
                (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (userFragment == null) {
            // Create the fragment
            userFragment = UsersFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), userFragment, R.id.contentFrame,false,null);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

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


}
