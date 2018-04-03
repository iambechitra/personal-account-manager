package com.example.bechitra.walleto.framents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bechitra.walleto.AddSpending;
import com.example.bechitra.walleto.AlertManager;
import com.example.bechitra.walleto.DatabaseHelper;;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.EarningDialog;
import com.example.bechitra.walleto.dialog.ExcessSpendingAlertDialog;
import com.example.bechitra.walleto.dialog.ResetDialog;
import com.example.bechitra.walleto.dialog.listner.AlertManagerListener;
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.dialog.listner.ResetListener;
import com.example.bechitra.walleto.table.Spending;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment{
    @BindView(R.id.addSpendingText) TextView addSpendingText;
    @BindView(R.id.addEarningText) TextView addEarningText;
    @BindView(R.id.mainBalance) TextView mainBalance;
    @BindView(R.id.earnBalanceText) TextView earnBalanceText;
    @BindView(R.id.spendBalanceText) TextView spendBalanceText;
    @BindView(R.id.settingText) TextView settingText;
    @BindView(R.id.lastSpendingAmount) TextView lastSpendingAmount;
    @BindView(R.id.lastSpendingCategory) TextView lastSpendingCategory;
    @BindView(R.id.lastSpendingTitle) TextView lastSpendingTitle;
    @BindView(R.id.lastSpendingDate) TextView lastSpendingDate;
    @BindView(R.id.lastSpendingNote) TextView lastSpendingNote;
    @BindView(R.id.excessSpendingAlertSwitch) Switch excessSpendingAlert;
    @BindView(R.id.halfPieChart) PieChart halfPieChart;
    Spending lastSpend;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        final DatabaseHelper db = new DatabaseHelper(view.getContext());
        String text = db.getBalanceForUser();
        lastSpend = db.getLastInsertedSpending();

        showPieChart();

        if(lastSpend != null) {
            lastSpendingAmount.setText("$" + lastSpend.getAmount());
            lastSpendingCategory.setText(lastSpend.getCategory());

            if(lastSpend.getDate().equals(new StringPatternCreator().getCurrentDate()))
                lastSpendingDate.setText("Today");
            else
                lastSpendingDate.setText(lastSpend.getDate());

            lastSpendingTitle.setText(lastSpend.getTitle());
            lastSpendingNote.setText(lastSpend.getNote());
        }

        earnBalanceText.setText("$"+db.getCalculationOfCurrentMonth("EARNING"));
        spendBalanceText.setText("$"+db.getCalculationOfCurrentMonth("SPENDING"));
     //   currentDate.setText(new StringPatternCreator().getCurrentDateString());

        settingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetDialog dialog = new ResetDialog();
                dialog.show(getFragmentManager(), "OK");
                dialog.setResetListener(new ResetListener() {
                    @Override
                    public void onResetData(boolean action) {
                        db.resetEveryThing();
                        reloadFragment();
                    }
                });
            }
        });

        if(text != null)
            mainBalance.setText("$"+text);
        addSpendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = new Intent(v.getContext(), AddSpending.class);
                startActivity(intent);
            }
        });

        addEarningText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EarningDialog dialog = new EarningDialog();
                dialog.show(getFragmentManager(), "TAG");
                dialog.setOnCloseDialogListener(new OnCloseDialogListener() {
                    @Override
                    public void onClose(boolean flag) {
                        if(flag)
                            reloadFragment();
                    }
                });
            }
        });

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

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }


    /*
     To set the alarm in a particular time;
     @param hour  - To set The alarm on that hour;
     @param minute - To set the alarm on that minute
     */
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

    private void showPieChart() {
        loadPieScreen();
        setPieData();
        halfPieChart.setBackgroundColor(Color.WHITE);
        halfPieChart.setUsePercentValues(true);
        halfPieChart.setMaxAngle(180);
        halfPieChart.setRotationAngle(180);
        halfPieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        halfPieChart.setRotationEnabled(false);
        halfPieChart.setDrawSliceText(false);

        // halfPieChart.setDescription("Category");
        //halfPieChart.setHoleRadius(25f);
        // halfPieChart.setTransparentCircleAlpha(0);
        //halfPieChart.setCenterText("Top Category");
        //halfPieChart.setCenterTextSize(10);
    }

    private void loadPieScreen() {
        //Display display = getActivity().getWindowManager().getDefaultDisplay();
        //DisplayMetrics matrix = new DisplayMetrics();
        // getActivity().getWindowManager().getDefaultDisplay().getMetrics(matrix);
        // int height = matrix.heightPixels;
        // int offset = (int)(height * 0.5);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)halfPieChart.getLayoutParams();
        params.setMargins(0,0,0,-250);
        halfPieChart.setLayoutParams(params);
    }

    private void setPieData() {
        ArrayList<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(30, "Bangladesh"));
        value.add(new PieEntry(47, "India"));
        value.add(new PieEntry(20, "Nepal"));

        PieDataSet dataSet = new PieDataSet(value, "Partner");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);

        Legend legend = halfPieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        halfPieChart.setData(data);
        halfPieChart.invalidate();
    }

}
