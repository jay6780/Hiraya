package com.shs.app.Activity.Student.StudentSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.shs.app.Activity.Admin.Adminsettings.assestment_activity;
import com.shs.app.Adapter.AnnouncementAdapter.AnnouncementAdapter2;
import com.shs.app.Adapter.imageAdapter.ImageAdapter;
import com.shs.app.Class.Announce.Announcement;
import com.shs.app.DialogUtils.Dialog;
import com.shs.app.DialogUtils.noInternetDialog.Nointernet;
import com.shs.app.R;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class Student extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ImageView studentImg;
    TextView fullnameText, userEmail, usernameText, phoneText, birthdayText;
    Banner pagebanner;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    DatabaseReference databaseReference;
    AnnouncementAdapter2 adapter;
    List<Announcement> announcementList;
    ListView memberListView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.logo) {
            Dialog dialog = new Dialog();
            dialog.rulesDialog(Student.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_logo, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
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
        birthdayText = headerView.findViewById(R.id.birthday);
        //refresh
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(Student.this);

        retrieveStudentDetails();
        fetchDataFromFirebase();

        memberListView = findViewById(R.id.memberListView);
        announcementList = new ArrayList<>();
        adapter = new AnnouncementAdapter2(this, announcementList);
        memberListView.setAdapter(adapter);

        memberListView.setVerticalScrollBarEnabled(false);
        memberListView.setHorizontalScrollBarEnabled(false);


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
                DialogPlus dialog = DialogPlus.newDialog(Student.this)
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
                    Intent intent = new Intent(getApplicationContext(), profile_update.class);
                    intent.putExtra("name", fullnameText.getText().toString());
                    intent.putExtra("username", usernameText.getText().toString());
                    intent.putExtra("email", userEmail.getText().toString());
                    intent.putExtra("image", (String) studentImg.getTag());
                    intent.putExtra("phone", phoneText.getText().toString());
                    intent.putExtra("birthday", birthdayText.getText().toString());
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    Dialog dialog = new Dialog();
                    dialog.logout(Student.this);
                    return true;
                }
                if (item.getItemId() == R.id.Home) {
                    Intent home = new Intent(getApplicationContext(), Student.class);
                    startActivity(home);
                    overridePendingTransition(0, 0);
                    finish();

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
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Student").child(userId);
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = dataSnapshot.child("name").getValue(String.class);
                    String image = dataSnapshot.child("image").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String userName = dataSnapshot.child("username").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String birthday = dataSnapshot.child("birthday").getValue(String.class);
                    userEmail.setText(email);
                    fullnameText.setText(fullName);
                    usernameText.setText(userName);
                    phoneText.setText(String.valueOf(phone));
                    birthdayText.setText(birthday);

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


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {

        if (isConnected()) {
            fetchDataFromFirebase();
            Toast.makeText(getApplicationContext(),"Refresh success", Toast.LENGTH_SHORT).show();
            memberListView.setAdapter(adapter);
            memberListView.setVisibility(View.GONE);
        } else {
            announcementList.clear();
            memberListView.setVisibility(View.GONE);
            Nointernet nointernet = new Nointernet();
            nointernet.fillDialog(Student.this);
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                swipeRefreshLayout.getDrawingRect(rect);
                int centerY = rect.centerY();
                int offset = centerY - (swipeRefreshLayout.getProgressCircleDiameter() / 2);
                swipeRefreshLayout.setProgressViewOffset(false, 0, offset);
                swipeRefreshLayout.setRefreshing(false);
                memberListView.setVisibility(View.VISIBLE);
            }
        }, 1500);
    }

    private void fetchDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Task");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcementList.clear();
                for (DataSnapshot announcementSnapshot : snapshot.getChildren()) {
                    Announcement announcement = announcementSnapshot.getValue(Announcement.class);
                    if (announcement != null) {
                        announcementList.add(announcement);
                    }
                }

                // Sort announcementList from newest to oldest based on time and date
                Collections.sort(announcementList, new Comparator<Announcement>() {
                    @Override
                    public int compare(Announcement announcement1, Announcement announcement2) {
                        // Assuming time and date are in a format that allows lexicographical comparison
                        String dateTime1 = announcement1.getDate() + " " + announcement1.getTime();
                        String dateTime2 = announcement2.getDate() + " " + announcement2.getTime();

                        // Reverse the order for newest to oldest
                        return dateTime2.compareTo(dateTime1);
                    }
                });

                adapter.notifyDataSetChanged();

                if (announcementList.isEmpty()) {
                    // Handle the case when the list is empty
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
