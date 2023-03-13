package com.example.passwordkeeper.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwordkeeper.MainActivity;
import com.example.passwordkeeper.R;

public class LoginFragment extends Fragment {

    private View view;
    private AppCompatButton enterButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        createEnterButton();
        AppFragmentManager.closeApp(this);
        return view;
    }

    private void createEnterButton() {
        enterButton = view.findViewById(R.id.enter_button);
        enterButton.setOnClickListener(o ->{
           AppFragmentManager.openFragment(new PasswordsListFragment());

        });
    }



}