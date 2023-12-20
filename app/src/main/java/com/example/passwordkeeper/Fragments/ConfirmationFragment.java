package com.example.passwordkeeper.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;


public class ConfirmationFragment extends Fragment {
    private View view;
    private AppCompatButton noButton;
    private AppCompatButton yesButton;
    private PasswordCard passwordCard;
    private PasswordLab passwordLab;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        passwordLab = PasswordLab.getLab(getContext());
        setButtons();
        AnimationHelper.appearFade(requireActivity(),view,0);
        return view;
    }

    private void setButtons() {
        noButton = view.findViewById(R.id.chancel_discard_password_card_button);
        noButton.setOnClickListener(c ->{
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        yesButton = view.findViewById(R.id.agree_discard_password_card_button);



        yesButton.setOnClickListener(c ->{
            AppFragmentManager.openFragment(new PasswordsListFragment());
        });




    }
}