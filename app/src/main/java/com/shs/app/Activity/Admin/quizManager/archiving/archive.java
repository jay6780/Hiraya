package com.shs.app.Activity.Admin.quizManager.archiving;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Activity.Admin.Adminsettings.Admin;
import com.shs.app.Adapter.quizAdapter.ArchiveQuizAdapter;
import com.shs.app.R;

import java.util.ArrayList;
import java.util.List;

public class archive extends AppCompatActivity {

    private FloatingActionButton float23;
    private RecyclerView archiveRecyclerView;
    private ArchiveQuizAdapter archiveQuizAdapter;
    private DatabaseReference archiveDatabaseReference;
    private DatabaseReference mainDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        getSupportActionBar().setTitle("Archive quiz");
        Drawable homeButton = getResources().getDrawable(R.drawable.ic_home);
        homeButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(homeButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        SpannableString text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D0A0A")));
        float23 = findViewById(R.id.bottom_navigation);
        archiveRecyclerView = findViewById(R.id.quiz);
        archiveRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get references to your Firebase archive and main database nodes
        archiveDatabaseReference = FirebaseDatabase.getInstance().getReference("archive");
        mainDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize your adapter for archived quizzes
        archiveQuizAdapter = new ArchiveQuizAdapter(new ArrayList<>(), mainDatabaseReference); // Pass the main database reference
        archiveRecyclerView.setAdapter(archiveQuizAdapter);

        float23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Disable animation
                finish();
            }
        });
        retrieveArchivedQuizNames();
    }

    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private void retrieveArchivedQuizNames() {
        archiveDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> archivedQuizNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String archivedQuizName = snapshot.getKey();
                    archivedQuizNames.add(archivedQuizName);
                }

                archiveQuizAdapter.setArchivedQuizNames(archivedQuizNames);
                archiveQuizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled if needed
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Admin.class);
        startActivity(intent);
        overridePendingTransition(0, 0); // Disable animation
        finish();
        super.onBackPressed();
    }

}
