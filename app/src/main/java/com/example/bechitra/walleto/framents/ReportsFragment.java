package com.example.bechitra.walleto.framents;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bechitra.walleto.R;

/**
 * Created by bechitra on 3/26/2018.
 */

public class ReportsFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, null);
        return view;
    }
}
