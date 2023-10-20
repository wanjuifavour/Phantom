package com.fav.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class StaffActivity extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        db = FirebaseFirestore.getInstance();
    }

    void getStaffs(){
        try {
           db.collection("staff").get().addOnCompleteListener( task -> {
               if (task.isSuccessful()){
                   QuerySnapshot queryDocumentSnapshots = task.getResult();
                   if (!queryDocumentSnapshots.isEmpty()){
                       for (DocumentSnapshot d: queryDocumentSnapshots){

                       }
                   }
               }
           });
        }catch (Exception e){

        }
    }
}