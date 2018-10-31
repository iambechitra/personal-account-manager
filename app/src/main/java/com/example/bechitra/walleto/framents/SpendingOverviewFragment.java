package com.example.bechitra.walleto.framents;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.adapter.RecyclerViewAdapter;
import com.example.bechitra.walleto.table.PrimeTable;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.example.bechitra.walleto.utility.MapHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/26/2018.
 */

public class SpendingOverviewFragment extends Fragment{

    @BindView(R.id.spendingOrEarningRecyclerView)
    RecyclerView spendingRecyclerView;

    @BindView(R.id.amountEntryTransaction) TextView amountText;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.lifeTimeEarnText) TextView lifeTimeEarnText;
    @BindView(R.id.lifeTimeSpendText) TextView lifeTimeSpendText;

    @BindView(R.id.nestedScroll)
    NestedScrollView scrollView;

    @BindView(R.id.resultSelectorSpinner)
    Spinner resultSelectorSpinner;

    DateManager dateManager;
    DataProcessor dataProcessor;

   // @BindView(R.id.spendingOrEarningFilterSwitch)
   // Switch filterByCurrentMonth;

   // @BindView(R.id.expandableLayout) ExpandableLayout expandableLayout;
    //@BindView(R.id.toggleView) RelativeLayout toggleView;

   // @BindView(R.id.advanceFilterText)
   // CheckBox advanceFilterText;

   // @BindView(R.id.calculateAmountCheckBox) CheckBox calculateAmountCheck;
   // @BindView(R.id.calculateAmountTextView) TextView calculateAmountText;

    List<CategoryProcessor> list;
    RecyclerViewAdapter recyclerViewAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spending_earning, container, false);
        ButterKnife.bind(this, view);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        dataProcessor = new DataProcessor(view.getContext());
        dateManager = new DateManager();


        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        resultSelectorSpinner.setAdapter(spinnerArrayAdapter);


        spendingRecyclerView.setHasFixedSize(true);
        spendingRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        reloadData();
        loadGraphData();

        return view;
    }

    private void loadGraphData() {
        ArrayList<Entry> earningEntry = new ArrayList<>();
        ArrayList<Entry> spendingEntry = new ArrayList<>();
        String upperBound = dateManager.getCurrentDate();
        String year = dateManager.getYearFromDate(upperBound);
        String lowerBound = "01/01/"+ year;
        Map<String, List<PrimeTable>> mapEarning = dataProcessor.getMonthlyData("EARNING", lowerBound, upperBound);
        Map<String, List<PrimeTable>> mapSpending = dataProcessor.getMonthlyData("SPENDING", lowerBound, upperBound);

        Map<Integer, Float> balance = new HashMap<>();
        int[] monthCount = new int[13];
        for(Map.Entry<String, List<PrimeTable>> entry : mapSpending.entrySet()) {
            float amount = Float.parseFloat(dataProcessor.getBalanceCalculation(entry.getValue()));
            StringTokenizer stk = new StringTokenizer(entry.getKey());
            String monthName = stk.nextToken();
            int monthID = dateManager.getMonthID(monthName);
            balance.put(monthID, amount);
            monthCount[monthID] = 1;
        }

        for(int i = 1; i < monthCount.length; i++) {
            if (monthCount[i] != 1)
                spendingEntry.add(new Entry(i, 0f));
            else
                spendingEntry.add(new Entry(i, balance.get(i)));
        }

        balance.clear();
        Arrays.fill(monthCount, 0);
        for(Map.Entry<String, List<PrimeTable>> entry : mapEarning.entrySet()) {
            float amount = Float.parseFloat(dataProcessor.getBalanceCalculation(entry.getValue()));
            StringTokenizer stk = new StringTokenizer(entry.getKey());
            String monthName = stk.nextToken();
            int monthID = dateManager.getMonthID(monthName);
            balance.put(monthID, amount);
            monthCount[monthID] = 1;
        }

        for(int i = 1; i < monthCount.length; i++) {
            if (monthCount[i] != 1)
                earningEntry.add(new Entry(i, 0f));
            else
                earningEntry.add(new Entry(i, balance.get(i)));
        }

        LineDataSet set1 = new LineDataSet(spendingEntry, "Spending");
        set1.setColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setValueTextSize(7f);
        set1.setValueTextColor(Color.BLACK);
        LineDataSet set2 = new LineDataSet(earningEntry, "Earning");
        set2.setColor(Color.GREEN);
        set2.setLineWidth(2f);
        set2.setValueTextColor(Color.BLACK);
        set2.setValueTextSize(7f);
        ArrayList<ILineDataSet> data = new ArrayList<>();
        data.add(set1);
        data.add(set2);
        lineChart.setData(new LineData(data));
        lineChart.getDescription().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        final String [] string = {"JAN", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "JAN", "FEB", "MAR", "DEC"};
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("Value DD", ""+value);
                return string[(int)value];
            }
        });
    }

    private void reloadData() {
        String upperBound = dateManager.getCurrentDate();
        String year = dateManager.getYearFromDate(upperBound);
        String lowerBound = "01/01/"+ year;
        Map<String, List<PrimeTable>> mapEarning = dataProcessor.getYearlyData("EARNING", lowerBound, upperBound);
        Map<String, List<PrimeTable>> mapSpending = dataProcessor.getYearlyData("SPENDING", lowerBound, upperBound);
        list = new ArrayList<>();

        double totalEarn = 0;
        for(Map.Entry<String, List<PrimeTable>> entry : mapEarning.entrySet()) {
            List<MapHelper> map = dataProcessor.getCategorisedData(entry.getValue());
            totalEarn = Double.parseDouble(dataProcessor.getBalanceCalculation(entry.getValue()));

            for(MapHelper m : map)
                list.add(new CategoryProcessor(m, "EARNING"));
        }

        double totalSpend = 0;
        for(Map.Entry<String, List<PrimeTable>> entry : mapSpending.entrySet()) {
            List<MapHelper> map = dataProcessor.getCategorisedData(entry.getValue());
            totalSpend = Double.parseDouble(dataProcessor.getBalanceCalculation(entry.getValue()));
            for(MapHelper m : map)
                list.add(new CategoryProcessor(m, "SPENDING"));
        }

        lifeTimeEarnText.setText("$"+totalEarn);
        lifeTimeSpendText.setText("$"+totalSpend);
        amountText.setText("$"+(totalEarn+totalSpend));

        recyclerViewAdapter = new RecyclerViewAdapter(list, view.getContext());
        spendingRecyclerView.setAdapter(recyclerViewAdapter);
        spendingRecyclerView.setNestedScrollingEnabled(false);
    }

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private void dataViewMonthly() {

    }
/*
    private void setViewExpandable()
    {
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();

                if(ROTATION_ANGLE == 0) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_in);
                    toggleView.startAnimation(animation);
                    ROTATION_ANGLE = 180;
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_out);
                    toggleView.startAnimation(animation);
                    ROTATION_ANGLE = 0;
                }
            }
        });
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup vg = (ViewGroup) view;
        vg.setClipChildren(false);
        vg.setClipToPadding(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
        loadGraphData();
    }
}
