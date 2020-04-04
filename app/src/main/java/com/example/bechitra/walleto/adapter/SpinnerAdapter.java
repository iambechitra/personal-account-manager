package com.example.bechitra.walleto.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.utility.ColorUtility;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter{
    private List<String> data;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<String>data) {
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = inflater.inflate(R.layout.layout_spinner_item, null);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(new ColorUtility().getColors(data.get(position)));
        gd.setShape(GradientDrawable.OVAL);

        RelativeLayout back = view.findViewById(R.id.circleBack);
        back.setBackground(gd);

        TextView icon = view.findViewById(R.id.circularIcon);
        icon.setBackgroundResource(new ColorUtility().getResource(data.get(position)));
        TextView category = view.findViewById(R.id.categoryItem);
        category.setText(data.get(position));

        return view;
    }
}
