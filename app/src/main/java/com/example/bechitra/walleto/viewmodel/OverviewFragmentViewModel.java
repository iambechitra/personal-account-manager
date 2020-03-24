package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.utility.MapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
