package com.intelixence.lastfm23.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ManagerDatabase extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_DATA_OFFLINE = "CREATE TABLE data_offline('category' TEXT, 'data' BLOB)";
    private static final String DB_NAME = "offline.sqlite";
    private static final int DB_VERSION = 1;

    public ManagerDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DATA_OFFLINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
