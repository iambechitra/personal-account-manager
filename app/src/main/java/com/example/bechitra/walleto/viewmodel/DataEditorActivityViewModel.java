package com.example.bechitra.walleto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.bechitra.walleto.DataRepository;

public class DataEditorActivityViewModel extends AndroidViewModel {
    DataRepository repository;
    public DataEditorActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);

        
    }
}
