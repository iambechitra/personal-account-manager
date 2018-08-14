package com.example.bechitra.walleto.activity;

import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.utility.DateManager;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryItemViewerActivity extends AppCompatActivity {

    @BindView(R.id.barchartCategory)
    BarChart barChart;
    @BindView(R.id.categoryNameInDialog)
    TextView categoryName;
    @BindView(R.id.recordCountCategoryText) TextView recordCount;
    @BindView(R.id.rowDataViewRecycler)
    RecyclerView rowDataViewRecycler;

    @BindView(R.id.rowViewScrollView)
    NestedScrollView scrollView;

    @BindView(R.id.labelLayout)
    RelativeLayout labelLayout;

    @BindView(R.id.backButton) TextView backButton;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        ButterKnife.bind(this);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(this);
        DateManager stk = new DateManager();
        String date = stk.getCurrentDate();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        String category = getIntent().getExtras().getString("category");
        categoryName.setText(category);
        statusBarColorChanger(new ColorUtility().getColors(category));
        String table = getIntent().getExtras().getString("table");

        //Log.d("table", table);

        labelLayout.setBackgroundColor(new ColorUtility().getColors(category));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        //dataSet.setValueTextSize(8f);
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
        rowDataViewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<TableData> row = db.getAllRowOfACategory(table, category);
        recordCount.setText(""+row.size());
        RowViewAdapter adapter = new RowViewAdapter(this, row, table);
        rowDataViewRecycler.setAdapter(adapter);
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

    private void statusBarColorChanger(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
