package com.example.bechitra.walleto.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.bechitra.walleto.DataRepository;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.WalletCreatorAdapter;
import com.example.bechitra.walleto.dialog.WalletCreatorDialog;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;
import com.example.bechitra.walleto.viewmodel.AccountManagementActivityViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountManagementActivity extends AppCompatActivity {

    @BindView(R.id.activatedWalletName) TextView activeWalletName;
    @BindView(R.id.activeWalletCurrentBalance) TextView activeWalletCurrentBalance;
    @BindView(R.id.totalSpending) TextView activeWalletTotalSpending;
    @BindView(R.id.totalEarning) TextView activeWalletTotalEarning;
    @BindView(R.id.newWalletCreatorLayout) LinearLayout newWalletCreatorLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    Wallet wallet;
    WalletCreatorAdapter adapter;
    List<Wallet> data;
    AccountManagementActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(AccountManagementActivityViewModel.class);

        adapter = new WalletCreatorAdapter(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        viewModel.getWalletList().observe(this, walletList -> {
            data = viewModel.getInactiveWallet(walletList);
            adapter.setData(data);
            wallet = viewModel.getActiveWallet(walletList);
            activeWalletName.setText(wallet.getName());
            activeWalletCurrentBalance.setText("$"+wallet.getBalance());

            List<Transaction> transactions = viewModel.getAllTransaction();

            double spend = viewModel.getBalanceByTaggedTransaction(transactions, DataRepository.SPENDING_TAG);
            double earn = viewModel.getBalanceByTaggedTransaction(transactions, DataRepository.EARNING_TAG);

            activeWalletTotalEarning.setText("$"+earn);
            activeWalletTotalSpending.setText("$"+spend);
        });

        newWalletCreatorLayout.setOnClickListener(view-> {
            WalletCreatorDialog dialog = new WalletCreatorDialog();
            dialog.show(getSupportFragmentManager(), "OK");
            dialog.setDialogListener((regex, flag) -> {
                        if (flag) {
                            wallet.setActive(false);
                            viewModel.updateWallet(wallet);
                            wallet = new Wallet(regex.toUpperCase(), 0, true);
                            viewModel.insertNewWallet(wallet);
                        } else
                            viewModel.insertNewWallet(new Wallet(regex.toUpperCase(), 0, false));

                    }
            );

        });

        adapter.setOnActiveClickedListener((flag, status) -> {
            Wallet active = wallet;
            wallet.setActive(false);
            Log.d("id", "activeWalletID "+active.getId());
            viewModel.updateWallet(wallet);
            wallet = data.get(flag);
            Log.d("id", "updateWalletID "+wallet.getId());
            wallet.setActive(true);
            viewModel.updateWallet(wallet);
        });

        adapter.setOnDeleteClickedListener((tag, flag) -> {
            viewModel.deleteTransactionByID(data.get(flag).getId());
            viewModel.deleteWallet(data.get(flag));
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
