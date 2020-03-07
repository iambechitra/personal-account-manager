package com.example.bechitra.walleto.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class DefaultSpinnerAdapter extends ArrayAdapter<String>{
    List<String> data;
    public DefaultSpinnerAdapter(@NonNull Context context, List<String> data) {
        super(context, android.R.layout.simple_spinner_item, data);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
