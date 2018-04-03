package com.example.bechitra.walleto;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.bechitra.walleto.adapter.ViewPagerAdapter;
import com.example.bechitra.walleto.framents.EarningFragment;
import com.example.bechitra.walleto.framents.HomeFragment;
import com.example.bechitra.walleto.framents.SpendingFragment;
import com.example.bechitra.walleto.framents.ReportsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

   // @BindView(R.id.bottomNovigationView) BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.floatingActionButton) FloatingActionButton rootFloatingButton;
    @BindView(R.id.earningFloatingButton) FloatingActionButton earningFab;
    @BindView(R.id.spendingFloatingButton) FloatingActionButton spendingFab;

    ViewPagerAdapter adapter;
    Animation floatingButtonOpen, floatingButtonClose, clockWiseRotation, antiClockWiseRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onSetFragment(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        floatingButtonOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_button_open);
        floatingButtonClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_button_close);
        //clo
    }

    public void onSetFragment(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragment(new HomeFragment(), "Home");
        adapter.setFragment(new SpendingFragment(), "Spending");
        adapter.setFragment(new EarningFragment(), "Earning");
        adapter.setFragment(new ReportsFragment(), "Report");
        viewPager.setAdapter(adapter);

    }


    private void setFragmentManager(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
      //  fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    /*
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }
*/
}

/*
<item
        android:id="@+id/navigationTransaction"
        android:enabled="true"
        android:icon="@drawable/ic_spending_24dp"
        android:title="@string/navigation_menu_transaction"
        app:showAsAction="ifRoom" />
 */
