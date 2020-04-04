package com.example.bechitra.walleto;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.bechitra.walleto.databinding.ActivityMainBinding;
import com.example.bechitra.walleto.framents.OverviewFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bechitra.walleto.activity.*;
import com.example.bechitra.walleto.framents.HomeFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onSetAlarmManager(1, 1, this);

        attachFragment(new HomeFragment());
        //dashboardBack.setBackgroundColor(view.getResources().getColor(R.color.tab_selected));

        binding.rootFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SpendingOrEarningDataSetterActivity.class);
                startActivity(intent);
            }
        });

        binding.dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFragment(new HomeFragment());
                binding.dashboardBack.setBackgroundResource(R.drawable.ic_background_custom);
                binding.overviewBack.setBackgroundResource(R.color.green);
            }
        });

        binding.overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFragment(new OverviewFragment());
                binding.overviewBack.setBackgroundResource(R.drawable.ic_background_custom);
                binding.dashboardBack.setBackgroundResource(R.color.green);
            }
        });

        binding.navigationDrawerIconText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
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