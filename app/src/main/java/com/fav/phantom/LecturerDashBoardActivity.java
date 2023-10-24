package com.fav.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LecturerDashBoardActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dash_board);
        mAuth = FirebaseAuth.getInstance();
        signOut = findViewById(R.id.btnLecturerSignOut);

        signOut.setOnClickListener(view -> {
            signOut();
        });
    }
    private void signOut() {
        try {
            mAuth.signOut();
            Intent intent = new Intent(LecturerDashBoardActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}