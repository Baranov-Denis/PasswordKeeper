package com.example.passwordkeeper.Fragments;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwordkeeper.MainActivity;
import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class AppFragmentManager {



    public static void openFragment(Fragment fragment){

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in_500, R.anim.fade_out_500, R.anim.fade_in_500, R.anim.fade_out_500);
        // добавление нового фрагмента в транзакцию
        transaction.replace(R.id.main_container_for_all_fragments, fragment);


        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        // Завершение транзакции
        transaction.commit();
    }

    public static void addFragment(Fragment fragment){

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in_500, R.anim.fade_out_500, R.anim.fade_in_500, R.anim.fade_out_500);

        // добавление нового фрагмента в транзакцию
        transaction.add(R.id.main_container_for_all_fragments, fragment);


        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
       // transaction.addToBackStack(null);

        // Завершение транзакции
        transaction.commit();
    }


    public static void setUpTargetForBackPressed(Fragment currentFragment, Fragment targetFragment) {
        currentFragment.requireActivity().getOnBackPressedDispatcher().addCallback(currentFragment.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openFragment(targetFragment);
            }
        });
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


    public static void setAddButton(View view, Activity activity) {
        FloatingActionButton addFloatingActionButton = view.findViewById(R.id.fab_add_new_password);
        addFloatingActionButton.setOnClickListener(o -> {
            PasswordCard card = new PasswordCard();
            PasswordLab.getLab(activity).addPasswordCard(card);
            AppFragmentManager.openFragment(PasswordCardFragment.newInstance(card.getId()));
        });
    }

    public static void setLeaveButton(View view ) {
        FloatingActionButton leaveFloatingActionButton = view.findViewById(R.id.fab_leave_button);
        leaveFloatingActionButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new LoginFragment());
        });
    }


}
