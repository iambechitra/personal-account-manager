package com.example.bechitra.walleto.utility;

public class DataOrganizer {
    DataLabel label;
    CategoryProcessor data;
    boolean isDataLabel;

    public DataOrganizer(DataLabel label, CategoryProcessor data, boolean isDataLabel) {
        this.label = label;
        this.data = data;
        this.isDataLabel = isDataLabel;
    }

    public DataOrganizer(CategoryProcessor data, boolean isDataLabel) {
        this.label = null;
        this.data = data;
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

    public CategoryProcessor getData() {
        return data;
    }

    public boolean isDataLabel() {
        return isDataLabel;
    }
}
