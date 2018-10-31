package com.example.bechitra.walleto.utility;

import com.example.bechitra.walleto.table.PrimeTable;

public class DataOrganizer {
    private CategoryProcessor processor;
    private DataLabel label;
    private PrimeTable data;
    private String table;
    boolean isDataLabel;

    public DataOrganizer(CategoryProcessor processor, boolean isDataLabel) {
        this.processor = processor;
        this.isDataLabel = isDataLabel;
    }

    public DataOrganizer(PrimeTable data, String table, boolean isDataLabel) {
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
    public PrimeTable getData() {
        return data;
    }
    public String getTable() { return table; }
    public boolean isDataLabel() {
        return isDataLabel;
    }
}
