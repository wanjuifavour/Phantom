package com.fav.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class StaffLoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button login;
    EditText email;
    EditText password;
    ProgressBar staffLogin;
    ArrayList<StaffUser> staffs;

    @Override
    protected void onStart() {
        super.onStart();
        getStaffs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        login = findViewById(R.id.btnStaffLogin);
        email = findViewById(R.id.etStaffLoginEmail);
        password = findViewById(R.id.etStaffLoginPassword);
        staffLogin = findViewById(R.id.pbStaffLogin);
        staffs = new ArrayList<>();

        login.setOnClickListener(view -> {
            loginStaff(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });

    }

    void loginStaff(String email,String password){
        startSignUp();
        for (StaffUser staff:staffs){
            if (Objects.equals(staff.getEmail(), email)){
                signInStaff(email,password,staff.getRole());
                break;
            }
        }
    }

    void signInStaff(String email, String password,String role) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            restoreUiState();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            restoreUiState();
            return;
        }
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    restoreUiState();
                    if (Objects.equals(role, "Lecturer")){
                        navigateToLecturerDashBoard();
                    }else if(Objects.equals(role, "Administrator")){
                        navigateToAdminDashBoard();
                    }else {
                        return;
                    }
                } else {
                    restoreUiState();
                }
            });
        } catch (Exception e) {
            restoreUiState();
            e.printStackTrace();
        }
    }


    void getStaffs() {
        try {
            db.collection("staff").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Fetched successful",Toast.LENGTH_LONG).show();
                            QuerySnapshot snapshots = task.getResult();
                            for (QueryDocumentSnapshot d : snapshots) {
                                StaffUser staff = d.toObject(StaffUser.class);
                                Log.d("STAFFS",staff.toString());
                                staffs.add(staff);
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void restoreUiState() {
        staffLogin.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
    }

    void startSignUp() {
        staffLogin.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
    }

    void navigateToLecturerDashBoard() {
        Intent intent = new Intent(StaffLoginActivity.this, LecturerDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void navigateToAdminDashBoard() {
        Intent intent = new Intent(StaffLoginActivity.this, LecturerDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}