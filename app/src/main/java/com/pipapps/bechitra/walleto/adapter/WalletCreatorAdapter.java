package com.pipapps.bechitra.walleto.adapter;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.dialog.WalletManagementDialog;
import com.pipapps.bechitra.walleto.dialog.listener.DialogListener;
import com.pipapps.bechitra.walleto.dialog.listener.OnLongClickItem;
import com.pipapps.bechitra.walleto.dialog.listener.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.pipapps.bechitra.walleto.room.entity.Wallet;
import com.pipapps.bechitra.walleto.utility.ColorUtility;

public class WalletCreatorAdapter extends RecyclerView.Adapter<WalletCreatorAdapter.WalletCreatorViewHolder>{
    List<Wallet> data;
    Context context;
    RelativeLayout.LayoutParams params;
    List<String> balance;
    OnItemClick listener;
    ColorUtility colorUtility;
    OnLongClickItem longClickListener;

    public WalletCreatorAdapter(Context context) {
        this.context = context;
        this.colorUtility = new ColorUtility();
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        balance = new ArrayList<>();
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
        if (data != null)
            return data.size();

        return 0;
    }

    public void setData(List<Wallet> data) {
        this.data = data;
        balance.clear();

        if (this.data.size() > 0)
            for (Wallet w : data)
                balance.add(""+w.getBalance());

        notifyDataSetChanged();
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
                            listener.onActiveClick(getAdapterPosition(), true);
                        else
                            longClickListener.onDeleteClick("TAG", getAdapterPosition());
                    }
                }
            });

        }
    }

    public void setOnActiveClickedListener(OnItemClick listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickedListener(OnLongClickItem listener) {
        this.longClickListener = listener;
    }

}
