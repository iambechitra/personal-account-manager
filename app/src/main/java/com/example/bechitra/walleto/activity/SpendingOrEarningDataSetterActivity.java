package com.example.bechitra.walleto.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.IntentCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.framents.EarningSetterFragment;
import com.example.bechitra.walleto.framents.SpendingSetterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class SpendingOrEarningDataSetterActivity extends AppCompatActivity {
    @BindView(R.id.segmentedButtonGroup)
    SegmentedButtonGroup segmentedButtonGroup;
    SpendingSetterFragment spendingSetterFragment;
    EarningSetterFragment earningSetterFragment;
    FragmentTransaction fragmentTransaction;

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

        segmentedButtonGroup.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                if(position == 0)
                    attachFragment(spendingSetterFragment);
                else
                    attachFragment(earningSetterFragment);
            }
        });
    }

    public void attachFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

}
