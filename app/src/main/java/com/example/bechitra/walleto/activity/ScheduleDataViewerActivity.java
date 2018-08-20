package com.example.bechitra.walleto.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.ScheduleViewAdapter;
import com.example.bechitra.walleto.dialog.listener.OnDeleteItem;
import com.example.bechitra.walleto.dialog.listener.OnSaveInstanceState;
import com.example.bechitra.walleto.table.Schedule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleDataViewerActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.backButton) TextView backButton;

    private ScheduleViewAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_data_viewer);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new ScheduleViewAdapter(this, filteredScheduler());
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<Schedule> filteredScheduler() {
        List<Schedule> list = db.getScheduledData();
        String currentActiveWallet = db.getActivatedWalletID();
        List<Schedule> adapterData = new ArrayList<>();

        for(Schedule s : list)
            if(s.getWalletID().equals(currentActiveWallet))
                adapterData.add(s);

        return adapterData;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setData(filteredScheduler());
        adapter.notifyDataSetChanged();
    }
}
