package com.example.passwordkeeper.PasswordLab;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.passwordkeeper.DropBoxHelper.DropBoxHelper;
import com.example.passwordkeeper.R;
import com.example.passwordkeeper.cipher.Cipher;
import com.example.passwordkeeper.database.ExternalBaseHelper;
import com.example.passwordkeeper.database.PasswordBaseHelper;
import com.example.passwordkeeper.database.PasswordCursorWrapper;
import com.example.passwordkeeper.database.PasswordDbSchema.PasswordTable;

public class PasswordLab {

    public static final String GLOBAL_TAG = "------>>>>";


    private static PasswordLab passwordLab;
    public String testPassword =".fhrjduJGH950j_(@*#&";
    private static String keyCode = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase externalDatabase;
    private int cardPosition;
    private DropBoxHelper dropBoxHelper;

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
        dropBoxHelper = DropBoxHelper.getDropboxHelper(context);
        sqLiteDatabase = new PasswordBaseHelper(context).getWritableDatabase();
        externalDatabase = null;
    }


    public void setCardPosition(int cardPosition){
        this.cardPosition = cardPosition;
    }

    public int getCardPosition(){
        return cardPosition;
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

        if(passwordCards.size()>1) {
            Collections.sort(passwordCards);
        }


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


    public void deletePasswordById(UUID id) {
        sqLiteDatabase.delete(PasswordTable.NAME, PasswordTable.Cols.UUID + "= ?", new String[]{id.toString()});
    }


    public boolean passwordIsWrong() {
        List<PasswordCard> passwords = getPasswords();
        if(passwords.size() != 0) {
            PasswordCard pasCard = passwords.get(0);
            String regex = "\\d{2}:\\d{2}:\\d{2} \\d{2}-\\d{2}-\\d{4}";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pasCard.getDate());
            Log.i("00000", "==== " + pasCard.getDate());
            if (matcher.matches()) return false;
            else return true;
        }else {
            return false;
        }
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

                Log.i(PasswordLab.GLOBAL_TAG, "EEEEEEEEEEEE " + currentDB.exists() );
                File on = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "Password keeper");
                on.mkdirs();
                //Имя файла
               // LocalDate currentDate = LocalDate.now();
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
              //  String formattedDate = currentDate.format(formatter);
                //StringBuilder outputFileName = new StringBuilder("/keeper_backup_");
              //  outputFileName.append(formattedDate);
            //    outputFileName.append(".db");

                 File backupDBFile = new File(on, "/keeper_backup.db");
               // File backupDBFile = new File(on, outputFileName.toString());

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

    public void saveDataBaseToDropbox(Context context, Activity activity){
        if (backUp()) {
            Toast.makeText(context, activity.getResources().getString(R.string.Backup_is_successful_to_phone_storage), Toast.LENGTH_LONG).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        // Ваш код, который требует интернет-соединения
                        // Например, попытка выполнить операции с Dropbox
                        if (dropBoxHelper.uploadDatabaseToDropbox()) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, activity.getResources().getString(R.string.File_successfully_added_to_dropbox), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, activity.getResources().getString(R.string.Something_went_wrong_file_does_not_saved), Toast.LENGTH_LONG).show();
                            }
                        });
                        // Обработка ошибки отсутствия интернет-соединения
                        // Здесь можно выполнить действия, чтобы сообщить пользователю о проблеме
                        // Например, показать диалоговое окно с предупреждением
                        // или отобразить сообщение об ошибке в интерфейсе пользователя
                        e.printStackTrace(); // Это позволяет записать информацию об ошибке в логи для отладки
                    }

                }
            }).start();
        }else {
            Toast.makeText(context, "activity.getResources().getString(R.string.Backup_is_failed)", Toast.LENGTH_LONG).show();
        }
    }

}
