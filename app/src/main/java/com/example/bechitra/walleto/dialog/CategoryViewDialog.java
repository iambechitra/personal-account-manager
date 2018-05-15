package com.example.bechitra.walleto.dialog;

import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
import com.example.bechitra.walleto.adapter.RowViewAdapter;
import com.example.bechitra.walleto.table.TableData;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryViewDialog extends DialogFragment{

    @BindView(R.id.barchartCategory) BarChart barChart;
    @BindView(R.id.categoryNameInDialog) TextView categoryName;
    @BindView(R.id.recordCountCategoryText) TextView recordCount;
    @BindView(R.id.rowDataViewRecycler)
    RecyclerView rowDataViewRecycler;

    @BindView(R.id.rowViewScrollView)
    NestedScrollView scrollView;

    DatabaseHelper db;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_category_viewer, null);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(view.getContext());
        StringPatternCreator stk = new StringPatternCreator();
        String date = stk.getCurrentDate();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        String category = getArguments().getString("category");
        categoryName.setText(category);

        String table = db.getSpendingTable();

        List<String> cat = Arrays.asList(getResources().getStringArray(R.array.ECATEGORY));

        for(String c : cat) {
            if (c.equals(category)) {
                table = db.getEarningTable();
                break;
            }
        }


        categoryName.setBackgroundColor(new ColorUtility().getColors(category));
        for(int i = 0; i < 12; i++) {
            List<TableData> list = db.filterDataFromTable(table,"%"+stk.getMonthWithYear(date), category);

            BigDecimal big = BigDecimal.ZERO;
            for(TableData spending : list)
                big = big.add(new BigDecimal(spending.getAmount()));

            float amount = Float.parseFloat(big.toString());
            String [] str = stk.getSeparatedDateArray(date);
            barEntries.add(new BarEntry(Integer.parseInt(str[1]), amount));
            date = stk.getPreviousMonth(date);
            Log.d("Integer", Integer.toString(Integer.parseInt(str[1])));
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Cost");
        dataSet.setValueTextSize(11f);
        dataSet.setColors(new ColorUtility().getColors(category));

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.setPinchZoom(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "DEC", "FEB", "MAR", "APR"};
        xAxis.setValueFormatter(new ValueFormatter(str));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);

        rowDataViewRecycler.setNestedScrollingEnabled(false);
        rowDataViewRecycler.setHasFixedSize(true);
        rowDataViewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        List<TableData> row = db.getAllRowOfACategory(table, category);
        recordCount.setText(""+row.size());
        RowViewAdapter adapter = new RowViewAdapter(view.getContext(), row);
        rowDataViewRecycler.setAdapter(adapter);


        return dialog;
    }

    class ValueFormatter implements IAxisValueFormatter {
        String[] name;

        ValueFormatter(String[] name) {
            this.name = name;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return name[(int)value];
        }
    }


}
