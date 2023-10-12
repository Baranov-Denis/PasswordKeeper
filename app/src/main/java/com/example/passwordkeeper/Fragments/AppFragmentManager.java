package com.example.passwordkeeper.Fragments;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwordkeeper.MainActivity;
import com.example.passwordkeeper.PasswordLab.LeaveTimer;
import com.example.passwordkeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AppFragmentManager {


    public static void openFragment(Fragment fragment) {

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

      //  transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        // добавление нового фрагмента в транзакцию
        transaction.replace(R.id.main_container_for_all_fragments, fragment);

        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        LeaveTimer.runLeaveTimer();
        // Завершение транзакции
        transaction.commit();
    }



    public static void addFragment(Fragment fragment) {

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

     //   transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        // добавление нового фрагмента в транзакцию
        transaction.add(R.id.main_container_for_all_fragments, fragment);

        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        LeaveTimer.runLeaveTimer();
        // Завершение транзакции
        transaction.commit();
    }



    public static void closeApp(Fragment currentFragment) {
        currentFragment.requireActivity().getOnBackPressedDispatcher().addCallback(currentFragment.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openFragment(new LoginFragment());
                currentFragment.requireActivity().finish();
            }
        });
    }


   /* public static void setAddButton(View view, Activity activity) {

        FloatingActionButton addFloatingActionButton = view.findViewById(R.id.fab_add_new_password);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addFloatingActionButton.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_for_fab);
                addFloatingActionButton.startAnimation(rotateAnimation);
            }
        }, 1000);


        addFloatingActionButton.setOnClickListener(o -> {
            PasswordCard card = new PasswordCard();
            PasswordLab.getLab(activity).addPasswordCard(card);
            AppFragmentManager.openFragment(PasswordCardFragment.newInstance(card.getId()));

            Handler mHandler = new Handler();
            hideFloatButton(view,activity);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Здесь запускаем переход на другой фрагмент

                    AppFragmentManager.openFragment(PasswordCardFragment.newInstance(card.getId()));
                }
            }, 1000);
        });
    }*/

    public static void hideFloatButton(FloatingActionButton fab, View view) {

        int animDurationDelay = view.getResources().getInteger(R.integer.animation_duration);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.hide_anim_for_fab);
                fab.startAnimation(rotateAnimation);
            }

        }, animDurationDelay + 10);

    }


/*
    public static void setLeaveButton(View view) {
        FloatingActionButton leaveFloatingActionButton = view.findViewById(R.id.fab_leave_button);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                leaveFloatingActionButton.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_anim_for_fab);
                leaveFloatingActionButton.startAnimation(rotateAnimation);
            }
        }, 1000);


        leaveFloatingActionButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new LoginFragment());
        });
    }

*/
}
