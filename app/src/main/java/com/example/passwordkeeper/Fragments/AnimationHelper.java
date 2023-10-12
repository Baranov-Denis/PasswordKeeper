package com.example.passwordkeeper.Fragments;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.passwordkeeper.R;

public class AnimationHelper {
    public static void hideToLeft(Activity activity, View view){
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.hide_to_left));
    }
    public static void hideToRight(Activity activity, View view){
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.hide_to_right));
    }

    public static void appearFromLeft(Activity activity, View view, Integer delay){
        appear(activity,view,delay,R.anim.appears_from_left);
    }
    public static void appearFromRight(Activity activity, View view, Integer delay){
        appear(activity,view,delay,R.anim.appears_from_right);
    }

    private static void appear(Activity activity, View view, Integer delay, int animation){
        view.setVisibility(View.INVISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.startAnimation(AnimationUtils.loadAnimation(activity,animation));
                view.setVisibility(View.VISIBLE);
            }
        }, delay);
    }

}
