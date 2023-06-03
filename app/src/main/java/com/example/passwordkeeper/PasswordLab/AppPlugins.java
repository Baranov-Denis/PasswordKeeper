package com.example.passwordkeeper.PasswordLab;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AppPlugins {

    public static void hideKeyboard(Context context, View view){
        // Получаем ссылку на InputMethodManager из системного сервиса
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Проверяем, активна ли виртуальная клавиатура
        if (imm.isActive()) {
            // Скрываем клавиатуру для текущего фокуса
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
