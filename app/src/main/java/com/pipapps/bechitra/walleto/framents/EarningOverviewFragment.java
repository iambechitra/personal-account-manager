package com.pipapps.bechitra.walleto.framents;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pipapps.bechitra.walleto.R;

import butterknife.ButterKnife;

public class EarningOverviewFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_earning, null);
        ButterKnife.bind(this, view);
        return view;
    }

}
