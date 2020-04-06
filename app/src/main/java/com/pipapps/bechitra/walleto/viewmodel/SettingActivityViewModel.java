package com.pipapps.bechitra.walleto.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.pipapps.bechitra.walleto.DataRepository;

public class SettingActivityViewModel extends AndroidViewModel {
    private DataRepository repository;
    public SettingActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public void reset() { repository.resetAllData(); }
}
