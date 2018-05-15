package com.example.bechitra.walleto.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.CategoryViewDialog;
import com.example.bechitra.walleto.utility.CategoryProcessor;
import com.example.bechitra.walleto.utility.ColorUtility;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{
    List<CategoryProcessor> list;
    static int counter = 0;
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
        gd.setColor(new ColorUtility().getColors(categoryProcessor.getName()));
        gd.setShape(GradientDrawable.OVAL);

        holder.layout.setBackground(gd);
        holder.icon.setBackgroundResource(new ColorUtility().getResource(categoryProcessor.getName()));
        if(Integer.parseInt(categoryProcessor.getNum()) > 1)
            holder.transaction.setText(categoryProcessor.getNum()+" transactions");

        holder.amount.setText("$"+categoryProcessor.getAmount());

        List<String>str = Arrays.asList(context.getResources().getStringArray(R.array.ECATEGORY));
        for(String s : str) {
            if(s.equals(categoryProcessor.getName())) {
                holder.amount.setTextColor(Color.GREEN);
                break;
            }
        }

        holder.category.setText(categoryProcessor.getName());
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
                    Bundle bundle = new Bundle();
                    CategoryViewDialog dialog = new CategoryViewDialog();
                    CategoryProcessor categoryProcessor = list.get(getAdapterPosition());
                    bundle.putString("category", categoryProcessor.getName());
                    dialog.setArguments(bundle);
                    dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "TAG");
                }
            });
            icon = itemView.findViewById(R.id.cicularIconText);
            category = itemView.findViewById(R.id.categoryItemText);
            amount = itemView.findViewById(R.id.categoryAmountText);
            layout = itemView.findViewById(R.id.circleBackLayout);
            transaction = itemView.findViewById(R.id.transactionNumber);
        }
    }


}
