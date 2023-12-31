package com.example.passwordkeeper.PasswordLab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.passwordkeeper.Fragments.AppFragmentManager;
import com.example.passwordkeeper.Fragments.LoginFragment;
import com.example.passwordkeeper.Fragments.PasswordsListFragment;

import java.util.Timer;
import java.util.TimerTask;

public class LeaveTimer {

    public static Timer timer;
    public static int timeBeforeLogOut = 50000; //300000 = 5 min

    public static void runLeaveTimer() {
        chancelTimer();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Redirect user to login screen
                leaveToLoginActivity();
            }
        },  timeBeforeLogOut);
    }



    public static void chancelTimer(){
        if (LeaveTimer.timer != null) {
            LeaveTimer.timer.cancel();
            LeaveTimer.timer = null;
        }
    }

    public static void resetTimer(){
        if (LeaveTimer.timer != null) {
            runLeaveTimer();
        }
    }

    public static void leaveToLoginActivity(){
        AppFragmentManager.openFragment(new LoginFragment());
    }

}
