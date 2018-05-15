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

import com.crashlytics.android.Crashlytics;
import com.example.bechitra.walleto.adapter.ViewPagerAdapter;
import com.example.bechitra.walleto.dialog.EarningDialog;
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.framents.EarningOverviewFragment;
import com.example.bechitra.walleto.framents.HomeFragment;
import com.example.bechitra.walleto.framents.SpendingOverviewFragment;
import com.example.bechitra.walleto.framents.ReportsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.rootFloatingButton) FloatingActionButton rootFloatingButton;
    @BindView(R.id.mainActivityLayout)
    RelativeLayout relativeLayout;

    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onSetFragment(viewPager);
        //DatabaseHelper db = new DatabaseHelper(this);
        //db.resetEveryThing();
        tabLayout.setupWithViewPager(viewPager);

        rootFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(v.getContext(), SpendingOrEarningDataSetter.class);
                startActivity(intent);
            }
        });

    }


    public void onSetFragment(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragment(new HomeFragment(), "Home");
        adapter.setFragment(new SpendingOverviewFragment(), "Overview");
        //adapter.setFragment(new EarningOverviewFragment(), "Earning");
     //   adapter.setFragment(new ReportsFragment(), "Overview");
        viewPager.setAdapter(adapter);

    }

    private void reloadActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}