package com.pipapps.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.pipapps.bechitra.walleto.DataRepository;
import com.pipapps.bechitra.walleto.room.entity.Schedule;

public class ScheduleManagementActivityViewModel extends AndroidViewModel {
    DataRepository repository;
    public ScheduleManagementActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public void updateSchedule(Schedule schedule) { repository.updateSchedule(schedule); }
    public void deleteSchedule(Schedule schedule) { repository.deleteSchedule(schedule); }
}
