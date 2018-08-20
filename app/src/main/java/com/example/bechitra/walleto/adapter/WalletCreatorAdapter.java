package com.example.bechitra.walleto.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.WalletManagementDialog;
import com.example.bechitra.walleto.dialog.listener.DialogListener;
import com.example.bechitra.walleto.dialog.listener.OnLongClickItem;
import com.example.bechitra.walleto.table.Wallet;
import com.example.bechitra.walleto.dialog.listener.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bechitra.walleto.utility.ColorUtility;

public class WalletCreatorAdapter extends RecyclerView.Adapter<WalletCreatorAdapter.WalletCreatorViewHolder>{
    List<Wallet> data;
    Context context;
    RelativeLayout.LayoutParams params;
    DatabaseHelper db;
    List<String> balance;
    OnItemClick listener;
    ColorUtility colorUtility;
    OnLongClickItem longClickListener;

    public WalletCreatorAdapter(List<Wallet> data, Context context) {
        this.data = data;
        this.context = context;
        this.colorUtility = new ColorUtility();
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        db = new DatabaseHelper(context);
        balance = new ArrayList<>();

        if(this.data.size() > 0)
            for(Wallet w : data)
                balance.add(db.getCurrentBalance(w.getID()));
    }

    @Override
    public WalletCreatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_wallet_viewer,  null);
        view.setLayoutParams(params);

        return new WalletCreatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalletCreatorViewHolder holder, int position) {
        Wallet wallet = data.get(position);
        holder.currentBalanceText.setText("$"+balance.get(position));
        holder.walletNameText.setText(wallet.getName());
        holder.layout.setBackground(colorUtility.generateOvalShape(colorUtility.getRandomColor()));
        char ch = wallet.getName().charAt(0);
        holder.textInCircle.setText(""+ch);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Wallet> data) {
        this.data = data;
        balance.clear();

        if(this.data.size() > 0)
            for(Wallet w : data)
                balance.add(db.getCurrentBalance(w.getID()));
    }

    class WalletCreatorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.walletNameText) TextView walletNameText;
        @BindView(R.id.currentBalance) TextView currentBalanceText;
        @BindView(R.id.circleBackLayout) RelativeLayout layout;
        @BindView(R.id.circularIconText) TextView textInCircle;
        @BindView(R.id.layoutOverview) RelativeLayout linearLayout;

        public WalletCreatorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            WalletManagementDialog dialog = new WalletManagementDialog();
            dialog.show(((FragmentActivity)context).getSupportFragmentManager(), "TAG");
            dialog.setDialogListener(new DialogListener() {
                @Override
                public void onSetDialog(String regex, boolean flag) {
                    if(flag) {
                        if(regex.equals("1"))
                            listener.onClick(getAdapterPosition(), true);
                        else
                            longClickListener.onLongClick("TAG", getAdapterPosition());
                    }
                }
            });

        }
    }

    public void setOnItemClickedListener(OnItemClick listener) {
        this.listener = listener;
    }

    public void setOnLongClickedListener(OnLongClickItem listener) {
        this.longClickListener = listener;
    }

}
