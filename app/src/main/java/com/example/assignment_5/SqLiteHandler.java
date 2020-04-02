package com.example.assignment_5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.Nullable;

public class SqLiteHandler extends SQLiteOpenHelper {


    public SqLiteHandler(Context context) {
        super(context, "transactionHistory.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table moneyManager (\'date\' text, \'amount\' Float, \'reason\' text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists moneyManager");
        onCreate(db);
    }

}
