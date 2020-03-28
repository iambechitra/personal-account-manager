package com.example.bechitra.walleto.viewmodel;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.utility.MapHelper;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class OverviewFragmentViewModel extends AndroidViewModel {
    private DataRepository repository;
    private DataProcessor dataProcessor;
    private DateManager dateManager;
    public OverviewFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        dataProcessor = new DataProcessor();
        dateManager = new DateManager();
    }

    public LiveData<List<Transaction>> getAllTransactionData() { return repository.getTransactionOfActivatedWallet(); }

    public double getLifeTimeCalculationByTag(List<Transaction> transactions, String tag) {
        return dataProcessor.getAmountByTag(transactions, tag);
    }

    public List<CategoryProcessor> getRecyclerData(List<Transaction> transactions) {
        String upperBound = dateManager.getCurrentDate();
        String year = dateManager.getYearFromDate(upperBound);
        String lowerBound = "01/01/" + year;

        Map<String, List<Transaction>> mapEarning = dataProcessor.getYearlyTransaction(
                dataProcessor.getTransactionsByTag(transactions, DataRepository.EARNING_TAG), lowerBound, upperBound);
        Map<String, List<Transaction>> mapSpending = dataProcessor.getYearlyTransaction(
                dataProcessor.getTransactionsByTag(transactions, DataRepository.SPENDING_TAG), lowerBound, upperBound);
        List<CategoryProcessor> list = new ArrayList<>();

        for (Map.Entry<String, List<Transaction>> entry : mapEarning.entrySet()) {
            List<MapHelper> map = dataProcessor.getCategorisedData(entry.getValue());

            for (MapHelper m : map)
                list.add(new CategoryProcessor(m, repository.EARNING_TAG));
        }

        for (Map.Entry<String, List<Transaction>> entry : mapSpending.entrySet()) {
            List<MapHelper> map = dataProcessor.getCategorisedData(entry.getValue());
            for (MapHelper m : map)
                list.add(new CategoryProcessor(m, repository.SPENDING_TAG));
        }

        return list;
    }

    public ArrayList<ILineDataSet> getLineChartData(List<Transaction> transaction) {
        ArrayList<Entry> earningEntry = new ArrayList<>();
        ArrayList<Entry> spendingEntry = new ArrayList<>();
        String upperBound = dateManager.getCurrentDate();
        String year = dateManager.getYearFromDate(upperBound);
        String lowerBound = "01/01/"+ year;

        Map<String, List<Transaction>> mapEarning = dataProcessor.getMonthlyData(dataProcessor
                .getTransactionsByTag(transaction, DataRepository.EARNING_TAG), lowerBound, upperBound);
        Map<String, List<Transaction>> mapSpending = dataProcessor.getMonthlyData(dataProcessor
                .getTransactionsByTag(transaction, DataRepository.SPENDING_TAG), lowerBound, upperBound);

        Map<Integer, Float> balance = new HashMap<>();
        int[] monthCount = new int[13];
        for(Map.Entry<String, List<Transaction>> entry : mapSpending.entrySet()) {
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
        for(Map.Entry<String, List<Transaction>> entry : mapEarning.entrySet()) {
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

        return data;
    }
}
