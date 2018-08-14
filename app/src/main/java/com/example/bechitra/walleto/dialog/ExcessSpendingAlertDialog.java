package com.example.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.listener.AlertManagerListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExcessSpendingAlertDialog extends DialogFragment{
    @BindView(R.id.spinnr) Spinner spinner;
    @BindView(R.id.timeSetTextView) TextView timeSetTextVeiw;
    @BindView(R.id.timeTextView) TextView timeText;
    AlertManagerListener listener;
    int hour = 6, min = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.excess_spending_alert_dialog, null);
        alertDialogBuilder.setView(view);
        ButterKnife.bind(this, view);

        List<String> spinnerItem = Arrays.asList(getResources().getStringArray(R.array.ALERT_PERCENTAGE));
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerItem);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        timeSetTextVeiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeText.setText(onItemChanged(hourOfDay, minute));
                        hour = hourOfDay;
                        min = minute;
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                timePickerDialog.show();
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null && spinner.getSelectedItem() != null) {
                    StringTokenizer stk = new StringTokenizer(spinner.getSelectedItem().toString(), "%");
                    listener.onSetNewAlert(stk.nextToken(), hour, min);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setTitle("Set Alert Manager");

        return alertDialogBuilder.create();
    }

    private String onItemChanged(int hour, int minute) {
        String TIME = "";
        if(isPostMeridian(hour)) {
            int accHour = hour - 12;
            if(accHour != 0)
                TIME = Integer.toString(accHour)+"."+Integer.toString(minute)+"PM";
            else
                TIME = "12."+Integer.toString(minute)+"PM";
        } else {
            if (hour != 0)
                TIME = Integer.toString(hour) + "." + Integer.toString(minute) + "AM";
            else
                TIME = "12." + Integer.toString(minute) + "AM";
        }

        return TIME;
    }

    private boolean isPostMeridian(int hour) {
        if (hour >=12)
            return true;
        return false;
    }

    public void setOnDialogListener(AlertManagerListener listener) {
        this.listener = listener;
    }
}
