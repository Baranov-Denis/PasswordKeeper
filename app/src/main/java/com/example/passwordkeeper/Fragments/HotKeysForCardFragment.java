package com.example.passwordkeeper.Fragments;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.passwordkeeper.R;


public class HotKeysForCardFragment extends Fragment {

    private View view;
    private final View cardView;
    private TextView loginTextView;
    private TextView passwordTextView;
    private ImageButton showButton;
    private Typeface robotFontFamilyTypeface;
    private boolean isTextHide = true;

    public HotKeysForCardFragment(View cardView) {
        this.cardView = cardView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hot_keys_for_card, container, false);
        AnimationHelper.appearFromLeft(requireActivity(),view,getResources().getInteger(R.integer.animation_duration));
        getTextViewsFromCard();
        connectRobotFontFamily();
        setShowButton();
        setLeaveButton();
        return view;
    }

    private void setShowButton() {
        showButton = view.findViewById(R.id.show_image_button);
        showButton.setOnClickListener(o -> {
            if (isTextHide){
                setLoginAndPasswordVisible(true);
                isTextHide = false;
            }else {
                setLoginAndPasswordVisible(false);
                isTextHide = true;
            }
        });
    }

    private void setLeaveButton() {
        ImageButton leaveFloatingActionButton = view.findViewById(R.id.leave_image_button);
        leaveFloatingActionButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new LoginFragment());
        });
    }

    private void getTextViewsFromCard() {
        loginTextView = cardView.findViewById(R.id.login_text_view);
        passwordTextView = cardView.findViewById(R.id.password_text_view);
    }

    private void setLoginAndPasswordVisible(boolean visible) {

        if (visible) {
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.icons8_eye_100);
            showButton.setImageDrawable(drawable);
            loginTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.icons8_eye_closed_100);
            showButton.setImageDrawable(drawable);
            loginTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        loginTextView.setTypeface(robotFontFamilyTypeface);
        passwordTextView.setTypeface(robotFontFamilyTypeface);
    }

    private void connectRobotFontFamily() {
        robotFontFamilyTypeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_mono_regular);
    }
    @Override
    public void onPause() {
        super.onPause();
       // AnimationHelper.hideToRight(requireActivity(), view);
    }

}