package com.shs.app.Activity.Admin.quizManager.MIL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Activity.Admin.Adminsettings.AboutUs;
import com.shs.app.Activity.Admin.Adminsettings.Admin;
import com.shs.app.Activity.Admin.Adminsettings.AdminRegister;
import com.shs.app.Activity.Admin.Adminsettings.Studentinfo;
import com.shs.app.Activity.Admin.Adminsettings.addtastk;
import com.shs.app.Activity.Admin.quizManager.PE.p_eSubject;
import com.shs.app.Adapter.quizAdapter.Mil_Adapter;
import com.shs.app.Adapter.quizAdapter.PeAdapter;
import com.shs.app.DialogUtils.Dialog;
import com.shs.app.DialogUtils.Dialog_task;
import com.shs.app.R;

import java.util.ArrayList;
import java.util.List;

public class Mil_quiz extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton uploadQuiz;
    private RecyclerView quizRecycler;
    private Mil_Adapter quizAdapter;
    private List<DataSnapshot> quizDataSnapshots;

    ImageView studentImg;
    TextView fullnameText,userEmail,usernameText,phoneText;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mil_quiz);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle("Media Information Literacy");
// Set the color of the title to black
        SpannableString text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D0A0A")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();
        DrawerArrowDrawable toggleDrawable = drawerToggle.getDrawerArrowDrawable();
        toggleDrawable.setColor(Color.YELLOW);

        View headerView = navigationView.getHeaderView(0);
        studentImg = headerView.findViewById(R.id.students);
        fullnameText = headerView.findViewById(R.id.fullname);
        userEmail = headerView.findViewById(R.id.email);
        usernameText = headerView.findViewById(R.id.username);
        phoneText = headerView.findViewById(R.id.phone);

        uploadQuiz = findViewById(R.id.publishBtn);

        quizRecycler = findViewById(R.id.quiz);
        quizRecycler.setHasFixedSize(true);
        quizRecycler.setLayoutManager(new LinearLayoutManager(this));

        quizDataSnapshots = new ArrayList<>();
        quizAdapter = new Mil_Adapter(this, quizDataSnapshots);
        quizRecycler.setAdapter(quizAdapter);


        retrieveStudentDetails();


        uploadQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addtastk.class);
                intent.putExtra("databaseReferenceName", "MIL");
                startActivity(intent);
            }
        });

        findViewById(R.id.fill_in_the_blanks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog fill = new Dialog();
                fill.MilFill(Mil_quiz.this);

            }

        });

        findViewById(R.id.multiple_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog choice = new Dialog();
                choice.MilChoice(Mil_quiz.this);
            }

        });

        // Initialize Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MIL");

        // Add a ValueEventListener to fetch data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizDataSnapshots.clear(); // Clear the list before adding data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    quizDataSnapshots.add(snapshot);
                }
                quizAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors if any
            }
        });




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    Dialog dialog = new Dialog();
                    dialog.logout(Mil_quiz.this);
                    return true;
                }
                if (item.getItemId() == R.id.Home) {
                    Intent home = new Intent(getApplicationContext(), Admin.class);
                    startActivity(home);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }

                if (item.getItemId() == R.id.Admin) {
                    Intent home = new Intent(getApplicationContext(), AdminRegister.class);
                    startActivity(home);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }

                if (item.getItemId() == R.id.About) {
                    Toast.makeText(getApplicationContext(),"About Us", Toast.LENGTH_SHORT).show();
                    Intent about = new Intent(getApplicationContext(), AboutUs.class);
                    startActivity(about);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;

                }

                if (item.getItemId() == R.id.info) {
                    Intent student = new Intent(getApplicationContext(), Studentinfo.class);
                    startActivity(student);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;


                }

                if (item.getItemId() == R.id.checklist) {
                    showChecklistDialog();
                    return true;
                }

                return false;
            }
        });
    }

    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
    private void retrieveStudentDetails() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child(userId);
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = dataSnapshot.child("name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String userName = dataSnapshot.child("username").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    phoneText.setText(String.valueOf(phone));
                    userEmail.setText(email);
                    fullnameText.setText(fullName);
                    usernameText.setText(userName);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Load image with CircleCrop transformation
                        RequestOptions requestOptions = new RequestOptions().circleCrop();
                        Glide.with(getApplicationContext())
                                .load(imageUrl)
                                .apply(requestOptions)
                                .into(studentImg);
                    } else {
                        studentImg.setImageResource(R.drawable.baseline_person_24);
                    }
                } else {
                    // Handle case where data doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }
    private void showChecklistDialog() {
        Dialog_task dialog = new Dialog_task();
        dialog.checklist2(Mil_quiz.this);

    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent back = new Intent(getApplicationContext(), addtastk.class);
            startActivity(back);
            overridePendingTransition(0,0);
            finish();
        }
    }

}