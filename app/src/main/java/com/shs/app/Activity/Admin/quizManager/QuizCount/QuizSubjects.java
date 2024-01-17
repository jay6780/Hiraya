package com.shs.app.Activity.Admin.quizManager.QuizCount;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shs.app.Activity.Admin.Adminsettings.AboutUs;
import com.shs.app.Activity.Admin.Adminsettings.Admin;
import com.shs.app.Activity.Admin.Adminsettings.AdminRegister;
import com.shs.app.Activity.Admin.Adminsettings.Studentinfo;
import com.shs.app.Activity.Admin.Adminsettings.profile_update2;
import com.shs.app.Adapter.imageAdapter.ImageAdapter;
import com.shs.app.Adapter.quizAdapter.QuizAdapter2;
import com.shs.app.Class.QuizClass.QuizItem;
import com.shs.app.DialogUtils.Dialog;
import com.shs.app.DialogUtils.Dialog_task;
import com.shs.app.R;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class QuizSubjects extends AppCompatActivity {

    ImageView studentImg;
    TextView fullnameText,userEmail,usernameText,phoneText;
    Banner pagebanner;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;


    private RecyclerView recyclerView;
    private QuizAdapter2 quizAdapter;
    private List<QuizItem> quizItemList;
    private List<String> nodeNames;
    private List<Integer> nodeCounts;

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
        setContentView(R.layout.activity_quiz_subjects);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        pagebanner = findViewById(R.id.banner);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle("HIRAYA");
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
        retrieveStudentDetails();


        // quiz
        recyclerView = findViewById(R.id.quiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Context context = this;
        quizItemList = new ArrayList<>();
        nodeNames = new ArrayList<>();
        nodeCounts = new ArrayList<>();
        quizAdapter = new QuizAdapter2(context, quizItemList, nodeNames, nodeCounts);
        recyclerView.setAdapter(quizAdapter);



        final List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.page1);
        images.add(R.mipmap.page2);
        images.add(R.mipmap.page3);


//        final Intent[] intents = new Intent[images.size()];
        pagebanner.setAdapter(new ImageAdapter(images))
                .setIndicator(new CircleIndicator(this))
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(Object data, int position) {
//                        // Handle banner item click events here
//                        if (position < intents.length && intents[position] != null) {
////                            startActivity(intents[position]);
////                            overridePendingTransition(0,0);
////                            finish();
//                        }
                    }
                })
                .start();
//
//        intents[0] = new Intent(this, student_java.class);
//        intents[1] = new Intent(this, python_student.class);


        studentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show confirmation dialog before proceeding to profile update
                DialogPlus dialog = DialogPlus.newDialog(QuizSubjects.this)
                        .setContentHolder(new ViewHolder(R.layout.cofirm))
                        .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setGravity(Gravity.BOTTOM)
                        .setCancelable(false)
                        .create();

                View dialogView = dialog.getHolderView();
                Button proceedButton = dialogView.findViewById(R.id.Yes);
                Button cancelButton = dialogView.findViewById(R.id.no);

                proceedButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    // Proceed to profile update activity
                    Intent intent = new Intent(getApplicationContext(), profile_update2.class);
                    intent.putExtra("name", fullnameText.getText().toString());
                    intent.putExtra("username", usernameText.getText().toString());
                    intent.putExtra("email", userEmail.getText().toString());
                    intent.putExtra("image", (String) studentImg.getTag());
                    intent.putExtra("phone", phoneText.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                });

                cancelButton.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                dialog.show();
            }
        });




        DatabaseReference Gen_Physics2 = FirebaseDatabase.getInstance().getReference("Gen_Physics2");
        DatabaseReference PE  = FirebaseDatabase.getInstance().getReference("PE");
        DatabaseReference GenChemistry2  = FirebaseDatabase.getInstance().getReference("generalchemistry2");
        DatabaseReference pr2  = FirebaseDatabase.getInstance().getReference("Practical_Research2");
        DatabaseReference Research  = FirebaseDatabase.getInstance().getReference("Research_project");
        DatabaseReference mil  = FirebaseDatabase.getInstance().getReference("MIL");
//        DatabaseReference normalRef = FirebaseDatabase.getInstance().getReference("Java_normal");

        // Add a ValueEventListener for each node separately to retrieve data from Firebase
        ValueEventListener Gen_Physics2Listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                nodeCounts.add(count);
                nodeNames.add("Gen_Physics2");
                updateAdapterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error fetching data from Firebase: " + databaseError.getMessage());
            }
        };

        ValueEventListener PEListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                nodeCounts.add(count);
                nodeNames.add("PE");
                updateAdapterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error fetching data from Firebase: " + databaseError.getMessage());
            }
        };

        ValueEventListener genche = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                nodeCounts.add(count);
                nodeNames.add("generalchemistry2");
                updateAdapterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error fetching data from Firebase: " + databaseError.getMessage());
            }
        };


        ValueEventListener practical = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                nodeCounts.add(count);
                nodeNames.add("Practical_Research2");
                updateAdapterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error fetching data from Firebase: " + databaseError.getMessage());
            }
        };

        ValueEventListener R_project = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                nodeCounts.add(count);
                nodeNames.add("Research_project");
                updateAdapterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error fetching data from Firebase: " + databaseError.getMessage());
            }
        };

        ValueEventListener media = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                nodeCounts.add(count);
                nodeNames.add("MIL");
                updateAdapterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error fetching data from Firebase: " + databaseError.getMessage());
            }
        };





        // Add the listeners to the database references
        GenChemistry2.addValueEventListener(genche);
        Gen_Physics2.addValueEventListener(Gen_Physics2Listener);
        PE.addValueEventListener(PEListener);
        pr2.addValueEventListener(practical);
        Research.addValueEventListener(R_project);
        mil.addValueEventListener(media);
    }

    private void updateAdapterData() {
        // Check if all data is retrieved for all nodes
        if (nodeCounts.size() == 6) { // Assuming you have four nodes: Easy_mode, Normal_mode, Hard_mode, and 4pics
            // Clear the list before adding new items
            quizItemList.clear();
            // Add a dummy QuizItem for each node (you can modify this according to your actual data structure)
            for (int i = 0; i < nodeCounts.size(); i++) {
                quizItemList.add(new QuizItem());
            }
            quizAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
        }



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    Dialog dialog = new Dialog();
                    dialog.logout(QuizSubjects.this);
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

    private void showChecklistDialog() {
        Dialog_task dialog = new Dialog_task();
        dialog.checklist2(QuizSubjects.this);

    }
    private void retrieveStudentDetails() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child(userId);
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = dataSnapshot.child("name").getValue(String.class);
                    String image = dataSnapshot.child("image").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String userName = dataSnapshot.child("username").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    phoneText.setText(String.valueOf(phone));
                    userEmail.setText(email);
                    fullnameText.setText(fullName);
                    usernameText.setText(userName);

                    studentImg.setTag(image);

                    if (image != null && !image.isEmpty()) {
                        // Load image with CircleCrop transformation
                        RequestOptions requestOptions = new RequestOptions().circleCrop();
                        Glide.with(getApplicationContext())
                                .load(image)
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


    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent back = new Intent(getApplicationContext(), Admin.class);
            startActivity(back);
            overridePendingTransition(0, 0);
            finish();
            super.onBackPressed();
        }
    }
}