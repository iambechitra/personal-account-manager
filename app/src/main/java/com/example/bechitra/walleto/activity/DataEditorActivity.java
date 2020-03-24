package com.example.bechitra.walleto.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.DefaultSpinnerAdapter;
import com.example.bechitra.walleto.dialog.RowDeleteDialog;
import com.example.bechitra.walleto.dialog.listener.OnCloseDialogListener;
import com.example.bechitra.walleto.table.Schedule;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.example.bechitra.walleto.utility.TransactionParcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bechitra.walleto.utility.DateManager;

public class DataEditorActivity extends AppCompatActivity {

    @BindView(R.id.amountEdit)
    EditText amountEt;
    @BindView(R.id.noteEdit) EditText note;
    @BindView(R.id.updateButton)
    Button updateButton;
    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;
    @BindView(R.id.circleBack)
    RelativeLayout circleBack;
    @BindView(R.id.circleIcon)
    TextView circleIcon;
    @BindView(R.id.repeatCheck)
    CheckBox repeatCheckbox;
    @BindView(R.id.dateText) TextView dateText;
    @BindView(R.id.backText) TextView back;
    @BindView(R.id.deleteText) TextView delete;
    @BindView(R.id.editText) TextView edit;
    @BindView(R.id.titleBar) RelativeLayout titleBarLayout;
    @BindView(R.id.repeatStatusSpinner) Spinner autoRepetitionSpinner;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    boolean editable = false;
    TransactionParcel data;

    private List<String> spinnerItem;
    DatabaseHelper db;
    Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data_editor);

       // android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        data = getIntent().getParcelableExtra("data");

        statusBarColorChanger(new ColorUtility().getColors(data.getCategory()));

      //  actionBar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        db = new DatabaseHelper(this);

        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        autoRepetitionSpinner.setAdapter(spinnerArrayAdapter);
        autoRepetitionSpinner.setVisibility(View.INVISIBLE);

        final Map<String, String> count = new HashMap<>();
        count.put("Daily", "1");
        count.put("Weekly", "7");
        count.put("Monthly", "30");
        count.put("Yearly", "365");

        // hideSoftKey(amountEt);

        schedule = db.getScheduledData(data.getTag(), data.getCategory(), ""+data.getAmount(),
                data.getNote(), data.getWalletID()+"");

        Log.d("information", data.getTag()+" "+data.getCategory()+" "+
                    data.getAmount()+" "+data.getNote()+" "+data.getWalletID());

        if(schedule != null) {
            autoRepetitionSpinner.setVisibility(View.VISIBLE);
            for(Map.Entry<String, String> map : count.entrySet()) {
                Log.d("schedule key", map.getValue()+" "+schedule.getRepeat());
                if(map.getValue().equals(schedule.getRepeat())){
                    Log.d("map item", map.getKey());

                    for(int i = 0; i < array.length; i++)
                        if(array[i].equals(map.getKey())) {
                        Log.d("auto repeat", array[i]);
                            autoRepetitionSpinner.setSelection(i);
                        }

                    break;
                }
            }
            repeatCheckbox.setChecked(true);
            autoRepetitionSpinner.setEnabled(false);
            autoRepetitionSpinner.setVisibility(View.VISIBLE);
            repeatCheckbox.setEnabled(false);
         } else
             Log.d("schedule", "null");



        repeatCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    autoRepetitionSpinner.setVisibility(View.VISIBLE);
                else
                    autoRepetitionSpinner.setVisibility(View.INVISIBLE);

            }
        });

        setNotEditable(amountEt);
        setNotEditable(note);
        repeatCheckbox.setEnabled(false);

       // actionBar.setBackgroundDrawable(new ColorDrawable(new ColorUtility().getColors(categoryString)));

        updateButton.setBackgroundColor(new ColorUtility().getColors(data.getCategory()));
        titleBarLayout.setBackgroundColor(new ColorUtility().getColors(data.getCategory()));


        circleBack.setBackground(getOvalShape(data.getCategory()));
        circleIcon.setBackgroundResource(new ColorUtility().getResource(data.getCategory()));

        amountEt.setText(""+data.getAmount());
        dateText.setText(data.getDate());
        note.setText(data.getNote());

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
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(position);
        categorySpinner.setEnabled(false);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable = true;
                setEditable(amountEt);
                setEditable(note);
                autoRepetitionSpinner.setEnabled(true);
                repeatCheckbox.setEnabled(true);
                categorySpinner.setEnabled(true);
                updateButton.setVisibility(View.VISIBLE);
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DataEditorActivity.this,
                            R.style.Theme_AppCompat_Light_Dialog, dateSetListener, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                    datePickerDialog.show();
                }
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d = new DateManager().getDate(dayOfMonth, month+1, year);
                dateText.setText(d);
            }
        };

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RowDeleteDialog d = new RowDeleteDialog();
                d.show(getSupportFragmentManager(), "TAG");
                d.setOnCloseDialogManager(new OnCloseDialogListener() {
                    @Override
                    public void onClose(boolean flag) {
                        db.deleteRowFromTable(data.getTag(), ""+data.getID());
                        if(schedule != null)
                            db.deleteRowFromTable("SCHEDULE", schedule.getID());
                        if(data.getFlag() == 1)
                            reloadActivity(MainActivity.class, new Bundle());
                        else {
                           Bundle bundle = new Bundle();
                           bundle.putString("category", data.getCategory());
                           bundle.putString("table", data.getTag());
                           reloadActivity(CategoryItemViewerActivity.class, bundle);
                        }

                    }
                });
            }

        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int color = new ColorUtility().getColors(spinnerItem.get(position));
                int icon = new ColorUtility().getResource(spinnerItem.get(position));
                circleBack.setBackground(getOvalShape(spinnerItem.get(position)));
                circleIcon.setBackgroundResource(icon);
                updateButton.setBackgroundColor(color);
                titleBarLayout.setBackgroundColor(color);
                statusBarColorChanger(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemID = ""+data.getID();
                String category = categorySpinner.getSelectedItem().toString();
                String date = dateText.getText().toString();
                String notes = note.getText().toString();
                String amount = amountEt.getText().toString();
                String repeat = count.get(autoRepetitionSpinner.getSelectedItem().toString());
                db.updateRowFromTable(data.getTag(), itemID, category, amountEt.getText().toString(), note.getText().toString(), date);
                if(repeatCheckbox.isChecked()) {
                    if(schedule != null) {
                        if(!data.getCategory().equals(category))
                            db.updateRowFromTable("SCHEDULE", schedule.getID(), "CATEGORY", category);
                        if(!(""+data.getAmount()).equals(amount))
                            db.updateRowFromTable("SCHEDULE", schedule.getID(), "AMOUNT", amount);
                        //if(!data.getDate().equals(date))
                            //db.updateRowFromTable("SCHEDULE", schedule.getID(), "DATE", date);
                        if(!data.getNote().equals(notes))
                            db.updateRowFromTable("SCHEDULE", schedule.getID(), "NOTE", notes);
                        if(!schedule.getRepeat().equals(repeat))
                            db.updateRowFromTable("SCHEDULE", schedule.getID(), "REPEAT", repeat);
                    } else
                        db.insertNewSchedule(new Schedule(null, data.getTag(), category, ""+amount, notes, date, repeat, ""+data.getWalletID(), "1"));
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
            }
        });

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

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();

        return true;
    }*/
}
