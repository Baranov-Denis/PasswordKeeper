package com.example.passwordkeeper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HotKeyButtonsFragment extends Fragment {

private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hot_key_buttons, container, false);
        setAddButton();
        return view;
    }

    private void setAddButton(){
        FloatingActionButton addFloatingActionButton = view.findViewById(R.id.fab_add_new_password);
        addFloatingActionButton.setOnClickListener(o->{
            PasswordCard card = new PasswordCard();
            PasswordLab.getLab(getActivity()).addPasswordCard(card);

AppFragmentManager.openFragment(PasswordCardFragment.newInstance(card.getId()));
        });
    }
}