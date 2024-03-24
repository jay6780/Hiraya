package com.shs.app.DialogUtils.noInternetDialog;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shs.app.R;

public class Nointernet {
    public void fillDialog(Activity activity) {
        DialogPlus dialog = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(R.layout.nonet))
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
