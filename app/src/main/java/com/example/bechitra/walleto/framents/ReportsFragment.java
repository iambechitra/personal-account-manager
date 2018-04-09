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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.bechitra.walleto.AlertManager;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.ExcessSpendingAlertDialog;
import com.example.bechitra.walleto.dialog.listner.AlertManagerListener;
import com.example.bechitra.walleto.graph.GraphData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/26/2018.
 */

public class ReportsFragment extends Fragment{
    @BindView(R.id.excessSpendingAlertSwitch) Switch excessSpendingAlert;
    DatabaseHelper db;

    @BindView(R.id.horizontalBarchart)
    HorizontalBarChart horizontalBarChart;
    Map<Integer, String> map;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    static int count = 0;
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
        setBarChart();

        return view;
    }

    public void onSetAlarmManager(int hour, int minute, Context context) {
        alarmPreset(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        long bM = ((hour*60)+minute)*60*1000;
        long aM = calendar.getTimeInMillis();
        Log.d(getClass().getSimpleName(), "bM:"+bM+" aM:"+aM);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, aM, AlarmManager.INTERVAL_DAY, pendingIntent);
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

    private void setBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        map = new HashMap<>();

        List<GraphData> graphData = db.getPieChartData(0);

        if(graphData != null) {
            for(int i = 0; i < graphData.size(); i++) {
                map.put((int)((i+1)*10), graphData.get(i).getTitle());
                entries.add(new BarEntry((i+1)*10f, Float.parseFloat(graphData.get(i).getData())));
                Log.d("Data", graphData.get(i).getData());
            }
        } else {
            entries.add(new BarEntry(1 * 10f, 50));
            map.put((int)(1*10f), "Data");
        }
        BarDataSet set = new BarDataSet(entries, "Category");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);
        BarData data = new BarData(set);
        data.setBarWidth(7f);
        horizontalBarChart.setData(data);
        horizontalBarChart.getDescription().setEnabled(false);
        horizontalBarChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                count++;
                Log.d("Value", count+" -> "+value);
                return map.get((int)value) != null ? map.get((int)value) : "";
            }
        });
        //horizontalBarChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
    }
}
