package com.example.passwordkeeper.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.passwordkeeper.PasswordLab.AppPlugins;
import com.example.passwordkeeper.PasswordLab.LeaveTimer;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private View view;
    private AppCompatButton enterButton;
    private AppCompatEditText passwordEditText;
   // private PasswordLab passwordLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        AnimationHelper.appearFade(requireActivity(),view,0);
        //passwordLab = PasswordLab.getLab(getContext());
        createEnterButton();
        AppFragmentManager.closeApp(this);
        PasswordLab.resetMainPasswordsBaseArrayList();
        return view;
    }

    private void createEnterButton() {
        enterButton = view.findViewById(R.id.enter_button);
        enterButton.setOnClickListener(o -> {
            passwordEditText = view.findViewById(R.id.enter_key_edit_text_fk);
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();
            if(!password.equals("")) {
                PasswordLab.setKeyCode(password);
            }else {
                PasswordLab.setKeyCode("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
            AppPlugins.hideKeyboard(requireContext(),view);
            AppFragmentManager.openFragment(new PasswordsListFragment());
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        AnimationHelper.hideFade(requireActivity(),view);
    }

}