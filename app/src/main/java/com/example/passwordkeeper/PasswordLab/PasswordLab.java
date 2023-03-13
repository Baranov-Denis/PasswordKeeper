package com.example.passwordkeeper.PasswordLab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.passwordkeeper.cipher.Cipher;
import com.example.passwordkeeper.database.ExternalBaseHelper;
import com.example.passwordkeeper.database.PasswordBaseHelper;
import com.example.passwordkeeper.database.PasswordCursorWrapper;
import com.example.passwordkeeper.database.PasswordDbSchema.PasswordTable;

public class PasswordLab {

    public static final String GLOBAL_TAG = "------>>>>";

    private static PasswordLab passwordLab;
    private static String keyCode = "999";
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase externalDatabase;


    public static String getKeyCode() {
        return keyCode;
    }

    public static void setKeyCode(String keyCode) {
        PasswordLab.keyCode = keyCode;
    }

    public static PasswordLab getLab(Context context) {
        if (passwordLab == null) {
            passwordLab = new PasswordLab(context);
        }
        return passwordLab;
    }

    private PasswordLab(Context context) {
        context = context.getApplicationContext();
        sqLiteDatabase = new PasswordBaseHelper(context).getWritableDatabase();
        externalDatabase = null;
    }

    public void loadExternalPasswordsList(Context context, String databasePath){
        List<PasswordCard> passwordCards = new ArrayList<>();
        ExternalBaseHelper.setExternalDatabaseName(databasePath);
        externalDatabase = new ExternalBaseHelper(context).getExternalDatabase();
        passwordCards = getPasswords();
        for (PasswordCard passwordCard : passwordCards){
            addPasswordCard(passwordCard);
        }
        externalDatabase = null;
    }

    public List<PasswordCard> getPasswords() {

        List<PasswordCard> passwordCards = new ArrayList<>();
        PasswordCard passwordCard = null;
        PasswordCursorWrapper cursor = null;

     if (externalDatabase == null) {
          cursor = queryPasswords(null, null);
     }else {
          cursor = externalQueryPasswords(null, null);
     }

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                passwordCard = cursor.getPasswordCard();
                if (passwordCard != null) {
                    passwordCards.add(passwordCard);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

    Collections.sort(passwordCards);


        return passwordCards;
    }

    public PasswordCard getPasswordCard(UUID uuid) {

        try (PasswordCursorWrapper cursor = queryPasswords(
                PasswordTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPasswordCard();
        }
    }


    public void deletePasswordById(UUID id) {
        sqLiteDatabase.delete(PasswordTable.NAME, PasswordTable.Cols.UUID + "= ?", new String[]{id.toString()});
    }


    public static boolean passwordIsWrong(PasswordCard passwordCard) {
        StringBuilder nameString = new StringBuilder();
        nameString.append(passwordCard.getResourceName());
        nameString.append(passwordCard.getLogin());
        nameString.append(passwordCard.getPassword());
        nameString.append(passwordCard.getDate());
        int charTest = 0;
        for (int i = 0; i < nameString.length(); i++) {
            charTest = nameString.charAt(i);
            if (charTest < 32 || (charTest > 126 && charTest < 1040) || charTest > 1104) {
                return true;
            }
        }
        return false;
    }

    public static boolean passwordIsWrongTestPasswordString() {
        PasswordCard passwordCard = passwordLab.getPasswords().get(0);
        return passwordIsWrong(passwordCard);
    }


    private static ContentValues getContentValues(PasswordCard passwordCard) {
        ContentValues values = new ContentValues();

//        values.put(PasswordTable.Cols.UUID, passwordCard.getId().toString());
//        values.put(PasswordTable.Cols.RESOURCE_NAME, passwordCard.getResourceName());
//        values.put(PasswordTable.Cols.LOGIN, passwordCard.getLogin());
//        values.put(PasswordTable.Cols.PASSWORD, passwordCard.getPassword());
//        values.put(PasswordTable.Cols.NOTE, passwordCard.getNote());
//        values.put(PasswordTable.Cols.DATE, passwordCard.getDate());

        values.put(PasswordTable.Cols.UUID, passwordCard.getId().toString());
        values.put(PasswordTable.Cols.RESOURCE_NAME, Cipher.encrypt(keyCode, passwordCard.getResourceName()));
        values.put(PasswordTable.Cols.LOGIN, Cipher.encrypt(keyCode, passwordCard.getLogin()));
        values.put(PasswordTable.Cols.PASSWORD, Cipher.encrypt(keyCode, passwordCard.getPassword()));
        values.put(PasswordTable.Cols.NOTE, Cipher.encrypt(keyCode, passwordCard.getNote()));
        values.put(PasswordTable.Cols.DATE, Cipher.encrypt(keyCode, passwordCard.getDate()));
        return values;
    }

    public void addPasswordCard(PasswordCard passwordCard) {
        ContentValues values = getContentValues(passwordCard);
        if(getPasswordCard(passwordCard.getId())==null) {
            sqLiteDatabase.insert(PasswordTable.NAME, null, values);
        }
    }

    public void updatePasswordCard(PasswordCard passwordCard) {

        String uuidString = passwordCard.getId().toString();
        ContentValues values = getContentValues(passwordCard);

        sqLiteDatabase.update(PasswordTable.NAME, values, PasswordTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void reloadAllCardsWithNewPassword(String newPassword){
        PasswordCard passwordCard;

        List oldCards =  getPasswords();
keyCode = newPassword;
        for (int i = 0 ; i < oldCards.size() ; i++){
            passwordCard = (PasswordCard)oldCards.get(i);
            updatePasswordCard(passwordCard);

        }
    }

    private PasswordCursorWrapper queryPasswords(String whereClause, String[] whereArgs) {

        Cursor cursor = sqLiteDatabase.query(
                PasswordTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PasswordCursorWrapper(cursor);

    }

    private PasswordCursorWrapper externalQueryPasswords(String whereClause, String[] whereArgs) {

        Cursor cursor = externalDatabase.query(
                PasswordTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PasswordCursorWrapper(cursor);

    }


    public boolean backUp() {
        try {
            File data = Environment.getDataDirectory();
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                String currentDBPath = "/data/com.example.dontforgetyourpassword/databases/password_base";
                File currentDB = new File(data, currentDBPath);


                File on = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "Password keeper");
                on.mkdirs();
                File backupDBFile = new File(on, "/keeper_backup.db");

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDBFile).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                }
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.i(PasswordLab.GLOBAL_TAG, "sdcard mounted readonly");
            } else {
                Log.i(PasswordLab.GLOBAL_TAG, "sdcard state: " + state);
            }
            return true;
        } catch (Exception e) {
            Log.i(PasswordLab.GLOBAL_TAG, e.getMessage());
        }
        return false;
    }

}
