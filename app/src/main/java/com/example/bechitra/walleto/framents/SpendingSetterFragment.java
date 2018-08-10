package com.example.bechitra.walleto.framents;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.CategoryCreatorDialog;
import com.example.bechitra.walleto.dialog.listner.DialogListener;
import com.example.bechitra.walleto.table.Schedule;
import com.example.bechitra.walleto.table.TableData;
import com.example.bechitra.walleto.adapter.SpinnerAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpendingSetterFragment extends Fragment{
    @BindView(R.id.catagorySpinner)
    Spinner categorySpinner;
    @BindView(R.id.newCatagoryCreatorText)
    TextView newCatagoryCreatorText;
    @BindView(R.id.spendingAmountEdit) EditText spendingAmountEdit;
    @BindView(R.id.additionalNoteEdit) EditText additionalNoteEdit;
    @BindView(R.id.spendingDateText) TextView spendingDateText;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.nestedScroll)
    NestedScrollView scrollView;
    @BindView(R.id.autoRepetition) Spinner autoRepetitionSpinner;
    @BindView(R.id.autoRepetitionCheckbox)
    CheckBox autoRepetitionCheckBox;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private View view;
    private List<String> spinnerItem;
    private SpinnerAdapter spinnerAdapter;
    private DatabaseHelper db;
    private int pos = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_spending_setter, null);
        ButterKnife.bind(this, view);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(view.getContext());

        List<String>categoryItems = Arrays.asList(getResources().getStringArray(R.array.SCATEGORY));
        spinnerItem = new ArrayList<>();
        for(String str : categoryItems)
            spinnerItem.add(str);

        spinnerAdapter = new SpinnerAdapter(spinnerItem, view.getContext());

        //spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, spinnerItem);
        //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        if(spinnerItem != null)
            categorySpinner.setSelection(0);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySpinner.setSelection(position);
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spendingDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                spendingDateText.setText(Integer.toString(dayOfMonth) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(year));
            }
        };


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categorySpinner.getSelectedItem() != null && !spendingAmountEdit.getText().toString().equals("")) {

                    BigDecimal big = new BigDecimal(spendingAmountEdit.getText().toString());
                    if (big.compareTo(BigDecimal.ZERO) == 1) {
                        String date = "";

                        if (!spendingDateText.getText().equals("TODAY"))
                            date = spendingDateText.getText().toString();
                        else
                            date = new StringPatternCreator().getCurrentDate();

                        StringPatternCreator stk = new StringPatternCreator();

                        TableData spending = new TableData(null, stk.stringFormatter(spinnerItem.get(pos)).trim(),
                                spendingAmountEdit.getText().toString(), stk.stringFormatter(additionalNoteEdit.getText().toString().toUpperCase()).trim(), date, db.getActivatedWalletID());

                        db.insertOnTable(db.getSpendingTable(), spending);

                        if(autoRepetitionCheckBox.isChecked())
                            createScheduler(date);

                        loadMainActivity();
                    }
                }
            }
        });

        newCatagoryCreatorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryCreatorDialog dialog = new CategoryCreatorDialog();
                dialog.setOnAddCategory(new DialogListener() {
                    boolean flag = false;
                    @Override
                    public void onSetDialog(String regex, boolean flag) {
                        if(!regex.equals("NULL")) {
                            for (String str : spinnerItem) {
                                if (str.equals(regex))
                                    flag = true;
                            }

                            if (!flag) {
                                Log.d("Cat", regex);
                                spinnerItem.add(regex);
                                spinnerAdapter.setData(spinnerItem);
                                spinnerAdapter.notifyDataSetChanged();
                                categorySpinner.setSelection(spinnerItem.size() - 1);
                            }
                        }
                    }
                });
                dialog.show(getFragmentManager(), "OK");
            }
        });

        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        autoRepetitionSpinner.setAdapter(spinnerArrayAdapter);
        autoRepetitionSpinner.setVisibility(View.INVISIBLE);

        autoRepetitionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    autoRepetitionSpinner.setVisibility(View.VISIBLE);
                else
                    autoRepetitionSpinner.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    private void createScheduler(String date) {
        HashMap<String, String> count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        String tableName = db.getSpendingTable();
        String LAST_ROW = db.getLastRowFromTable(tableName);
        db.insertNewSchedule(new Schedule(null, LAST_ROW, tableName, count.get(autoRepetitionSpinner.getSelectedItem().toString()), date));
    }

    private void loadMainActivity() {
        Intent i = new Intent(view.getContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
        startActivity(i);
    }
}
