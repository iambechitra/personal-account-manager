package com.example.bechitra.walleto;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bechitra.walleto.dialog.CategoryCreatorDialog;
import com.example.bechitra.walleto.dialog.listner.OnAddCategory;
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.table.Spending;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddSpending extends AppCompatActivity {

    @BindView(R.id.titleOfSpendingEdit) EditText titleOfSpendingEdit;
    @BindView(R.id.catagorySpinner) Spinner catagorySpinner;
    @BindView(R.id.newCatagoryCreatorText) TextView newCatagoryCreatorText;
    @BindView(R.id.spendingAmountEdit) EditText spendingAmountEdit;
    @BindView(R.id.additionalNoteEdit) EditText additionalNoteEdit;
    @BindView(R.id.spendingDateText) TextView spendingDateText;
    @BindView(R.id.confirmButton)
    Button confirmButton;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    OnCloseDialogListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        ButterKnife.bind(this);

        final DatabaseHelper db = new DatabaseHelper(this);

        final List<String> spinnerItem = db.getDistinctCategory("SPENDING");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagorySpinner.setAdapter(spinnerAdapter);

        if(spinnerItem != null)
            catagorySpinner.setSelection(0);

        catagorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catagorySpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spendingDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSpending.this,
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
                if (catagorySpinner.getSelectedItem() != null && !spendingAmountEdit.getText().toString().equals("")) {

                    BigDecimal big = new BigDecimal(spendingAmountEdit.getText().toString());
                    if (big.compareTo(BigDecimal.ZERO) == 1) {
                        String date = "";

                        if (!spendingDateText.getText().equals("TODAY"))
                            date = spendingDateText.getText().toString();
                        else
                            date = new StringPatternCreator().getCurrentDate();

                        Spending spending = new Spending(titleOfSpendingEdit.getText().toString().toUpperCase(), catagorySpinner.getSelectedItem().toString(),
                                spendingAmountEdit.getText().toString(), additionalNoteEdit.getText().toString().toUpperCase(), date);

                        db.onInsertSpending(spending);
                        finish();
                        loadMainActivity();
                    }
                }
            }
        });

        newCatagoryCreatorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryCreatorDialog dialog = new CategoryCreatorDialog();
                dialog.show(getSupportFragmentManager(), "OK");
                dialog.setOnAddCategory(new OnAddCategory() {
                    boolean flag = false;
                    @Override
                    public void setCategory(String category) {
                        if(!category.equals("NULL")) {
                            for (String str : spinnerItem) {
                                if (str.equals(category.toUpperCase()))
                                    flag = true;
                            }

                            if (!flag) {
                                spinnerItem.add(category.toUpperCase());
                                spinnerAdapter.notifyDataSetChanged();
                                catagorySpinner.setSelection(spinnerItem.size() - 1);
                            }
                        }
                    }
                });
            }
        });
    }

    private void loadMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loadMainActivity();
    }
}
