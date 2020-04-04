package com.example.bechitra.walleto.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.OrganisedDataViewerAdapter;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.table.PrimeTable;
import com.example.bechitra.walleto.utility.DataOrganizer;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.viewmodel.DataQueryActivityViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.*;

public class DataQueryActivity extends AppCompatActivity{
    @BindView(R.id.dataViewerRecyclerView) RecyclerView dataViewerRecycler;
    @BindView(R.id.scrollView) NestedScrollView scrollView;
    @BindView(R.id.dataViewerBarChart) BarChart barChart;
    @BindView(R.id.expandMoreRelativeLayout) RelativeLayout expandMoreLayout;
    @BindView(R.id.viewTypeSpinner) Spinner dataViewTypeSpinner;
    @BindView(R.id.toDatePickerText) TextView endDatePickerTextView;
    @BindView(R.id.fromDatePickerText) TextView startDatePickerTextView;
    @BindView(R.id.fromDateEdit) EditText startDateEditText;
    @BindView(R.id.toDateEdit) EditText endDateEditText;
    @BindView(R.id.spendingTableRadioButton) RadioButton spendingTableRadioButton;
    @BindView(R.id.earningTableRadioButton) RadioButton earningTableRadioButton;
    @BindView(R.id.expandableLayout) ExpandableLayout expandableLayout;

    OrganisedDataViewerAdapter adapter;
    DataProcessor dataProcessor;
    DateManager dateManager;
    DataQueryActivityViewModel viewModel;
    private DatePickerDialog.OnDateSetListener dateSetListener1;
    private DatePickerDialog.OnDateSetListener dateSetListener2;
    private float ROTATION_ANGLE = 0;

    static List<Transaction> transactions;

