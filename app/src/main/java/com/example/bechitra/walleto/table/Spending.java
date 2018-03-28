package com.example.bechitra.walleto.table;

public class Spending {
    String title, category, amount, note, date;

    public Spending(String title, String category, String amount, String note, String date) {
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public String getTitle() {
        return title;
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
