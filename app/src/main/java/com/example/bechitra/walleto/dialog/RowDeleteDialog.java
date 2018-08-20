package com.example.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;
import com.example.bechitra.walleto.dialog.listener.OnCloseDialogListener;

public class RowDeleteDialog extends DialogFragment {
    OnCloseDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Delete Data");
        alertDialogBuilder.setMessage("By Deleting data you will lost the row. The action can not be undone after that.");

        alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null)
                    listener.onClose(true);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    public void setOnCloseDialogManager(OnCloseDialogListener listener) {
        this.listener = listener;
    }
}
