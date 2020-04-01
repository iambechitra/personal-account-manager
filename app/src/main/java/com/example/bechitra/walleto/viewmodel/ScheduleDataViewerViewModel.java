package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.room.entity.Schedule;

import java.util.List;

public class ScheduleDataViewerViewModel extends AndroidViewModel {
    DataRepository repository;
    public ScheduleDataViewerViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public LiveData<List<Schedule>> getAllSchedule() {
        return repository.getScheduleOfActivatedWallet();
    }
}
