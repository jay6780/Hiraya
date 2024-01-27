package com.shs.app.DialogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shs.app.Activity.Admin.quizManager.MIL.Mil_choice;
import com.shs.app.Activity.Admin.quizManager.MIL.Mil_fill;
import com.shs.app.Activity.Admin.quizManager.PE.peFillIntheblanks;
import com.shs.app.Activity.Admin.quizManager.PE.pe_multiple;
import com.shs.app.Activity.Admin.quizManager.QuizSubjects.GenPhysics.generalphysics_fill;
import com.shs.app.Activity.Admin.quizManager.QuizSubjects.GenPhysics.generalphysics_fill_update;
import com.shs.app.Activity.Admin.quizManager.QuizSubjects.GenPhysics.generalphysics_multiple;
import com.shs.app.Activity.Admin.quizManager.Reseach_project.R_projectFill;
import com.shs.app.Activity.Admin.quizManager.Reseach_project.R_project_multiple;
import com.shs.app.Activity.Admin.quizManager.generalChemistryQuiz.generalchemistry2_addfill;
import com.shs.app.Activity.Admin.quizManager.generalChemistryQuiz.generalchemistry2_choiceadd;
import com.shs.app.Activity.Admin.quizManager.practicalResearch2.pr2Fill;
import com.shs.app.Activity.Admin.quizManager.practicalResearch2.pr2Multiple;
import com.shs.app.Activity.login.login;
import com.shs.app.R;

public class Dialog {


    public void fillDialog(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.fill_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Fill in the blanks", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), generalphysics_fill.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void choiceDialog(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.choice_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Multiple choice", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), generalphysics_multiple.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void PedialogFill(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.fill_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Fill in the blanks", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), peFillIntheblanks.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void PedialogChoice(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.choice_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Multiple choice", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), pe_multiple.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }



    public void Pr2fill(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.fill_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Fill in the blanks", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), pr2Fill.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void Pr2choice(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.choice_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Multiple choice", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), pr2Multiple.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }





    public void RprojectFill(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.fill_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Fill in the blanks", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), R_projectFill.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void RprojectChoice(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.choice_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Multiple choice", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), R_project_multiple.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }







    public void genChemistry2fill(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.fill_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Fill in the blanks", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), generalchemistry2_addfill.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void genChemistry2choice(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.choice_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Multiple choice", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), generalchemistry2_choiceadd.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }



    public void MilFill(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.fill_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Fill in the blanks", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), Mil_fill.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void MilChoice(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.choice_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Multiple choice", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), Mil_choice.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }



    public void logout(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.logout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button logout = dialogView.findViewById(R.id.Yes);
        Button cancel = dialogView.findViewById(R.id.no);
        logout.setOnClickListener(v -> {
            Toast.makeText(activity, "Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(activity.getApplicationContext(), login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void permission(Context context) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.permission))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button ok = dialogView.findViewById(R.id.Yes);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void showChecklistDialog(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.spinner_dialog)) // Replace R.layout.spinner_dialog with your custom layout for the spinner dialog
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();

        View dialogView = dialog.getHolderView();
        Spinner checklistSpinner = dialogView.findViewById(R.id.spinner); // Replace R.id.spinner with your Spinner ID in the custom layout

        // Get the array from resources
        String[] checklistItems = activity.getResources().getStringArray(R.array.checklist_items);

        // Set up the adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, checklistItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistSpinner.setAdapter(adapter);

        Button okButton = dialogView.findViewById(R.id.ok_button); // Replace R.id.ok_button with your OK button ID
        Button cancelButton = dialogView.findViewById(R.id.cancel_button); // Replace R.id.cancel_button with your Cancel button ID

        okButton.setOnClickListener(v -> {
            String selectedItem = checklistItems[checklistSpinner.getSelectedItemPosition()];
            Toast.makeText(activity, selectedItem + " selected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            // Perform actions based on the selected item
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    public void rulesDialog(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.rules))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .create();
        View dialogView = dialog.getHolderView();
        Button ok = dialogView.findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}
