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
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.adapter.RecyclerViewAdapter;
import com.example.bechitra.walleto.adapter.SpendingRecyclerAdapter;
import com.example.bechitra.walleto.table.TableData;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    DatabaseHelper db;

   // @BindView(R.id.spendingOrEarningFilterSwitch)
   // Switch filterByCurrentMonth;

   // @BindView(R.id.expandableLayout) ExpandableLayout expandableLayout;
    //@BindView(R.id.toggleView) RelativeLayout toggleView;

   // @BindView(R.id.advanceFilterText)
   // CheckBox advanceFilterText;

   // @BindView(R.id.calculateAmountCheckBox) CheckBox calculateAmountCheck;
   // @BindView(R.id.calculateAmountTextView) TextView calculateAmountText;

    SpendingRecyclerAdapter adapter;
    List<TableData> spendingList;
    int ROTATION_ANGLE = 0;
    int CHECKED = 0;

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
        db = new DatabaseHelper(view.getContext());


        spendingRecyclerView.setHasFixedSize(true);
        spendingRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        reloadData();
        loadGraphData();

        return view;
    }

    private void loadGraphData() {
        StringPatternCreator stk = new StringPatternCreator();
        String date = stk.getCurrentDate();
        HashMap<Integer, String> map = new HashMap<>();
        ArrayList<Entry> value = new ArrayList<>();

        for(int i = 0; i < 12; i++) {
            String [] s = stk.getSeparatedDateArray(date);
            map.put(Integer.parseInt(s[1]), date);
            date = stk.getPreviousMonth(date);
        }

        for(int i = 0; i < 12; i++) {
            List<TableData> list = db.getDataOnPattern(db.getSpendingTable(),db.getActivatedWalletID(),"%"+stk.getMonthWithYear(map.get(i+1)));

            float num = 0;
            if(list.size()> 0) {
                for (TableData spending : list)
                    num += Float.parseFloat(spending.getAmount());
            }

            value.add(new Entry(i+1, num));
        }

        ArrayList<Entry> val = new ArrayList<>();

        for(int i = 0; i < 12; i++) {
            List<TableData> list = db.getDataOnPattern(db.getEarningTable(),db.getActivatedWalletID(),"%"+stk.getMonthWithYear(map.get(i+1)));
            Log.d("Earning List", ""+list.size());

            float num = 0;

            if(list.size()> 0) {
                for (TableData spending : list)
                    num += Float.parseFloat(spending.getAmount());
            }

            val.add(new Entry(i+1, num));
        }

        LineDataSet set1 = new LineDataSet(value, "Spending");
        set1.setColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setValueTextSize(8f);
        set1.setValueTextColor(Color.BLACK);
        LineDataSet set2 = new LineDataSet(val, "Earning");
        set2.setColor(Color.GREEN);
        set2.setLineWidth(2f);
        set2.setValueTextColor(Color.BLACK);
        set2.setValueTextSize(8f);
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
        List<String> categoryA = db.getDistinctCategory(db.getSpendingTable());
        List<String> categoryB = db.getDistinctCategory(db.getEarningTable());
        list = new ArrayList<>();
        for(String str : categoryB)
            list.add(new CategoryProcessor(db.getEarningTable(), str, db.getTotalAmountForACategory(str, db.getEarningTable()),
                    ""+db.getAllRowOfACategory(db.getEarningTable(), str).size()));
        double earn = 0;
        for(CategoryProcessor c : list)
            earn += Double.parseDouble(c.getAmount());

        lifeTimeEarnText.setText("$"+earn);

        for(String str : categoryA)
            list.add(new CategoryProcessor(db.getSpendingTable(), str, db.getTotalAmountForACategory(str, db.getSpendingTable()),
                    ""+db.getAllRowOfACategory(db.getSpendingTable(), str).size()));

        double balance = 0;
        for(CategoryProcessor c : list)
            balance += Double.parseDouble(c.getAmount());

        lifeTimeSpendText.setText("$"+(balance-earn));

        amountText.setText("$"+balance);
        recyclerViewAdapter = new RecyclerViewAdapter(list, view.getContext());
        spendingRecyclerView.setAdapter(recyclerViewAdapter);
        spendingRecyclerView.setNestedScrollingEnabled(false);
    }

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
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
