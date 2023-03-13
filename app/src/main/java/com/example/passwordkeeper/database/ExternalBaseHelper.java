package com.example.passwordkeeper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExternalBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static String EXTERNAL_DATABASE_NAME = "external_password_base";
    private SQLiteDatabase externalDatabase;

    public static void setExternalDatabaseName(String externalDatabaseName) {
        EXTERNAL_DATABASE_NAME = externalDatabaseName;
    }



    public SQLiteDatabase getExternalDatabase() {
        return externalDatabase;
    }

    public ExternalBaseHelper(@Nullable Context context) {
        super(context, EXTERNAL_DATABASE_NAME, null, VERSION);//OPEN_READWRITE  CREATE_IF_NECESSARY

        externalDatabase = SQLiteDatabase.openDatabase(EXTERNAL_DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
