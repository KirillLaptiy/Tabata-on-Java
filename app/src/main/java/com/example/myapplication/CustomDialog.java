package com.example.myapplication;

import android.app.AlertDialog;
import android.view.View;

public class CustomDialog {
    private final View dialogView;
    private final AlertDialog dialog;

    public CustomDialog(View dialogView, AlertDialog dialog) {
        this.dialogView = dialogView;
        this.dialog = dialog;
    }

    public View getDialogView() {
        return dialogView;
    }

    public AlertDialog getDialog() {
        return dialog;
    }
}
