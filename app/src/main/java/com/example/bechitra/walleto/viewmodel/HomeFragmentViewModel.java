package com.example.bechitra.walleto.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.utility.EntrySet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {
    private LiveData<List<Transaction>> transactionData;
    private DataRepository repository;
    private LiveData<List<Wallet>> walletData;
    private DataProcessor processor;
    private DateManager manager;
    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        transactionData = repository.getTransactionOfActivatedWallet();
        walletData = repository.getAllWallet();
        processor = new DataProcessor();
        manager = new DateManager();
    }

    public LiveData<List<Transaction>> getTransactionData() { return transactionData; }
    public LiveData<List<Wallet>> getWalletData() { return walletData; }
    public Wallet getActiveWallet() { return repository.getActiveWallet(); }
    public List<Transaction> getTransactionByTag(List<Transaction> transactions, String tag) {
        return processor.getTransactionsByTag(transactions, tag);
    }

    public List<EntrySet> getPieChartData(List<Transaction> data) {
        List<EntrySet> graphData = new ArrayList<>();
        String[]date = manager.getSeparatedDateArray(manager.getCurrentDate());

        String lowerBound = "01/"+date[1]+"/"+date[2];
        String upperBound = manager.getCurrentDate();
        List<Transaction> row = processor.getTransactionsByRange(processor.getTransactionsByTag(data, DataRepository.SPENDING_TAG), lowerBound, upperBound);
        double totalTransaction = 0;

        List<String> contentStore = new ArrayList<>();

        if(row != null) {
            for (Transaction t : row) {
                if (graphData.contains(t.getCategory())) {
                    Log.d("tape", t.getCategory());
                    int categoryIndex = graphData.indexOf(t.getCategory());
                    Log.d("index", ""+categoryIndex);
                    graphData.get(categoryIndex).setValue(graphData.get(categoryIndex).getValue() + t.getAmount());
                } else {
                    graphData.add(new EntrySet(t.getCategory(), t.getAmount()));
                }

                totalTransaction += t.getAmount();
            }

            for(EntrySet entrySet : graphData) {
                int entryIndex = graphData.indexOf(entrySet);
                double transactionRatio = graphData.get(entryIndex).getValue()/totalTransaction;
                graphData.get(entryIndex).setValue(transactionRatio * 100);
            }

            Collections.sort(graphData,(object1, object2) -> compareValue(object1.getValue(), object2.getValue()));
        }
        else
            graphData.add(new EntrySet("Spending", 100.0));


        return graphData;
    }

    private int compareValue(double value, double value1) {
        if(value > value1)
            return 1;
        else if(value == value1)
            return 0;
        else
            return -1;
    }
}
