package com.pipapps.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pipapps.bechitra.walleto.DataRepository;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.utility.DataProcessor;

import java.util.ArrayList;
import java.util.List;

public class CategorisedDataViewerViewModel extends AndroidViewModel {
    private DataRepository repository;
    LiveData<List<Transaction>> listLiveData;
    DataProcessor processor;

    public CategorisedDataViewerViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        listLiveData = repository.getTransactionOfActivatedWallet();
        processor = new DataProcessor();
    }

    public LiveData<List<Transaction>> getAllTransaction() {
        return listLiveData;
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
