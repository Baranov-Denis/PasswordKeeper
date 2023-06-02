package com.example.passwordkeeper.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;


public class ChangePasswordFragment extends Fragment {

    private View view;

    private EditText newPassword;
    private EditText confirmNewPassword;
    private AppCompatButton chancelButton;
    private AppCompatButton agreeButton;
    private PasswordLab passwordLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        passwordLab = PasswordLab.getLab(getContext());
        setButtons();
        return view;
    }





    private void setButtons() {

        newPassword = view.findViewById(R.id.new_password_text_view);
        confirmNewPassword = view.findViewById(R.id.confirm_new_password_text_view);
        chancelButton = view.findViewById(R.id.chancel_change_password);
        agreeButton = view.findViewById(R.id.agree_change_button);


        agreeButton.setOnClickListener(a -> {
            String newPasswordString = newPassword.getText().toString();
            String confirmNewPasswordString = confirmNewPassword.getText().toString();
            if(newPasswordString.equals(confirmNewPasswordString)&&newPasswordString.length()>3){
                passwordLab.reloadAllCardsWithNewPassword(newPasswordString);
                changeTestPhrase();
               AppFragmentManager.openFragment(new PasswordsListFragment());
            }

        });

        chancelButton.setOnClickListener(e->{
            AppFragmentManager.openFragment(new PasswordsListFragment());
        });
    }

    private void changeTestPhrase(){
        PasswordCard serviceCard = new PasswordCard();
        serviceCard.setResourceName(passwordLab.TEST_PHRASE);
        serviceCard.setLogin(passwordLab.TEST_PHRASE);
        serviceCard.setPassword("----");
        serviceCard.setNote("note");
        passwordLab.addServiceLine(serviceCard);
    }
}