package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;


import java.util.ArrayList;
import java.util.List;

public class AccountManagementActivityViewModel extends AndroidViewModel {
    private DataRepository repository;
    private LiveData<List<Wallet>> walletList;
    public AccountManagementActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);

        walletList = repository.getAllWallet();
    }

    public LiveData<List<Wallet>> getWalletList() {
        return walletList;
    }

    public void insertNewWallet(Wallet wallet) { repository.insertWallet(wallet); }

    public List<Wallet> getInactiveWallet(List<Wallet> walletList) {
        List<Wallet> wallets = new ArrayList<>();

        for (Wallet wallet : walletList)
            if (!wallet.isActive())
                wallets.add(wallet);

        return wallets;
    }

    public void deleteWallet(Wallet wallet) { repository.deleteWallet(wallet);}

    public Wallet getActiveWallet(List<Wallet> walletList) {
        for (Wallet wallet : walletList)
            if (wallet.isActive())
                return wallet;

        return null;
    }

    public void deleteTransactionByID(long walletID) {
        repository.deleteTransactionByID(walletID);
    }

    public LiveData<List<Transaction>> getAllTransaction() {
        return repository.getTransactionOfActivatedWallet();
    }

    public void updateWallet(Wallet wallet) { repository.updateWallet(wallet); }
}
