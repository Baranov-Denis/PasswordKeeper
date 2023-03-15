package com.example.passwordkeeper.CustomToast;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.passwordkeeper.R;

public class MyToast {

    public static void showMyToast(Activity activity){
        Toast toast = Toast.makeText(activity.getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        View view = activity.getLayoutInflater().inflate(R.layout.activity_custom_toast,(ViewGroup)activity.findViewById(R.id.main_container_for_all_fragments));
        toast.setView(view);
        toast.show();
    }
}
