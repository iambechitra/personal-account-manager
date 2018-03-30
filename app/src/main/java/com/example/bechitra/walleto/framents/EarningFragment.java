package com.example.bechitra.walleto.framents;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.EarningRecyclerAdapter;
import com.example.bechitra.walleto.table.Earning;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningFragment extends Fragment{
    List<Earning> earningList;
    @BindView(R.id.spendingOrEarningRecyclerView)
    RecyclerView earningRecyclerView;
    EarningRecyclerAdapter adapter;

    @BindView(R.id.spendingOrEarningFilterSwitch)
    Switch filterByCurrentMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_earning, null);
        ButterKnife.bind(this, view);
        final DatabaseHelper db = new DatabaseHelper(view.getContext());

        earningList = db.getAllEarning();
        earningRecyclerView.setHasFixedSize(true);
        earningRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new EarningRecyclerAdapter(view.getContext(), earningList);
        earningRecyclerView.setAdapter(adapter);

        filterByCurrentMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    earningList.clear();
                    earningList = db.getCurrentMonthEarning();
                    adapter.setData(earningList);
                    adapter.notifyDataSetChanged();

                } else {
                    earningList.clear();
                    earningList = db.getAllEarning();
                    adapter.setData(earningList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
