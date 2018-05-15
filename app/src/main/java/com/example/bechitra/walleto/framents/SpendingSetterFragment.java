package com.example.bechitra.walleto.framents;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.bechitra.walleto.table.TableData;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
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

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_spending_setter, null);
        ButterKnife.bind(this, view);

        final DatabaseHelper db = new DatabaseHelper(view.getContext());

        final List<String> spinnerItem = Arrays.asList(getResources().getStringArray(R.array.SCATEGORY));

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerItem);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        if(spinnerItem != null)
            categorySpinner.setSelection(0);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySpinner.setSelection(position);
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

                        TableData spending = new TableData(null, stk.stringFormatter(categorySpinner.getSelectedItem().toString()).trim(),
                                spendingAmountEdit.getText().toString(), stk.stringFormatter(additionalNoteEdit.getText().toString().toUpperCase()).trim(), date);

                        db.insertOnTable(db.getSpendingTable(), spending);
                        loadMainActivity();
                    }
                }
            }
        });

        newCatagoryCreatorText.setOnClickListener(new View.OnClickListener() {
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
                                categorySpinner.setSelection(spinnerItem.size() - 1);
                            }
                        }
                    }
                });
            }
        });

        return view;
    }

    private void loadMainActivity() {
        Intent i = new Intent(view.getContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
        startActivity(i);
    }
}
