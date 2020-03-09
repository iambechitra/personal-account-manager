package com.example.bechitra.walleto;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.activity.*;
import com.example.bechitra.walleto.framents.HomeFragment;
import com.example.bechitra.walleto.framents.SpendingOverviewFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.rootFloatingButton)
    FloatingActionButton rootFloatingButton;
    @BindView(R.id.dashboard)
    TextView dashboardTextView;
    @BindView(R.id.dashboardBack) RelativeLayout dashboardBack;
    @BindView(R.id.overviewBack) RelativeLayout overviewBack;

    @BindView(R.id.navigationDrawerIconText) TextView navDrawerIconText;

    @BindView(R.id.overview) TextView overviewTextView;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onSetAlarmManager(1, 1, this);

        getBaseContext().getTheme().applyStyle(R.style.Theme_AppCompat_Light, true);
        attachFragment(new HomeFragment());
        //dashboardBack.setBackgroundColor(view.getResources().getColor(R.color.tab_selected));

        rootFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SpendingOrEarningDataSetterActivity.class);
                startActivity(intent);
            }
        });

        dashboardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFragment(new HomeFragment());
                dashboardBack.setBackgroundResource(R.drawable.ic_background_custom);
                overviewBack.setBackgroundResource(R.color.green);
            }
        });

        overviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFragment(new SpendingOverviewFragment());
                overviewBack.setBackgroundResource(R.drawable.ic_background_custom);
                dashboardBack.setBackgroundResource(R.color.green);
            }
        });

        navDrawerIconText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manageAccount: {
                        startActivity(AccountManagementActivity.class);
                        break;
                    }
                    case R.id.scheduleData: {
                        startActivity(ScheduleDataViewerActivity.class);
                        break;
                    }

                    case R.id.dataQuery : {
                        startActivity(DataQueryActivity.class);
                        break;
                    }

                    case R.id.settingsMenu : {
                        startActivity(SettingsActivity.class);
                        break;
                    }
                }

                return true;
            }
        });

    }

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }


    public void attachFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void onSetAlarmManager(int hour, int minute, Context context) {
        alarmPreset(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);//@param hour is your hour;
        calendar.set(Calendar.MINUTE, minute);//@param minute is your minute;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void stopAlarmManager(Context context) {
        alarmPreset(context);
        alarmManager.cancel(pendingIntent);
    }
    private void alarmPreset(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertManager.class);
        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}