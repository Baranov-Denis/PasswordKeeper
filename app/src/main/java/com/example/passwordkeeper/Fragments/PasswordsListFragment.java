package com.example.passwordkeeper.Fragments;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Locale;

import com.example.passwordkeeper.PasswordLab.PasswordCard;
import com.example.passwordkeeper.PasswordLab.PasswordLab;
import com.example.passwordkeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PasswordsListFragment extends Fragment {


    private FloatingActionButton addFloatingActionButton;
    private FloatingActionButton leaveFloatingActionButton;

    int animDurationDelay ;



    private RecyclerView passwordRecyclerView;
    private PasswordAdapter passwordAdapter;
    private View view;
    PasswordLab passwordLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_passwords_list, container, false);



        passwordRecyclerView = view.findViewById(R.id.password_recycler_view);
        passwordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        passwordLab = PasswordLab.getLab(getContext());
        updateUI();
        animDurationDelay = getResources().getInteger(R.integer.fab_animation_duration);
        addFloatingActionButton = view.findViewById(R.id.fab_add_new_password);
        leaveFloatingActionButton = view.findViewById(R.id.fab_leave_button);

        AppFragmentManager.setUpTargetForBackPressed(this, new LoginFragment());
        //  AppFragmentManager.setAddButton(view, this.getActivity());
        setAddButton();
        setLeaveButton();
        setUpTargetForBackPressed();
       // AppFragmentManager.setLeaveButton(view);
        return view;
    }

    public void setUpTargetForBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                int animDurationDelay = getResources().getInteger(R.integer.fab_animation_duration);

                AppFragmentManager.hideFloatButton(addFloatingActionButton, view);
                AppFragmentManager.hideFloatButton(leaveFloatingActionButton, view);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AppFragmentManager.openFragment(new LoginFragment());
                    }
                }, animDurationDelay );

            }
        });
    }


    private void updateUI() {
        PasswordLab passwordLab = PasswordLab.getLab(getActivity());
        List<PasswordCard> passwordCards = passwordLab.getPasswords();

        if (passwordAdapter == null) {
            passwordAdapter = new PasswordAdapter(passwordCards);
            passwordRecyclerView.setAdapter(passwordAdapter);
        } else {
            passwordAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //TODO
        //     inflater.inflate(R.menu.fragment_password_list, menu);
    }

    @Override
    public void onPause() {

        super.onPause();

    }


    public void setAddButton() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addFloatingActionButton.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_anim_for_fab);
                addFloatingActionButton.startAnimation(rotateAnimation);
            }
        }, animDurationDelay);


        addFloatingActionButton.setOnClickListener(o -> {
            PasswordCard card = new PasswordCard();
            PasswordLab.getLab(getActivity()).addPasswordCard(card);

            Handler mHandler = new Handler();
            AppFragmentManager.hideFloatButton(addFloatingActionButton, view);
            AppFragmentManager.hideFloatButton(leaveFloatingActionButton, view);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Здесь запускаем переход на другой фрагмент

                    AppFragmentManager.openFragment(PasswordCardFragment.newInstance(card.getId()));
                }
            }, animDurationDelay);
        });
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
            AppFragmentManager.hideFloatButton(addFloatingActionButton, view);
            AppFragmentManager.hideFloatButton(leaveFloatingActionButton, view);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppFragmentManager.openFragment(new LoginFragment());
                }
            }, animDurationDelay);
        });
    }







/*
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_password:
                PasswordCard card = new PasswordCard();
                PasswordLab.getLab(getActivity()).addPasswordCard(card);
                Intent intent = PasswordActivity.newIntent(getActivity(), card.getId());
                startActivity(intent);
                TransitAnimation.animationFade(getActivity());
                return true;

            case R.id.start_backup:

                if (passwordLab.backUp()) {
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_successful_to_SD_card), Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.load_base:

/**
 * Test isExternalStorageManager() без этого не будут загружаться перенесенные сохранения
 */

            /*    if (Environment.isExternalStorageManager()) {

                    //todo when permission is granted
                    Intent intentForStartFileManager = new Intent(Intent.ACTION_GET_CONTENT);

             */
    // intentForStartFileManager.setType("*/*");
                  /*  someActivityResultLauncher.launch(intentForStartFileManager);


                } else {
                    //request for the permission
                    Intent intent12 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent12.setData(uri);
                    startActivity(intent12);
                    TransitAnimation.animationFade(getActivity());
                }
                return true;

            case R.id.settings:
                Intent settingIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingIntent);
                TransitAnimation.animationFade(getActivity());
                return true;


            case android.R.id.home:
                LeaveTimer.leaveToLoginActivity(getContext(), getActivity());
                TransitAnimation.animationFade(getActivity());
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
*/

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
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
                        passwordAdapter = null;
                        updateUI();
                    }
                }
            });


    /**
     * TODO
     * /*
     * private void setButton() {
     * AppCompatButton addButton = view.findViewById(R.id.add_new_password);
     * addButton.setOnClickListener(a -> {
     * PasswordCard card = new PasswordCard();
     * PasswordLab.getLab(getActivity()).addPasswordCard(card);
     * Intent intent = PasswordActivity.newIntent(getActivity(), card.getId());
     * startActivity(intent);
     * });
     * }
     */


    private class PasswordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PasswordCard passwordCard;
        private final TextView resourceNameTextView;

        public PasswordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            resourceNameTextView = itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(this);
        }

        public void bind(PasswordCard passwordCard) {


            this.passwordCard = passwordCard;

            if (PasswordLab.passwordIsWrong(passwordCard)) {
                resourceNameTextView.setText(getActivity().getResources().getString(R.string.access_denied));
            } else {
                String[] title = passwordCard.getResourceName().split("");
                title[0] = title[0].toUpperCase(Locale.ROOT);
                StringBuilder result = new StringBuilder();
                for (String s : title) {
                    result.append(s);
                }
                resourceNameTextView.setText(result);
            }
        }


        @Override
        public void onClick(View v) {

            Handler mHandler = new Handler();
            //AppFragmentManager.hideFloatButton(view);
            AppFragmentManager.hideFloatButton(addFloatingActionButton, view);
            AppFragmentManager.hideFloatButton(leaveFloatingActionButton, view);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppFragmentManager.openFragment(PasswordCardFragment.newInstance(passwordCard.getId()));
                }
            }, animDurationDelay);

        }
    }

    private class PasswordAdapter extends RecyclerView.Adapter<PasswordHolder> {

        private final List<PasswordCard> passwordCards;

        public PasswordAdapter(List<PasswordCard> passwordCards) {
            this.passwordCards = passwordCards;
        }

        @NonNull
        @Override
        public PasswordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PasswordHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PasswordHolder holder, int position) {
            PasswordCard passwordCard = passwordCards.get(position);
            holder.bind(passwordCard);
        }

        @Override
        public int getItemCount() {
            return passwordCards.size();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


}