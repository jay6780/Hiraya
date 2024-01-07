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

import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shs.app.Activity.login.login;
import com.shs.app.R;

public class Dialog {

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
            activity.overridePendingTransition(0, 0);
            activity.startActivity(intent);
            activity.finish();

        });
        cancel.setOnClickListener(v -> {
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
}
