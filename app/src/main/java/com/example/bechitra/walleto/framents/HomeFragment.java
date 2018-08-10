package com.example.bechitra.walleto.framents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.DataOrganizerAdapter;
import com.example.bechitra.walleto.dialog.ResetDialog;
import com.example.bechitra.walleto.dialog.listner.DialogListener;
import com.example.bechitra.walleto.dialog.listner.ResetListener;
import com.example.bechitra.walleto.graph.GraphData;
import com.example.bechitra.walleto.table.TableData;
import com.example.bechitra.walleto.utility.DataOrganizer;
import com.example.bechitra.walleto.utility.DataProcessor;
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
   // @BindView(R.id.lastSpendingAmount) TextView lastSpendingAmount;
  //  @BindView(R.id.lastSpendingCategory) TextView lastSpendingCategory;
  //  @BindView(R.id.lastSpendingTitle) TextView lastSpendingTitle;
  //  @BindView(R.id.lastSpendingDate) TextView lastSpendingDate;
  //  @BindView(R.id.lastSpendingNote) TextView lastSpendingNote;
    @BindView(R.id.halfPieChart) PieChart pieChart;
    @BindView(R.id.relativeChart) RelativeLayout clickView;
    @BindView(R.id.mainActivityLayout)
    NestedScrollView scrollView;

    @BindView(R.id.currentMonthRecycler) RecyclerView currentRecycler;
    DataOrganizerAdapter adapter;
    TableData lastSpend;
    DatabaseHelper db;
    int viewWidth, viewHeight;
    List<DataOrganizer>list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(view.getContext());
        //String text = db.getBalanceForUser();
        //lastSpend = db.getLastInsertedRow(db.getSpendingTable());

        showPieChart();

        currentRecycler.setHasFixedSize(true);
        currentRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        DataProcessor dataProcessor = new DataProcessor(view.getContext());
        list = dataProcessor.getProcessedData(db.getSpendingTable());

        adapter = new DataOrganizerAdapter(view.getContext(), list, new DialogListener() {
            @Override
            public void onSetDialog(String regex, boolean flag) {
                reloadActivity();
            }
        });
        currentRecycler.setAdapter(adapter);
        currentRecycler.setNestedScrollingEnabled(false);

/*
        if(lastSpend != null) {
            lastSpendingAmount.setText("$" + lastSpend.getAmount());
            lastSpendingCategory.setText(lastSpend.getCategory());

            if(lastSpend.getDate().equals(new StringPatternCreator().getCurrentDate()))
                lastSpendingDate.setText("Today");
            else
                lastSpendingDate.setText(lastSpend.getDate());

           // lastSpendingTitle.setText(lastSpend.getTitle());
           // lastSpendingNote.setText(lastSpend.getNote());
        }*/

        earnBalanceText.setText("$"+db.getCalculationOfCurrentMonth(db.getEarningTable(), db.getActivatedWalletID()));
        spendBalanceText.setText("$"+db.getCalculationOfCurrentMonth(db.getSpendingTable(), db.getActivatedWalletID()));
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

       mainBalance.setText(db.getCurrentBalance(db.getActivatedWalletID()));


        return view;
    }

    private void reloadActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void reloadFragment() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Activity activity = getActivity();
        getActivity().startActivity(intent);
        activity.finish();
    }


    private void showPieChart() {
        setPieData();
        loadPieScreen();
        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        //halfPieChart.setMaxAngle(180);
        pieChart.setRotationAngle(180);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawSliceText(false);
        Log.d("height", Integer.toString(viewHeight));
        //halfPieChart.setHighl(false);

        // halfPieChart.setDescription("Category");
       // halfPieChart.setHoleRadius(25f);
       // halfPieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Top Category");
        pieChart.setCenterTextSize(10);
        pieChart.setTransparentCircleRadius(56f);
    }

    private void loadPieScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)pieChart.getLayoutParams();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int)(height * 0.5);
        params.setMargins(0,0,0,0);
        pieChart.setLayoutParams(params);
    }

    private void setPieData() {
        ArrayList<PieEntry> value = new ArrayList<>();

        List<GraphData> val = db.getPieChartData(4);
        //Log.d("Pie "+val.size(), ""+val.get(4).getTitle());
        for(GraphData d : val)
            value.add(new PieEntry((float) Double.parseDouble(d.getYAxis()), d.getXAxis()));

        PieDataSet dataSet = new PieDataSet(value, "");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setAutomaticallyDisableSliceSpacing(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

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

}
