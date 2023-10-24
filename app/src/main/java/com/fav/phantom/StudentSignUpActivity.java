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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentSignUpActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView arrowBack;
    EditText studentEmail;
    EditText studentPassword;
    EditText studentName;
    EditText studentConfirmPassword;
    EditText studentRegistrationCode;
    ProgressBar signUpProgress;
    Authentication authentication;
    Button signUpButton;
    TextView textError;
    TextView login;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            navigateToDashBoard();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_auth);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        authentication = new Authentication(StudentSignUpActivity.this, getApplicationContext());
        studentEmail = findViewById(R.id.etStudentEmail);
        studentPassword = findViewById(R.id.etStudentPassword);
        studentName = findViewById(R.id.etStudentUserName);
        studentConfirmPassword = findViewById(R.id.etStudentConfirmPassword);
        studentRegistrationCode = findViewById(R.id.etStudentRegistration);
        login = findViewById(R.id.tvStudentLogin);
        signUpProgress = findViewById(R.id.pbSignUp);
        arrowBack = findViewById(R.id.arrow_back);
        textError = findViewById(R.id.tvError);
        signUpButton = findViewById(R.id.btnStudentSignUp);


        signUpButton.setOnClickListener(view ->
                {
                    registerStudent(studentEmail.getText().toString(),
                            studentPassword.getText().toString(),
                            studentConfirmPassword.getText().toString(),
                            studentName.getText().toString(),
                            studentRegistrationCode.getText().toString()
                    );
                }
        );

        arrowBack.setOnClickListener(
                view -> {
                    onBackPressed();
                }
        );
        login.setOnClickListener(
                view -> {
                    Intent intent = new Intent(StudentSignUpActivity.this, StudentLoginActivity.class);
                    startActivity(intent);
                }
        );
    }

    void registerStudent(
            String email,
            String password,
            String confirmPassword,
            String userName,
            String registrationCode) {
        startSignUp();
        try {
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
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(getApplicationContext(), "Please enter userName!", Toast.LENGTH_LONG).show();
                restoreUiState();
                return;
            }
            if (TextUtils.isEmpty(registrationCode)) {
                Toast.makeText(getApplicationContext(), "Please enter registration!", Toast.LENGTH_LONG).show();
                restoreUiState();
                return;
            }
            if (!password.equals(confirmPassword)){
                Toast.makeText(getApplicationContext(), "Password and confirm password do not match!", Toast.LENGTH_LONG).show();
                restoreUiState();
                return;
            }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                StudentSignUpActivity.this, task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Sign Up successful", Toast.LENGTH_LONG).show();
                                        StudentUser student = new StudentUser(
                                                email,
                                                userName,
                                                registrationCode
                                        );
                                        addUserToDatabase(student);
                                        restoreUiState();
                                        navigateToDashBoard();
                                    } else {
                                        showError("Could not register");
                                        restoreUiState();
                                    }
                                }
                        );
        } catch (Exception e) {
            showError(e.getMessage());
            restoreUiState();
        }
    }

    void addUserToDatabase(StudentUser student) {
        CollectionReference collection = db.collection("students");
        try {
            collection.add(student).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration not successful", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    void showError(String errorMessage) {
        textError.setText(errorMessage);
        textError.setVisibility(View.VISIBLE);
    }

    void navigateToDashBoard() {
        Intent intent = new Intent(StudentSignUpActivity.this, StudentDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    void startSignUp() {
        signUpProgress.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.GONE);
    }

    void restoreUiState() {
        signUpProgress.setVisibility(View.GONE);
        textError.setVisibility(View.GONE);
        signUpButton.setVisibility(View.VISIBLE);
    }


}