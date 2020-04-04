package com.pipapps.bechitra.walleto.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.databinding.ActivityDataSetterBinding;
import com.pipapps.bechitra.walleto.framents.EarningSetterFragment;
import com.pipapps.bechitra.walleto.framents.SpendingSetterFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



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
            ViewCompat.setBackgroundTintList(
                    viewBind.spendingButton,
                    ColorStateList.valueOf(getResources().getColor(R.color.green)));
            viewBind.spendingButton.setTextColor(getResources().getColor(R.color.white));

            ViewCompat.setBackgroundTintList(
                    viewBind.earningButton,
                    ColorStateList.valueOf(getResources().getColor(R.color.white)));
            viewBind.earningButton.setTextColor(getResources().getColor(R.color.black));
        });

        viewBind.earningButton.setOnClickListener(view -> {
            attachFragment(earningSetterFragment);
            ViewCompat.setBackgroundTintList(
                    viewBind.earningButton,
                    ColorStateList.valueOf(getResources().getColor(R.color.green)));
            viewBind.earningButton.setTextColor(getResources().getColor(R.color.white));


            ViewCompat.setBackgroundTintList(
                    viewBind.spendingButton,
                    ColorStateList.valueOf(getResources().getColor(R.color.white)));
            viewBind.spendingButton.setTextColor(getResources().getColor(R.color.black));
        });
    }

    public void attachFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

}
