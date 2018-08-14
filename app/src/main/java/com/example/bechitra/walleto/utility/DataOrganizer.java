package com.example.bechitra.walleto.utility;

import com.example.bechitra.walleto.table.TableData;

public class DataOrganizer {
    DataLabel label;
    TableData data;
    String table;
    boolean isDataLabel;

    public DataOrganizer(DataLabel label, TableData data, boolean isDataLabel) {
        this.label = label;
        this.data = data;
        this.isDataLabel = isDataLabel;
    }

    public DataOrganizer(TableData data,String table, boolean isDataLabel) {
        this.label = null;
        this.data = data;
        this.table = table;
        this.isDataLabel = isDataLabel;
    }

    public DataOrganizer(DataLabel label, boolean isDataLabel) {
        this.label = label;
        this.data = null;
        this.isDataLabel = isDataLabel;
    }

    public DataLabel getLabel() {
        return label;
    }

    public TableData getData() {
        return data;
    }
    public String getTable() { return table; }
    public boolean isDataLabel() {
        return isDataLabel;
    }
}
