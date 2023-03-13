package com.example.passwordkeeper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.database.PasswordDbSchema.PasswordTable;

import java.util.UUID;


import com.example.passwordkeeper.cipher.Cipher;


public class PasswordCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PasswordCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public PasswordCard getPasswordCard() {
        PasswordCard passwordCard = null;

        String uuidString = getString(getColumnIndex(PasswordTable.Cols.UUID));
        String resourceNameString = getString(getColumnIndex(PasswordTable.Cols.RESOURCE_NAME));
        String loginString = getString(getColumnIndex(PasswordTable.Cols.LOGIN));
        String passwordString = getString(getColumnIndex(PasswordTable.Cols.PASSWORD));
        String noteString = getString(getColumnIndex(PasswordTable.Cols.NOTE));
        String dateString = getString(getColumnIndex(PasswordTable.Cols.DATE));
        try {
            passwordCard = new PasswordCard(UUID.fromString(uuidString));
//        passwordCard.setResourceName(resourceNameString);
//        passwordCard.setLogin(loginString);
//        passwordCard.setPassword(passwordString);
//        passwordCard.setNote(noteString);
//        passwordCard.setDate(dateString);

            passwordCard.setResourceName(Cipher.decrypt(PasswordLab.getKeyCode(), resourceNameString));
            passwordCard.setLogin(Cipher.decrypt(PasswordLab.getKeyCode(), loginString));
            passwordCard.setPassword(Cipher.decrypt(PasswordLab.getKeyCode(), passwordString));
            passwordCard.setNote(Cipher.decrypt(PasswordLab.getKeyCode(), noteString));
            passwordCard.setDate(Cipher.decrypt(PasswordLab.getKeyCode(), dateString));
        } catch (Exception e) {

        }
        return passwordCard;
    }
}
