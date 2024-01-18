package com.shs.app.DialogUtils.Mil_dialog;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

    public mil_dialog(mil_checkList activity, List<Students> studentList) {
        this.activity = activity;
        this.studentList = studentList;
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
}