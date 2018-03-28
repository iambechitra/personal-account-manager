package com.example.bechitra.walleto.framents;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bechitra.walleto.AddSpending;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.EarningDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/27/2018.
 */

public class HomeFragment extends Fragment{
    @BindView(R.id.addSpendingText) TextView addSpendingText;
    @BindView(R.id.addEarningText) TextView addEarningText;
    @BindView(R.id.mainBalance) TextView mainBalance;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home, null);
        ButterKnife.bind(this, view);
        DatabaseHelper db = new DatabaseHelper(view.getContext());
        String text = db.getBalanceForUser();
        if(text != null)
            mainBalance.setText(text);
        addSpendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddSpending.class);
                startActivity(intent);
            }
        });

        addEarningText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EarningDialog dialog = new EarningDialog();
                dialog.show(getFragmentManager(), "TAG");
            }
        });

        return view;
    }
}
