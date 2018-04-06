package com.example.bechitra.walleto.framents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.bechitra.walleto.AlertManager;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.ExcessSpendingAlertDialog;
import com.example.bechitra.walleto.dialog.listner.AlertManagerListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/26/2018.
 */

public class ReportsFragment extends Fragment{
    @BindView(R.id.excessSpendingAlertSwitch)
    Switch excessSpendingAlert;
    DatabaseHelper db;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.report_fragment, null);
        ButterKnife.bind(this, view);

       db = new DatabaseHelper(getContext());

       if(!db.getOnAlertPercentage().equals("0"))
            excessSpendingAlert.setChecked(true);

        excessSpendingAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ExcessSpendingAlertDialog dialog = new ExcessSpendingAlertDialog();
                    dialog.setOnDialogListener(new AlertManagerListener() {
                        @Override
                        public void onSetNewAlert(String percentage, int hour, int minute) {
                            db.insertOnUserTable(percentage);
                            Log.d("Per", percentage);
                            Log.d("PerC", db.getOnAlertPercentage());
                            onSetAlarmManager(hour, minute, view.getContext());
                        }
                    });
                    dialog.show(getFragmentManager(), "OK");
                } else {
                    db.insertOnUserTable("0");
                    stopAlarmManager(getContext());
                }
            }
        });

        return view;
    }

    public void onSetAlarmManager(int hour, int minute, Context context) {
        alarmPreset(context);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, ((hour*60)+minute)*60*1000, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void stopAlarmManager(Context context) {
        alarmPreset(context);
        alarmManager.cancel(pendingIntent);
    }
    private void alarmPreset(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertManager.class);
        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
