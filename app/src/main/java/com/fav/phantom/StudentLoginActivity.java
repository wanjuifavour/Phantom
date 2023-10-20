package com.fav.phantom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class StudentLoginActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView tvUser;
    EditText email;
    EditText password;
    Button login;
    ArrayList<StudentUser> students;

    ProgressBar signInProgress;

    @Override
    protected void onStart() {
        super.onStart();
        getStudents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        students = new ArrayList<>();
        tvUser = findViewById(R.id.tvUsers);
        login = findViewById(R.id.btnStudentLogin);
        email = findViewById(R.id.etStudentLoginEmail);
        password = findViewById(R.id.etStudentLoginPassword);
        signInProgress = findViewById(R.id.pbStudentLogin);


        login.setOnClickListener( view -> {
           loginStudent(email.getText().toString(),password.getText().toString());
        });

    }

    void getStudents(){
        try {
            db.collection("students").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"fetched the data",Toast.LENGTH_LONG).show();
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    StudentUser student = document.toObject(StudentUser.class);
                                    Log.d("STUDENTS", document.getId() + " => " + document.getData() + " => " + student.getEmail());
                                    students.add(student);
                                }
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Could not fetch the data",Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    void loginStudent(String email,String password){
        for (StudentUser std : students){
            if (Objects.equals(std.getEmail(), email)){
                signInStudent(email,password);
                break;
            }
        }
    }

    private void signInStudent(String email, String password) {
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
        startSignIn();
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                        navigateToDashBoard();
                    }else {
                        Toast.makeText(getApplicationContext(), "Login not successful", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("FBAUTHENTICATION", Objects.requireNonNull(e.getMessage()));
            restoreUiState();
        }
    }

    private void startSignIn() {
        signInProgress.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
    }

    private void restoreUiState() {
        signInProgress.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
    }

    void navigateToDashBoard(){
        Intent intent = new Intent(StudentLoginActivity.this, StudentDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        students = new ArrayList<>();
    }
}