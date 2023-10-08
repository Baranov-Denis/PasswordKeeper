package com.example.passwordkeeper.DropBoxHelper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class SharedPreferencesHelper {

    // Имя файла SharedPreferences
    private static final String PREFS_NAME = "MyPrefsFile";
    // Ключи для сохранения и получения данных
    private static final String DROPBOX_TOKEN = "dropbox_token";
    private static final String PASSWORD_LENGTH = "password_length";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String EXPIRES_TIME = "expires_time";

    // Метод для сохранения данных
    public static void saveData(Context context, String dropbox_token, String money_target) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(DROPBOX_TOKEN, dropbox_token);
        editor.putString(PASSWORD_LENGTH, money_target);
        editor.apply();
    }

    public static void saveToken(Context context, String dropbox_token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(DROPBOX_TOKEN, dropbox_token);
        editor.apply();
    }

    public static void saveRefreshToken(Context context, String refresh_token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(REFRESH_TOKEN, refresh_token);
        editor.apply();
    }

    public static void saveExpiresTime(Context context, String expires_time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(EXPIRES_TIME, expires_time);
        editor.apply();
    }

    public static void savePasswordLength(Context context, String money_target) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PASSWORD_LENGTH, money_target);
        editor.apply();
    }

    // Метод для получения данных
    public static UserData getData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String dropboxToken = prefs.getString(DROPBOX_TOKEN, "");
        String moneyTarget = prefs.getString(PASSWORD_LENGTH, "");
        String refreshToken = prefs.getString(REFRESH_TOKEN,"");
        String expiresTime = prefs.getString(EXPIRES_TIME,"");
        return new UserData(dropboxToken, moneyTarget,refreshToken,expiresTime);
    }

    // Класс для хранения данных
    public static class UserData {
        private String dropboxToken;
        private String passwordLength;
        private String refreshToken;
        private String expiresTime;

        public UserData(String dropboxToken, String passwordLength, String refreshToken, String expiresTime) {
            this.dropboxToken = dropboxToken;
            this.passwordLength = passwordLength;
            this.refreshToken = refreshToken;
            this.expiresTime = expiresTime;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
        public String getExpiresTime() {
            return expiresTime;
        }
        public String getDropboxToken() {
            return dropboxToken;
        }
        public String getPasswordLength() {
            if(Objects.equals(passwordLength, "")) passwordLength = "1";
            return passwordLength;
        }
    }

}
