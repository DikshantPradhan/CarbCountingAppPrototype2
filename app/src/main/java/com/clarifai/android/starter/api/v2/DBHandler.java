package com.clarifai.android.starter.api.v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dikshant on 2/5/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, "Name", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String test = "test";
        db.execSQL(test);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        //db.execSQL(“DROP TABLE IF EXISTS ” + TABLE_SHOPS);
// Creating tables again
        onCreate(db);
    }
}
