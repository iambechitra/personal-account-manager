package com.example.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.listener.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletCreatorDialog extends DialogFragment {

    @BindView(R.id.walletCreatorEdit)
    EditText walletCreatorEdit;
    @BindView(R.id.isActiveCheckBox)
    CheckBox isActiveWalletChecker;
    DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_wallet_creator, null);
        alertDialogBuilder.setView(view);
        ButterKnife.bind(this, view);

        alertDialogBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!walletCreatorEdit.getText().toString().equals("")) {
                    if(isActiveWalletChecker.isChecked())
                        listener.onSetDialog(walletCreatorEdit.getText().toString(), true);
                    else
                        listener.onSetDialog(walletCreatorEdit.getText().toString(), false);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setTitle("New Wallet");

        return alertDialogBuilder.create();
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
}
