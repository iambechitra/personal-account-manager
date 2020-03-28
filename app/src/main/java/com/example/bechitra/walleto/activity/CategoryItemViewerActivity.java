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
import com.example.bechitra.walleto.databinding.ActivityCategoryViewBinding;
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

   // @BindView(R.id.barchartCategory)
    //BarChart barChart;
    //@BindView(R.id.categoryNameInDialog)
    //TextView categoryName;
    //@BindView(R.id.recordCountCategoryText) TextView recordCount;
    //@BindView(R.id.rowDataViewRecycler)
    //RecyclerView rowDataViewRecycler;

    //@BindView(R.id.rowViewScrollView)
    //NestedScrollView scrollView;

    //@BindView(R.id.labelLayout)
    //RelativeLayout labelLayout;

    //@BindView(R.id.recordBar) RelativeLayout recordBar;

    //@BindView(R.id.backButton) TextView backButton;

    //DatabaseHelper db;
    RowViewAdapter adapter;
    DataProcessor dataProcessor;
    DateManager dateManager;
    CategoryItemViewerViewModel viewModel;
    ActivityCategoryViewBinding viewBind;
    List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityCategoryViewBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());
        //setContentView(R.layout.activity_category_view);
        ButterKnife.bind(this);
        dataProcessor = new DataProcessor(this);
        dateManager = new DateManager();

        viewBind.rowViewScrollView.setFocusableInTouchMode(true);
        viewBind.rowViewScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        String date = dateManager.getCurrentDate();
        String category = getIntent().getExtras().getString("category");
        //db = new DatabaseHelper(this);
        viewModel = new ViewModelProvider(this).get(CategoryItemViewerViewModel.class);
        viewModel.getAllTransaction().observe(this, transactions -> {
            transactionList = viewModel.getTransactionByTag(transactions, getIntent().getExtras().getString("table"));
            List<Transaction> filteredData = viewModel.getCategorisedTransactionWithinBound(transactionList, category,"01/01/"+dateManager.getYearFromDate(date), date);
            adapter.setData(filteredData);
            viewBind.recordCountCategoryText.setText(""+filteredData.size());
            graphPreset(viewModel.getMonthlyData(filteredData, "01/01/"+dateManager.getYearFromDate(date), date), category);
            viewBind.barchartCategory.invalidate();
        });



        viewBind.categoryNameInDialog.setText(category);
        int color = new ColorUtility().getColors(category);
        statusBarColorChanger(color);

        viewBind.recordBar.setBackgroundColor(new ColorUtility().getLighterColor(color, 0.8f));

        viewBind.labelLayout.setBackgroundColor(new ColorUtility().getColors(category));

        viewBind.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewBind.rowDataViewRecycler.setNestedScrollingEnabled(false);
        viewBind.rowDataViewRecycler.setHasFixedSize(true);
        viewBind.rowDataViewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new RowViewAdapter(this);
        viewBind.rowDataViewRecycler.setAdapter(adapter);
    }

    private void graphPreset(Map<String, List<Transaction>> map, String category) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
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
        viewBind.barchartCategory.setData(data);
        viewBind.barchartCategory.setPinchZoom(false);
        viewBind.barchartCategory.setDrawValueAboveBar(true);
        viewBind.barchartCategory.setDoubleTapToZoomEnabled(false);
        viewBind.barchartCategory.getDescription().setEnabled(false);

        XAxis xAxis = viewBind.barchartCategory.getXAxis();
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "DEC", "FEB", "MAR", "APR"};
        xAxis.setValueFormatter(new ValueFormatter(str));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
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
