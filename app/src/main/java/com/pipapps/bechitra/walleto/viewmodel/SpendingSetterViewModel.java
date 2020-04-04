package com.pipapps.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.pipapps.bechitra.walleto.DataRepository;
import com.pipapps.bechitra.walleto.room.entity.Schedule;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.room.entity.Wallet;

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
