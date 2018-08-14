package com.example.bechitra.walleto.activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.MainActivity;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.adapter.WalletCreatorAdapter;
import com.example.bechitra.walleto.dialog.WalletCreatorDialog;
import com.example.bechitra.walleto.dialog.listener.DialogListener;
import com.example.bechitra.walleto.dialog.listener.OnItemClick;
import com.example.bechitra.walleto.dialog.listener.OnLongClickItem;
import com.example.bechitra.walleto.table.Wallet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountManagementActivity extends AppCompatActivity {

    @BindView(R.id.activatedWalletName) TextView activeWalletName;
    @BindView(R.id.activeWalletCurrentBalance) TextView activeWalletCurrentBalance;
    @BindView(R.id.totalSpending) TextView activeWalletTotalSpending;
    @BindView(R.id.totalEarning) TextView activeWalletTotalEarning;
    @BindView(R.id.newWalletCreatorLayout) RelativeLayout newWalletCreatorLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.nestedScroll)
    NestedScrollView scrollView;

    DatabaseHelper db;
    Wallet wallet;
    WalletCreatorAdapter adapter;
    List<Wallet> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);

        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        db = new DatabaseHelper(this);
        data = db.getInactivateWallet();

        adapter = new WalletCreatorAdapter(data, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        wallet = db.getActivatedWallet();
        setActivatedWalletInfo();

        newWalletCreatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletCreatorDialog dialog = new WalletCreatorDialog();
                dialog.show(getSupportFragmentManager(), "OK");
                dialog.setDialogListener(new DialogListener() {
                    @Override
                    public void onSetDialog(String regex, boolean flag) {
                        if(flag) {
                            db.updateWalletTable(wallet.getID(), "ACTIVATED", "0");
                            db.insertNewWallet(regex.toUpperCase(), flag);
                            setActivatedWalletInfo();
                        } else
                            db.insertNewWallet(regex.toUpperCase(), flag);
                        reloadDataToRecycler();
                    }
                });
            }
        });

        adapter.setOnItemClickedListener(new OnItemClick() {
            @Override
            public void onClick(int flag, boolean status) {
                db.updateWalletTable(wallet.getID(), "ACTIVATED", "0");
                db.updateWalletTable(data.get(flag).getID(), "ACTIVATED", "1");
                setActivatedWalletInfo();
                reloadDataToRecycler();
            }
        });

        adapter.setOnLongClickedListener(new OnLongClickItem() {
            @Override
            public void onLongClick(String tag, int flag) {
                db.deleteRowFromTable(db.getWalletTable(),"WALLET_ID",  data.get(flag).getID());
                db.deleteRowFromTable(db.getSpendingTable(), "WALLET_ID", data.get(flag).getID());
                db.deleteRowFromTable(db.getEarningTable(), "WALLET_ID", data.get(flag).getID());
                reloadDataToRecycler();
            }
        });

    }

    private void reloadDataToRecycler() {
        data = db.getInactivateWallet();
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    private void setActivatedWalletInfo() {
        wallet = db.getActivatedWallet();
        String balance = db.getCurrentBalance(wallet.getID());
        String earning = db.getTotalAmount(db.getEarningTable(), wallet.getID());
        String spending = db.getTotalAmount(db.getSpendingTable(), wallet.getID());

        activeWalletName.setText(wallet.getName());
        activeWalletCurrentBalance.setText("$"+balance);
        activeWalletTotalEarning.setText("$"+earning);
        activeWalletTotalSpending.setText("$"+spending);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
