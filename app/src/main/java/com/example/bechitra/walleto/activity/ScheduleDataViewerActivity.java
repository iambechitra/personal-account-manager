package com.example.bechitra.walleto.activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bechitra.walleto.adapter.ScheduleViewAdapter;
import com.example.bechitra.walleto.databinding.ActivityScheduleDataViewerBinding;
import com.example.bechitra.walleto.viewmodel.ScheduleDataViewerViewModel;

public class ScheduleDataViewerActivity extends AppCompatActivity {
    private ScheduleViewAdapter adapter;
    ActivityScheduleDataViewerBinding viewBind;
    ScheduleDataViewerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityScheduleDataViewerBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());

        viewModel = new ViewModelProvider(this).get(ScheduleDataViewerViewModel.class);
        viewModel.getAllSchedule().observe(this, schedules -> {
            adapter.setData(schedules);
        });

        viewBind.recyclerView.setHasFixedSize(true);
        viewBind.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new ScheduleViewAdapter(this);
        viewBind.recyclerView.setAdapter(adapter);

        viewBind.backButton.setOnClickListener(view -> finish());
    }
}
