package com.fav.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class StudentDashBoardActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button signOutButton;

    ArrayList<StudentUser> students;
    LinearLayout mainContent;
    ProgressBar fetchingStudent;

    StudentUser studentUser;

    String userEmail;
    TextView userName;



    @Override
    protected void onStart() {
        super.onStart();
        getStudents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash_board);
        db = FirebaseFirestore.getInstance();
        students = new ArrayList<>();
        signOutButton = findViewById(R.id.btnSignOut);
        fetchingStudent = findViewById(R.id.pbFetchStudent);
        mainContent = findViewById(R.id.llMainContent);
        userName = findViewById(R.id.tvUserName);
        mAuth = FirebaseAuth.getInstance();
        userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        signOutButton.setOnClickListener(
                view -> {
                    signOut();
                }
        );
    }

    private void signOut() {
        try {
            mAuth.signOut();
            Intent intent = new Intent(StudentDashBoardActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getStudents(){
        fetchingStudents();
        try {
            db.collection("students").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"fetched the data",Toast.LENGTH_LONG).show();
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            //A variable to keep track of the number of documents
                            //This is a simplification of avoiding a lecture to Login in a student account
                            //The logic might change or be improved in the future
                            int documentCount = 0;
                            int documentSize = queryDocumentSnapshots.size();
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    StudentUser student = document.toObject(StudentUser.class);
                                    Log.d("STUDENTS", document.getId() + " => " + document.getData() + " => " + student.getEmail());
                                    if (Objects.equals(student.getEmail(), userEmail)){
                                        studentUser = student;
                                        Log.d("FBAUTHENTICATION",studentUser.getRegistrationCode());
                                        fetchSuccessful();
                                        break;
                                    }
                                    documentCount++;
                                }
                                Log.d("DOC_COUNT","Document count " + documentCount);
                                Log.d("DOC_COUNT","Document size " + documentSize);
                                //If document size Equals document count then it means the registered user is not a student
                                //And hence he/she should not access student portal
                                if (documentCount == documentSize){
                                    signOut();
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
    private void fetchingStudents() {
        mainContent.setVisibility(View.GONE);
        fetchingStudent.setVisibility(View.VISIBLE);
    }

    private void fetchSuccessful(){
        mainContent.setVisibility(View.VISIBLE);
        fetchingStudent.setVisibility(View.GONE);
        userName.setText(studentUser.getRegistrationCode());
    }

}