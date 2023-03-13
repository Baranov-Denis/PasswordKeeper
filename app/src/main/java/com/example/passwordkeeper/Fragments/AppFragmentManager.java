package com.example.passwordkeeper.Fragments;

import android.os.Handler;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwordkeeper.MainActivity;
import com.example.passwordkeeper.R;

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


}
