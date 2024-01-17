package com.shs.app.Activity.Admin.Adminsettings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Adapter.tableAdapter.CustomTableDataAdapter;
import com.shs.app.Adapter.tableAdapter.CustomTableHeaderAdapter;
import com.shs.app.Adapter.tableAdapter.SeparationLineTableDecoration;
import com.shs.app.Class.Student.Students;
import com.shs.app.R;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class checklist extends AppCompatActivity {

    private DatabaseReference studentRef;
    private List<Students> studentList;
    private TableView<String[]> tableView;  // Note the type change to String[]
    private String[][] studentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Initialize TableView
        tableView = findViewById(R.id.tableView);
        tableView.setColumnCount(4);  // Set the number of columns based on your data (1 for names, 3 for assessments)

        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        studentRef = firebaseDatabase.getReference("Student");
        studentList = new ArrayList<>();

        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String studentId = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    Students student = new Students(studentId, null, null, name, null);
                    studentList.add(student);
                }

                // Convert studentList to a 2D array for the table data
                studentData = new String[studentList.size()][4];  // Adjust columns accordingly

                for (int i = 0; i < studentList.size(); i++) {
                    Students student = studentList.get(i);
                    studentData[i][0] = student.getName();
                    // Set default values for the other columns
                    studentData[i][1] = "N/A";
                    studentData[i][2] = "N/A";
                    studentData[i][3] = "N/A";
                }

                // Create the adapter and decoration after populating studentData
                TableDataAdapter<String[]> customTableDataAdapter = new CustomTableDataAdapter(checklist.this, studentData);

                SeparationLineTableDecoration decoration = new SeparationLineTableDecoration(
                        checklist.this,
                        getResources().getColor(R.color.black),
                        getResources().getDimension(R.dimen.separatorHeight),
                        customTableDataAdapter
                );

                tableView.addView(decoration);

                List<String[]> headerData = new ArrayList<>();
                headerData.add(new String[]{"Name", "Written Works", "Performance Task", "Quarterly Assessment"});
                tableView.setHeaderAdapter(new CustomTableHeaderAdapter(checklist.this,
                        "Name", "Written Works", "Performance Task", "Quarterly Assessment"));

                tableView.setColumnWeight(1, 1);
                tableView.setColumnWeight(2, 1);
                tableView.setColumnWeight(3, 1);

                tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(getResources().getColor(R.color.white), getResources().getColor(R.color.white)));

                tableView.setDataAdapter(customTableDataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
