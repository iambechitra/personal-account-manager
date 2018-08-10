package com.example.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.listner.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryCreatorDialog extends DialogFragment{
    @BindView(R.id.categoryCreatorEdit)
    EditText categoryCreatorEdit;
    DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.category_dialog, null);
        alertDialogBuilder.setView(view);
        ButterKnife.bind(this, view);

        alertDialogBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    if(!categoryCreatorEdit.getText().toString().equals("")) {
                        Log.d("Category ", categoryCreatorEdit.getText().toString());
                        listener.onSetDialog(categoryCreatorEdit.getText().toString(), false);
                    }
                    else
                        listener.onSetDialog("NULL", false);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setTitle("Category");

        return alertDialogBuilder.create();
    }

    public void setOnAddCategory(DialogListener listener) {
        this.listener = listener;
    }

}
