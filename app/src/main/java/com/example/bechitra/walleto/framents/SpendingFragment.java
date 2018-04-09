package com.example.bechitra.walleto.framents;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.SpendingRecyclerAdapter;
import com.example.bechitra.walleto.dialog.AdvanceFilterDialog;
import com.example.bechitra.walleto.dialog.listner.FilterDialogListener;
import com.example.bechitra.walleto.table.Spending;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bechitra on 3/26/2018.
 */

public class SpendingFragment extends Fragment{

    @BindView(R.id.spendingOrEarningRecyclerView)
    RecyclerView spendingRecyclerView;

    @BindView(R.id.spendingOrEarningFilterSwitch)
    Switch filterByCurrentMonth;

    @BindView(R.id.expandableLayout) ExpandableLayout expandableLayout;
    @BindView(R.id.toggleView) RelativeLayout toggleView;

    @BindView(R.id.advanceFilterText)
    CheckBox advanceFilterText;

    @BindView(R.id.calculateAmountCheckBox) CheckBox calculateAmountCheck;
    @BindView(R.id.calculateAmountTextView) TextView calculateAmountText;

    SpendingRecyclerAdapter adapter;
    List<Spending> spendingList;
    int ROTATION_ANGLE = 0;
    int CHECKED = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_earning, container, false);
        ButterKnife.bind(this, view);
        final DatabaseHelper db = new DatabaseHelper(view.getContext());

        spendingList = db.getAllSpending();
        spendingRecyclerView.setHasFixedSize(true);
        spendingRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new SpendingRecyclerAdapter(spendingList, view.getContext());
        spendingRecyclerView.setAdapter(adapter);

        filterByCurrentMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    spendingList.clear();
                    spendingList = db.getCurrentMonthSpending();
                    adapter.setData(spendingList);
                    adapter.notifyDataSetChanged();

                } else {
                    spendingList.clear();
                    spendingList = db.getAllSpending();
                    adapter.setData(spendingList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        setViewExpandable();

        advanceFilterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CHECKED == 0) {
                    advanceFilterText.setChecked(true);
                    Bundle bundle = new Bundle();
                    bundle.putString("TABLE_NAME", "SPENDING");
                    AdvanceFilterDialog dialog = new AdvanceFilterDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "OK");
                    dialog.setFilterDialogListener(new FilterDialogListener() {
                        @Override
                        public void onSetFilterData(String day, String month, String year, String category) {
                            String pattern = day + "/" + month + "/" + year;
                            spendingList.clear();
                            Log.d("Pattern", pattern);
                            Log.d("Category", category);
                            spendingList = db.filterQuerySpending(pattern, category);
                            adapter.setData(spendingList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    CHECKED = 1;
                } else {
                    advanceFilterText.setChecked(false);
                    CHECKED = 0;
                }
            }
        });

        calculateAmountCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    double amount = 0;
                    for(Spending sp : spendingList)
                        amount += Double.parseDouble(sp.getAmount());

                    calculateAmountText.setVisibility(calculateAmountText.VISIBLE);
                    calculateAmountText.setText("$"+Double.toString(amount));
                } else
                    calculateAmountText.setVisibility(calculateAmountText.GONE);
            }
        });

        return view;
    }

    private void setViewExpandable()
    {
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();

                if(ROTATION_ANGLE == 0) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_in);
                    toggleView.startAnimation(animation);
                    ROTATION_ANGLE = 180;
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_out);
                    toggleView.startAnimation(animation);
                    ROTATION_ANGLE = 0;
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup vg = (ViewGroup) view;
        vg.setClipChildren(false);
        vg.setClipToPadding(false);
    }
}
