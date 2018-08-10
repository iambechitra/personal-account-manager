package com.example.bechitra.walleto.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.AutoRepetitionDataViewerAdapter;
import com.example.bechitra.walleto.dialog.listner.OnDeleteItem;
import com.example.bechitra.walleto.table.Schedule;
import com.example.bechitra.walleto.table.TableData;
import com.example.bechitra.walleto.utility.ScheduleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleDataViewerActivity extends AppCompatActivity {
    AutoRepetitionDataViewerAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_data_viewer);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AutoRepetitionDataViewerAdapter(this, filteredScheduler());

        adapter.setOnDeleteItemListener(new OnDeleteItem() {
            @Override
            public void onDelete(String TAG, int flag) {
                db.deleteRowFromTable("SCHEDULE", TAG);
                adapter.setData(filteredScheduler());
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private List<ScheduleData> filteredScheduler() {
        List<Schedule> list = db.getScheduledData();
        String currentActiveWallet = db.getActivatedWalletID();
        List<ScheduleData> data = new ArrayList<>();

        for(Schedule s : list) {
            TableData tData = db.getDataFromRow(s.getItemID(), s.getTableName());
            if(tData.getWalletID().equals(currentActiveWallet))
                data.add(new ScheduleData(s.getID(), tData.getCategory(), tData.getAmount(), s.getTableName(), s.getTime(), s.getRepeat()));

        }

        return data;
    }
}
