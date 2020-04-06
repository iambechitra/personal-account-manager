package com.pipapps.bechitra.walleto.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.pipapps.bechitra.walleto.DataRepository;
import com.pipapps.bechitra.walleto.MainActivity;
import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.adapter.DefaultSpinnerAdapter;
import com.pipapps.bechitra.walleto.databinding.ActivityDataEditorBinding;
import com.pipapps.bechitra.walleto.dialog.RowDeleteDialog;
import com.pipapps.bechitra.walleto.dialog.listener.OnCloseDialogListener;
import com.pipapps.bechitra.walleto.room.entity.Schedule;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.utility.ColorUtility;
import com.pipapps.bechitra.walleto.utility.TransactionParcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pipapps.bechitra.walleto.utility.DateManager;
import com.pipapps.bechitra.walleto.viewmodel.DataEditorActivityViewModel;

public class DataEditorActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListener;
    boolean editable = false;
    TransactionParcel data;
    private List<String> spinnerItem;
    Schedule schedule;
    DataEditorActivityViewModel viewModel;
    ActivityDataEditorBinding viewBind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityDataEditorBinding.inflate(getLayoutInflater());

        setContentView(viewBind.getRoot());

        data = getIntent().getParcelableExtra("data");

        statusBarColorChanger(new ColorUtility().getColors(data.getCategory()));

        final Map<String, String> count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        viewModel = new ViewModelProvider(this).get(DataEditorActivityViewModel.class);
        schedule = viewModel.getScheduleByTransaction(data.getID());

        scheduleRepeatSet(count);

        viewBind.repeatCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                viewBind.repeatStatusSpinner.setVisibility(View.VISIBLE);
            else
                viewBind.repeatStatusSpinner.setVisibility(View.INVISIBLE);

        });

        setNotEditable(viewBind.amountEdit);
        setNotEditable(viewBind.noteEdit);
        viewBind.repeatCheck.setEnabled(false);

        viewBind.updateButton.setBackgroundColor(new ColorUtility().getColors(data.getCategory()));
        viewBind.titleBar.setBackgroundColor(new ColorUtility().getColors(data.getCategory()));

        viewBind.circleBack.setBackground(getOvalShape(data.getCategory()));
        viewBind.circleIcon.setBackgroundResource(new ColorUtility().getResource(data.getCategory()));
        viewBind.amountEdit.setText(""+data.getAmount());
        viewBind.dateText.setText(data.getDate());
        viewBind.noteEdit.setText(data.getNote());

        List<String> temp;
        if(data.getTag().equals(DataRepository.EARNING_TAG))
            temp = Arrays.asList(getResources().getStringArray(R.array.ECATEGORY));
        else
            temp = Arrays.asList(getResources().getStringArray(R.array.SCATEGORY));

        spinnerItem = new ArrayList<>();

        for(String str : temp)
            spinnerItem.add(str);

        int position = 0;
        for(int i = 0; i < spinnerItem.size(); i++) {
            if (spinnerItem.get(i).equals(data.getCategory())) {
                position = i;
                break;
            }
        }
        DefaultSpinnerAdapter adapter = new DefaultSpinnerAdapter(this, spinnerItem);
        viewBind.categorySpinner.setAdapter(adapter);
        viewBind.categorySpinner.setSelection(position);
        viewBind.categorySpinner.setEnabled(false);


        viewBind.backText.setOnClickListener(view -> finish());

        viewBind.editText.setOnClickListener(view -> {
            editable = true;
            setEditable(viewBind.amountEdit);
            setEditable(viewBind.noteEdit);
            viewBind.repeatStatusSpinner.setEnabled(true);
            viewBind.repeatCheck.setEnabled(true);
            viewBind.categorySpinner.setEnabled(true);
            viewBind.updateButton.setVisibility(View.VISIBLE);
        });

        viewBind.dateText.setOnClickListener(view -> {
            if(editable) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DataEditorActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                datePickerDialog.show();
            }
        });

        dateSetListener = (view, year, month, dayOfMonth) -> {
            String d = new DateManager().getDate(dayOfMonth, month+1, year);
            viewBind.dateText.setText(d);
        };

        viewBind.deleteText.setOnClickListener(view -> {
            RowDeleteDialog d = new RowDeleteDialog();
            d.show(getSupportFragmentManager(), "TAG");
            d.setOnCloseDialogManager(new OnCloseDialogListener() {
                @Override
                public void onClose(boolean flag) {
                    viewModel.deleteTransaction(new Transaction(
                            data.getID(), data.getCategory(), data.getAmount(), data.getNote(),
                            data.getDate(), data.getTag(), data.getWalletID()
                    ));
                    if(schedule != null)
                        viewModel.deleteSchedule(schedule);


                    if(data.getFlag() == 1)
                        reloadActivity(MainActivity.class, new Bundle());
                    else {
                       Bundle bundle = new Bundle();
                       bundle.putString("category", data.getCategory());
                       bundle.putString("table", data.getTag());
                       reloadActivity(CategoryItemViewerActivity.class, bundle);
                    }

                    finish();
                }
            });
        });

        viewBind.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int color = new ColorUtility().getColors(spinnerItem.get(position));
                int icon = new ColorUtility().getResource(spinnerItem.get(position));
                viewBind.circleBack.setBackground(getOvalShape(spinnerItem.get(position)));
                viewBind.circleIcon.setBackgroundResource(icon);
                viewBind.updateButton.setBackgroundColor(color);
                viewBind.titleBar.setBackgroundColor(color);
                statusBarColorChanger(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewBind.updateButton.setOnClickListener(view -> {
            String category = viewBind.categorySpinner.getSelectedItem().toString();
            String date = viewBind.dateText.getText().toString();
            String notes = viewBind.noteEdit.getText().toString();
            String amount = viewBind.amountEdit.getText().toString();
            String repeat = count.get(viewBind.repeatStatusSpinner.getSelectedItem().toString());

            Schedule uSchedule = new Schedule(
                    data.getTag(), category, Double.parseDouble(amount), notes,
                    date, repeat, data.getWalletID(), data.getID(), true
            );

            viewModel.updateTransaction(new Transaction(
                    data.getID(), category, Double.parseDouble(amount), notes, date, data.getTag(), data.getWalletID()
            ));

            if(viewBind.repeatCheck.isChecked()) {
                if (schedule != null) {
                    uSchedule.setId(schedule.getId());
                    viewModel.updateSchedule(uSchedule);
                    Log.d("tag", "update Schedule");
                }
                else {
                    Log.d("tag", "new Schedule "+uSchedule.getTransactionID()+" "+data.getID());
                    viewModel.insertSchedule(uSchedule);
                }

            }
            if(data.getFlag() == 1) {
                reloadActivity(MainActivity.class, new Bundle());
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putString("category", data.getCategory());
                bundle.putString("table", data.getTag());
                reloadActivity(CategoryItemViewerActivity.class, bundle);
            }
        });

    }

    private void scheduleRepeatSet(Map<String, String> count) {
        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        viewBind.repeatStatusSpinner.setAdapter(spinnerArrayAdapter);
        viewBind.repeatStatusSpinner.setVisibility(View.INVISIBLE);

        if(schedule != null) {
            viewBind.repeatStatusSpinner.setVisibility(View.VISIBLE);
            for(Map.Entry<String, String> map : count.entrySet()) {
                if(map.getValue().equals(schedule.getRepeat())){
                    for(int i = 0; i < array.length; i++)
                        if(array[i].equals(map.getKey()))
                            viewBind.repeatStatusSpinner.setSelection(i);


                    break;
                }
            }
            viewBind.repeatCheck.setChecked(true);
            viewBind.repeatStatusSpinner.setEnabled(false);
            viewBind.repeatStatusSpinner.setVisibility(View.VISIBLE);
            viewBind.repeatCheck.setEnabled(false);
        } else
            Log.d("schedule", "null");

    }

    private void hideSoftKey(View view)
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setNotEditable(EditText view) {
        view.setTag(view.getKeyListener());
        view.setKeyListener(null);
    }

    private void setEditable(EditText view) {
        view.setKeyListener((KeyListener)view.getTag());
    }

    private GradientDrawable getOvalShape(String categoryString) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(new ColorUtility().getColors(categoryString));
        gd.setShape(GradientDrawable.OVAL);

        return gd;
    }

    private void reloadActivity(Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        finish();
        startActivity(intent);
    }

    private void statusBarColorChanger(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
