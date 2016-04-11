package com.example.shade_000.datahandler.users;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shade_000.datahandler.R;

import com.example.shade_000.datahandler.data.source.local.DatabaseContract;

/**
 * Created by shade_000 on 01/04/2016.
 */
public class UserAdapter extends CursorAdapter {

    //region Fields
    private LayoutInflater inflater;
    //endregion

    //region Constructor
    public UserAdapter(Context context, Cursor c, boolean autoQuery) {
        super(context, c, autoQuery);
        inflater = LayoutInflater.from(context);
    }
    //endregion

    //region Overrides
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.adapter_user_item, null);
        view.setTag(getDefaultViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.userAlis.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_NAME_NAME)));
        viewHolder.userEmail.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_NAME_EMAIL)));
    }
    //endregion

    //region Methods

    private ViewHolder getDefaultViewHolder(View view){
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.userAlis = (TextView)view.findViewById(R.id.adapter_user_item_alias);
        viewHolder.userEmail = (TextView)view.findViewById(R.id.adapter_user_item_email);
        return viewHolder;
    }

    //endregion

    private class ViewHolder  {
        TextView userAlis, userEmail;
    }
}
