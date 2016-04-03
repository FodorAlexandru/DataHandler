package com.example.shade_000.datahandler.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import common.constants.DatabaseContract;

/**
 * Created by shade_000 on 3/29/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //region Constructor
    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null,  DatabaseContract.DATABASE_VERSION);
    }
    //endregion

    //region Overrides
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseContract.User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            sqLiteDatabase.execSQL(DatabaseContract.User.DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
    //endregion
}
