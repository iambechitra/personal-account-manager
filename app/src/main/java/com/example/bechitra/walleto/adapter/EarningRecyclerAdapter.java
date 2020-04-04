package com.example.bechitra.walleto.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DateManager;

import java.util.List;

/// %%%%%%%%%%%%%%%%%%%%%%%%% NO USE OF THIS ADAPTER %%%%%%%%%%%%%%%%%%%%%%%%%%%%% \\\

public class EarningRecyclerAdapter extends RecyclerView.Adapter<EarningRecyclerAdapter.EarningRecyclerViewGroup>{
    private Context context;
    private List<Transaction> earningList;
    private RelativeLayout.LayoutParams params;

    public EarningRecyclerAdapter(Context context, List<Transaction> earningList) {
        this.context = context;
        this.earningList = earningList;
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public EarningRecyclerViewGroup onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_earning_recycler,  null);
        view.setLayoutParams(params);
        return new EarningRecyclerViewGroup(view);
    }

    @Override
    public void onBindViewHolder(EarningRecyclerViewGroup holder, int position) {
        DateManager spc = new DateManager();
        Transaction earning = earningList.get(position);
        holder.categoryOfEarning.setText(earning.getCategory());
        holder.earningAmount.setText("$"+earning.getAmount());
        holder.earningDate.setText(spc.monthStringBuilder(earning.getDate()));
    }

    @Override
    public int getItemCount() {
        return earningList.size();
    }

    public void setData(List<Transaction> list) {
        this.earningList = list;
    }

    class EarningRecyclerViewGroup extends RecyclerView.ViewHolder {
        TextView categoryOfEarning, earningAmount, earningDate;

        public EarningRecyclerViewGroup(View itemView) {
            super(itemView);

            categoryOfEarning = itemView.findViewById(R.id.earningRecyclerCategory);
            earningAmount = itemView.findViewById(R.id.earningRecyclerAmount);
            earningDate = itemView.findViewById(R.id.earningRecyclerDate);

        }
    }

    private String getMonthName(int month) {
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return str[month-1];
    }
}
