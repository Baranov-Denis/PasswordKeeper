package com.example.passwordkeeper.PasswordLab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.passwordkeeper.Fragments.AppFragmentManager;
import com.example.passwordkeeper.Fragments.LoginFragment;
import com.example.passwordkeeper.Fragments.PasswordsListFragment;

import java.util.Timer;
import java.util.TimerTask;

public class LeaveTimer {

    public static Timer timer;

    public static long timeDefault = 60000;//60000 = 1min
    public static long timeBeforeLogOut = timeDefault;

    public static void runLeaveTimer(long factor) {
        //timeBeforeLogOut = timeDefault * factor;
        //timeDefault = timeDefault * factor;

        timeDefault = factor;
        chancelTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Redirect user to login screen
                leaveToLoginActivity();
            }
        },  timeDefault);
        Log.i("timer   ", timeDefault + "");
    }



    public static void chancelTimer(){
        if (LeaveTimer.timer != null) {
            LeaveTimer.timer.cancel();
            LeaveTimer.timer = null;
        }
    }

    public static void resetTimer(){
        if (LeaveTimer.timer != null) {
            runLeaveTimer(timeDefault);
        }
    }

    public static void leaveToLoginActivity(){
        AppFragmentManager.openFragment(new LoginFragment());
    }

}
