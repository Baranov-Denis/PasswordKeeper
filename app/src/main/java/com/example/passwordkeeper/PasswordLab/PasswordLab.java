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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public final String TEST_PHRASE = "this_is_a_phrase_for_testing_password_!@#$%^&*()";

    private static PasswordLab passwordLab;
    private static String keyCode = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
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

    public void loadExternalPasswordsList(Context context, String databasePath) {
        List<PasswordCard> passwordCards = new ArrayList<>();
        ExternalBaseHelper.setExternalDatabaseName(databasePath);
        externalDatabase = new ExternalBaseHelper(context).getExternalDatabase();
        passwordCards = getPasswords();
        for (PasswordCard passwordCard : passwordCards) {
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
        } else {
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

    public PasswordCard getPasswordCardByUUID(UUID uuid) {

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

    public PasswordCard getPasswordCardForTestingPassword() {

        try (PasswordCursorWrapper cursor = queryPasswords(
                PasswordTable.Cols.RESOURCE_NAME + " = ?",
                new String[]{passwordLab.TEST_PHRASE}
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


    public boolean passwordIsWrong() {
       /* StringBuilder nameString = new StringBuilder();
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
        return false;*/
        PasswordCard pasCard = getPasswordCardForTestingPassword();
        if(pasCard.getLogin().equals(passwordLab.TEST_PHRASE)) {
            return false;
        } else{ return true;}
    }

    public  boolean passwordIsWrongTestPasswordString() {
        PasswordCard passwordCard = passwordLab.getPasswords().get(0);
        return passwordIsWrong();
    }


    private static ContentValues getContentValues(PasswordCard passwordCard) {
        ContentValues values = new ContentValues();

        values.put(PasswordTable.Cols.UUID, passwordCard.getId().toString());
        values.put(PasswordTable.Cols.RESOURCE_NAME, Cipher.encrypt(keyCode, passwordCard.getResourceName()));
        values.put(PasswordTable.Cols.LOGIN, Cipher.encrypt(keyCode, passwordCard.getLogin()));
        values.put(PasswordTable.Cols.PASSWORD, Cipher.encrypt(keyCode, passwordCard.getPassword()));
        values.put(PasswordTable.Cols.NOTE, Cipher.encrypt(keyCode, passwordCard.getNote()));
        values.put(PasswordTable.Cols.DATE, Cipher.encrypt(keyCode, passwordCard.getDate()));
        return values;
    }

    private static ContentValues getContentValuesForServiceLine(PasswordCard passwordCard) {
        ContentValues values = new ContentValues();

        values.put(PasswordTable.Cols.UUID, passwordCard.getId().toString());
        //RESOURCE_NAME не закодирован для кодового слова
        values.put(PasswordTable.Cols.RESOURCE_NAME, passwordCard.getResourceName());
        //LOGIN закодирован для проверки пароля
        values.put(PasswordTable.Cols.LOGIN, Cipher.encrypt(keyCode, passwordCard.getLogin()));
        values.put(PasswordTable.Cols.PASSWORD, Cipher.encrypt(keyCode, passwordCard.getPassword()));
        //NOTE не закодирован подсказка
        values.put(PasswordTable.Cols.NOTE, passwordCard.getNote());
        values.put(PasswordTable.Cols.DATE, Cipher.encrypt(keyCode, passwordCard.getDate()));
        return values;
    }

    public void addPasswordCard(PasswordCard passwordCard) {
        ContentValues values = getContentValues(passwordCard);
        if (getPasswordCardByUUID(passwordCard.getId()) == null) {
            sqLiteDatabase.insert(PasswordTable.NAME, null, values);
        }
    }

    public void updatePasswordCard(PasswordCard passwordCard) {

        String uuidString = passwordCard.getId().toString();
        ContentValues values = getContentValues(passwordCard);

        sqLiteDatabase.update(PasswordTable.NAME, values, PasswordTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void addServiceLine(PasswordCard passwordCard) {
        ContentValues values = getContentValuesForServiceLine(passwordCard);
        if (getPasswordCardByUUID(passwordCard.getId()) == null) {
            sqLiteDatabase.insert(PasswordTable.NAME, null, values);
        }
    }

    public void updateServiceLine(PasswordCard passwordCard) {
        String uuidString = passwordCard.getId().toString();
        ContentValues values = getContentValuesForServiceLine(passwordCard);
        sqLiteDatabase.update(PasswordTable.NAME, values, PasswordTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void reloadAllCardsWithNewPassword(String newPassword) {
        PasswordCard passwordCard;

        List<PasswordCard> oldCards = getPasswords();
        keyCode = newPassword;
        for (int i = 0; i < oldCards.size(); i++) {
            passwordCard = (PasswordCard) oldCards.get(i);
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

                String currentDBPath = "/data/com.example.passwordkeeper/databases/password_base";

                File currentDB = new File(data, currentDBPath);


                File on = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "Password keeper");
                on.mkdirs();
                //Имя файла
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                String formattedDate = currentDate.format(formatter);
                StringBuilder outputFileName = new StringBuilder("/keeper_backup_");
                outputFileName.append(formattedDate);
                outputFileName.append(".db");
                // File backupDBFile = new File(on, "/keeper_backup.db");
                File backupDBFile = new File(on, outputFileName.toString());

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
