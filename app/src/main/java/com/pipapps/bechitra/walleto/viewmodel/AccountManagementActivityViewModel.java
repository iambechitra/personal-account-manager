package com.pipapps.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pipapps.bechitra.walleto.DataRepository;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.room.entity.Wallet;
import com.pipapps.bechitra.walleto.utility.DataProcessor;


import java.util.ArrayList;
import java.util.List;

public class AccountManagementActivityViewModel extends AndroidViewModel {
    private DataRepository repository;
    private LiveData<List<Wallet>> walletList;
    private DataProcessor processor;
    public AccountManagementActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        this.processor = new DataProcessor();
        walletList = repository.getAllWallet();
    }

    public LiveData<List<Wallet>> getWalletList() {
        return walletList;
    }

    public double getBalanceByTaggedTransaction(List<Transaction> transactions, String tag) {
        return processor.getAmountByTag(transactions, tag);
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

    public List<Transaction> getAllTransaction() {
        return repository.getListTransaction();
    }

    public void updateWallet(Wallet wallet) { repository.updateWallet(wallet); }
}
