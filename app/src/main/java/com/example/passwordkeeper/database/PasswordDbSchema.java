package com.example.passwordkeeper.database;

public class PasswordDbSchema {

    public static final class PasswordTable{

        public static final String NAME = "passwords";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String RESOURCE_NAME = "resource_name";
            public static final String LOGIN = "login";
            public static final String PASSWORD = "password";
            public static final String NOTE = "note";
            public static final String DATE = "date";
        }

    }


}
