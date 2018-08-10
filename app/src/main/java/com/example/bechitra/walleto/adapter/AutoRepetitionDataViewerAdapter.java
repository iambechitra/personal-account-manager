package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.dialog.listner.OnDeleteItem;
import com.example.bechitra.walleto.table.TableData;
import com.example.bechitra.walleto.utility.ScheduleData;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoRepetitionDataViewerAdapter extends RecyclerView.Adapter<AutoRepetitionDataViewerAdapter.DataBinder>{
    Context context;
    List<ScheduleData> data;
    RelativeLayout.LayoutParams params;
    DatabaseHelper db;

    OnDeleteItem listener;

    public AutoRepetitionDataViewerAdapter(Context context, List<ScheduleData> data) {
        this.data = data;
        this.context = context;
        db = new DatabaseHelper(context);
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public DataBinder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_schedule_data,  null);
        view.setLayoutParams(params);

        return new DataBinder(view);
    }

    public void setData(List<ScheduleData> list) { this.data = list; }

    @Override
    public void onBindViewHolder(DataBinder holder, int position) {
        ScheduleData schedule = data.get(position);
        holder.categoryText.setText(schedule.getCategory());
        holder.itemAmountText.setText(schedule.getAmount()+"$");
        holder.tableName.setText(schedule.getTable());
        holder.lastRepeatDateText.setText(schedule.getLastRepeat());
        Log.d("DIFF", schedule.getRepetitionInterval());

        if(schedule.getRepetitionInterval().equals("1"))
            holder.repetitionText.setText("Repeat Tomorrow");
        else {
            StringPatternCreator spc = new StringPatternCreator();
            String currentDate = spc.getCurrentDate();
            long diff = 0;
            try {
                diff = spc.dateDifference(currentDate, spc.addDate(schedule.getLastRepeat(), Integer.parseInt(schedule.getRepetitionInterval())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.repetitionText.setText("Repeat within "+diff+" days");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DataBinder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        @BindView(R.id.repetitionText) TextView repetitionText;
        @BindView(R.id.lastRepeatDateText) TextView lastRepeatDateText;
        @BindView(R.id.tableName) TextView tableName;
        @BindView(R.id.itemAmountText) TextView itemAmountText;
        @BindView(R.id.categoryText) TextView categoryText;
        @BindView(R.id.relativeLayout) RelativeLayout relativeLayout;

        public DataBinder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            listener.onDelete(data.get(getAdapterPosition()).getID(), getAdapterPosition());
            return true;
        }
    }

    public void setOnDeleteItemListener(OnDeleteItem listener) {
        this.listener = listener;
    }
}
