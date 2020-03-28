package com.example.bechitra.walleto.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.adapter.RecyclerViewAdapter;
import com.example.bechitra.walleto.databinding.FragmentSpendingEarningBinding;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.example.bechitra.walleto.utility.DataProcessor;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.viewmodel.OverviewFragmentViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bechitra on 3/26/2018.
 */

public class OverviewFragment extends Fragment{
    DateManager dateManager;
    DataProcessor dataProcessor;

    List<CategoryProcessor> list;
    RecyclerViewAdapter recyclerViewAdapter;
    View view;
    OverviewFragmentViewModel viewModel;
    FragmentSpendingEarningBinding viewBind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBind = FragmentSpendingEarningBinding.inflate(inflater, container, false);
        view = viewBind.getRoot();

        viewBind.nestedScroll.setFocusableInTouchMode(true);
        viewBind.nestedScroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        viewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new OverviewFragmentViewModel(requireActivity().getApplication());
            }
        }).get(OverviewFragmentViewModel.class);

        dataProcessor = new DataProcessor(view.getContext());
        dateManager = new DateManager();


        String[] array = {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        viewBind.resultSelectorSpinner.setAdapter(spinnerArrayAdapter);


        viewBind.spendingOrEarningRecyclerView.setHasFixedSize(true);
        viewBind.spendingOrEarningRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        viewModel.getAllTransactionData().observe(getViewLifecycleOwner(), transactions -> {
            double spend = viewModel.getLifeTimeCalculationByTag(transactions, DataRepository.SPENDING_TAG);
            double earn = viewModel.getLifeTimeCalculationByTag(transactions, DataRepository.EARNING_TAG);

            viewBind.lifeTimeEarnText.setText(""+earn);
            viewBind.lifeTimeSpendText.setText(""+spend);
            viewBind.amountEntryTransaction.setText(""+(earn+spend));

            recyclerViewAdapter.setData(viewModel.getRecyclerData(transactions));
            setGraphData(transactions);

        });

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity());
        viewBind.spendingOrEarningRecyclerView.setAdapter(recyclerViewAdapter);
        viewBind.spendingOrEarningRecyclerView.setNestedScrollingEnabled(false);


        return view;
    }

    private void setGraphData(List<Transaction> transactions) {
        ArrayList<ILineDataSet> data = viewModel.getLineChartData(transactions);
        viewBind.lineChart.setData(new LineData(data));
        viewBind.lineChart.getDescription().setEnabled(false);
        XAxis xAxis = viewBind.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        viewBind.lineChart.invalidate();

        final String [] string = {"JAN", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "JAN", "FEB", "MAR", "DEC"};
        xAxis.setValueFormatter((value, axis) -> string[(int)value]);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup vg = (ViewGroup) view;
        vg.setClipChildren(false);
        vg.setClipToPadding(false);
    }
}
