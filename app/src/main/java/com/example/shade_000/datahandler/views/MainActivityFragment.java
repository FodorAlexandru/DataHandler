package com.example.shade_000.datahandler.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.example.shade_000.datahandler.R;
import com.example.shade_000.datahandler.business.adapter.UserAdapter;
import com.example.shade_000.datahandler.business.loaders.UserObservingLoader;

import java.sql.SQLException;

import common.base.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    //region Fields
    private UserAdapter mUserAdapter;
    //endregion

    //region Overrides

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls(view);
        initLoader();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new UserObservingLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(mUserAdapter != null && cursor != null){
            mUserAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mUserAdapter != null){
            mUserAdapter.swapCursor(null);
        }
    }

    //endregion

    //region Methods

    private void initControls(View view){
        ListView listView = (ListView) view.findViewById(R.id.fragment_main_list_view);
        mUserAdapter = new UserAdapter(getContext(),null,false);
        listView.setAdapter(mUserAdapter);
    }

    private void initLoader(){
        getLoaderManager().initLoader(1, null, this);
    }


}
