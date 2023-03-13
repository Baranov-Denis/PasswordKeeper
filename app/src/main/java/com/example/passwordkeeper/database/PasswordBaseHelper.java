package com.example.passwordkeeper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.example.passwordkeeper.database.PasswordDbSchema.PasswordTable;

public class PasswordBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "password_base";

    public PasswordBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table " + PasswordTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                PasswordTable.Cols.UUID + ", " +
                PasswordTable.Cols.RESOURCE_NAME + ", " +
                PasswordTable.Cols.LOGIN + ", " +
                PasswordTable.Cols.PASSWORD + ", " +
                PasswordTable.Cols.NOTE + ", " +
                PasswordTable.Cols.DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
