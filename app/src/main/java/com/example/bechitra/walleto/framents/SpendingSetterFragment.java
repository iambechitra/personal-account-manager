package com.example.bechitra.walleto.framents;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.SpinnerAdapter;
import com.example.bechitra.walleto.dialog.CategoryCreatorDialog;
import com.example.bechitra.walleto.dialog.listener.DialogListener;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.utility.SaveInstanceState;
import com.example.bechitra.walleto.viewmodel.SpendingSetterViewModel;

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
    private SpendingSetterViewModel spendingSetterViewModel;
    private int pos = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_spending_setter, null);
        ButterKnife.bind(this, view);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        spendingSetterViewModel = new ViewModelProvider(requireActivity()).get(SpendingSetterViewModel.class);

        List<String>categoryItems = Arrays.asList(getResources().getStringArray(R.array.SCATEGORY));
        spinnerItem = new ArrayList<>();
        for(String str : categoryItems)
            spinnerItem.add(str);

        spinnerAdapter = new SpinnerAdapter(spinnerItem, view.getContext());
        categorySpinner.setAdapter(spinnerAdapter);

        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        autoRepetitionSpinner.setAdapter(spinnerArrayAdapter);

        if(getArguments() == null) {
            categorySpinner.setSelection(0);
            autoRepetitionSpinner.setVisibility(View.INVISIBLE);
        } else
            loadSavedState(getArguments());

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

        dateSetListener = (view, year, month, dayOfMonth) -> spendingDateText.setText(new DateManager().getDate(dayOfMonth, month+1, year));


        confirmButton.setOnClickListener(view -> {
            if (categorySpinner.getSelectedItem() != null && !spendingAmountEdit.getText().toString().equals("")) {

                BigDecimal big = new BigDecimal(spendingAmountEdit.getText().toString());
                if (big.compareTo(BigDecimal.ZERO) == 1) {
                    String date = "";
                    DateManager stk = new DateManager();
                    String category = stk.stringFormatter(spinnerItem.get(pos)).trim();

                    if (!spendingDateText.getText().equals("TODAY"))
                        date = spendingDateText.getText().toString();
                    else
                        date = new DateManager().getCurrentDate();

                    Transaction spending = new Transaction(category, Double.parseDouble(spendingAmountEdit.getText().toString()),
                            stk.stringFormatter(additionalNoteEdit.getText().toString().toUpperCase()).trim(),
                            date, DataRepository.SPENDING_TAG, spendingSetterViewModel.getActivatedWalletID());

                    long rowId = spendingSetterViewModel.insertTransaction(spending);
                    double bal = Double.parseDouble(spendingAmountEdit.getText().toString());

                    Wallet wallet = spendingSetterViewModel.getActiveWallet();
                    wallet.setBalance(wallet.getBalance() - bal);

                    spendingSetterViewModel.updateWallet(wallet);

                    if(autoRepetitionCheckBox.isChecked())
                        spendingSetterViewModel.insertSchedule(new Schedule(DataRepository.SPENDING_TAG, category, Double.parseDouble(spendingAmountEdit.getText().toString()),
                                stk.stringFormatter(additionalNoteEdit.getText().toString().toUpperCase()).trim(), date, getRepeat(), spending.getWalletID(),rowId, true));

                    getActivity().finish();
                }
            }
        });

        newCatagoryCreatorText.setOnClickListener(viewView -> {
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
            dialog.show(getActivity().getSupportFragmentManager(), "OK");
        });

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

    private void loadSavedState(Bundle savedInstanceState) {
        SaveInstanceState state = new SaveInstanceState(savedInstanceState);
        if (state.isExist()) {
            categorySpinner.setSelection(spinnerItem.indexOf(state.getCategory()));
            spendingAmountEdit.setText(state.getAmount());
            additionalNoteEdit.setText(state.getNote());
            spendingDateText.setText(state.getDate());

            if (state.isRepetitionChaked()) {
                autoRepetitionCheckBox.setChecked(true);
                autoRepetitionSpinner.setVisibility(View.VISIBLE);

                String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
                String repeat = state.getRepeatKey();
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals(repeat)) {
                        autoRepetitionSpinner.setSelection(i);
                        break;
                    }
                }

            } else
                autoRepetitionSpinner.setVisibility(View.INVISIBLE);
        }
    }

    private String getRepeat() {
        HashMap<String, String> count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        return count.get(autoRepetitionSpinner.getSelectedItem().toString());
    }
}
