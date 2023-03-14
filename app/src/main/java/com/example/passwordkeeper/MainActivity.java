package com.example.passwordkeeper;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;


import com.example.passwordkeeper.Fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    public static int animationDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createLoginFragment();
    }


    private void createLoginFragment(){
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        animationDelay = getResources().getInteger(R.integer.fab_animation_duration);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container_for_all_fragments,loginFragment);
        fragmentTransaction.commit();
    }


}