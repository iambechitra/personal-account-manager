package com.example.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.listener.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletManagementDialog extends DialogFragment{
    @BindView(R.id.deleteCheck) CheckBox deleteCheckBox;
    @BindView(R.id.activateCheck) CheckBox activateCheckBox;
    DialogListener listener;

    int flag = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_wallet_manger_dialog, null);
        alertDialogBuilder.setView(view);
        ButterKnife.bind(this, view);

        activateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    deleteCheckBox.setChecked(false);
                    flag = 1;
                } else
                    flag = -1;
            }
        });

        deleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    activateCheckBox.setChecked(false);
                    flag = 0;
                } else
                    flag = -1;
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    if(flag == -1)
                        listener.onSetDialog(null, false);
                    else
                        listener.onSetDialog(Integer.toString(flag), true);

                }
            }
        });

        alertDialogBuilder.setTitle("Select Operation");

        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
}
