package com.shs.app.Activity.Student.StudentSettings.quiz_scores.Gen2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Activity.Student.StudentSettings.Student;
import com.shs.app.R;


import java.text.DecimalFormat;

public class Gen_Physics2_scores2 extends AppCompatActivity {
    TextView user_id, score, totalQuestions, passedOrFailed, percentage;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
            setContentView(R.layout.activity_gen_physics2_scores2);
            exit = findViewById(R.id.exits2);
            user_id = findViewById(R.id.user_id);
            score = findViewById(R.id.score);
            percentage = findViewById(R.id.percentage);
            passedOrFailed = findViewById(R.id.pass);
            totalQuestions = findViewById(R.id.total_questions);
            // Set the title of the action bar


            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Back to QUIZ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Student.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
            });

            // Retrieve user ID from FirebaseAuth
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                user_id.setText(userId);
            }

// Retrieve score and total questions from Firebase Realtime Database
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            databaseRef.child("Gen_Physics2_score").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int quizScore = dataSnapshot.getValue(Integer.class);
                        score.setText("Score: " + quizScore);

                        // Save the score to another database named "written_works"
                        DatabaseReference writtenWorksRef = FirebaseDatabase.getInstance().getReference("written_works");
                        // Use "General_Physics2_performanceTask" as the key
                        writtenWorksRef.child(currentUser.getUid()).child("General_Physics2_written_works").setValue(String.valueOf(quizScore));

                        // Center the score vertically
                        score.setGravity(Gravity.CENTER_VERTICAL);

                        // Retrieve total questions from Firebase Realtime Database
                        databaseRef.child("Gen_Physics2").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int total = (int) dataSnapshot.getChildrenCount();
                                    totalQuestions.setText("Total questions: " + total);

                                    // Calculate percentage passed
                                    double percentagePassed = ((double) quizScore / total) * 100;
                                    if (percentagePassed >= 60) {
                                        passedOrFailed.setText("Passed");
                                    } else {
                                        passedOrFailed.setText("Failed");
                                    }

                                    // Format percentage passed as an integer
                                    DecimalFormat df = new DecimalFormat("#");
                                    percentage.setText("Percentage: " + df.format(percentagePassed) + "%");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("scorecheck", "Failed to read value.", databaseError.toException());
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("scorecheck", "Failed to read value.", databaseError.toException());
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Student.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

