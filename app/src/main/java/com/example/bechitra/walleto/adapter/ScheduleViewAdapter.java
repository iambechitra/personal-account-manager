package com.example.bechitra.walleto.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.activity.ScheduleManagementActivity;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.utility.DateManager;
import com.example.bechitra.walleto.dialog.listener.OnDeleteItem;
import com.example.bechitra.walleto.utility.ScheduleParcel;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.DataBinder>{
    Context context;
    List<Schedule> data;
    RelativeLayout.LayoutParams params;

    OnDeleteItem listener;

    public ScheduleViewAdapter(Context context) {
        this.context = context;
        this.params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public DataBinder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_schedule_data,  null);
        view.setLayoutParams(params);

        return new DataBinder(view);
    }

    public void setData(List<Schedule> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataBinder holder, int position) {
        Schedule schedule = data.get(position);
        holder.categoryText.setText(schedule.getCategory());
        holder.itemAmountText.setText(schedule.getAmount()+"$");
        holder.tableName.setText(schedule.getTag());
        holder.lastRepeatDateText.setText(schedule.getDate());

        if(schedule.getRepeat().equals("1"))
            holder.repetitionText.setText("Repeat Tomorrow");
        else {
            DateManager spc = new DateManager();
            String currentDate = spc.getCurrentDate();
            long diff = 0;
            try {
                diff = spc.dateDifference(currentDate, spc.addDate(schedule.getDate(), Integer.parseInt(schedule.getRepeat())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.repetitionText.setText("Repeat within "+diff+" days");
        }
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    class DataBinder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.repetitionText) TextView repetitionText;
        @BindView(R.id.lastRepeatDateText) TextView lastRepeatDateText;
        @BindView(R.id.tableName) TextView tableName;
        @BindView(R.id.itemAmountText) TextView itemAmountText;
        @BindView(R.id.categoryText) TextView categoryText;
        @BindView(R.id.relativeLayout) RelativeLayout relativeLayout;

        public DataBinder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ScheduleManagementActivity.class);
            ScheduleParcel parcelSchedule = new ScheduleParcel(data.get(getAdapterPosition()));
            intent.putExtra("schedule", parcelSchedule);
            context.startActivity(intent);
        }
    }

    public void setOnDeleteItemListener(OnDeleteItem listener) {
        this.listener = listener;
    }
}
