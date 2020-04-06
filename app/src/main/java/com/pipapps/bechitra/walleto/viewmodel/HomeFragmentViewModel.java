package com.pipapps.bechitra.walleto.viewmodel;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pipapps.bechitra.walleto.DataRepository;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.room.entity.Wallet;
import com.pipapps.bechitra.walleto.utility.DataOrganizer;
import com.pipapps.bechitra.walleto.utility.DataProcessor;
import com.pipapps.bechitra.walleto.utility.DateManager;
import com.pipapps.bechitra.walleto.utility.EntrySet;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {
    private DataRepository repository;
    private DataProcessor processor;
    private DateManager manager;
    private String lowerBound, upperBound;
    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        processor = new DataProcessor();
        manager = new DateManager();
        upperBound = manager.getCurrentDate();
        lowerBound = manager.getFirstDate(upperBound);
    }

    public LiveData<List<Transaction>> getTransactionData() { return repository.getTransactionOfActivatedWallet(); }
    public Wallet getActiveWallet() { return repository.getActiveWallet(); }

    public List<Transaction> getListOfTransaction() {
        return processor.getTransactionsByRange(repository.getListTransaction(), lowerBound, upperBound);
    }

    public List<DataOrganizer> getTransactionOfCurrentMonth(List<Transaction> transactions, String tag) {
        return processor.getProcessedTransactionOfCurrentMonth(transactions, tag);
    }

    public LiveData<Wallet> getActiveWalletData() {
        return repository.getActiveWalletData();
    }

    private List<EntrySet> getPieChartData(List<Transaction> data) {
        List<EntrySet> graphData = new ArrayList<>();
        String[]date = manager.getSeparatedDateArray(manager.getCurrentDate());

        String lowerBound = "01/"+date[1]+"/"+date[2];
        String upperBound = manager.getCurrentDate();
        List<Transaction> row = processor.getTransactionsByRange(processor.getTransactionsByTag(data, DataRepository.SPENDING_TAG), lowerBound, upperBound);
        List<String> distinctCategory = new ArrayList<>();

        double totalTransaction = 0;

        if(row != null) {
            for (Transaction t : row) {
                if (distinctCategory.contains(t.getCategory()) && !graphData.isEmpty()) {
                    int categoryIndex = graphData.indexOf(t.getCategory());
                    if(categoryIndex >= 0)
                        graphData.get(categoryIndex).setValue(graphData.get(categoryIndex).getValue() + t.getAmount());
                } else {
                    graphData.add(new EntrySet(t.getCategory(), t.getAmount()));
                    distinctCategory.add(t.getCategory());
                }

                totalTransaction += t.getAmount();
            }

            for(EntrySet entrySet : graphData) {
                int entryIndex = graphData.indexOf(entrySet);
                double transactionRatio = graphData.get(entryIndex).getValue()/totalTransaction;
                graphData.get(entryIndex).setValue(transactionRatio * 100);
            }

            Collections.sort(graphData,(object1, object2) -> compareValue(object1.getValue(), object2.getValue()));
        }
        else
            graphData.add(new EntrySet("Spending", 100.0));


        return graphData;
    }

    public List<DataOrganizer> getProcessedTransactionCurrentMonth(List<Transaction> transactions, String tag) {
        return processor.getProcessedTransactionOfCurrentMonth(transactions, tag);
    }

    public double getAmountByTag(List<Transaction> transactions, String tag) {
        return processor.getAmountByTag(transactions, tag);
    }

    private int compareValue(double value, double value1) {
        if(value > value1)
            return 1;
        else if(value == value1)
            return 0;
        else
            return -1;
    }





    public PieData getPieData(List<Transaction> data, Legend pieLegend) {
        ArrayList<PieEntry> values = new ArrayList<>();
        List<EntrySet> val = getPieChartData(data);
        ArrayList<PieEntry> value = new ArrayList<>();
        for(EntrySet d : val)
            values.add(new PieEntry((float) d.getValue(), d.getKey()));

        int index = (values.size() > 5) ? 5 : values.size();

        for(int i = 1; i <= index; i++) {
            value.add(values.get(values.size()-i));
        }

        PieDataSet dataSet = new PieDataSet(value, "");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setAutomaticallyDisableSliceSpacing(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);
        Legend legend = pieLegend;
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setForm(Legend.LegendForm.CIRCLE);

        return pieData;
    }
}
