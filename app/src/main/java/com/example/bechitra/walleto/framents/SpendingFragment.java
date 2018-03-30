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
import com.example.bechitra.walleto.adapter.SpendingRecyclerAdapter;
import com.example.bechitra.walleto.table.Spending;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/26/2018.
 */

public class SpendingFragment extends Fragment{

    @BindView(R.id.spendingOrEarningRecyclerView)
    RecyclerView spendingRecyclerView;

    @BindView(R.id.spendingOrEarningFilterSwitch)
    Switch filterByCurrentMonth;

    SpendingRecyclerAdapter adapter;
    List<Spending> spendingList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_earning, null);
        ButterKnife.bind(this, view);
        final DatabaseHelper db = new DatabaseHelper(view.getContext());

        spendingList = db.getAllSpending();
        spendingRecyclerView.setHasFixedSize(true);
        spendingRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new SpendingRecyclerAdapter(spendingList, view.getContext());
        spendingRecyclerView.setAdapter(adapter);

        filterByCurrentMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    spendingList.clear();
                    spendingList = db.getCurrentMonthSpending();
                    adapter.setData(spendingList);
                    adapter.notifyDataSetChanged();

                } else {
                    spendingList.clear();
                    spendingList = db.getAllSpending();
                    adapter.setData(spendingList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
