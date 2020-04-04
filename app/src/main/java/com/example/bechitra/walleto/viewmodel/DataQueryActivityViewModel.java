package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DataOrganizer;
import com.example.bechitra.walleto.utility.DataProcessor;

import java.util.List;
import java.util.Map;

public class DataQueryActivityViewModel extends AndroidViewModel {
    private DataRepository repository;
    private DataProcessor processor;
    public DataQueryActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new DataRepository(application);
        processor = new DataProcessor();
    }

    public LiveData<List<Transaction>> getAllTransaction() { return repository.getTransactionOfActivatedWallet(); }

    public List<DataOrganizer> getYearlyTransaction(List<Transaction> transactions, String lowerBound, String upperBound) {
        return transactions.isEmpty() ? null :processor.getProcessedYearlyData(transactions, lowerBound, upperBound);
    }

    public List<DataOrganizer> getMonthlyTransaction(List<Transaction> transactions, String lowerBound, String upperBound) {
        return transactions.isEmpty() ? null :processor.getProcessedMonthlyData(transactions, lowerBound, upperBound);
    }

    public List<DataOrganizer> getWeeklyTransaction(List<Transaction> transactions, String lowerBound, String upperBound) {
        return transactions.isEmpty() ? null :processor.getProcessedWeeklyData(transactions, lowerBound, upperBound);
    }

    public List<DataOrganizer> getDailyTransaction(List<Transaction> transactions, String lowerBound, String upperBound) {
        return transactions.isEmpty() ? null : processor.getProcessedDailyData(transactions, lowerBound, upperBound);
    }

    public List<Transaction> getTransactionByTag(List<Transaction> transactions, String tag) { return processor.getTransactionsByTag(transactions, tag); }

    public Map<String, List<Transaction>> getMonthlyDataMap(List<Transaction> transactions, String lowerBound, String upperBound) {
        return transactions.isEmpty() ? null : processor.getMonthlyData(transactions, lowerBound, upperBound);
    }

    public double getBalanceCalculation(List<Transaction> value) {
        return processor.getBalanceOfTransaction(value);
    }

    public Map<String, List<Transaction>> getMonthlyData(List<Transaction> transactionByTag, String lowerBound, String upperBound) {
        return processor.getMonthlyData(transactionByTag, lowerBound, upperBound);
    }
}