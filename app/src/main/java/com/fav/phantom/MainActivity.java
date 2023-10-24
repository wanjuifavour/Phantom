package com.fav.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnStudentPortal;
    Button btnStaffPortal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStaffPortal = findViewById(R.id.btnStaffPortal);
        btnStudentPortal = findViewById(R.id.btnStudentPortal);

        btnStudentPortal.setOnClickListener(
                view -> {
                    Intent intent = new Intent(MainActivity.this, StudentSignUpActivity.class);
                    startActivity(intent);
                }
        );
        btnStaffPortal.setOnClickListener(
                view -> {
                    Intent intent = new Intent(MainActivity.this, StaffSignUpActivity.class);
                    startActivity(intent);
                }
        );


    }
}