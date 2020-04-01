package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CategoryItemViewerViewModel extends AndroidViewModel {
    private DataRepository repository;
    private DataProcessor processor;
    private DateManager dateManager;
    private LiveData<List<Transaction>> transactions;
    public CategoryItemViewerViewModel(@NonNull Application application) {
        super(application);

        repository = new DataRepository(application);
        processor = new DataProcessor();
        dateManager = new DateManager();
        transactions = repository.getTransactionOfActivatedWallet();
    }

    public LiveData<List<Transaction>> getAllTransaction() {
        return transactions;
    }

    public List<Transaction> getCategorisedTransactionWithinBound(List<Transaction> transactions,String category, String lowerBound, String upperBound) {
        if(transactions.isEmpty())
            return null;
        else {
            List<Transaction> transaction = new ArrayList<>();

            for (Transaction t : processor.getTransactionsByRange(transactions, lowerBound, upperBound))
                if (t.getCategory().equals(category))
                    transaction.add(t);

            return transaction;
        }
    }

    public List<Transaction> getTransactionByTag(List<Transaction> transactions, String tag) {
        return processor.getTransactionsByTag(transactions, tag);
    }

    public Map<String, List<Transaction>> getMonthlyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        return processor.getMonthlyData(transactions, lowerBound, upperBound);
    }

    public BarDataSet getGraphData(List<Transaction> transactions, String category, String date) {
        Map<String, List<Transaction>> map = processor.getMonthlyData(transactions,
                                            "01/01/"+dateManager.getYearFromDate(date), date);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        Map<Integer, Float> balance = new HashMap<>();
        int[] monthCount = new int[13];
        for(Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            float amount = Float.parseFloat(processor.getBalanceCalculation(entry.getValue()));
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

        return dataSet;
    }

}
