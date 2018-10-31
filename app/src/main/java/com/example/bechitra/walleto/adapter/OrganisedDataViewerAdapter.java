package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.activity.CategorisedDataViewerActivity;
import com.example.bechitra.walleto.utility.*;
import com.example.bechitra.walleto.activity.DataEditorActivity;
import com.example.bechitra.walleto.table.PrimeTable;

import java.util.List;

public class OrganisedDataViewerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<DataOrganizer> list;
    RelativeLayout.LayoutParams params;
    DateManager spc;

    public OrganisedDataViewerAdapter(Context context, List<DataOrganizer> list) {
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
            label.date.setText(date);
            label.amount.setText("$"+data.getLabel().getAmount());
        } else {
            DataViewer view = (DataViewer)holder;
            DataOrganizer data = list.get(position);
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(new ColorUtility().getColors(data.getCategoryProcessor().getCategory()));
            gd.setShape(GradientDrawable.OVAL);

            view.layout.setBackground(gd);
            view.icon.setBackgroundResource(new ColorUtility().getResource(data.getCategoryProcessor().getCategory()));
            view.category.setText(data.getCategoryProcessor().getCategory());
            view.amount.setText("$"+data.getCategoryProcessor().getAmount());

            int length = Integer.parseInt(data.getCategoryProcessor().getLength());
            String transaction = "1 transaction";

            if(length > 1)
                transaction = (length+" transactions");

            view.totalTransection.setText(transaction);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isDataLabel())
            return 0;

        return 1;
    }

    public void setData(List<DataOrganizer> data) {
        this.list = data;
    }

    class LabelViewer extends RecyclerView.ViewHolder {
        TextView date, amount;

        public LabelViewer(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateEntry);
            amount = itemView.findViewById(R.id.amountEntry);
        }
    }

    class DataViewer extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.cicularIconText) TextView icon;
        @BindView(R.id.categoryItemText) TextView category;
        @BindView(R.id.categoryAmountText) TextView amount;
        @BindView(R.id.circleBackLayout) RelativeLayout layout;
        @BindView(R.id.transactionNumber) TextView totalTransection;

        public DataViewer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, CategorisedDataViewerActivity.class);
            CategorisedDataParcel parcel = new CategorisedDataParcel(list.get(getAdapterPosition()).getCategoryProcessor());
            intent.putExtra("category", parcel);
            context.startActivity(intent);
        }
    }

}
