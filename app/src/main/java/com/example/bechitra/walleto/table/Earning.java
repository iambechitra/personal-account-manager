package com.example.bechitra.walleto.table;

public class Earning {
    String category, amount, date;

    public Earning(String category, String amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
