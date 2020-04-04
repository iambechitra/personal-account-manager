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
/// ############################### NO USE OF THIS ADAPTER ######################### \\\
public class SpendingRecyclerAdapter extends RecyclerView.Adapter<SpendingRecyclerAdapter.SpendingRecyclerViewHolder>{
    private List<Transaction> spendingsList;
    private Context context;
    private RelativeLayout.LayoutParams params;

    public SpendingRecyclerAdapter(List<Transaction> spendingsList, Context context) {
        this.spendingsList = spendingsList;
        this.context = context;
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public SpendingRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_spending_recycler,  null);
        view.setLayoutParams(params);
        return new SpendingRecyclerViewHolder(view);
    }

    public void setData(List<Transaction> list) {
        this.spendingsList = list;
    }

    @Override
    public void onBindViewHolder(SpendingRecyclerViewHolder holder, int position) {
        Transaction spending = spendingsList.get(position);
        DateManager spc = new DateManager();

        String []str = spc.getSeparatedDateArray(spending.getDate());

        holder.spendingRecyclerDay.setText(str[0]);
        holder.spendingRecyclerMonth.setText(spc.getMonthName(Integer.parseInt(str[1])));
        holder.spendingRecyclerYear.setText(str[2]);
        holder.spendingRecyclerTitle.setText(""+spending.getId());
        holder.spendingRecyclerCategory.setText(spending.getCategory());
        holder.spendingRecyclerNote.setText(spending.getNote());
        holder.spendingRecyclerAmount.setText("$"+spending.getAmount());

    }

    @Override
    public int getItemCount() {
        return spendingsList.size();
    }

    class SpendingRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView spendingRecyclerCategory, spendingRecyclerTitle, spendingRecyclerNote, spendingRecyclerAmount, spendingRecyclerDay, spendingRecyclerMonth, spendingRecyclerYear;

        public SpendingRecyclerViewHolder(View itemView) {
            super(itemView);
            spendingRecyclerTitle = itemView.findViewById(R.id.spendingRecyclerTitleText);
            spendingRecyclerCategory = itemView.findViewById(R.id.spendingRecyclerCategoryText);
            spendingRecyclerNote = itemView.findViewById(R.id.spendingRecyclerNoteText);
            spendingRecyclerAmount = itemView.findViewById(R.id.spendingRecyclerAmountText);
            spendingRecyclerDay = itemView.findViewById(R.id.spendingRecyclerDayText);
            spendingRecyclerMonth = itemView.findViewById(R.id.spendingRecyclerMonthText);
            spendingRecyclerYear = itemView.findViewById(R.id.spendingRecyclerYearText);
        }
    }
}
