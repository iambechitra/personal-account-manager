package com.example.bechitra.walleto.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;

public class EarningSetterViewModel extends AndroidViewModel {
    private DataRepository repository;
    public EarningSetterViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public void insertTransaction(Transaction transaction) {
        repository.insertTransaction(transaction);
    }

    public void insertSchedule(Schedule schedule) {
        repository.insertSchedule(schedule);
    }

    public long getActivatedWalletID() {
        return repository.getActiveWalletID();
    }
}
