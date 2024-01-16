package com.shs.app.DialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shs.app.Activity.Admin.Uploadtask.GeneralChemistryActivity;
import com.shs.app.Activity.Admin.Uploadtask.MILActivity;
import com.shs.app.Activity.Admin.Uploadtask.PhysicalEducationActivity;
import com.shs.app.Activity.Admin.Uploadtask.PracticalResearchActivity;
import com.shs.app.Activity.Admin.Uploadtask.ResearchProjectActivity;
import com.shs.app.Activity.Admin.quizManager.MIL.Mil_quiz;
import com.shs.app.Activity.Admin.quizManager.PE.p_eSubject;
import com.shs.app.Activity.Admin.quizManager.QuizSubjects.GenPhysics.quiz_general_physics;
import com.shs.app.Activity.Admin.quizManager.Reseach_project.Research_project_quiz;
import com.shs.app.Activity.Admin.quizManager.generalChemistryQuiz.generalchemistry2_quiz;
import com.shs.app.Activity.Admin.quizManager.practicalResearch2.pr2Quiz;
import com.shs.app.R;

import java.util.HashMap;
import java.util.Map;

public class Dialog_task {
    public void showtaskDialog(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.spinner_dialog))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();

        View dialogView = dialog.getHolderView();
        Spinner checklistSpinner = dialogView.findViewById(R.id.spinner);

        String[] checklistItems = activity.getResources().getStringArray(R.array.checklist_items);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, checklistItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistSpinner.setAdapter(adapter);

        // Map to store intents/actions for each item
        Map<String, Class<?>> itemIntentMap = new HashMap<>();
        itemIntentMap.put("Select Subject", null);
        itemIntentMap.put("General Physics2", quiz_general_physics.class);
        itemIntentMap.put("General Chemistry2", generalchemistry2_quiz.class);
        itemIntentMap.put("Practical Research2", pr2Quiz.class);
        itemIntentMap.put("Research Project", Research_project_quiz.class);
        itemIntentMap.put("MIL", Mil_quiz.class);
        itemIntentMap.put("Physical Education", p_eSubject.class);

        Button okButton = dialogView.findViewById(R.id.ok_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        okButton.setOnClickListener(v -> {
            String selectedItem = checklistItems[checklistSpinner.getSelectedItemPosition()];
            String toastMessage = "";

            // Set individual toast messages for each item
            switch (selectedItem) {
                case "General Physics2":
                    toastMessage = "General Physics2";
                    break;
                case "General Chemistry2":
                    toastMessage = "General Chemistry2";
                    break;
                case "Practical Research2":
                    toastMessage = "Practical Research2";
                    break;
                case "Research Project":
                    toastMessage = "Research Project";
                    break;
                case "MIL":
                    toastMessage = "MIL";
                    break;
                case "Physical Education":
                    toastMessage = "Physical Education";
                    break;
                default:
                    toastMessage = "Select Subject"; // Default message if none of the cases match
                    break;
            }
            Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            // Perform actions based on the selected item
            Class<?> intentClass = itemIntentMap.get(selectedItem);
            if (intentClass != null) {
                Intent intent = new Intent(activity, intentClass);
                // Add any extra data or configurations to the intent if needed
                activity.startActivity(intent);
                activity.finish();
                activity.overridePendingTransition(0,0);
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
