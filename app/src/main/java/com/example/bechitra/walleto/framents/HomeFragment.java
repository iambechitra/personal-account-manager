package com.example.bechitra.walleto.framents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bechitra.walleto.AlertManager;
import com.example.bechitra.walleto.DatabaseHelper;;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.ExcessSpendingAlertDialog;
import com.example.bechitra.walleto.dialog.ResetDialog;
import com.example.bechitra.walleto.dialog.listner.AlertManagerListener;
import com.example.bechitra.walleto.dialog.listner.ResetListener;
import com.example.bechitra.walleto.graph.GraphData;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment{
    @BindView(R.id.mainBalance) TextView mainBalance;
    @BindView(R.id.earnBalanceText) TextView earnBalanceText;
    @BindView(R.id.spendBalanceText) TextView spendBalanceText;
    @BindView(R.id.settingText) TextView settingText;
    @BindView(R.id.lastSpendingAmount) TextView lastSpendingAmount;
    @BindView(R.id.lastSpendingCategory) TextView lastSpendingCategory;
    @BindView(R.id.lastSpendingTitle) TextView lastSpendingTitle;
    @BindView(R.id.lastSpendingDate) TextView lastSpendingDate;
    @BindView(R.id.lastSpendingNote) TextView lastSpendingNote;
    @BindView(R.id.halfPieChart) PieChart halfPieChart;
    @BindView(R.id.relativeChart) RelativeLayout clickView;
    Spending lastSpend;
    DatabaseHelper db;
    int viewWidth, viewHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        db = new DatabaseHelper(view.getContext());
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
                        reloadActivity();
                    }
                });
            }
        });

        if(text != null)
            mainBalance.setText("$"+text);


        return view;
    }

    private void reloadActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }


    private void showPieChart() {
        setPieData();
        loadPieScreen();
        halfPieChart.setBackgroundColor(Color.WHITE);
        halfPieChart.setUsePercentValues(true);
        halfPieChart.getDescription().setEnabled(false);
        halfPieChart.setDrawHoleEnabled(true);
        halfPieChart.setMaxAngle(180);
        halfPieChart.setRotationAngle(180);
        halfPieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        halfPieChart.setRotationEnabled(false);
        halfPieChart.setDrawSliceText(false);
        Log.d("height", Integer.toString(viewHeight));
        //halfPieChart.setHighl(false);

        // halfPieChart.setDescription("Category");
        //halfPieChart.setHoleRadius(25f);
        // halfPieChart.setTransparentCircleAlpha(0);
        //halfPieChart.setCenterText("Top Category");
        //halfPieChart.setCenterTextSize(10);
    }

    private void loadPieScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)halfPieChart.getLayoutParams();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int)(height * 0.2);
        params.setMargins(10,0,10,-offset);
        halfPieChart.setLayoutParams(params);
    }

    private void setPieData() {
        ArrayList<PieEntry> value = new ArrayList<>();

        List<GraphData> val = db.getPieChartData(4);
        for(GraphData d : val)
            value.add(new PieEntry((float) Double.parseDouble(d.getData()), d.getTitle()));

        PieDataSet dataSet = new PieDataSet(value, "");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setAutomaticallyDisableSliceSpacing(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
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
