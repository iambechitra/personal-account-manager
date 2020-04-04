package com.pipapps.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.pipapps.bechitra.walleto.dialog.listener.OnResetListener;

public class ResetDialog extends DialogFragment{
    OnResetListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Reset Data");
        alertDialogBuilder.setMessage("By resetting data you will lost everything. The action can not be undone after that.");

        alertDialogBuilder.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null)
                    listener.onResetData(true);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null)
                    listener.onResetData(false);

                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    public void setOnResetListener(OnResetListener listener) {
        this.listener = listener;
    }
}
