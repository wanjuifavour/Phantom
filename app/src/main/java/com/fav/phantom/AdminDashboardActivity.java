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

public class AdminDashboardActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button signOutButton;
    ArrayList<StaffUser> staffs;
    LinearLayout mainContent;
    ProgressBar fetchingStaffs;
    StaffUser staffUser;
    String userEmail;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        signOutButton = findViewById(R.id.btnAdminSignOut);
        signOutButton.setOnClickListener( view -> {
            signOut();
        });
    }
    void signOut(){
        try {
            mAuth.signOut();
            Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}