package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.listner.OnLongClickItem;
import com.example.bechitra.walleto.table.Wallet;
import com.example.bechitra.walleto.dialog.listner.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletCreatorAdapter extends RecyclerView.Adapter<WalletCreatorAdapter.WalletCreatorViewHolder>{
    List<Wallet> data;
    Context context;
    RelativeLayout.LayoutParams params;
    DatabaseHelper db;
    List<String> balance;
    OnItemClick listener;
    OnLongClickItem longClickListener;

    public WalletCreatorAdapter(List<Wallet> data, Context context) {
        this.data = data;
        this.context = context;
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

    class WalletCreatorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        @BindView(R.id.walletNameText) TextView walletNameText;
        @BindView(R.id.currentBalance) TextView currentBalanceText;
        @BindView(R.id.circleBackLayout) RelativeLayout layout;
        @BindView(R.id.layoutOverview) RelativeLayout linearLayout;

        public WalletCreatorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition(), true);
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick("TAG", getAdapterPosition());
            return true;
        }
    }

    public void setOnItemClickedListener(OnItemClick listener) {
        this.listener = listener;
    }

    public void setOnLongClickedListener(OnLongClickItem listener) {
        this.longClickListener = listener;
    }

}
