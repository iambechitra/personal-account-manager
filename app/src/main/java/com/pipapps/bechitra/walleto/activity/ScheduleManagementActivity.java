package com.pipapps.bechitra.walleto.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.adapter.DefaultSpinnerAdapter;
import com.pipapps.bechitra.walleto.databinding.ActivityScheduleManagementBinding;
import com.pipapps.bechitra.walleto.dialog.RowDeleteDialog;
import com.pipapps.bechitra.walleto.room.entity.Schedule;
import com.pipapps.bechitra.walleto.utility.DateManager;
import com.pipapps.bechitra.walleto.utility.ScheduleParcel;
import com.pipapps.bechitra.walleto.viewmodel.ScheduleManagementActivityViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;

public class ScheduleManagementActivity extends AppCompatActivity {
    @BindView(R.id.editSchedule) TextView editButton;
    @BindView(R.id.isActiveSwitch) Switch isActiveSwitch;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ScheduleParcel schedule;
    private List<String> spinnerItem;
    private String DATE = "";
    private Map<String, String> count;
    ActivityScheduleManagementBinding viewBind;
    ScheduleManagementActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityScheduleManagementBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());

        viewModel = new ViewModelProvider(this).get(ScheduleManagementActivityViewModel.class);

        DATE = new DateManager().getCurrentDate();

        schedule = getIntent().getParcelableExtra("schedule");

        viewBind.amountEdit.setText(""+schedule.getAmount());
        viewBind.noteEdit.setText(schedule.getNote());
        viewBind.dateText.setText(schedule.getDate());

        viewBind.backButton.setOnClickListener(view -> finish());

        viewBind.deleteSchedule.setOnClickListener(view -> {
            RowDeleteDialog dialog = new RowDeleteDialog();
            dialog.show(getSupportFragmentManager(), "TAG");

            dialog.setOnCloseDialogManager(flag -> {
                if(flag) {
                    Schedule scheduleBuilder = new Schedule(
                            schedule.getTag(), schedule.getCategory(), schedule.getAmount(),
                            schedule.getNote(), schedule.getDate(), schedule.getRepeat(),
                            schedule.getWalletID(), schedule.getTransactionID(),
                            schedule.isActive()
                    );

                    scheduleBuilder.setId(schedule.getID());
                    viewModel.deleteSchedule(scheduleBuilder);
                    finish();
                }
            });
        });

        viewBind.updateButton.setOnClickListener(view -> {
            if(isDataChanged()) {
                createToastMassage("Update Successful!");
                finish();
            } else {
                createToastMassage("Press Back to exit");
            }
        });

        String[] arrays = {"SPENDING","EARNING"};
        ArrayAdapter<String> spinnerArrayAdapterForTable = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        arrays); //selected item will look like a spinner set from XML
        spinnerArrayAdapterForTable.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        viewBind.tableSpinner.setAdapter(spinnerArrayAdapterForTable);

        if(schedule.getTag().equals(arrays[0]))
            viewBind.tableSpinner.setSelection(0);
        else
            viewBind.tableSpinner.setSelection(1);

        List<String> temp;
        if(schedule.getTag().equals("EARNING"))
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
        viewBind.categorySpinner.setAdapter(adapter);
        viewBind.categorySpinner.setSelection(position);

        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        viewBind.autoRepetitionSpinner.setAdapter(spinnerArrayAdapter);
        initializeRepetitionSpinner(schedule, array);


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DATE = new DateManager().getDate(dayOfMonth, month+1, year);
                viewBind.dateText.setText(DATE);
            }
        };

        viewBind.dateText.setOnClickListener(new View.OnClickListener() {
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

    private void initializeRepetitionSpinner(ScheduleParcel schedule, String[] array) {
        count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        for(Map.Entry<String, String> map : count.entrySet())
            if(map.getValue().equals(schedule.getRepeat()))
                for(int i = 0; i < array.length; i++)
                    if(array[i].equals(map.getKey()))
                        viewBind.autoRepetitionSpinner.setSelection(i);

    }

    private boolean isDataChanged() {
        String tag = viewBind.tableSpinner.getSelectedItem().toString();
        String category = viewBind.categorySpinner.getSelectedItem().toString();
        String amount = viewBind.amountEdit.getText().toString();
        String notes = viewBind.noteEdit.getText().toString();
        String repeat = count.get(viewBind.autoRepetitionSpinner.getSelectedItem().toString());
        Schedule uSchedule = new Schedule(
                tag, category, Double.parseDouble(amount), notes,schedule.getDate(), repeat, schedule.getWalletID(),schedule.getTransactionID(), schedule.isActive()
        );
        uSchedule.setId(schedule.getID());
        viewModel.updateSchedule(uSchedule);

        return true;
    }

    private void createToastMassage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
