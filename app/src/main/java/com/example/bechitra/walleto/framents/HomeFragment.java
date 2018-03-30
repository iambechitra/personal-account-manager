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
import com.example.bechitra.walleto.DatabaseHelper;;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.EarningDialog;
import com.example.bechitra.walleto.dialog.SettingDialog;
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.dialog.listner.ResetListener;
import com.example.bechitra.walleto.table.Spending;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment{
    @BindView(R.id.addSpendingText) TextView addSpendingText;
    @BindView(R.id.addEarningText) TextView addEarningText;
    @BindView(R.id.mainBalance) TextView mainBalance;
    @BindView(R.id.earnBalanceText) TextView earnBalanceText;
    @BindView(R.id.spendBalanceText) TextView spendBalanceText;
    @BindView(R.id.currentDate) TextView currentDate;
    @BindView(R.id.settingText) TextView settingText;
    @BindView(R.id.lastSpending) TextView lastSpending;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        final DatabaseHelper db = new DatabaseHelper(view.getContext());
        String text = db.getBalanceForUser();
        Spending lastSpend = db.getLastInsertedSpending();

        if(lastSpend != null)
            lastSpending.setText("$"+lastSpend.getAmount());

        earnBalanceText.setText("$"+db.getCalculationOfCurrentMonth("EARNING"));
        spendBalanceText.setText("$"+db.getCalculationOfCurrentMonth("SPENDING"));
        currentDate.setText(new StringPatternCreator().getCurrentDateString());

        settingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingDialog dialog = new SettingDialog();
                dialog.show(getFragmentManager(), "OK");
                dialog.setResetListener(new ResetListener() {
                    @Override
                    public void onResetData(boolean action) {
                        db.resetEveryThing();
                        reloadFragment();
                    }
                });
            }
        });



        if(text != null)
            mainBalance.setText("$"+text);
        addSpendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = new Intent(v.getContext(), AddSpending.class);
                startActivity(intent);
            }
        });

        addEarningText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EarningDialog dialog = new EarningDialog();
                dialog.show(getFragmentManager(), "TAG");
                dialog.setOnCloseDialogListener(new OnCloseDialogListener() {
                    @Override
                    public void onClose(boolean flag) {
                        if(flag)
                            reloadFragment();
                    }
                });
            }
        });

        return view;
    }

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
