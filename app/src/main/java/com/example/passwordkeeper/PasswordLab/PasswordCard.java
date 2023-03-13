package com.example.passwordkeeper.PasswordLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PasswordCard implements Comparable<PasswordCard> {

    private UUID id;
    private String resourceName;
    private String login;
    private String password;
    private String note;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UUID getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PasswordCard(){
        this(UUID.randomUUID());
    }

    public PasswordCard(UUID id){
        this.id = id;
        date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
    }

    @Override
    public int compareTo(PasswordCard o) {
        return this.getResourceName().toLowerCase(Locale.ROOT).compareTo(o.getResourceName().toLowerCase());
    }
}
