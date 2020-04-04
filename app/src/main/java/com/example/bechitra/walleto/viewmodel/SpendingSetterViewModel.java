package com.example.bechitra.walleto.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;

public class SpendingSetterViewModel extends AndroidViewModel {
    private DataRepository repository;
    public SpendingSetterViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public long insertTransaction(Transaction transaction) {
        return repository.insertTransaction(transaction);
    }

    public void insertSchedule(Schedule schedule) {
        repository.insertSchedule(schedule);
    }

    public Wallet getActiveWallet() { return repository.getActiveWallet(); }

    public void updateWallet(Wallet wallet) { repository.updateWallet(wallet); }

    public long getActivatedWalletID() {
        return repository.getActiveWalletID();
    }
}
