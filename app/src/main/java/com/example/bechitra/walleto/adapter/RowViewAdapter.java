package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.table.TableData;

import java.util.List;

public class RowViewAdapter extends RecyclerView.Adapter<RowViewAdapter.RowViewer>{
    Context context;
    List<TableData> data;
    RelativeLayout.LayoutParams params;

    public RowViewAdapter(Context context, List<TableData> data) {
        this.context = context;
        this.data = data;
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
        TableData tData = data.get(position);

        if(!tData.getNote().equals(""))
            holder.note.setText(tData.getNote());

        StringPatternCreator stk = new StringPatternCreator();
        holder.date.setText(stk.monthStringBuilder(tData.getDate()));
        holder.amount.setText("$"+tData.getAmount());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RowViewer extends RecyclerView.ViewHolder {
        TextView note, date, amount;

        public RowViewer(View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.noteViewItem);
            date = itemView.findViewById(R.id.dateViewItem);
            amount = itemView.findViewById(R.id.amountViewItemText);
        }
    }
}
