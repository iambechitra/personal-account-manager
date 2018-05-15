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
import com.example.bechitra.walleto.dialog.listner.OnCloseDialogListener;
import com.example.bechitra.walleto.table.TableData;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
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

    @BindView(R.id.earningNoteEdit) EditText earningNoteEdit;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_earning_setter, null);
        ButterKnife.bind(this, view);

        final DatabaseHelper db = new DatabaseHelper(getActivity());

        final List<String> spinnerItem = Arrays.asList(getResources().getStringArray(R.array.ECATEGORY));

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

                        TableData earning = new TableData(null, stk.stringFormatter(earningCatagorySpinner.getSelectedItem().toString()).trim(), earningAmountEdit.getText().toString(),
                                                         earningNoteEdit.getText().toString(), date);
                        db.insertOnTable(db.getEarningTable(), earning);
                        loadMainActivity();
                    }

                }
            }
        });

        return view;
    }

    private void loadMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
        startActivity(i);
    }
}
