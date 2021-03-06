package com.pipapps.bechitra.walleto.utility;

import com.pipapps.bechitra.walleto.room.entity.Transaction;

public class DataOrganizer {
    private CategoryProcessor processor;
    private DataLabel label;
    private Transaction data;
    private String table;
    boolean isDataLabel;

    public DataOrganizer(CategoryProcessor processor, boolean isDataLabel) {
        this.processor = processor;
        this.isDataLabel = isDataLabel;
    }

    public DataOrganizer(Transaction data, String table, boolean isDataLabel) {
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
    public CategoryProcessor getCategoryProcessor() {
        return processor;
    }
    public Transaction getData() {
        return data;
    }
    public String getTable() { return table; }
    public boolean isDataLabel() {
        return isDataLabel;
    }
}
