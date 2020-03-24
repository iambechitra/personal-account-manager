package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DataProcessor;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemViewerViewModel extends AndroidViewModel {
    private DataRepository repository;
    private DataProcessor processor;
    private LiveData<List<Transaction>> transactions;
    public CategoryItemViewerViewModel(@NonNull Application application) {
        super(application);

        repository = new DataRepository(application);
        processor = new DataProcessor();
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

}
