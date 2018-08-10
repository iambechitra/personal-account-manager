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
import android.widget.Toast;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.CategoryCreatorDialog;
import com.example.bechitra.walleto.dialog.listner.DialogListener;
import com.example.bechitra.walleto.adapter.SpinnerAdapter;
import com.example.bechitra.walleto.table.Schedule;
import com.example.bechitra.walleto.table.TableData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningSetterFragment extends Fragment{

    @BindView(R.id.earningDateText)
    TextView earningDateText;
    @BindView(R.id.earningAmountEdit)
    EditText earningAmountEdit;
    @BindView(R.id.earningCatagorySpinner)
    Spinner earningCatagorySpinner;
    @BindView(R.id.earningCategoryCreatorText) TextView earningCatagoryCreatorText;

    @BindView(R.id.earningSetConfirmButton)
    Button confirmButton;

    @BindView(R.id.autoRepetitionSpinner) Spinner autoRepetitionSpinner;
    @BindView(R.id.autoRepetitionCheckbox)
    CheckBox autoRepetitionCheckBox;

    @BindView(R.id.nestedScroll)
    NestedScrollView scrollView;

    @BindView(R.id.earningNoteEdit) EditText earningNoteEdit;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatabaseHelper db;
    private List<String> spinnerItem;
    private SpinnerAdapter spinnerAdapter;
    private int itemSelected = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_earning_setter, null);
        ButterKnife.bind(this, view);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(getActivity());

        List<String>items  = Arrays.asList(getResources().getStringArray(R.array.ECATEGORY));
        spinnerItem = new ArrayList<>();
        for(String str : items)
            spinnerItem.add(str);

        spinnerAdapter = new SpinnerAdapter(spinnerItem, view.getContext());


        earningCatagorySpinner.setAdapter(spinnerAdapter);

        if(spinnerItem!= null)
            earningCatagorySpinner.setSelection(0);

        earningCatagorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                earningCatagorySpinner.setSelection(position);
                itemSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        earningDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                earningDateText.setText(Integer.toString(dayOfMonth) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(year));
            }
        };

        earningCatagoryCreatorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryCreatorDialog dialog = new CategoryCreatorDialog();
                dialog.show(getFragmentManager(), "OK");
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
                                spinnerItem.add(regex);
                                spinnerAdapter.setData(spinnerItem);
                                spinnerAdapter.notifyDataSetChanged();
                                earningCatagorySpinner.setSelection(spinnerItem.size() - 1);
                            }
                        }
                    }
                });

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(earningCatagorySpinner.getSelectedItem() != null && !earningAmountEdit.getText().toString().equals("")) {
                    StringPatternCreator stk = new StringPatternCreator();
                    BigDecimal big = new BigDecimal(earningAmountEdit.getText().toString());
                    if(big.compareTo(BigDecimal.ZERO) == 1) {
                        String date = "";

                        if (!earningDateText.getText().equals("TODAY"))
                            date = earningDateText.getText().toString();
                        else
                            date = stk.getCurrentDate();

                        TableData earning = new TableData(null, stk.stringFormatter(spinnerItem.get(itemSelected)).trim(), earningAmountEdit.getText().toString(),
                                                         earningNoteEdit.getText().toString(), date, db.getActivatedWalletID());
                        db.insertOnTable(db.getEarningTable(), earning);

                        if(autoRepetitionCheckBox.isChecked())
                            createScheduler(date);

                        loadMainActivity();
                    }
                }

                if(autoRepetitionCheckBox.isChecked()) {
                    Toast.makeText(getActivity(), autoRepetitionSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                }
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

        String tableName = db.getEarningTable();
        String LAST_ROW = db.getLastRowFromTable(tableName);
        db.insertNewSchedule(new Schedule(null, LAST_ROW, tableName, count.get(autoRepetitionSpinner.getSelectedItem().toString()), date));
    }

    private void loadMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
        startActivity(i);
    }
}
