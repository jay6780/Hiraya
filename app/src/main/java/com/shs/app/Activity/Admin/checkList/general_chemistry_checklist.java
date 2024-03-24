package com.shs.app.Activity.Admin.checkList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
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
import com.shs.app.Activity.Admin.Adminsettings.assestment_activity;
import com.shs.app.Adapter.tableAdapter.CustomTableDataAdapter;
import com.shs.app.Adapter.tableAdapter.CustomTableHeaderAdapter;
import com.shs.app.Adapter.tableAdapter.SeparationLineTableDecoration;
import com.shs.app.Class.Student.Students;
import com.shs.app.DialogUtils.GenChemistry.GenChemistryDialog;
import com.shs.app.DialogUtils.GenPhysics.genPhysicsDialog;
import com.shs.app.DialogUtils.Dialog;
import com.shs.app.DialogUtils.Dialog_task;
import com.shs.app.R;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class general_chemistry_checklist extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    ImageView studentImg;
    TextView fullnameText, userEmail, usernameText, phoneText;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private boolean isDialogShowing = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseReference studentRef;
    private List<Students> studentList;
    private TableView<String[]> tableView;  // Note the type change to String[]
    private String[][] studentData;
    FloatingActionButton rotateBtn, delete;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chemistry_checklist);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle("General Chemistry");
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

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(general_chemistry_checklist.this);
        rotateBtn = findViewById(R.id.rotate);
        delete = findViewById(R.id.clear);
        retrieveStudentDetails();

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

        // Initialize TableView
        tableView = findViewById(R.id.tableView);
        tableView.setColumnCount(4);  // Set the number of columns based on your data (1 for names, 3 for assessments)

        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        studentRef = firebaseDatabase.getReference("Student");
        studentList = new ArrayList<>();


        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataForStudentDialog();

            }
        });


        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String studentId = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);
                    Students student = new Students(studentId, null, image, name, null);
                    studentList.add(student);
                }

                // Convert studentList to a 2D array for the table data
                studentData = new String[studentList.size()][4];  // Adjust columns accordingly

                for (int i = 0; i < studentList.size(); i++) {
                    Students student = studentList.get(i);
                    studentData[i][0] = student.getName();
                    // Set default values for the other columns
                    studentData[i][1] = "0";
                    retrievePerformanceTaskData(student.getId(), i);
                    retrievePerformanceTaskData2(student.getId(), i);
                    retrievePerformanceTaskData3(student.getId(), i);
                    studentData[i][3] = "0";
                }

                // Create the adapter and decoration after populating studentData

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    Dialog dialog = new Dialog();
                    dialog.logout(general_chemistry_checklist.this);
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
                    Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_SHORT).show();
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

    private void deleteDataForStudentDialog() {
        GenChemistryDialog deleteDialog = new GenChemistryDialog(this, studentList,studentData);
        deleteDialog.deleteDataForStudentDialog();
    }


    private void retrievePerformanceTaskData3(String studentId, final int rowIndex) {
        DatabaseReference generalChemistryRef = FirebaseDatabase.getInstance().getReference().child("Grade2").child(studentId).child("General_Chemistry2_quarterly_assessment");
        generalChemistryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (rowIndex < studentData.length) {
                    if (dataSnapshot.exists()) {
                        String performanceTaskScore = dataSnapshot.getValue(String.class);
                        studentData[rowIndex][3] = performanceTaskScore;
                    } else {
                        studentData[rowIndex][3] = "0";
                    }
                    updateTableView();
                } else {
                    // Handle the case where rowIndex is out of bounds
                    // For example, you can log a message or take appropriate action
                    Log.e("ArrayIndexOutOfBounds", "Row index is out of bounds: " + rowIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    private void retrievePerformanceTaskData2(String studentId, final int rowIndex) {
        DatabaseReference generalChemistryRef = FirebaseDatabase.getInstance().getReference().child("written_works").child(studentId).child("general_chemistry_written_works");
        generalChemistryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (rowIndex >= 0 && rowIndex < studentData.length) { // Check if rowIndex is within bounds
                    if (dataSnapshot.exists()) {
                        String performanceTaskScore = dataSnapshot.getValue(String.class);
                        // Update the performance task data in the studentData array
                        studentData[rowIndex][1] = performanceTaskScore;
                    } else {
                        // Set the value to "N/A" if there is no grade
                        studentData[rowIndex][1] = "0";
                    }

                    // Update the TableView with the modified studentData
                    updateTableView();
                } else {
                    // Handle the case where rowIndex is out of bounds
                    // For example, you can log a message or take appropriate action
                    Log.e("ArrayIndexOutOfBounds", "Row index is out of bounds: " + rowIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    private void retrievePerformanceTaskData(String studentId, final int rowIndex) {
        DatabaseReference generalChemistryRef = FirebaseDatabase.getInstance().getReference().child("Grade").child(studentId).child("General_Chemistry2_performanceTask");
        generalChemistryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (rowIndex >= 0 && rowIndex < studentData.length) { // Check if rowIndex is within bounds
                    if (dataSnapshot.exists()) {
                        String performanceTaskScore = dataSnapshot.getValue(String.class);
                        // Update the performance task data in the studentData array
                        studentData[rowIndex][2] = performanceTaskScore;
                    } else {
                        // Set the value to "N/A" if there is no grade
                        studentData[rowIndex][2] = "0";
                    }

                    // Update the TableView with the modified studentData
                    updateTableView();
                } else {
                    // Handle the case where rowIndex is out of bounds
                    // For example, you can log a message or take appropriate action
                    Log.e("ArrayIndexOutOfBounds", "Row index is out of bounds: " + rowIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }


    private void updateTableView() {
        // Create the adapter and decoration after populating studentData
        TableDataAdapter<String[]> customTableDataAdapter = new CustomTableDataAdapter(general_chemistry_checklist.this, studentData);

        SeparationLineTableDecoration decoration = new SeparationLineTableDecoration(
                general_chemistry_checklist.this,
                getResources().getColor(R.color.black),
                getResources().getDimension(R.dimen.separatorHeight),
                customTableDataAdapter
        );

        tableView.addView(decoration);

        // Set the header and other properties
        List<String[]> headerData = new ArrayList<>();
        headerData.add(new String[]{"Name", "Written Works", "Performance Task", "Quarterly Assessment"});
        tableView.setHeaderAdapter(new CustomTableHeaderAdapter(general_chemistry_checklist.this,
                "Name", "Written Works", "Performance Task", "Quarterly Assessment"));

        tableView.setColumnWeight(1, 1);
        tableView.setColumnWeight(2, 1);
        tableView.setColumnWeight(3, 1);

        tableView.addDataLongClickListener((rowIndex, clickedData) -> {
            // Get the corresponding student from the filtered list
            String studentName = clickedData[0];
            Students student = getStudentFromFilteredList(studentName);

            if (student != null) {
                String studentId = student.getId();
                String studentImage = student.getImage();
                showEditScoresDialog(studentId, studentName, studentImage, rowIndex);
            } else {
                Log.e("StudentNotFound", "Student not found in filtered list.");
            }

            return true;
        });
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(getResources().getColor(R.color.beige), getResources().getColor(R.color.beige)));

        tableView.setDataAdapter(customTableDataAdapter);
    }

    private Students getStudentFromFilteredList(String studentName) {
        for (Students student : studentList) {
            if (student.getName().equals(studentName)) {
                return student;
            }
        }
        return null; // Return null if student is not found
    }

    private void showEditScoresDialog(final String studentId, final String studentName, String studentImage, final int rowIndex) {

        GenChemistryDialog deleteDialog = new GenChemistryDialog(this, studentList,studentData);
        deleteDialog.editScores(studentId,studentName,studentImage,rowIndex);
    }


    private void refreshingData() {
        tableView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        Toast.makeText(getApplicationContext(),"Refresh Success",Toast.LENGTH_SHORT).show();
        studentData = new String[0][4];
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String studentId = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);
                    Students student = new Students(studentId, null, image, name, null);
                    studentList.add(student);
                }
                studentData = new String[studentList.size()][4];

                for (int i = 0; i < studentList.size(); i++) {
                    Students student = studentList.get(i);
                    studentData[i][0] = student.getName();
                    studentData[i][1] = "0";
                    retrievePerformanceTaskData(student.getId(), i);
                    retrievePerformanceTaskData2(student.getId(), i);
                    retrievePerformanceTaskData3(student.getId(), i);
                    studentData[i][3] = "0";
                }

                updateTableView();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Rect rect = new Rect();
                        swipeRefreshLayout.getDrawingRect(rect);
                        int centerY = rect.centerY();
                        int offset = centerY - (swipeRefreshLayout.getProgressCircleDiameter() / 2);
                        swipeRefreshLayout.setProgressViewOffset(false, 0, offset);
                        swipeRefreshLayout.setRefreshing(false);
                        tableView.setVisibility(View.VISIBLE);
                    }
                }, 1500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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

    private void showChecklistDialog() {
        Dialog_task dialog = new Dialog_task();
        dialog.checklist2(general_chemistry_checklist.this);

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
            super.onBackPressed();
            Intent i = new Intent(getApplicationContext(), Admin.class);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterStudentData(newText);
        return false;
    }

    private void filterStudentData(String query) {
        List<Students> filteredList = new ArrayList<>();

        for (Students student : studentList) {
            if (student.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(student);
            }
        }

        // Convert filteredList to a 2D array for the table data
        studentData = new String[filteredList.size()][4];

        for (int i = 0; i < filteredList.size(); i++) {
            Students student = filteredList.get(i);
            studentData[i][0] = student.getName();
            studentData[i][1] = "N/A";
            retrievePerformanceTaskData(student.getId(), i);
            retrievePerformanceTaskData2(student.getId(), i);
            retrievePerformanceTaskData3(student.getId(), i);
            studentData[i][3] = "N/A";
        }

        updateTableView();
    }

    @Override
    public void onRefresh() {
        refreshingData();
    }

}