package com.pipapps.bechitra.walleto.framents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.dialog.ExcessSpendingAlertDialog;
import com.pipapps.bechitra.walleto.dialog.listener.AlertManagerListener;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/26/2018.
 */

public class ReportsFragment extends Fragment{
    @BindView(R.id.excessSpendingAlertSwitch) Switch excessSpendingAlert;
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

       //db = new DatabaseHelper(getContext());

     //  if(!db.getOnAlertPercentage().equals("0"))
      //      excessSpendingAlert.setChecked(true);

        excessSpendingAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ExcessSpendingAlertDialog dialog = new ExcessSpendingAlertDialog();
                    dialog.setOnDialogListener(new AlertManagerListener() {
                        @Override
                        public void onSetNewAlert(String percentage, int hour, int minute) {
                           // db.insertOnUserTable(percentage);
                            Log.d("Per", percentage);
                           // Log.d("PerC", db.getOnAlertPercentage());
                            //onSetAlarmManager(hour, minute, view.getContext());
                        }
                    });
                    dialog.show(getFragmentManager(), "OK");
                } else {
                   // db.insertOnUserTable("0");
                    //stopAlarmManager(getContext());
                }
            }
        });
       // setBarChart();

        return view;
    }

/*
    private void setBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        map = new HashMap<>();

        List<EntrySet> graphData = db.getPieChartData(0);

        if(graphData != null) {
            for(int i = 0; i < graphData.size(); i++) {
                map.put((int)((i+1)*10), graphData.get(i).getKey());
                entries.add(new BarEntry((i+1)*10f, (float)graphData.get(i).getValue()));
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

 */
}
