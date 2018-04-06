package com.example.bechitra.walleto;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.bechitra.walleto.adapter.ViewPagerAdapter;
import com.example.bechitra.walleto.dialog.EarningDialog;
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.framents.EarningFragment;
import com.example.bechitra.walleto.framents.HomeFragment;
import com.example.bechitra.walleto.framents.SpendingFragment;
import com.example.bechitra.walleto.framents.ReportsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.earningFloatingButton)
    LinearLayout earningFab;
    @BindView(R.id.spendingFloatingButton)
    LinearLayout spendingFab;
    @BindView(R.id.rootFloatingButton) FloatingActionButton rootFloatingButton;
    @BindView(R.id.mainActivityLayout)
    RelativeLayout relativeLayout;

    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onSetFragment(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        rootFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(earningFab.getVisibility() == earningFab.VISIBLE && spendingFab.getVisibility() == spendingFab.VISIBLE) {
                    earningFab.setVisibility(earningFab.GONE);
                    spendingFab.setVisibility(spendingFab.GONE);
                } else {
                    earningFab.setVisibility(earningFab.VISIBLE);
                    spendingFab.setVisibility(spendingFab.VISIBLE);
                }
            }
        });

        setClickableButton();

    }

    public void setClickableButton() {
        spendingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(v.getContext(), AddSpending.class);
                startActivity(intent);
            }
        });

        earningFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EarningDialog dialog = new EarningDialog();
                dialog.show(getSupportFragmentManager(), "TAG");
                dialog.setOnCloseDialogListener(new OnCloseDialogListener() {
                    @Override
                    public void onClose(boolean flag) {
                        reloadActivity();
                    }
                });
            }
        });
    }

    public void onSetFragment(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragment(new HomeFragment(), "Home");
        adapter.setFragment(new SpendingFragment(), "Spending");
        adapter.setFragment(new EarningFragment(), "Earning");
        adapter.setFragment(new ReportsFragment(), "Report");
        viewPager.setAdapter(adapter);

    }

    private void reloadActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}