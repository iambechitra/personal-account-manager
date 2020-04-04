package com.example.bechitra.walleto;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.room.DatabaseRoom;
import com.example.bechitra.walleto.room.dao.ScheduleDao;
import com.example.bechitra.walleto.room.dao.TransactionDao;
import com.example.bechitra.walleto.room.dao.WalletDao;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;

import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    public final static String EARNING_TAG = "earning";
    public final static String SPENDING_TAG = "spending";

    private TransactionDao transactionDao;
    private ScheduleDao scheduleDao;
    private WalletDao walletDao;

    private long activeWalletID;
    private LiveData<List<Transaction>> transactionOfActivatedWallet;
    private LiveData<List<Schedule>> scheduleOfActivatedWallet;
    private LiveData<List<Wallet>> allWallet;

    public DataRepository(Application application) {
        DatabaseRoom db = DatabaseRoom.getInstance(application);

        transactionDao = db.transactionDao();
        scheduleDao = db.scheduleDao();
        walletDao =db.walletDao();

        activeWalletID = walletDao.getActivatedWalletID();
        allWallet = walletDao.getAllWallet();
        transactionOfActivatedWallet = transactionDao.getAllTransactionOfActiveWallet(activeWalletID);
        scheduleOfActivatedWallet = scheduleDao.getAllScheduleFromActiveWallet(activeWalletID);
    }

    public LiveData<List<Transaction>> getTransactionOfActivatedWallet() {
        return transactionOfActivatedWallet;
    }

    public LiveData<List<Wallet>> getAllWallet() {
        return allWallet;
    }

    public LiveData<List<Schedule>> getScheduleOfActivatedWallet() {
        return scheduleOfActivatedWallet;
    }

    List<Schedule> getAllScheduleList() { return scheduleDao.getAllScheduleList(); }

    public long insertTransaction(Transaction transaction) {
            return transactionDao.insert(transaction);
    }

    public void insertSchedule(Schedule schedule) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> {
            scheduleDao.insert(schedule);
        });
    }

    public List<Transaction> getListTransaction() {
        return transactionDao.getListOfTransactionOfActiveWallet(walletDao.getActivatedWalletID());
    }

    public void insertWallet(Wallet wallet) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> {
            walletDao.insert(wallet);
        });
    }

    public void updateWallet(Wallet wallet) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> {
            walletDao.update(wallet);
        });
    }

    public void updateSchedule(Schedule schedule) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> {
            scheduleDao.update(schedule);
        });
    }

    public void deleteTransactionByID(long walletID) { transactionDao.deleteTransactionByID(walletID); }

    public void updateTransaction(Transaction transaction) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> {
            transactionDao.update(transaction);
        });
    }

    public void deleteTransaction(Transaction transaction) {
        transactionDao.delete(transaction);
    }

    public void deleteSchedule(Schedule schedule) {
        scheduleDao.delete(schedule);
    }

    public Schedule getScheduleByTransaction(long transactionID) {
        return scheduleDao.getScheduleByTransaction(transactionID);
    }

    public void deleteWallet(Wallet wallet) {
        walletDao.delete(wallet);
    }

    public long getActiveWalletID() {
        return activeWalletID;
    }

    public List<String> getDistinctCategory (String tag){
        List<String> list = new ArrayList<>();

        for(Transaction transaction : transactionOfActivatedWallet.getValue()) {
            if(transaction.getTag() == tag && transaction.getWalletID() == activeWalletID)
                if(!list.contains(transaction.getCategory()))
                    list.add(transaction.getCategory());
        }

        return list;
    }

    public LiveData<List<Transaction>> getTransactionWithinRange(String lowerBound, String upperBound) {
        return transactionDao.getTransactionWithinRange(lowerBound, upperBound, activeWalletID);
    }

    public LiveData<Wallet> getActiveWalletData() { return walletDao.getActiveWallet(); }

    public Wallet getActiveWallet() {
        return walletDao.getActivedWallet();
    }

    public void resetAllData() {
        transactionDao.reset();
        scheduleDao.reset();
        walletDao.resetWallet();
        walletDao.insert(new Wallet("PRIMARY", 0, true));
    }
}
