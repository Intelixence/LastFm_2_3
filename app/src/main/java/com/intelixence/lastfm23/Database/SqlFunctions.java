package com.intelixence.lastfm23.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.intelixence.lastfm23.Utils.CustomLog;

public class SqlFunctions {

    public static boolean dataAlreadyExist(Context context, int code_request){
        ManagerDatabase managerDatabase = new ManagerDatabase(context);
        SQLiteDatabase sqlite_db = managerDatabase.getWritableDatabase();
        if (sqlite_db != null){
            String selectQuery = null;
            if (code_request == 1){
                selectQuery = "SELECT * FROM data_offline WHERE category = 'TopTracks'";
            }else{
                selectQuery = "SELECT * FROM data_offline WHERE category = 'TopArtists'";
            }
            Cursor cursor = sqlite_db.rawQuery(selectQuery, null);
            int row_count = cursor.getCount();
            cursor.close();
            if (row_count > 0){
                CustomLog.i("SqlFunctions", "data exist");
                return true;
            }else{
                CustomLog.i("SqlFunctions", "data not exist");
                return false;
            }
        }else{
            CustomLog.i("SqlFunctions", "data not exists");
            return false;
        }
    }

    public static void insertData(Context context, int code_request, String response){
        ManagerDatabase managerDatabase = new ManagerDatabase(context);
        SQLiteDatabase sqlite_db = managerDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (code_request == 1){
            contentValues.put("category", "TopTracks");
        }else{
            contentValues.put("category", "TopArtists");
        }
        contentValues.put("data", response);
        sqlite_db.insert("data_offline", null, contentValues);
        CustomLog.i("SqlFunctions", "data inserted");
    }

    public static void updateData(Context context, int code_request, String response){
        ManagerDatabase managerDatabase = new ManagerDatabase(context);
        SQLiteDatabase sqlite_db = managerDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String category = null;
        if (code_request == 1){
            category = "TopTracks";
        }else{
            category = "TopArtists";
        }
        contentValues.put("data", response);
        sqlite_db.update("data_offline", contentValues, "category='"+category+"'", null);
        CustomLog.i("SqlFunctions", "update row");
    }

    public static String getData(Context context, int code_request){
        ManagerDatabase managerDatabase = new ManagerDatabase(context);
        SQLiteDatabase sqlite_db = managerDatabase.getWritableDatabase();
        if (sqlite_db != null){
            String selectQuery = null;
            if (code_request == 1){
                selectQuery = "SELECT * FROM data_offline WHERE category = 'TopTracks'";
            }else{
                selectQuery = "SELECT * FROM data_offline WHERE category = 'TopArtists'";
            }
            Cursor cursor = sqlite_db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String data = cursor.getString(cursor.getColumnIndex("data"));
            cursor.close();
            return data;
        }else{
            CustomLog.i("SqlFunctions", "data not exists in getData");
            return null;
        }
    }

}
