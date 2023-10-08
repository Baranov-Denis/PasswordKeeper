package com.example.passwordkeeper.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwordkeeper.MainActivity;
import com.example.passwordkeeper.PasswordLab.AppPlugins;
import com.example.passwordkeeper.PasswordLab.LeaveTimer;
import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginFragment extends Fragment {

    private View view;
    private AppCompatButton enterButton;
    private AppCompatEditText passwordEditText;
    private FloatingActionButton helpFab;

    private PasswordLab passwordLab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        passwordLab = PasswordLab.getLab(getContext());
        createEnterButton();
        AppFragmentManager.closeApp(this);
        setHelpFabButton();
        LeaveTimer.chancelTimer();
        return view;
    }

    private void setPasswordGeneratorLength(){

    }

    private void createEnterButton() {
        enterButton = view.findViewById(R.id.enter_button);
        enterButton.setOnClickListener(o -> {
            passwordEditText = view.findViewById(R.id.enter_key_edit_text_fk);
            String password = passwordEditText.getText().toString();
            if(!password.equals("")) {
                PasswordLab.setKeyCode(password);

            }else {
                PasswordLab.setKeyCode("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
            AppPlugins.hideKeyboard(requireContext(),view);

            AppFragmentManager.openFragment(new PasswordsListFragment());

        });
    }


    public void setHelpFabButton() {
        helpFab = view.findViewById(R.id.fab_help_button);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpFab.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_anim_for_fab);
                helpFab.startAnimation(rotateAnimation);
            }
        }, MainActivity.animationDelay);

        helpFab.setOnClickListener(o -> {
            Handler mHandler = new Handler();
            AppFragmentManager.hideFloatButton(helpFab, view);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Здесь запускаем переход на другой фрагмент
                    Toast.makeText(getContext(), "This is will help you!!!", Toast.LENGTH_SHORT).show();
                    helpFab.hide();
                }
            }, MainActivity.animationDelay);
        });
    }


}