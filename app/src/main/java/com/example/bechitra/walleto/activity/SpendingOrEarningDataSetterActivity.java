package com.example.bechitra.walleto.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.databinding.ActivityDataSetterBinding;
import com.example.bechitra.walleto.framents.EarningSetterFragment;
import com.example.bechitra.walleto.framents.SpendingSetterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

;

public class SpendingOrEarningDataSetterActivity extends AppCompatActivity {
    SpendingSetterFragment spendingSetterFragment;
    EarningSetterFragment earningSetterFragment;
    FragmentTransaction fragmentTransaction;
    ActivityDataSetterBinding viewBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityDataSetterBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());

        spendingSetterFragment = new SpendingSetterFragment();
        earningSetterFragment = new EarningSetterFragment();
        earningSetterFragment.setArguments(savedInstanceState);
        spendingSetterFragment.setArguments(savedInstanceState);

        attachFragment(spendingSetterFragment);

        viewBind.spendingButton.setOnClickListener(view -> {
            attachFragment(spendingSetterFragment);
        });

        viewBind.earningButton.setOnClickListener(view -> {
            attachFragment(earningSetterFragment);
        });
    }

    public void attachFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

}
