package com.example.bechitra.walleto.framents;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.bechitra.walleto.utility.EntrySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.DataOrganizerAdapter;
import com.example.bechitra.walleto.utility.DataOrganizer;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
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
    @BindView(R.id.halfPieChart) PieChart pieChart;
    @BindView(R.id.relativeChart) RelativeLayout clickView;
    @BindView(R.id.mainActivityLayout) NestedScrollView scrollView;
    @BindView(R.id.currentMonthRecycler) RecyclerView currentRecycler;
    @BindView(R.id.tableSelectorSpinner) Spinner tableSelectorSpinner;

    DataOrganizerAdapter adapter;
    DatabaseHelper db;
    List<DataOrganizer>list;
    DataProcessor dataProcessor;
    DateManager dateManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(view.getContext());
        dateManager = new DateManager();

        showPieChart();

        String[] array = {"Spending", "Earning"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        tableSelectorSpinner.setAdapter(spinnerArrayAdapter);

        currentRecycler.setHasFixedSize(true);
        currentRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        dataProcessor = new DataProcessor(view.getContext());
        list = dataProcessor.getProcessedDataCurrentMonth(db.getSpendingTable());

        adapter = new DataOrganizerAdapter(view.getContext(), list);
        currentRecycler.setAdapter(adapter);
        currentRecycler.setNestedScrollingEnabled(false);

        tableSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    list.clear();
                    list = dataProcessor.getProcessedDataCurrentMonth(db.getSpendingTable());
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                } else {
                    list.clear();
                    list = dataProcessor.getProcessedDataCurrentMonth(db.getEarningTable());
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        earnBalanceText.setText("$"+db.getCalculationOfCurrentMonth(db.getEarningTable(), db.getActivatedWalletID()));
        spendBalanceText.setText("$"+db.getCalculationOfCurrentMonth(db.getSpendingTable(), db.getActivatedWalletID()));

        mainBalance.setText("$"+db.getCurrentBalance(db.getActivatedWalletID()));

        return view;
    }

    private void dataLoader() {
        list.clear();
        showPieChart();
        list = dataProcessor.getProcessedDataCurrentMonth(tableSelectorSpinner.getSelectedItem().toString().toUpperCase());
        earnBalanceText.setText("$"+db.getCalculationOfCurrentMonth(db.getEarningTable(), db.getActivatedWalletID()));
        spendBalanceText.setText("$"+db.getCalculationOfCurrentMonth(db.getSpendingTable(), db.getActivatedWalletID()));
        mainBalance.setText("$"+db.getCurrentBalance(db.getActivatedWalletID()));

        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }


    private void showPieChart() {
        setPieData();
        loadPieScreen();

        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(180);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawSliceText(false);

        pieChart.setCenterText("Top Category");
        pieChart.setCenterTextSize(10);
        pieChart.setTransparentCircleRadius(56f);
    }

    private void loadPieScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)pieChart.getLayoutParams();
        params.setMargins(0,0,0,0);
        pieChart.setLayoutParams(params);
    }

    private void setPieData() {
        ArrayList<PieEntry> valu = new ArrayList<>();
        List<EntrySet> val = db.getPieChartData(4);
        ArrayList<PieEntry> value = new ArrayList<>();
        for(EntrySet d : val)
            valu.add(new PieEntry((float) d.getValue(), d.getKey()));

        int index = (valu.size() > 5) ? 5 : valu.size();

        for(int i = 1; i <= index; i++) {
            value.add(valu.get(valu.size()-i));
        }

        PieDataSet dataSet = new PieDataSet(value, "");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setAutomaticallyDisableSliceSpacing(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setForm(Legend.LegendForm.CIRCLE);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataLoader();
    }
}
