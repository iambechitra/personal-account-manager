package com.pipapps.bechitra.walleto.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.pipapps.bechitra.walleto.adapter.RowViewAdapter;
import com.pipapps.bechitra.walleto.databinding.ActivityCategoryViewBinding;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.utility.ColorUtility;
import com.pipapps.bechitra.walleto.utility.DateManager;
import com.pipapps.bechitra.walleto.viewmodel.CategoryItemViewerViewModel;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class CategoryItemViewerActivity extends AppCompatActivity {
    RowViewAdapter adapter;
    DateManager dateManager;
    CategoryItemViewerViewModel viewModel;
    ActivityCategoryViewBinding viewBind;
    List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityCategoryViewBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());
        dateManager = new DateManager();

        viewBind.rowViewScrollView.setFocusableInTouchMode(true);
        viewBind.rowViewScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        String date = dateManager.getCurrentDate();
        String category = getIntent().getExtras().getString("category");

        viewModel = new ViewModelProvider(this).get(CategoryItemViewerViewModel.class);
        viewModel.getAllTransaction().observe(this, transactions -> {
            transactionList = viewModel.getTransactionByTag(transactions, getIntent().getExtras().getString("table"));
            List<Transaction> filteredData = viewModel.getCategorisedTransactionWithinBound(transactionList, category,"01/01/"+dateManager.getYearFromDate(date), date);
            adapter.setData(filteredData);
            viewBind.recordCountCategoryText.setText(""+filteredData.size());
            graphPreset(filteredData, category, date);
            viewBind.barchartCategory.invalidate();
        });

        viewBind.categoryNameInDialog.setText(category);
        int color = new ColorUtility().getColors(category);
        statusBarColorChanger(color);

        viewBind.recordBar.setBackgroundColor(new ColorUtility().getLighterColor(color, 0.8f));

        viewBind.labelLayout.setBackgroundColor(new ColorUtility().getColors(category));

        viewBind.backButton.setOnClickListener(view -> finish());

        viewBind.rowDataViewRecycler.setNestedScrollingEnabled(false);
        viewBind.rowDataViewRecycler.setHasFixedSize(true);
        viewBind.rowDataViewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new RowViewAdapter(this);
        viewBind.rowDataViewRecycler.setAdapter(adapter);
    }

    private void graphPreset(List<Transaction> transactions, String category, String date) {
        BarData data = new BarData(viewModel.getGraphData(transactions, category, date));
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
