package com.example.bechitra.walleto.framents;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.dialog.CategoryCreatorDialog;
import com.example.bechitra.walleto.dialog.listener.DialogListener;
import com.example.bechitra.walleto.adapter.SpinnerAdapter;
import com.example.bechitra.walleto.table.PrimeTable;
import com.example.bechitra.walleto.viewmodel.EarningSetterViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningSetterFragment extends Fragment {

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
    //private DatabaseHelper db;
    DataRepository repository;
    private List<String> spinnerItem;
    private SpinnerAdapter spinnerAdapter;
    private int itemSelected = 0;
    EarningSetterViewModel earningSetterViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_earning_setter, null);
        ButterKnife.bind(this, view);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        earningSetterViewModel = new ViewModelProvider(requireActivity()).get(EarningSetterViewModel.class);

        repository = new DataRepository(getActivity().getApplication());

        List<String>items  = Arrays.asList(getResources().getStringArray(R.array.ECATEGORY));
        spinnerItem = new ArrayList<>();
        for(String str : items)
            spinnerItem.add(str);

        try {
            List<String> dbCategory = repository.getDistinctCategory(DataRepository.EARNING_TAG);

            for (String s : dbCategory)
                if (!spinnerItem.contains(s))
                    spinnerItem.add(s);
        } catch (Exception e) {}

        spinnerAdapter = new SpinnerAdapter(spinnerItem, view.getContext());
        earningCatagorySpinner.setAdapter(spinnerAdapter);

        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        autoRepetitionSpinner.setAdapter(spinnerArrayAdapter);

        if(getArguments() == null) {
            earningCatagorySpinner.setSelection(0);
            autoRepetitionSpinner.setVisibility(View.INVISIBLE);
            Log.d("inState", "null");
        } else
            loadSavedState(getArguments());



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


        earningDateText.setOnClickListener(views -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
            datePickerDialog.show();
        });

        dateSetListener = (view1, year, month, dayOfMonth) -> earningDateText.setText( new DateManager().getDate(dayOfMonth, month+1, year) );

        earningCatagoryCreatorText.setOnClickListener(views -> {
            CategoryCreatorDialog dialog = new CategoryCreatorDialog();
            dialog.show(getActivity().getSupportFragmentManager(), "OK");
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

        });

        confirmButton.setOnClickListener(views -> {
            if(earningCatagorySpinner.getSelectedItem() != null && !earningAmountEdit.getText().toString().equals("")) {
                DateManager stk = new DateManager();
                BigDecimal big = new BigDecimal(earningAmountEdit.getText().toString());
                if(big.compareTo(BigDecimal.ZERO) == 1) {
                    String date = "";
                    String category = stk.stringFormatter(spinnerItem.get(itemSelected)).trim();

                    if (!earningDateText.getText().equals("TODAY"))
                        date = earningDateText.getText().toString();
                    else
                        date = stk.getCurrentDate();

                    Transaction earning = new Transaction(category, Double.parseDouble(earningAmountEdit.getText().toString()), stk.stringFormatter(earningNoteEdit.getText().toString().toUpperCase()).trim(),
                            date, DataRepository.EARNING_TAG, repository.getActiveWalletID());

                    long rowID = earningSetterViewModel.insertTransaction(earning);

                    if(autoRepetitionCheckBox.isChecked()) {
                        earningSetterViewModel.insertSchedule(new Schedule(DataRepository.EARNING_TAG, category,
                                Double.parseDouble(earningAmountEdit.getText().toString()), stk.stringFormatter(earningNoteEdit.getText().toString().toUpperCase()).trim(),
                                date, getRepeat(), earning.getWalletID(), rowID, true));
                    }

                    getActivity().finish();
                }
            }
        });

        autoRepetitionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                autoRepetitionSpinner.setVisibility(View.VISIBLE);
            else
                autoRepetitionSpinner.setVisibility(View.INVISIBLE);
        });

        return view;
    }

    private void loadSavedState(Bundle savedInstanceState) {
        /*
        SaveInstanceState state = new SaveInstanceState(savedInstanceState);
        if(state.isExist()) {
            earningCatagorySpinner.setSelection(spinnerItem.indexOf(state.getCategory()));
            earningAmountEdit.setText(state.getAmount());
            earningNoteEdit.setText(state.getNote());
            earningDateText.setText(state.getDate());

            if(state.isRepetitionChaked()) {
                autoRepetitionCheckBox.setChecked(true);
                autoRepetitionSpinner.setVisibility(View.VISIBLE);

                String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
                String repeat = state.getRepeatKey();
                for(int i = 0; i < array.length; i++) {
                    if(array[i].equals(repeat)) {
                        autoRepetitionSpinner.setSelection(i);
                        break;
                    }
                }

            } else
                autoRepetitionSpinner.setVisibility(View.INVISIBLE);
        }
        */
    }


    private String getRepeat() {
        HashMap<String, String> count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        return count.get(autoRepetitionSpinner.getSelectedItem().toString());
    }

    private void loadMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
        startActivity(i);
    }
}
