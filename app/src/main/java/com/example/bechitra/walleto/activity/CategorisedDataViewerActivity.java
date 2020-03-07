package com.example.bechitra.walleto.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.RowViewAdapter;
import com.example.bechitra.walleto.table.PrimeTable;
import com.example.bechitra.walleto.utility.CategorisedDataParcel;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.example.bechitra.walleto.utility.DateManager;

import java.util.ArrayList;
import java.util.List;

public class CategorisedDataViewerActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.circleBack) RelativeLayout circleBack;
    @BindView(R.id.titleBar) RelativeLayout titleBar;
    @BindView(R.id.circleIcon) TextView circleIcon;
    @BindView(R.id.titleTextView ) TextView titleTextView;
    @BindView(R.id.recordBarText) TextView recordBarText;
    @BindView(R.id.backText) TextView backText;

    RowViewAdapter adapter;
    DatabaseHelper db;
    CategorisedDataParcel parcel;
    int color = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorised_data_viewer);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);
        parcel = getIntent().getParcelableExtra("category");
        color = new ColorUtility().getColors(parcel.getCategory());
        recordBarText.setBackgroundColor(new ColorUtility().getLighterColor(color, 0.8f));
        titleTextView.setText(new DateManager().stringFormatter(parcel.getCategory()));
        statusBarColorChanger(color);
        titleBar.setBackgroundColor(new ColorUtility().getColors(parcel.getCategory()));
        circleBack.setBackground(getOvalShape(parcel.getCategory()));
        circleIcon.setBackgroundResource(new ColorUtility().getResource(parcel.getCategory()));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new RowViewAdapter(this, getDataOnCategory(), parcel.getTable());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private List<PrimeTable> getDataOnCategory() {
        List<PrimeTable> list = db.getDataWithinARange(parcel.getTable(), parcel.getLowerbound(), parcel.getUpperbound());
        List<PrimeTable> filtered = new ArrayList<>();

        for(PrimeTable p : list)
            if(p.getCategory().equals(parcel.getCategory()))
                filtered.add(p);

        return  filtered;
    }
}
