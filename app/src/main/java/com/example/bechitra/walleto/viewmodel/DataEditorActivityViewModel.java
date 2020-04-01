package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DataProcessor;

import java.util.List;

public class DataEditorActivityViewModel extends AndroidViewModel {
    DataRepository repository;
    DataProcessor processor;
    public DataEditorActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public void updateTransaction(Transaction transaction) {
        repository.updateTransaction(transaction);
    }

    public void deleteTransaction(Transaction transaction) {
        repository.deleteTransaction(transaction);
    }

    public Schedule getScheduleByTransaction(long transactionID) {
        return repository.getScheduleByTransaction(transactionID);
    }

    public void deleteSchedule(Schedule schedule) {
        repository.deleteSchedule(schedule);
    }

    public void insertSchedule(Schedule schedule) {
        repository.insertSchedule(schedule);
    }

    public LiveData<List<Schedule>> getAllSchedule() {
        return repository.getScheduleOfActivatedWallet();
    }

    public void updateSchedule(Schedule schedule) {
        repository.updateSchedule(schedule);
    }
}
