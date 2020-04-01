package com.example.bechitra.walleto.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.framents.EarningSetterFragment;
import com.example.bechitra.walleto.framents.SpendingSetterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

;

public class SpendingOrEarningDataSetterActivity extends AppCompatActivity {
    SpendingSetterFragment spendingSetterFragment;
    EarningSetterFragment earningSetterFragment;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.spendingButton) Button spendingButton;

    @BindView(R.id.earningButton) Button earningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_setter);
        ButterKnife.bind(this);

        spendingSetterFragment = new SpendingSetterFragment();
        earningSetterFragment = new EarningSetterFragment();
        earningSetterFragment.setArguments(savedInstanceState);
        spendingSetterFragment.setArguments(savedInstanceState);

        attachFragment(spendingSetterFragment);

        spendingButton.setOnClickListener(view -> {
            attachFragment(spendingSetterFragment);
        });

        earningButton.setOnClickListener(view -> {
            attachFragment(earningSetterFragment);
        });
    }

    public void attachFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

}
