package com.example.passwordkeeper.PasswordLab;

import android.content.Context;

import com.example.passwordkeeper.DropBoxHelper.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PasswordGenerator {
    //Список гласных
    private static ArrayList<Character> vowelCharactersList;
    //Список согласных
    private static ArrayList<Character> consonantList;
    //Список символов
    private static ArrayList<Character> symbolsList;
    //Длина пароля
    private static int passwordLength = 20;




    private static boolean vowelCharacter;

    public static boolean isVowelCharacter() {
        return vowelCharacter;
    }

    public static void setVowelCharacter(boolean vowelCharacter) {
        PasswordGenerator.vowelCharacter = vowelCharacter;
    }

    public static String generate(Context context) {
        if(SharedPreferencesHelper.getData(context).getPasswordLength()!=null) {
            passwordLength = Integer.parseInt(SharedPreferencesHelper.getData(context).getPasswordLength());
        }else passwordLength = 1;
        listInitiation();
        StringBuilder password = new StringBuilder();

        char tempChar;

        for (int i = 0; i < passwordLength; i++) {

            if (isVowelCharacter()) {
                tempChar = getRandomConsonantOrSymbolCharFromArray();
            }else {
                tempChar = getRandomVowelOrSymbolCharFromArray();
            }

            password.append(tempChar);
        }
        return password.toString();
    }


    private static char getRandomVowelOrSymbolCharFromArray() {
            return setCharBig(getRandomVowelCharFromArray());
    }

    private static char getRandomConsonantOrSymbolCharFromArray() {
        Random random = new Random();
        //Чем больше bound тем реже используются символы
        if(random.nextInt(2)==0) {
            return getRandomSymbolCharFromArray();
        }else {
            return setCharBig(getRandomConsonantCharFromArray());
        }
    }

    private static char getRandomVowelCharFromArray() {
        Random random = new Random();
        setVowelCharacter(true);
        return setCharBig(vowelCharactersList.get(random.nextInt(vowelCharactersList.size())));
    }

    private static char getRandomConsonantCharFromArray() {
        Random random = new Random();
        setVowelCharacter(false);
        return setCharBig(consonantList.get(random.nextInt(consonantList.size())));
    }

    private static char getRandomSymbolCharFromArray() {
        Random random = new Random();
        setVowelCharacter(false);
        return symbolsList.get(random.nextInt(symbolsList.size()));
    }

    private static char setCharBig(Character ch) {
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            return ch.toString().toUpperCase().charAt(0);
        } else return ch;
    }


    private static void listInitiation() {
        vowelCharactersList = new ArrayList<>();
        vowelCharactersList.addAll(Arrays.asList('e', 'u', 'i', 'o', 'a', 'e', 'u', 'i', 'o', 'a', 'e', 'u', 'i', 'o', 'a','e', 'u', 'i', 'o', 'a', 'e', 'u', 'i', 'o', 'a', 'e', 'u', 'i', 'o', 'a', 'y', 'q'));
        consonantList = new ArrayList<>();
        consonantList.addAll(Arrays.asList('w', 'r', 't', 'p', 's', 'd', 'f', 'g', 'h', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm','w', 'r', 't', 'p', 's', 'd', 'f', 'g', 'h', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'j'));
        symbolsList = new ArrayList<>();
        symbolsList.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '?', ':', ';', '-', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '=', '+', '.', '/'));
    }


}
