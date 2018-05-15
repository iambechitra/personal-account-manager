package com.example.bechitra.walleto.framents;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.bechitra.walleto.adapter.EarningRecyclerAdapter;
import com.example.bechitra.walleto.dialog.AdvanceFilterDialog;
import com.example.bechitra.walleto.dialog.listner.FilterDialogListener;
import com.example.bechitra.walleto.table.TableData;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningOverviewFragment extends Fragment{
    List<TableData> earningList;
    @BindView(R.id.spendingOrEarningRecyclerView)
    RecyclerView earningRecyclerView;
    EarningRecyclerAdapter adapter;

    @BindView(R.id.expandableLayout)
    ExpandableLayout expandableLayout;
    @BindView(R.id.toggleView)
    RelativeLayout toggleView;

    @BindView(R.id.advanceFilterText)
    CheckBox advanceFilterText;

    @BindView(R.id.spendingOrEarningFilterSwitch)
    Switch filterByCurrentMonth;

    @BindView(R.id.calculateAmountCheckBox) CheckBox calculateAmountCheck;
    @BindView(R.id.calculateAmountTextView) TextView calculateAmountText;

    int ROTATION_ANGLE = 0, CHECKED = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_earning, null);
        ButterKnife.bind(this, view);
        final DatabaseHelper db = new DatabaseHelper(view.getContext());

        earningList = db.getAllRow(db.getEarningTable());
        earningRecyclerView.setHasFixedSize(true);
        earningRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new EarningRecyclerAdapter(view.getContext(), earningList);
        earningRecyclerView.setAdapter(adapter);

        filterByCurrentMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    earningList.clear();
                    earningList = db.getCurrentMonthData(db.getEarningTable());
                    adapter.setData(earningList);
                    adapter.notifyDataSetChanged();

                } else {
                    earningList.clear();
                    earningList = db.getAllRow(db.getEarningTable());
                    adapter.setData(earningList);
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
                    bundle.putString("TABLE_NAME", "EARNING");
                    AdvanceFilterDialog dialog = new AdvanceFilterDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "OK");
                    dialog.setFilterDialogListener(new FilterDialogListener() {
                        @Override
                        public void onSetFilterData(String day, String month, String year, String category) {
                            String pattern = day + "/" + month + "/" + year;
                            earningList.clear();
                            Log.d("Pattern", pattern);
                            Log.d("Category", category);
                            earningList = db.filterDataFromTable(db.getEarningTable(),pattern, category);
                            adapter.setData(earningList);
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
                    for(TableData sp : earningList)
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
}
