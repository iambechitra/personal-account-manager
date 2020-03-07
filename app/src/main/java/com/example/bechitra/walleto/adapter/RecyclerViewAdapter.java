package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.activity.CategoryItemViewerActivity;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.example.bechitra.walleto.utility.ColorUtility;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{
    List<CategoryProcessor> list;
    Context context;
    RelativeLayout.LayoutParams params;

    public RecyclerViewAdapter(List<CategoryProcessor> list, Context context) {
        this.list = list;
        this.context = context;
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_transection_overview,  null);
        view.setLayoutParams(params);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        CategoryProcessor categoryProcessor = list.get(position);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(new ColorUtility().getColors(categoryProcessor.getCategory()));
        gd.setShape(GradientDrawable.OVAL);

        holder.layout.setBackground(gd);
        holder.icon.setBackgroundResource(new ColorUtility().getResource(categoryProcessor.getCategory()));
        if(Integer.parseInt(categoryProcessor.getLength()) > 1)
            holder.transaction.setText(categoryProcessor.getLength()+" transactions");

        holder.amount.setText("$"+categoryProcessor.getAmount());

        List<String>str = Arrays.asList(context.getResources().getStringArray(R.array.ECATEGORY));
        for(String s : str) {
            if(s.equals(categoryProcessor.getCategory())) {
                holder.amount.setTextColor(Color.parseColor("#2E7D32"));
                break;
            }
        }

        holder.category.setText(categoryProcessor.getCategory());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView icon, category, amount, transaction;
        RelativeLayout layout;
        public RecyclerViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAction(getAdapterPosition());
                }
            });
            icon = itemView.findViewById(R.id.cicularIconText);
            category = itemView.findViewById(R.id.categoryItemText);
            amount = itemView.findViewById(R.id.categoryAmountText);
            layout = itemView.findViewById(R.id.circleBackLayout);
            transaction = itemView.findViewById(R.id.transactionNumber);
        }
    }

    private void onClickAction(int position) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, CategoryItemViewerActivity.class);
        CategoryProcessor categoryProcessor = list.get(position);
        bundle.putString("category", categoryProcessor.getCategory());
        bundle.putString("table", categoryProcessor.getTable());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
