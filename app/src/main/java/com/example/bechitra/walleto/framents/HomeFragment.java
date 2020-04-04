package com.example.bechitra.walleto.framents;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.adapter.DataOrganizerAdapter;
import com.example.bechitra.walleto.databinding.FragmentHomeBinding;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.viewmodel.HomeFragmentViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;

import java.util.List;

;

public class HomeFragment extends Fragment{
    DataOrganizerAdapter adapter;
    FragmentHomeBinding binding;
    HomeFragmentViewModel homeFragmentViewModel;
    private List<Transaction> transactions;
    boolean tableFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.mainActivityLayout.setFocusableInTouchMode(true);
        binding.mainActivityLayout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        homeFragmentViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HomeFragmentViewModel(requireActivity().getApplication());
            }
        }).get(HomeFragmentViewModel.class);

        String[] array = {"Spending", "Earning"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        binding.tableSelectorSpinner.setAdapter(spinnerArrayAdapter);

        binding.currentMonthRecycler.setHasFixedSize(true);
        binding.currentMonthRecycler.setLayoutManager(
                new LinearLayoutManager(view.getContext(),
                        LinearLayoutManager.VERTICAL, false));

        homeFragmentViewModel.getTransactionData().observe(getViewLifecycleOwner(), (transactionList)->{
            transactions = transactionList;
            viewPieChart(transactions);
            refreshData(transactions);
        });

        homeFragmentViewModel.getActiveWalletData().observe(getViewLifecycleOwner(), wallet -> {
                transactions = homeFragmentViewModel.getListOfTransaction();
                viewPieChart(transactions);
                refreshData(transactions);
            Log.d("tag", "i was called "+transactions.size());
        });

        adapter = new DataOrganizerAdapter(requireContext());
        binding.currentMonthRecycler.setAdapter(adapter);
        binding.currentMonthRecycler.setNestedScrollingEnabled(false);


        binding.tableSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    tableFlag = false;
                    adapter.setData(homeFragmentViewModel.getTransactionOfCurrentMonth(transactions, DataRepository.SPENDING_TAG));
                } else {
                    tableFlag = true;
                    adapter.setData(homeFragmentViewModel.getTransactionOfCurrentMonth(transactions, DataRepository.EARNING_TAG));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void refreshData(List<Transaction> transactions) {
        adapter.setData(homeFragmentViewModel.getProcessedTransactionCurrentMonth( transactions,
                (tableFlag) ? DataRepository.EARNING_TAG :DataRepository.SPENDING_TAG));

        double earning = homeFragmentViewModel.getAmountByTag(transactions, DataRepository.EARNING_TAG);
        double spending = homeFragmentViewModel.getAmountByTag(transactions, DataRepository.SPENDING_TAG);
        binding.earnBalanceText.setText(""+earning);
        binding.spendBalanceText.setText(""+spending);
        binding.mainBalance.setText(""+(earning-spending));
    }

    private void viewPieChart(List<Transaction> transactions) {
        PieData pieData = homeFragmentViewModel.getPieData(transactions, binding.halfPieChart.getLegend());
        loadPieScreen();

        binding.halfPieChart.setBackgroundColor(Color.WHITE);
        binding.halfPieChart.setUsePercentValues(true);
        binding.halfPieChart.getDescription().setEnabled(false);
        binding.halfPieChart.setDrawHoleEnabled(true);

        binding.halfPieChart.setRotationAngle(180);
        binding.halfPieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        binding.halfPieChart.setRotationEnabled(false);
        binding.halfPieChart.setDrawEntryLabels(false);

        binding.halfPieChart.setCenterText("Top Category");
        binding.halfPieChart.setCenterTextSize(10);
        binding.halfPieChart.setTransparentCircleRadius(56f);

        binding.halfPieChart.setData(pieData);
        binding.halfPieChart.invalidate();
    }



    private void loadPieScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)binding.halfPieChart.getLayoutParams();
        params.setMargins(0,0,0,0);
        binding.halfPieChart.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
