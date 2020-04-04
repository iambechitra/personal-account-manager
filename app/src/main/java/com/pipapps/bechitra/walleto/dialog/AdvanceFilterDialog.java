package com.pipapps.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.utility.DateManager;
import com.pipapps.bechitra.walleto.dialog.listener.FilterDialogListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvanceFilterDialog extends DialogFragment{
    private FilterDialogListener listener;
    @BindView(R.id.daysChecked) CheckBox daysChecked;
    @BindView(R.id.monthChecked) CheckBox monthChecked;
    @BindView(R.id.yearChecked) CheckBox yearChecked;
    @BindView(R.id.categoryChecked) CheckBox categoryChecked;
    @BindView(R.id.dayTextDiolog) TextView dayTextDiolog;
    @BindView(R.id.categoryItemSpinner) Spinner categorySpinner;
    @BindView(R.id.yearItemSpinner) Spinner yearItemSpinner;
    @BindView(R.id.monthSpinner) Spinner monthSpinner;
    boolean []checked = new boolean[4];

    @NonNull
    @Override

    /// ################### NEED TO CHECK ################# \\\
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.advance_filter_dialog, null);
        alertDialogBuilder.setView(view);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();

        daysChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dayTextDiolog.setVisibility(dayTextDiolog.VISIBLE);
                    checked[0] = true;
                }
                else {
                    dayTextDiolog.setVisibility(dayTextDiolog.GONE);
                    checked[0] = false;
                }
            }
        });

        monthChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    monthSpinner.setVisibility(monthChecked.VISIBLE);
                    checked[1] = true;
                }
                else {
                    monthSpinner.setVisibility(monthChecked.GONE);
                    checked[1] = false;
                }
            }
        });

        yearChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    yearItemSpinner.setVisibility(yearChecked.VISIBLE);
                    checked[2] = true;
                }
                else {
                    yearItemSpinner.setVisibility(yearChecked.GONE);
                    checked[2] = false;
                }
            }
        });

        categoryChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    categorySpinner.setVisibility(categoryChecked.VISIBLE);
                    checked[3] = true;
                }
                else {
                    categorySpinner.setVisibility(categoryChecked.GONE);
                    checked[3] = false;
                }
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String day = (!dayTextDiolog.getText().toString().equals("") && checked[0])? dayTextDiolog.getText().toString():"%";
                String month = monthSpinner.getSelectedItem() != null  && checked[1]? Integer.toString(new DateManager().getMonthID(monthSpinner.getSelectedItem().toString())):"%";
                String year = yearItemSpinner.getSelectedItem() != null && checked[2]? yearItemSpinner.getSelectedItem().toString() : "%";
                String category = categorySpinner.getSelectedItem() != null && checked[3]? categorySpinner.getSelectedItem().toString() : "%";

                listener.onSetFilterData(day, month, year, category);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setTitle("Advance Filter Option");
        DateManager creator = new DateManager();
        String[]lst = creator.getAllMonthName();
        List<String> monthSpinnerItem = new ArrayList<>();
        for(String str :lst)
            monthSpinnerItem.add(str);

        ArrayAdapter<String> monthSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, monthSpinnerItem);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);

        List<String> categoryItem = null;//= db.getDistinctCategory(bundle.getString("TABLE_NAME"));
        List<String> yearItem = null;//= db.getDistinctDate(bundle.getString("TABLE_NAME"));
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryItem);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        ArrayAdapter<String> yearSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, yearItem);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearItemSpinner.setAdapter(yearSpinnerAdapter);

        return alertDialogBuilder.create();
    }
    
    public void setFilterDialogListener(FilterDialogListener listener) {
        this.listener = listener;
    }
}
