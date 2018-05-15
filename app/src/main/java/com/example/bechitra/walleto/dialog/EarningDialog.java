package com.example.bechitra.walleto.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.listner.DialogListener;
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.table.TableData;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningDialog extends android.support.v4.app.DialogFragment{
    @BindView(R.id.earningDateText) TextView earningDateText;
    @BindView(R.id.earningAmountEdit) EditText earningAmountEdit;
    @BindView(R.id.earningCatagorySpinner) Spinner earningCatagorySpinner;
    @BindView(R.id.earningCategoryCreatorText) TextView earningCatagoryCreatorText;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private OnCloseDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_earning_setter, null);
        alertDialogBuilder.setView(view);
        ButterKnife.bind(this, view);

        final DatabaseHelper db = new DatabaseHelper(getActivity());

        final List<String> spinnerItem = db.getDistinctCategory("EARNING");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerItem);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        earningCatagorySpinner.setAdapter(spinnerAdapter);

        if(spinnerItem!= null)
            earningCatagorySpinner.setSelection(0);

        earningCatagorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                earningCatagorySpinner.setSelection(position);
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

        alertDialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(earningCatagorySpinner.getSelectedItem() != null && !earningAmountEdit.getText().toString().equals("")) {
                    StringPatternCreator stk = new StringPatternCreator();
                    BigDecimal big = new BigDecimal(earningAmountEdit.getText().toString());
                    if(big.compareTo(BigDecimal.ZERO) == 1) {
                        String date = "";

                        if (!earningDateText.getText().equals("TODAY"))
                            date = earningDateText.getText().toString();
                        else
                            date = stk.getCurrentDate();

                        TableData earning = new TableData(null,stk.stringFormatter(earningCatagorySpinner.getSelectedItem().toString()).trim(), earningAmountEdit.getText().toString(), date, null);
                        db.insertOnTable(db.getEarningTable(),earning);
                    }

                }

                if(listener != null)
                    listener.onClose(true);

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        earningCatagoryCreatorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryCreatorDialog dialog = new CategoryCreatorDialog();
                dialog.show(getFragmentManager(), "OK");
                dialog.setOnAddCategory(new DialogListener() {
                    boolean flag = false;
                    @Override
                    public void onSetDialog(String category) {
                        if(!category.equals("NULL")) {
                            for (String str : spinnerItem) {
                                if (str.equals(category.toUpperCase()))
                                    flag = true;
                            }

                            if (!flag) {
                                spinnerItem.add(category.toUpperCase());
                                spinnerAdapter.notifyDataSetChanged();
                                earningCatagorySpinner.setSelection(spinnerItem.size() - 1);
                            }
                        }
                    }
                });

            }
        });

        alertDialogBuilder.setTitle("Set earning");

        return alertDialogBuilder.create();
    }

    public void setOnCloseDialogListener(OnCloseDialogListener listener) {
        this.listener = listener;
    }
}
