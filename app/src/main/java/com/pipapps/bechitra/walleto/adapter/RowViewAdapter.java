package com.pipapps.bechitra.walleto.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pipapps.bechitra.walleto.R;
import com.pipapps.bechitra.walleto.activity.DataEditorActivity;
import com.pipapps.bechitra.walleto.room.entity.Transaction;
import com.pipapps.bechitra.walleto.utility.TransactionParcel;
import com.pipapps.bechitra.walleto.utility.DateManager;

import java.util.List;

public class RowViewAdapter extends RecyclerView.Adapter<RowViewAdapter.RowViewer>{
    Context context;
    List<Transaction> data;
    RelativeLayout.LayoutParams params;
    String tableName;

    public RowViewAdapter(Context context) {
        this.context = context;
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public RowViewer onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_row_viewer,  null);
        view.setLayoutParams(params);

        return new RowViewer(view);
    }

    @Override
    public void onBindViewHolder(RowViewer holder, int position) {
        Transaction tData = data.get(position);

        if(!tData.getNote().equals(""))
            holder.note.setText(tData.getNote());

        DateManager stk = new DateManager();
        holder.date.setText(stk.monthStringBuilder(tData.getDate()));
        holder.amount.setText("$"+tData.getAmount());

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setData(List<Transaction> transactions) {
        this.data = transactions;
        notifyDataSetChanged();
    }

    class RowViewer extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView note, date, amount;

        public RowViewer(View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.noteViewItem);
            date = itemView.findViewById(R.id.dateViewItem);
            amount = itemView.findViewById(R.id.amountViewItemText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Transaction transaction = data.get(getAdapterPosition());
            TransactionParcel parcelTransaction = new TransactionParcel(transaction.getTag(), transaction, 0);
            Intent intent = new Intent(context, DataEditorActivity.class);
            intent.putExtra("data", parcelTransaction);
            context.startActivity(intent);
        }
    }
}
