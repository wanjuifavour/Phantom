package com.fav.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class StaffSignUpActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView arrowBack;
    EditText staffEmail;
    EditText staffPassword;
    EditText staffName;
    EditText staffConfirmPassword;
    Button signUp;
    RadioGroup role;
    String roleSelected;
    ProgressBar signUpProgress;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_sign_up);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        staffName = findViewById(R.id.etStaffUserName);
        staffEmail = findViewById(R.id.etStaffEmail);
        staffPassword = findViewById(R.id.etStaffPassword);
        staffConfirmPassword = findViewById(R.id.etStaffConfirmPassword);
        arrowBack = findViewById(R.id.ivStaffArrowBack);
        signUp = findViewById(R.id.btnStaffSignUp);
        role = findViewById(R.id.rgRole);
        RadioButton radioButton = findViewById(role.getCheckedRadioButtonId());
        roleSelected = radioButton.getText().toString();
        signUpProgress = findViewById(R.id.pbStaffSignUp);
        login = findViewById(R.id.tvStudentLogin);

        role.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton radioButton1 = findViewById(checkedId);
            roleSelected = radioButton1.getText().toString();
        });

        arrowBack.setOnClickListener(
                view -> {
                    onBackPressed();
                }
        );

        signUp.setOnClickListener(
                view -> {
                    String email = staffEmail.getText().toString();
                    String password = staffPassword.getText().toString();
                    String name = staffName.getText().toString();
                    registerStaff(email, password, name, roleSelected);
                }
        );

        login.setOnClickListener(view -> {
            navigateToLoginScreen();
        });

    }


    void registerStaff(
            String email,
            String password,
            String userName,
            String role
    ) {
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
        startSignUp();
        StaffUser staffUser = new StaffUser(email, userName, role);
        try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sign Up successful", Toast.LENGTH_LONG).show();
                            addStaffUser(staffUser);
                            restoreUiState();
                            if (Objects.equals(roleSelected, "Administrator")) {
                                navigateToAdministratorDashBoard();
                            } else {
                                navigateToLecturerDashBoard();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Sign Up failed", Toast.LENGTH_LONG).show();
                            restoreUiState();
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            restoreUiState();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void addStaffUser(StaffUser staffUser) {
        try {
            db.collection("staff").add(staffUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startSignUp() {
        signUpProgress.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.GONE);
    }

    private void restoreUiState() {
        signUpProgress.setVisibility(View.GONE);
        signUp.setVisibility(View.VISIBLE);
    }

    void navigateToLecturerDashBoard() {
        Intent intent = new Intent(StaffSignUpActivity.this, LecturerDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void navigateToAdministratorDashBoard() {
        Intent intent = new Intent(StaffSignUpActivity.this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void navigateToLoginScreen(){
        Intent intent = new Intent(StaffSignUpActivity.this, StaffLoginActivity.class);
        startActivity(intent);
    }
}