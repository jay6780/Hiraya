package com.shs.app.DialogUtils.Mil_dialog;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import com.shs.app.Activity.Admin.checkList.mil_checkList;
import com.shs.app.Class.Student.Students;
import com.shs.app.R;

import java.util.List;

public class mil_dialog {
    private mil_checkList activity;
    private List<Students> studentList;
    private String[][] studentData;

    public mil_dialog(mil_checkList activity, List<Students> studentList, String[][] studentData) {
        this.activity = activity;
        this.studentList = studentList;
        this.studentData = studentData;
    }

    public void deleteDataForStudentDialog() {
        if (studentList == null) {
            return;
        }

        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.delete_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button delete = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        delete.setOnClickListener(v -> {
            Toast.makeText(activity.getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
            for (Students student : studentList) {
                deleteDataForStudent(student.getId());
            }
            dialog.dismiss();
            Intent intent = new Intent(activity, mil_checkList.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(0,0);
            activity.finish();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void deleteDataForStudent(String studentId) {
        DatabaseReference grade2Ref = FirebaseDatabase.getInstance().getReference().child("Grade2").child(studentId).child("MIL_quarterly_assessment");
        DatabaseReference writtenWorksRef = FirebaseDatabase.getInstance().getReference().child("written_works").child(studentId).child("MIL_written_works");
        DatabaseReference gradeRef = FirebaseDatabase.getInstance().getReference().child("Grade").child(studentId).child("MIL_performanceTask");
        grade2Ref.removeValue();

        writtenWorksRef.removeValue();
        gradeRef.removeValue();
    }

    public void editScores(String studentId, String studentName, String studentImage, int rowIndex) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.dialog_edit_scores))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();

        // Set content view
        View dialogView = dialog.getHolderView();
        EditText writtenWorksEditText = dialogView.findViewById(R.id.editTextWrittenWorks);
        EditText performanceTaskEditText = dialogView.findViewById(R.id.editTextPerformanceTask);
        EditText quarterlyAssessmentEditText = dialogView.findViewById(R.id.editTextQuarterlyAssessment);
        Button updatedBtn = dialogView.findViewById(R.id.update);
        Button cancel = dialogView.findViewById(R.id.cancel);
        TextView name = dialogView.findViewById(R.id.name);
        ImageView avatar = dialogView.findViewById(R.id.avatar);
        name.setText(studentName);

        Glide.with(activity)
                .load(studentImage)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .circleCrop()
                .into(avatar);

        writtenWorksEditText.setText(studentData[rowIndex][1]);
        performanceTaskEditText.setText(studentData[rowIndex][2]);
        quarterlyAssessmentEditText.setText(studentData[rowIndex][3]);

        updatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String writtenWorksScore = writtenWorksEditText.getText().toString();
                String performanceTaskScore = performanceTaskEditText.getText().toString();
                String quarterlyAssessmentScore = quarterlyAssessmentEditText.getText().toString();

                DatabaseReference gradeRef = FirebaseDatabase.getInstance().getReference().child("Grade").child(studentId);
                gradeRef.child("MIL_performanceTask").setValue(performanceTaskScore);
                DatabaseReference writtenWorksRef = FirebaseDatabase.getInstance().getReference().child("written_works").child(studentId);
                writtenWorksRef.child("MIL_written_works").setValue(writtenWorksScore);
                DatabaseReference grade2Ref = FirebaseDatabase.getInstance().getReference().child("Grade2").child(studentId);
                grade2Ref.child("MIL_quarterly_assessment").setValue(quarterlyAssessmentScore);

                // Update the studentData array
                studentData[rowIndex][1] = writtenWorksScore;
                studentData[rowIndex][2] = performanceTaskScore;
                studentData[rowIndex][3] = quarterlyAssessmentScore;
                Toast.makeText(activity, "Scores updated for " + studentName, Toast.LENGTH_SHORT).show();
                activity.onRefresh();

                // Dismiss dialog
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}