    String START;
    String END;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_query);
        ButterKnife.bind(this);
        //db = new DatabaseHelper(this);
        viewModel = new ViewModelProvider(this).get(DataQueryActivityViewModel.class);
        transactions = viewModel.getAllTransaction().getValue();

        viewModel.getAllTransaction().observe(this, transactions1 -> {
            transactions = transactions1;

            adapter.setData(viewModel.getMonthlyTransaction(viewModel.getTransactionByTag(transactions,
                    DataRepository.SPENDING_TAG), "22/8/2015", dateManager.getCurrentDate()));

            generateBarChart(viewModel.getMonthlyDataMap(viewModel.getTransactionByTag(transactions, DataRepository.SPENDING_TAG), "01/01/"+
                     dateManager.getYearFromDate(dateManager.getCurrentDate()), dateManager.getCurrentDate()));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        });

        //transactions = viewModel.getAllTransaction().getValue();

        dateManager = new DateManager();

        final Animation rotateIn = AnimationUtils.loadAnimation(this, R.anim.rotate_in);
        final Animation rotateOut = AnimationUtils.loadAnimation(this, R.anim.rotate_out);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        //Disabling edit text from user input;
        endDateEditText.setTag(endDateEditText.getKeyListener());
        endDateEditText.setKeyListener(null);
        startDateEditText.setTag(startDateEditText.getKeyListener());
        startDateEditText.setKeyListener(null);

        initialiseDataViewTypeSpinner();
        DefaultSettingsView();
        radioButtonHandler();
        dateInputHandler();

        dataProcessor = new DataProcessor(this);

        dataViewerRecycler.setHasFixedSize(true);
        dataViewerRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new OrganisedDataViewerAdapter(this);
        dataViewerRecycler.setAdapter(adapter);
        dataViewerRecycler.setNestedScrollingEnabled(false);

        dataViewTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshRecyclerView(startDateEditText.getText().toString(), endDateEditText.getText().toString(), true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        expandMoreLayout.setOnClickListener(v -> {
            expandableLayout.toggle();

            if(ROTATION_ANGLE == 0) {
                ROTATION_ANGLE = 180;
                expandMoreLayout.startAnimation(rotateIn);
            } else {
                ROTATION_ANGLE = 0;
                expandMoreLayout.startAnimation(rotateOut);
            }
        });

    }

    private void DefaultSettingsView() {
        END = dateManager.getCurrentDate();
        endDateEditText.setText(END);
        String[] arr = dateManager.getSeparatedDateArray(END);
        int year = Integer.parseInt(arr[2]);
        year -= 3;
        START = arr[0]+"/"+arr[1]+"/"+year;
        Log.d("start", START);
        startDateEditText.setText(START);
        spendingTableRadioButton.setChecked(true);
        dataViewTypeSpinner.setSelection(1);
    }

    private void dateInputHandler() {
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d = dateManager.getDate(dayOfMonth, month+1, year);
                startDateEditText.setText(d);
                refreshRecyclerView(d,endDateEditText.getText().toString(), false);
            }
        };

        startDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DataQueryActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog, dateSetListener1, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                datePickerDialog.show();
            }
        });

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d = dateManager.getDate(dayOfMonth, month+1, year);
                endDateEditText.setText(d);
                refreshRecyclerView(startDateEditText.getText().toString(), d, false);
            }
        };

        endDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DataQueryActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog, dateSetListener2, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                datePickerDialog.show();
            }
        });
    }

    private void radioButtonHandler() {
        spendingTableRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    earningTableRadioButton.setChecked(false);
                    refreshRecyclerView(startDateEditText.getText().toString(), endDateEditText.getText().toString(), true);
                    resetChartData(dataProcessor.getMonthlyData(viewModel.getTransactionByTag(transactions, DataRepository.SPENDING_TAG), "01/01/"+
                            dateManager.getYearFromDate(dateManager.getCurrentDate()), dateManager.getCurrentDate()), true);
                }
            }
        });

        earningTableRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                spendingTableRadioButton.setChecked(false);
                refreshRecyclerView(startDateEditText.getText().toString(), endDateEditText.getText().toString(), true);
                resetChartData(dataProcessor.getMonthlyData(viewModel.getTransactionByTag(transactions, DataRepository.EARNING_TAG), "01/01/" +
                        dateManager.getYearFromDate(dateManager.getCurrentDate()), dateManager.getCurrentDate()), true);
            }
        });
    }

    private void initialiseDataViewTypeSpinner() {
        String[] array = {"Yearly", "Monthly", "Weekly", "Daily"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        dataViewTypeSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void refreshRecyclerView(String start, String end, boolean flag) {
        if(dateManager.dateDifference(start, end) >= 0) {
            if(earningTableRadioButton.isChecked()) {
                List<DataOrganizer> organizerList = requestDataAccess(viewModel.getTransactionByTag(transactions, DataRepository.EARNING_TAG), start, end);

                adapter.setData(organizerList);

            } else {
                List<DataOrganizer> organizerList = requestDataAccess(viewModel.getTransactionByTag(transactions, DataRepository.SPENDING_TAG), start, end);

                adapter.setData(organizerList);
            }
        }else
            if(flag)
                Toast.makeText(this, "Invalid Date Entry", Toast.LENGTH_SHORT).show();


    }

    private List<DataOrganizer> requestDataAccess(List<Transaction> transactions, String lowerBound, String upperBound) {
        String type = dataViewTypeSpinner.getSelectedItem().toString();

        if(type.equals("Daily"))
            return viewModel.getDailyTransaction(transactions, lowerBound, upperBound);

        else if(type.equals("Weekly"))
            return viewModel.getWeeklyTransaction(transactions, lowerBound, upperBound);

        else if(type.equals("Monthly"))
            return viewModel.getMonthlyTransaction(transactions, lowerBound, upperBound);

        else
            return viewModel.getYearlyTransaction(transactions, lowerBound, upperBound);
    }



    private void generateBarChart(Map<String, List<Transaction>> map) {
        resetChartData(map, false);
        barChart.setPinchZoom(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "DEC", "FEB", "MAR", "APR"};
        xAxis.setValueFormatter(new ValueFormatter(str));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
    }

    private void resetChartData (Map<String, List<Transaction>> map, boolean flag) {
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
        //dataSet.setValueTextSize(8f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        if(flag) {
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
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
