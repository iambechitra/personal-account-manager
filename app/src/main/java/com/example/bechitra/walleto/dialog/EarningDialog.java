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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.dialog.listener.DialogListener;
import com.example.bechitra.walleto.dialog.listener.OnCloseDialogListener;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/// ######################### NEED TO CHECK ######################## \\\
public class EarningDialog extends DialogFragment {
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

        //final DatabaseHelper db = new DatabaseHelper(getActivity());

        final List<String> spinnerItem = null;// db.getDistinctCategory("EARNING");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerItem);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        earningCatagorySpinner.setAdapter(spinnerAdapter);

        //if(spinnerItem!= null)
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


        earningDateText.setOnClickListener(viewView -> {
            Calendar calendar;
            calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
            datePickerDialog.show();
        });

        dateSetListener = (view1, year, month, dayOfMonth) -> earningDateText.setText(dayOfMonth + "/" + (month + 1) + "/" + Integer.toString(year));

        alertDialogBuilder.setPositiveButton("ADD", (dialog, which) -> {
            if(earningCatagorySpinner.getSelectedItem() != null && !earningAmountEdit.getText().toString().equals("")) {
                DateManager stk = new DateManager();
                BigDecimal big = new BigDecimal(earningAmountEdit.getText().toString());
                if(big.compareTo(BigDecimal.ZERO) == 1) {
                    String date;

                    if (!earningDateText.getText().equals("TODAY"))
                        date = earningDateText.getText().toString();
                    else
                        date = stk.getCurrentDate();

                    //PrimeTable earning = new PrimeTable(null,stk.stringFormatter(earningCatagorySpinner.getSelectedItem().toString()).trim(), earningAmountEdit.getText().toString(), date, null, db.getActivatedWalletID());
                    //db.insertOnTable(db.getEarningTable(),earning);
                }

            }

            if(listener != null)
                listener.onClose(true);

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
                    public void onSetDialog(String regex, boolean flag) {
                        if(!regex.equals("NULL")) {
                            for (String str : spinnerItem) {
                                if (str.equals(regex.toUpperCase()))
                                    flag = true;
                            }

                            if (!flag) {
                                spinnerItem.add(regex.toUpperCase());
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
