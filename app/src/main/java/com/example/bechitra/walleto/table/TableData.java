package com.example.bechitra.walleto.table;

public class TableData {
    String ID, category, amount, note, date;

    public TableData(String ID, String category, String amount, String note, String date) {
        this.ID = ID;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }
}
