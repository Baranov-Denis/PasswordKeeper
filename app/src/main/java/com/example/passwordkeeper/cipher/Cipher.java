package com.example.passwordkeeper.cipher;

import com.example.passwordkeeper.PasswordLab.PasswordCard;

import java.util.ArrayList;
import java.util.List;

public class Cipher {

  /*  public static PasswordCard encryptCard(String key, PasswordCard passwordCard){
        passwordCard.setResourceName(encrypt(key , passwordCard.getResourceName()));
        passwordCard.setLogin(encrypt(key , passwordCard.getLogin()));
        passwordCard.setPassword(encrypt(key , passwordCard.getPassword()));
        passwordCard.setNote(encrypt(key , passwordCard.getNote()));
        passwordCard.setDate(encrypt(key , passwordCard.getDate()));
        return passwordCard;
    }

    public static PasswordCard decryptCard(String key, PasswordCard passwordCard){
        passwordCard.setResourceName(decrypt(key , passwordCard.getResourceName()));
        passwordCard.setLogin(decrypt(key , passwordCard.getLogin()));
        passwordCard.setPassword(decrypt(key , passwordCard.getPassword()));
        passwordCard.setNote(decrypt(key , passwordCard.getNote()));
        passwordCard.setDate(decrypt(key , passwordCard.getDate()));
        return passwordCard;
    }*/


    //Шифровка строки
    public static String encrypt(String key, String inputString) {
        if (inputString != null) {
            List<Integer> keyList = setCharList(key, inputString);
            List<Integer> stringList = getListFromString(inputString);
            List<Integer> encryptedList = coder(keyList, stringList);
            return listToString(encryptedList);
        } else return inputString;
    }


    public static PasswordCard encryptCard(String key, PasswordCard passwordCard) {
        PasswordCard newPasswordCard = new PasswordCard(passwordCard.getId());
       // PasswordCard newPasswordCard = new PasswordCard(UUID.randomUUID());

        String resourceName = passwordCard.getResourceName();
        String login = passwordCard.getLogin();
        String password = passwordCard.getPassword();
        String note = passwordCard.getNote();
        String date = passwordCard.getDate();



        newPasswordCard.setResourceName(decrypt(key, resourceName));
        newPasswordCard.setLogin(decrypt(key, login));
        newPasswordCard.setPassword(decrypt(key, password));
        newPasswordCard.setNote(decrypt(key, note));
        newPasswordCard.setDate(decrypt(key, date));

        return newPasswordCard;
    }

//Расшифровка строки
    public static String decrypt(String key, String inputString) {
        if (inputString != null) {
            List<Integer> keyList = setCharList(key, inputString);
            List<Integer> stringList = getListFromString(inputString);
            List<Integer> encryptedList = encoder(keyList, stringList);
            return listToString(encryptedList);
        } else return inputString;
    }


    private static List<Integer> getListFromString(String string) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            list.add((int) string.charAt(i));
        }
        return list;
    }

    private static String listToString(List<Integer> list) {
        StringBuilder outputString = new StringBuilder();
        for (Integer integer : list) {
            outputString.append((char) (int) integer);
        }
        return outputString.toString();
    }

    private static List<Integer> setCharList(String key, String inputString) {
        List<Integer> charList = new ArrayList<>();
        // int keyLength= Math.max(10, inputString.length());
        int keyLength = 50;
        if (key.length() <= keyLength) {
            while (charList.size() < keyLength) {
                charList = addCharToString(charList, key);
            }
        } else {
            charList = addCharToString(charList, key);
        }

        charList = remixList(charList);

        return charList;
    }

    private static List<Integer> addCharToString(List<Integer> list, String string) {
        int sum = 0;
        for (int i = 0; i < string.length(); i++) {
            sum += string.charAt(i);
        }
        for (int i = 0; i < string.length(); i++) {
            list.add((int) string.charAt(i) + sum);
        }
        return list;
    }

    private static List<Integer> remixList(List<Integer> list) {
        List<Integer> outputList = new ArrayList<>();
        int b = list.size() - 1;
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) {
                outputList.add(list.get(i) + list.get(b) + list.size());
            } else {
                outputList.add(list.get(i) + list.get(b) - list.size());
            }
            b--;
        }
        return outputList;
    }


    private static List<Integer> coder(List<Integer> keyList, List<Integer> stringList) {
        List<Integer> outputList = new ArrayList<>();

        int keyCounter = -1;

        int stringCounter = -1;

        int sum = 0;
        for (Integer integer : stringList) sum += integer;


        for (int a = 0; a < stringList.size(); a++) {

            if (stringCounter < stringList.size() - 1) {
                stringCounter++;
            } else {
                stringCounter = 0;
            }

            if (keyCounter < keyList.size() - 1) {
                keyCounter++;
            } else {
                keyCounter = 0;
            }


            outputList.add(stringList.get(stringCounter) + keyList.get(keyCounter) + sum);


        }
        outputList.add(sum + 33);
        outputList.add(stringList.size() + 33);

        return outputList;
    }

    private static List<Integer> encoder(List<Integer> keyList, List<Integer> stringList) {
        List<Integer> outputList = new ArrayList<>();

        int keyCounter = -1;

        int stringCounter = -1;

        int sum = stringList.get(stringList.size() - 2) - 33;


        for (int a = 0; a < stringList.get(stringList.size() - 1) - 33; a++) {

            if (stringCounter < stringList.size() - 3) {
                stringCounter++;
            } else {
                stringCounter = 0;
            }

            if (keyCounter < keyList.size() - 1) {
                keyCounter++;
            } else {
                keyCounter = 0;
            }

            outputList.add(stringList.get(stringCounter) - keyList.get(keyCounter) - sum);


        }

        return outputList;
    }
}
