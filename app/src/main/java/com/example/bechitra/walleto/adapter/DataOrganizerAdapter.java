package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.activity.DataEditorActivity;
import com.example.bechitra.walleto.utility.ColorUtility;
import com.example.bechitra.walleto.utility.DataOrganizer;

import java.util.List;

public class DataOrganizerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<DataOrganizer> list;
    RelativeLayout.LayoutParams params;
    DateManager spc;

    public DataOrganizerAdapter(Context context) {
        this.context = context;
        this.list = list;
        spc = new DateManager();
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.data_lebel,  null);
            view.setLayoutParams(params);

            return new LabelViewer(view);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_transection_overview,  null);
        view.setLayoutParams(params);
        return new DataViewer(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == 0) {
            LabelViewer label = (LabelViewer)holder;
            DataOrganizer data = list.get(position);
            String date = data.getLabel().getDate();

            if(date.equals(spc.getCurrentDate()))
                label.date.setText("Today");
            else {
                String[] str = spc.getSeparatedDateArray(date);
                int day = Integer.parseInt(str[0]);
                String d = Integer.toString(day+1);
                String pattern = d+"/"+str[1]+"/"+str[2];

                if(pattern.equals(spc.getCurrentDate()))
                    label.date.setText("Yesterday");
                else
                    label.date.setText(date);
            }

            label.amount.setText("$"+data.getLabel().getAmount());
        } else {
            DataViewer view = (DataViewer)holder;
            DataOrganizer data = list.get(position);
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(new ColorUtility().getColors(data.getData().getCategory()));
            gd.setShape(GradientDrawable.OVAL);

            view.layout.setBackground(gd);
            view.icon.setBackgroundResource(new ColorUtility().getResource(data.getData().getCategory()));
            view.category.setText(data.getData().getCategory());
            view.amount.setText("$"+data.getData().getAmount());
        }
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isDataLabel())
            return 0;

        return 1;
    }

    public void setData(List<DataOrganizer> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    class LabelViewer extends RecyclerView.ViewHolder {
        TextView date, amount;

        public LabelViewer(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateEntry);
            amount = itemView.findViewById(R.id.amountEntry);
        }
    }

    class DataViewer extends RecyclerView.ViewHolder {
        TextView icon, category, amount;
        RelativeLayout layout;

        public DataViewer(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickEvent(getAdapterPosition());
                }
            });

            icon = itemView.findViewById(R.id.cicularIconText);
            category = itemView.findViewById(R.id.categoryItemText);
            amount = itemView.findViewById(R.id.categoryAmountText);
            layout = itemView.findViewById(R.id.circleBackLayout);
        }
    }

    public void itemClickEvent(int adapterPosition) {
        Intent intent = new Intent(context, DataEditorActivity.class);
        Transaction primeTable = list.get(adapterPosition).getData();
        String tableName = list.get(adapterPosition).getTable();

        //DataParser parser = new DataParser(tableName, primeTable, 1);

        //intent.putExtra("data", parser);
        //context.startActivity(intent);
    }
}
