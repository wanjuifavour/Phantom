package com.fav.phantom;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Authentication {
    private final Activity activity;

    private Context context;
    private final FirebaseAuth mAuth;
    Boolean isSignInSuccessful;

    public Authentication(Activity activity,Context context) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        this.context = context;

    }

    Boolean registerStudent(String email,String password){

        return isSignInSuccessful;
    }
}


