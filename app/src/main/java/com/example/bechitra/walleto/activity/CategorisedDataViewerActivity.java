package com.example.bechitra.walleto.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Window;
import android.view.WindowManager;
import com.example.bechitra.walleto.adapter.RowViewAdapter;
import com.example.bechitra.walleto.databinding.ActivityCategorisedDataViewerBinding;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.CategorisedDataParcel;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.viewmodel.CategorisedDataViewerViewModel;

import java.util.List;

public class CategorisedDataViewerActivity extends AppCompatActivity {
    RowViewAdapter adapter;
    CategorisedDataParcel parcel;
    int color = 0;
    CategorisedDataViewerViewModel viewModel;
    ActivityCategorisedDataViewerBinding viewBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivityCategorisedDataViewerBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());

        viewModel = new ViewModelProvider(this).get(CategorisedDataViewerViewModel.class);

        viewModel.getAllTransaction().observe(this, transactions -> {
            List<Transaction> filteredData = viewModel.getTransactionByTag(transactions, parcel.getTable());

            adapter.setData(
                    viewModel.getCategorisedTransactionWithinBound(
                            filteredData,
                            parcel.getCategory(),
                            parcel.getLowerbound(),
                            parcel.getUpperbound())
            );
        });

        parcel = getIntent().getParcelableExtra("category");
        color = new ColorUtility().getColors(parcel.getCategory());
        viewBind.recordBarText.setBackgroundColor(new ColorUtility().getLighterColor(color, 0.8f));
        viewBind.titleTextView.setText(new DateManager().stringFormatter(parcel.getCategory()));
        statusBarColorChanger(color);
        viewBind.titleBar.setBackgroundColor(new ColorUtility().getColors(parcel.getCategory()));
        viewBind.circleBack.setBackground(getOvalShape(parcel.getCategory()));
        viewBind.circleIcon.setBackgroundResource(new ColorUtility().getResource(parcel.getCategory()));


        viewBind.recyclerView.setHasFixedSize(true);
        viewBind.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new RowViewAdapter(this);
        viewBind.recyclerView.setAdapter(adapter);
        viewBind.recyclerView.setNestedScrollingEnabled(false);

        viewBind.backText.setOnClickListener(view -> finish());
    }

    private GradientDrawable getOvalShape(String categoryString) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(new ColorUtility().getColors(categoryString));
        gd.setShape(GradientDrawable.OVAL);

        return gd;
    }

    private void statusBarColorChanger(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
