package com.example.bechitra.walleto.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.DefaultSpinnerAdapter;
import com.example.bechitra.walleto.dialog.RowDeleteDialog;
import com.example.bechitra.walleto.dialog.listener.OnCloseDialogListener;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.utility.ScheduleParser;

import java.util.*;

public class ScheduleManagementActivity extends AppCompatActivity {
    @BindView(R.id.deleteSchedule) TextView deleteButton;
    @BindView(R.id.editSchedule) TextView editButton;
    @BindView(R.id.backButton) TextView backButton;
    @BindView(R.id.tableSpinner) Spinner tableSpinner;
    @BindView(R.id.categorySpinner) Spinner categorySpinner;
    @BindView(R.id.noteEdit) EditText noteEdit;
    @BindView(R.id.amountEdit) EditText amountEdit;
    @BindView(R.id.dateText) TextView dateText;
    @BindView(R.id.autoRepetitionSpinner) Spinner autoRepetitionSpinner;
    @BindView(R.id.isActiveSwitch) Switch isActiveSwitch;
    @BindView(R.id.updateButton) TextView updateButton;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ScheduleParser schedule;
    private List<String> spinnerItem;
    private DatabaseHelper db;
    private String DATE = "";
    private Map<String, String> count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_management);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);
        DATE = new DateManager().getCurrentDate();

        schedule = getIntent().getParcelableExtra("schedule");

        amountEdit.setText(schedule.getAmount());
        noteEdit.setText(schedule.getNote());
        dateText.setText(schedule.getDate());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RowDeleteDialog dialog = new RowDeleteDialog();
                dialog.show(getSupportFragmentManager(), "TAG");

                dialog.setOnCloseDialogManager(new OnCloseDialogListener() {
                    @Override
                    public void onClose(boolean flag) {
                        if(flag) {
                            db.deleteRowFromTable("SCHEDULE", schedule.getID());
                            finish();
                        }
                    }
                });
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataChanged(schedule)) {
                    createToastMassage("Update Successful!");
                    finish();
                } else {
                    createToastMassage("Press Back to exit");
                }
            }
        });

        String[] arrays = {"SPENDING","EARNING"};
        ArrayAdapter<String> spinnerArrayAdapterForTable = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        arrays); //selected item will look like a spinner set from XML
        spinnerArrayAdapterForTable.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        tableSpinner.setAdapter(spinnerArrayAdapterForTable);

        if(schedule.getTableName().equals(arrays[0]))
            tableSpinner.setSelection(0);
        else
            tableSpinner.setSelection(1);

        List<String> temp;
        if(schedule.getTableName().equals("EARNING"))
            temp = Arrays.asList(getResources().getStringArray(R.array.ECATEGORY));
        else
            temp = Arrays.asList(getResources().getStringArray(R.array.SCATEGORY));

        spinnerItem = new ArrayList<>();

        for(String str : temp)
            spinnerItem.add(str);

        int position = 0;
        for(int i = 0; i < spinnerItem.size(); i++)
            if (spinnerItem.get(i).equals(schedule.getCategory())) {
                position = i;
                break;
            }

        DefaultSpinnerAdapter adapter = new DefaultSpinnerAdapter(this, spinnerItem);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(position);

        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        autoRepetitionSpinner.setAdapter(spinnerArrayAdapter);
        initializeRepetitionSpinner(schedule, array);


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DATE = new DateManager().getDate(dayOfMonth, month+1, year);
                dateText.setText(DATE);
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleManagementActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                datePickerDialog.show();
            }
        });
    }

    private void initializeRepetitionSpinner(ScheduleParser schedule, String[] array) {
        count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        for(Map.Entry<String, String> map : count.entrySet())
            if(map.getValue().equals(schedule.getRepeat()))
                for(int i = 0; i < array.length; i++)
                    if(array[i].equals(map.getKey()))
                        autoRepetitionSpinner.setSelection(i);

    }

    private boolean isDataChanged(ScheduleParser parser) {
        String tableName = tableSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String amount = amountEdit.getText().toString();
        String notes = noteEdit.getText().toString();
        String repeat = count.get(autoRepetitionSpinner.getSelectedItem().toString());

        boolean flag = false;

        if(!parser.getTableName().equals(tableName)) {
            db.updateRowFromTable("SCHEDULE", schedule.getID(), "TABLE_NAME", tableName);
            flag = true;
        }

        if(!parser.getCategory().equals(category)) {
            db.updateRowFromTable("SCHEDULE", schedule.getID(), "CATEGORY", category);
            flag = true;
        }

        if(!parser.getAmount().equals(amount)) {
            db.updateRowFromTable("SCHEDULE", schedule.getID(), "AMOUNT", amount);
            flag = true;
        }

        if(!parser.getDate().equals(DATE)) {
            db.updateRowFromTable("SCHEDULE", schedule.getID(), "DATE", DATE);
            flag = true;
        }

        if(!parser.getNote().equals(notes)) {
            db.updateRowFromTable("SCHEDULE", schedule.getID(), "NOTE", notes);
            flag = true;
        }

        if(!schedule.getRepeat().equals(repeat)) {
            db.updateRowFromTable("SCHEDULE", schedule.getID(), "REPEAT", repeat);
            flag = true;
        }

        return flag;
    }

    private void createToastMassage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
