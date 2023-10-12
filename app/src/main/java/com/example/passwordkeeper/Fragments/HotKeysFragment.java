package com.example.passwordkeeper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;


public class HotKeysFragment extends Fragment {

    private View view;
    private ImageButton settingsIB;
    private ImageButton addIB;
    private ImageButton leaveIB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hot_keys, container, false);
        // Настройка анимации для появления фрагмента
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.appears_from_right);
        // Применение анимации к View фрагмента
        view.startAnimation(fadeIn);
        initButtons();
        return view;
    }

    private void initButtons() {
        settingsIB = view.findViewById(R.id.settings_image_button);
        addIB = view.findViewById(R.id.add_image_button);
        leaveIB = view.findViewById(R.id.leave_image_button);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        setSettingsIB();
        setAddIB();
        setLeaveIB();
    }

    private void setSettingsIB() {
        settingsIB.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new SettingsFragment());
        });
    }

    private void setAddIB() {
        addIB.setOnClickListener(o -> {
            PasswordCard card = new PasswordCard();
            PasswordLab.getLab(getActivity()).addPasswordCard(card);
            AppFragmentManager.openFragment(PasswordCardFragment.newInstance(card.getId()));
        });
    }

    private void setLeaveIB() {
        leaveIB.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new LoginFragment());
        });
    }
}