package com.example.bechitra.walleto.framents;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;

import butterknife.ButterKnife;

public class EarningOverviewFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_earning, null);
        ButterKnife.bind(this, view);
        DatabaseHelper db = new DatabaseHelper(view.getContext());


        return view;
    }

}
