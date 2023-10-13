package com.example.passwordkeeper.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.api.services.drive.model.File;

import com.example.passwordkeeper.PasswordLab.AppPlugins;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.UUID;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;
import com.example.passwordkeeper.PasswordLab.PasswordGenerator;


public class PasswordCardFragment extends Fragment {

    private static final String ARG_PASSWORD_ID = "password_id";

    private Typeface robotFontFamilyTypeface;

    private FloatingActionButton eyeFloatingActionButton;
    private FloatingActionButton leaveFloatingActionButton;
    private int animDurationDelay;

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

    private boolean isShowed = false;

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
        passwordCard = passwordLab.getPasswordCardByUUID(passwordId);
        animDurationDelay = getResources().getInteger(R.integer.animation_duration);
    }


    private void hideAllFloatButtons() {
        //Скрываю клавиатуру
        AppPlugins.hideKeyboard(requireContext(), view);

        if (leaveFloatingActionButton.isOrWillBeShown())
            AppFragmentManager.hideFloatButton(leaveFloatingActionButton, view);
        if (eyeFloatingActionButton.isOrWillBeShown())
            AppFragmentManager.hideFloatButton(eyeFloatingActionButton, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //  addShowPasswordEyeActionButton();
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

    private void connectRobotFontFamily() {
        robotFontFamilyTypeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_mono_regular);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password_card, container, false);
        AnimationHelper.appearFromRight(requireActivity(), view,0);
        resourceNameTextView = view.findViewById(R.id.resource_name_text_view);
        loginTextView = view.findViewById(R.id.login_text_view);
        passwordTextView = view.findViewById(R.id.password_text_view);
        noteTextView = view.findViewById(R.id.note_text_view);
        dateTextView = view.findViewById(R.id.date_text_view);

        backButton = view.findViewById(R.id.back_button_fp);
        changeButton = view.findViewById(R.id.change_button_fp);
        deleteButton = view.findViewById(R.id.delete_button_fp);

        connectRobotFontFamily();


        //Инициализация Fab
        eyeFloatingActionButton = view.findViewById(R.id.fab_eye_button);
        leaveFloatingActionButton = view.findViewById(R.id.fab_leave_button);

        if (passwordLab.passwordIsWrong()) {
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


            //Если (passwordCard.getResourceName() != null) это значит что это ЗАПОЛНЕННЫЙ Password Card
            if (passwordCard.getResourceName() != null) {
                //Отключаю редактирование полей
                disableEditText();
                //Подключаю кнопки
                setButtons();
                //Подключаю Fab с глазом для кратковременного просмотра логина и пароля
                setEyeActionButton();
            } else {
                //Тут условия для создания нового Password Card
                //Включаю видимость логина и пароля
                setLoginAndPasswordVisible(true);
                //Скрываю дату создания
                view.findViewById(R.id.last_changed).setVisibility(View.GONE);
                //
                setAddButtons();
                //Скрываю Fab с глазом
                eyeFloatingActionButton.hide();
            }

        }
        setTextViewListeners(true);
        setUpTargetForBackPressed();
        setLeaveButton();
        //  AppFragmentManager.addFragment(new HotKeyButtonsFragment());

        return view;
    }


    public void setLeaveButton() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                leaveFloatingActionButton.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_anim_for_fab);
                leaveFloatingActionButton.startAnimation(rotateAnimation);
            }
        }, animDurationDelay);


        leaveFloatingActionButton.setOnClickListener(o -> {

            Handler mHandler = new Handler();
            hideAllFloatButtons();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppFragmentManager.openFragment(new LoginFragment());
                }
            }, animDurationDelay);
        });
    }


    public void setUpTargetForBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                int animDurationDelay = getResources().getInteger(R.integer.animation_duration);

                hideAllFloatButtons();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppFragmentManager.openFragment(new PasswordsListFragment());
                    }
                }, animDurationDelay);

            }
        });
    }


    private void setOnlyBackButtons() {

        backButton.setOnClickListener(b -> {
            Handler mHandler = new Handler();
            hideAllFloatButtons();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToPasswordListActivity();
                }
            }, animDurationDelay);
        });
        changeButton.setVisibility(View.GONE);
        deleteButton.setOnClickListener(d -> {
            passwordLab.deletePasswordById(passwordCard.getId());
            Handler mHandler = new Handler();
            hideAllFloatButtons();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToPasswordListActivity();
                }
            }, animDurationDelay);
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


    //Устанавливаю кнопки для обычного режима
    private void setButtons() {

        /**
         * Delete button
         */
        deleteButton.setOnClickListener(d -> {
         /*   FragmentManager fragmentManager = getParentFragmentManager();
            DeleteDialogFragment newFragment = DeleteDialogFragment.newInstance(passwordCard.getId().toString());
            newFragment.show(fragmentManager, DeleteDialogFragment.DELETE);*/
            AppFragmentManager.addFragment(new DeleteDialogConfirmationFragment(passwordCard));
            // passwordLab.deletePasswordById(passwordCard.getId());
            // goToPasswordListActivity();
        });


        /**
         * Back button
         */
        backButton.setOnClickListener(b -> {
            if (!passwordLab.passwordIsWrong()) {
                passwordLab.updatePasswordCard(passwordCard);
            }
            Handler mHandler = new Handler();
            hideAllFloatButtons();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToPasswordListActivity();
                }
            }, animDurationDelay);

        });


        /**
         * Change button
         */
        changeButton.setOnClickListener(e -> {

            //Скрыть Fab с глазом
            eyeFloatingActionButton.hide();
            //Включаю редактирование текста для ввода пароля и остальных полей
            enableEditText();
            //Включаю видимость пароля и логина (откл звездочки)
            setLoginAndPasswordVisible(true);
            //Отключаю копирование логина и пароля при нажатии на них
            setTextViewListeners(false);

            setAddButtons();

            /**
             *Переназначаю кнопку Delete -> Generate
             */
        /*    deleteButton.setText(getActivity().getResources().getString(R.string.Generate));
            //Устанавливаем действие для generate генерируем пароль и вставляем его в поле пароля
            deleteButton.setOnClickListener(del -> {
                passwordTextView.setText(PasswordGenerator.generate());
            });
*/
            /**
             *Переназначаю кнопку Change -> Save
             */
 /*           changeButton.setText(getActivity().getResources().getString(R.string.Save));
            //Устанавливаем действие для Save
            changeButton.setOnClickListener(r -> {

                eyeFloatingActionButton.show();
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
                eyeFloatingActionButton.show();
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

  */
        });
    }


    /**
     * Устанавливаю кнопки для заполнения нового пароля
     */
    private void setAddButtons() {

        /**
         *Переназначаю кнопку Delete -> Generate
         */
        deleteButton.setText(getActivity().getResources().getString(R.string.Generate));
        deleteButton.setOnClickListener(del -> {
            passwordTextView.setText(PasswordGenerator.generate(requireContext()));
        });


        /**
         *Переназначаю кнопку BACK
         */
        backButton.setText(getActivity().getResources().getString(R.string.chancel_button_text));
        backButton.setOnClickListener(ch -> {
            //   if(!allFill()) passwordLab.deletePasswordById(passwordCard.getId());
            Handler mHandler = new Handler();
            hideAllFloatButtons();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToPasswordListActivity();
                }
            }, animDurationDelay);
        });


        /**
         *Переназначаю кнопку Change -> Save
         */
        changeButton.setText(getActivity().getResources().getString(R.string.save_button_text));
        changeButton.setOnClickListener(e -> {
            if (allFill()) {
                saveTextFromFieldsToPasswordCard();
                passwordLab.updatePasswordCard(passwordCard);
                Toast toast = Toast.makeText(getActivity(), passwordCard.getResourceName() + " " + getActivity().getResources().getString(R.string.password_saved), Toast.LENGTH_SHORT);
                toast.show();
                passwordLab.saveDataBaseToDropbox(getContext(), getActivity());

                eyeFloatingActionButton.hide();
                Handler mHandler = new Handler();
                hideAllFloatButtons();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToPasswordListActivity();
                    }
                }, animDurationDelay);
            } else {
                Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Need_to_fill_all_fields), Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    private void setEyeActionButton() {

        // floatingActionButtonEye.hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                eyeFloatingActionButton.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_anim_for_fab);
                eyeFloatingActionButton.startAnimation(rotateAnimation);
            }
        }, animDurationDelay);

        eyeFloatingActionButton.setOnClickListener(e -> {
            if (isShowed) {
                setLoginAndPasswordVisible(false);
                isShowed = false;
            } else {
                setLoginAndPasswordVisible(true);
                isShowed = true;
            }
            Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.just_one_rotate);
            eyeFloatingActionButton.startAnimation(rotateAnimation);
        });

   /*     eyeFloatingActionButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Действие при начале удержания кнопки нажатой
                    setLoginAndPasswordVisible(true);
                    Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.just_one_rotate);
                    eyeFloatingActionButton.startAnimation(rotateAnimation);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Действие при окончании удержания кнопки нажатой
                    setLoginAndPasswordVisible(false);
                    Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.just_one_rotate);
                    eyeFloatingActionButton.startAnimation(rotateAnimation);
                    return true;
                }
                return false;
            }

        });*/

    }

    /**
     * public void setLeaveButton() {
     * <p>
     * Handler handler = new Handler();
     * handler.postDelayed(new Runnable() {
     *
     * @Override public void run() {
     * leaveFloatingActionButton.setVisibility(View.VISIBLE);
     * Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_anim_for_fab);
     * leaveFloatingActionButton.startAnimation(rotateAnimation);
     * }
     * }, animDurationDelay);
     * <p>
     * <p>
     * leaveFloatingActionButton.setOnClickListener(o -> {
     * <p>
     * Handler mHandler = new Handler();
     * AppFragmentManager.hideFloatButton(floatingActionButtonEye, view);
     * AppFragmentManager.hideFloatButton(leaveFloatingActionButton, view);
     * mHandler.postDelayed(new Runnable() {
     * @Override public void run() {
     * AppFragmentManager.openFragment(new LoginFragment());
     * }
     * }, animDurationDelay);
     * });
     * }
     * <p>
     * //
     */


    private void setLoginAndPasswordVisible(boolean visible) {

        if (visible) {
            loginTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            loginTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        loginTextView.setTypeface(robotFontFamilyTypeface);
        passwordTextView.setTypeface(robotFontFamilyTypeface);
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
        if (!passwordLab.passwordIsWrong()) {
            passwordLab.updatePasswordCard(passwordCard);
        }
        AnimationHelper.hideToLeft(requireActivity(),view);
    }


}

