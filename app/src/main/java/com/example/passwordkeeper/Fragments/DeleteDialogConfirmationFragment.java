package com.example.passwordkeeper.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;


public class DeleteDialogConfirmationFragment extends Fragment {


    private View view;

    private EditText newPassword;
    private EditText confirmNewPassword;
    private AppCompatButton chancelButton;
    private AppCompatButton deleteButton;
    private PasswordCard passwordCard;
    private PasswordLab passwordLab;

    public DeleteDialogConfirmationFragment(PasswordCard passwordCard) {
        this.passwordCard = passwordCard;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delete_dialog_confirmation, container, false);
        passwordLab = PasswordLab.getLab(getContext());
        setResourceName();
        setButtons();
        return view;
    }

    private void setButtons() {
        chancelButton = view.findViewById(R.id.chancel_delete_password_card_button);
        chancelButton.setOnClickListener(c ->{
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        deleteButton = view.findViewById(R.id.agree_delete_password_card_button);





        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Ваш код для обработки длительного нажатия
                // Этот блок будет выполнен только при длительном нажатии на кнопку
                Log.i("00000", passwordCard.getId().toString());
                passwordLab.deletePasswordById(passwordCard.getId());
                AppFragmentManager.openFragment(new PasswordsListFragment());
                return true; // Верните true, чтобы показать, что событие обработано
            }
        });


    }

    private void setResourceName() {
        TextView resourceNameTextView = view.findViewById(R.id.password_card_name_on_delete_dialog);
        resourceNameTextView.setText(passwordCard.getResourceName());
    }
}