package com.example.bechitra.walleto.activity;

import android.os.Build;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.adapter.RowViewAdapter;
import com.example.bechitra.walleto.table.PrimeTable;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.example.bechitra.walleto.viewmodel.CategoryItemViewerViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.*;

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

    @BindView(R.id.recordBar) RelativeLayout recordBar;

    @BindView(R.id.backButton) TextView backButton;

    DatabaseHelper db;
    DataProcessor dataProcessor;
    DateManager dateManager;
    CategoryItemViewerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        ButterKnife.bind(this);
        dataProcessor = new DataProcessor(this);
        dateManager = new DateManager();

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        //db = new DatabaseHelper(this);
        viewModel = new ViewModelProvider(this).get(CategoryItemViewerViewModel.class);
        viewModel.getAllTransaction().observe(this, transactions -> {
            
        });
        String date = dateManager.getCurrentDate();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        String category = getIntent().getExtras().getString("category");
        categoryName.setText(category);
        int color = new ColorUtility().getColors(category);
        statusBarColorChanger(color);
        String table = getIntent().getExtras().getString("table");

        recordBar.setBackgroundColor(new ColorUtility().getLighterColor(color, 0.8f));

        labelLayout.setBackgroundColor(new ColorUtility().getColors(category));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Map<String, List<Transaction>> map = dataProcessor.getMonthlyData(new ArrayList<>(), "01/01/"+dateManager.getYearFromDate(date), date);

        //Balance Preparation of BarChart
        Map<Integer, Float> balance = new HashMap<>();
        int[] monthCount = new int[13];
        for(Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            float amount = Float.parseFloat(dataProcessor.getBalanceCalculation(entry.getValue()));
            StringTokenizer stk = new StringTokenizer(entry.getKey());
            String monthName = stk.nextToken();
            int monthID = dateManager.getMonthID(monthName);
            balance.put(monthID, amount);
            monthCount[monthID] = 1;
        }

        //Assigning value to BarChart;
        for(int i = 1; i < monthCount.length; i++)
            if(monthCount[i] != 1)
                barEntries.add(new BarEntry(i, 0f));
            else
                barEntries.add(new BarEntry(i, balance.get(i)));


        BarDataSet dataSet = new BarDataSet(barEntries, "Cost");
        dataSet.setValueTextSize(8f);
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

        List<PrimeTable> row = db.getAllRowOfACategory(table, category);
        recordCount.setText(""+row.size());
        RowViewAdapter adapter = new RowViewAdapter(this);
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
