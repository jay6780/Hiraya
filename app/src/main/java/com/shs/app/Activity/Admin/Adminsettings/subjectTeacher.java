package com.shs.app.Activity.Admin.Adminsettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shs.app.R;

public class subjectTeacher extends AppCompatActivity {
    Spinner selectSub;
    FirebaseAuth mAuth;
    DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_teacher);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        adminRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child(mAuth.getCurrentUser().getUid());

        selectSub = findViewById(R.id.subjectTeacher);
        String[] subjects = {"Subject teacher in?","General Physics2", "General Chemistry2", "Practical Research2", "Research Project MIL", "Physical Education"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSub.setAdapter(adapter);
        selectSub.setSelection(0);

        // Save selected subject to Firebase Realtime Database
        selectSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {
                    String selectedSubject = subjects[position];
                    adminRef.child("teacher").setValue(selectedSubject)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Navigate to another activity
                                    Intent intent = new Intent(subjectTeacher.this, Admin.class);
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }
}