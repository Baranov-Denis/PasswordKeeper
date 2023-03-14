package com.example.passwordkeeper.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.api.services.drive.model.File;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.UUID;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;
import com.example.passwordkeeper.PasswordLab.PasswordGenerator;


public class PasswordCardFragment extends Fragment {

    private static final String ARG_PASSWORD_ID = "password_id";

    private  FloatingActionButton floatingActionButtonEye;

    private View view;

    private PasswordCard passwordCard;
    private EditText resourceNameTextView;
    private EditText loginTextView;
    private EditText passwordTextView;
    private EditText noteTextView;
    private TextView dateTextView;

    private AppCompatButton backButton;
    private AppCompatButton changeButton;
    private AppCompatButton deleteButton;

    private PasswordLab passwordLab;

    public static PasswordCardFragment newInstance(UUID passwordId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PASSWORD_ID, passwordId);
        PasswordCardFragment passwordFragment = new PasswordCardFragment();
        passwordFragment.setArguments(args);
        return passwordFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID passwordId = (UUID) getArguments().getSerializable(PasswordCardFragment.ARG_PASSWORD_ID);
        passwordLab = PasswordLab.getLab(getActivity());
        passwordCard = passwordLab.getPasswordCard(passwordId);
    }



    @Override
    public void onResume() {
        super.onResume();
        addShowPasswordEyeActionButton();
    }

    private void disableEditText() {
        resourceNameTextView.setFocusable(false);
        loginTextView.setFocusable(false);
        passwordTextView.setFocusable(false);
        noteTextView.setFocusable(false);
    }

    private void enableEditText() {
        resourceNameTextView.setFocusableInTouchMode(true);
        loginTextView.setFocusableInTouchMode(true);
        passwordTextView.setFocusableInTouchMode(true);
        noteTextView.setFocusableInTouchMode(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password_card, container, false);

        resourceNameTextView = view.findViewById(R.id.resource_name_text_view);
        loginTextView = view.findViewById(R.id.login_text_view);
        passwordTextView = view.findViewById(R.id.password_text_view);
        noteTextView = view.findViewById(R.id.note_text_view);
        dateTextView = view.findViewById(R.id.date_text_view);

        backButton = view.findViewById(R.id.back_button_fp);
        changeButton = view.findViewById(R.id.change_button_fp);
        deleteButton = view.findViewById(R.id.delete_button_fp);

        floatingActionButtonEye = view.findViewById(R.id.fab_eye_button);

        if (PasswordLab.passwordIsWrong(passwordCard)) {
            resourceNameTextView.setText(getActivity().getResources().getString(R.string.access_denied));
            loginTextView.setText(getActivity().getResources().getString(R.string.access_denied));
            passwordTextView.setText(getActivity().getResources().getString(R.string.access_denied));
            noteTextView.setText(getActivity().getResources().getString(R.string.access_denied));
            dateTextView.setText(getActivity().getResources().getString(R.string.access_denied));
            setOnlyBackButtons();
        } else {
            resourceNameTextView.setText(passwordCard.getResourceName());
            loginTextView.setText(passwordCard.getLogin());
            passwordTextView.setText(passwordCard.getPassword());
            noteTextView.setText(passwordCard.getNote());
            dateTextView.setText(passwordCard.getDate());

            if (passwordCard.getResourceName() != null) {
                disableEditText();
                setButtons();

            } else {
                setLoginAndPasswordVisible(true);
                view.findViewById(R.id.last_changed).setVisibility(View.GONE);
                setAddButtons();
                floatingActionButtonEye.hide();
            }
        }
        setTextViewListeners(true);
        AppFragmentManager.setUpTargetForBackPressed(this,new PasswordsListFragment());
        AppFragmentManager.setLeaveButton(view);
        return view;
    }


    private void setOnlyBackButtons() {


        backButton.setOnClickListener(b -> {
            goToPasswordListActivity();
        });
        changeButton.setVisibility(View.GONE);
        deleteButton.setOnClickListener(d -> {
            passwordLab.deletePasswordById(passwordCard.getId());
            goToPasswordListActivity();
        });
    }



    private boolean allFill() {
        //Проверка нужно заполнить как минимум название ресурса
        return (!resourceNameTextView.getText().toString().trim().equals(""));
    }

    private void goToPasswordListActivity() {
      /*  Intent intent = new Intent(getActivity(), PasswordListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        TransitAnimation.animationFade(getActivity());*/
        AppFragmentManager.openFragment(new PasswordsListFragment());
    }

    private void saveTextFromFieldsToPasswordCard() {
        passwordCard.setResourceName(resourceNameTextView.getText().toString().trim());
        passwordCard.setLogin(loginTextView.getText().toString().trim());
        passwordCard.setPassword(passwordTextView.getText().toString().trim());
        passwordCard.setNote(noteTextView.getText().toString().trim());
        passwordCard.setDate(new Date());
    }


    private void setButtons() {
/**
 * Delete button
 */
        deleteButton.setOnClickListener(d -> {
         /*   FragmentManager fragmentManager = getParentFragmentManager();
            DeleteDialogFragment newFragment = DeleteDialogFragment.newInstance(passwordCard.getId().toString());
            newFragment.show(fragmentManager, DeleteDialogFragment.DELETE);*/
        });

/**
 * Back button
 */
        backButton.setOnClickListener(b -> {
            if (!PasswordLab.passwordIsWrong(passwordCard)) {
                passwordLab.updatePasswordCard(passwordCard);
            }
            goToPasswordListActivity();
        });

/**
 * Change button
 */
        changeButton.setOnClickListener(e -> {
            FloatingActionButton floatingActionButtonEye = view.findViewById(R.id.fab_eye_button);
            floatingActionButtonEye.hide();


            enableEditText();
            setTextViewListeners(false);
            deleteButton.setText(getActivity().getResources().getString(R.string.Generate));
            deleteButton.setOnClickListener(del -> {
                passwordTextView.setText(PasswordGenerator.generate());
            });

            setLoginAndPasswordVisible(true);


            changeButton.setText(getActivity().getResources().getString(R.string.Save));
            changeButton.setOnClickListener(r -> {

                floatingActionButtonEye.show();
                setLoginAndPasswordVisible(false);


                if (allFill()) {
                    saveTextFromFieldsToPasswordCard();
                    disableEditText();
                    deleteButton.setText(getActivity().getResources().getString(R.string.Delete));
                    changeButton.setText(getActivity().getResources().getString(R.string.change_button_text));
                    backButton.setText(getActivity().getResources().getString(R.string.Back));
                    setTextViewListeners(true);
                    setButtons();
                    dateTextView.setText(passwordCard.getDate());
                    passwordCard.setNote(noteTextView.getText().toString().trim());
                    passwordLab.updatePasswordCard(passwordCard);
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Need_to_fill_all_fields), Toast.LENGTH_SHORT).show();
                }
            });

            backButton.setText(getActivity().getResources().getString(R.string.Cancel));
            backButton.setOnClickListener(o -> {
                floatingActionButtonEye.show();
                backButton.setText(getActivity().getResources().getString(R.string.Back));
                deleteButton.setText(getActivity().getResources().getString(R.string.Delete));
                setLoginAndPasswordVisible(false);
                resourceNameTextView.setText(passwordCard.getResourceName());
                loginTextView.setText(passwordCard.getLogin());
                passwordTextView.setText(passwordCard.getPassword());
                disableEditText();
                changeButton.setText(getActivity().getResources().getString(R.string.change_button_text));
                setButtons();
            });
        });
    }

    private void setAddButtons() {

        deleteButton.setText(getActivity().getResources().getString(R.string.Generate));
        deleteButton.setOnClickListener(del -> {
            passwordTextView.setText(PasswordGenerator.generate());
        });


        backButton.setText(getActivity().getResources().getString(R.string.chancel_button_text));
        backButton.setOnClickListener(ch -> {
            passwordLab.deletePasswordById(passwordCard.getId());
            goToPasswordListActivity();
        });


        changeButton.setText(getActivity().getResources().getString(R.string.save_button_text));
        changeButton.setOnClickListener(e -> {
            if (allFill()) {
                saveTextFromFieldsToPasswordCard();
                passwordLab.updatePasswordCard(passwordCard);
                Toast toast = Toast.makeText(getActivity(), passwordCard.getResourceName() + " " + getActivity().getResources().getString(R.string.password_saved), Toast.LENGTH_SHORT);
                toast.show();
                goToPasswordListActivity();
            } else {
                Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Need_to_fill_all_fields), Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    private void addShowPasswordEyeActionButton() {

        // floatingActionButtonEye.hide();

        floatingActionButtonEye.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Действие при начале удержания кнопки нажатой
                    setLoginAndPasswordVisible(true);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Действие при окончании удержания кнопки нажатой
                    setLoginAndPasswordVisible(false);
                    return true;
                }
                return false;
            }

        });


    }

    private void setLoginAndPasswordVisible( boolean visible) {
        if (visible) {
            loginTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            loginTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void setTextViewListeners(Boolean enable) {
        if (enable) {
            loginTextView.setOnClickListener(l -> {
                copyTextToClipBoard(loginTextView.getText().toString().trim(), getActivity().getResources().getString(R.string.login));
            });
            passwordTextView.setOnClickListener(p -> {
                copyTextToClipBoard(passwordTextView.getText().toString().trim(), getActivity().getResources().getString(R.string.password));
            });
        } else {
            loginTextView.setOnClickListener(l -> {
            });
            passwordTextView.setOnClickListener(p -> {
            });
        }
    }


    private void copyTextToClipBoard(String text, String from) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        Toast.makeText(getActivity(), resourceNameTextView.getText() + " " + from + " " + getActivity().getResources().getString(R.string.copied), Toast.LENGTH_SHORT).show();
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (passwordCard == null || passwordCard.getPassword() == null) {
            passwordLab.deletePasswordById(passwordCard.getId());
        }
        if (!PasswordLab.passwordIsWrong(passwordCard)) {
            passwordLab.updatePasswordCard(passwordCard);
        }
    }




}

