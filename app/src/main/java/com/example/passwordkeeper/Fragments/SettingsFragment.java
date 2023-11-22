package com.example.passwordkeeper.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordkeeper.DropBoxHelper.DropBoxHelper;
import com.example.passwordkeeper.DropBoxHelper.SharedPreferencesHelper;
import com.example.passwordkeeper.PasswordLab.AppPlugins;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private View view;
    private DropBoxHelper dropBoxHelper;
    private PasswordLab passwordLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        AnimationHelper.appearFade(requireActivity(), view, 0);
        dropBoxHelper = DropBoxHelper.getDropboxHelper(getContext());
        passwordLab = PasswordLab.getLab(getContext());
        setButtons();
        setUpTargetForBackPressed();
        return view;
    }

    private void setButtons() {
        Button getTokenButton = view.findViewById(R.id.get_token_button);
        EditText setTokenEditText = view.findViewById(R.id.enter_token_edit_text_settings_fragment);
        Button enterTokenButton = view.findViewById(R.id.enter_token_button);
        Button saveAndSendDatabaseButton = view.findViewById(R.id.save_and_send_database_button);
        EditText setPasswordLength = view.findViewById(R.id.enter_password_length_edit_text_settings_fragment);
        if (SharedPreferencesHelper.getData(requireContext()).getPasswordLength() != null) {
            setPasswordLength.setText(SharedPreferencesHelper.getData(requireContext()).getPasswordLength());
        }
        Button ladDatabaseButton = view.findViewById(R.id.load_external_database_button);
        Button backButton = view.findViewById(R.id.back_from_settings_button);
        Button changeMainPassword = view.findViewById(R.id.change_main_password_button);


        getTokenButton.setOnClickListener(o -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startActivity(DropBoxHelper.getFirstTokenFromDropbox());
                }
            }).start();

        });

        enterTokenButton.setOnClickListener(o -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!setTokenEditText.getText().toString().equals("")) {
                        SharedPreferencesHelper.saveToken(requireContext(), dropBoxHelper.getAccessToken(setTokenEditText.getText().toString()));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), requireActivity().getResources().getString(R.string.Token_was_successfully_updated), Toast.LENGTH_LONG).show();
                                setTokenEditText.setText("");
                            }
                        });
                        //
                    } else {
                        refreshToken();
                    }
                }
            }).start();
        });

        saveAndSendDatabaseButton.setOnClickListener(o -> {
            passwordLab.saveDataBaseToDropbox(getContext(), getActivity());
        });

        ladDatabaseButton.setOnClickListener(o -> {
            loadDBFromBackup();
        });


        backButton.setOnClickListener(b -> {
            AppFragmentManager.openFragment(new PasswordsListFragment());
        });

        changeMainPassword.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new ChangePasswordFragment());
        });

    }

    private void refreshToken() {
        if (dropBoxHelper.refreshAccessToken()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), requireActivity().getResources().getString(R.string.Token_was_successfully_updated), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void setUpTargetForBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AppFragmentManager.openFragment(new PasswordsListFragment());
            }
        });


    }

    private void loadDBFromBackup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
                Intent intentForStartFileManager = new Intent(Intent.ACTION_GET_CONTENT);
                intentForStartFileManager.setType("*/*");
                someActivityResultLauncher.launch(intentForStartFileManager);
            } else {
                //request for the permission
                Intent intent12 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent12.setData(uri);
                startActivity(intent12);
            }
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        // doSomeOperations();
                        String filePath = data.getData().getPath().replace("/root/", "");
                        Toast.makeText(getContext(), filePath, Toast.LENGTH_LONG).show();
                        passwordLab.loadExternalPasswordsList(getContext(), filePath);
                        AppFragmentManager.openFragment(new PasswordsListFragment());
                    }
                }
            });

    @Override
    public void onPause() {
        AppPlugins.hideKeyboard(requireContext(),view);
        EditText setPasswordLength = view.findViewById(R.id.enter_password_length_edit_text_settings_fragment);
        int length = 8;
        if (!setPasswordLength.getText().toString().equals("")) {
            length = Integer.parseInt(setPasswordLength.getText().toString());
            if (length > 40) length = 40;
            if (length < 8) length = 8;
        }
        SharedPreferencesHelper.savePasswordLength(requireContext(), String.valueOf(length));
        super.onPause();
        AnimationHelper.hideFade(requireActivity(), view);
    }
}