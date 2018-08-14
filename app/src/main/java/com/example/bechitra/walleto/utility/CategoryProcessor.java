package com.example.bechitra.walleto.utility;

public class CategoryProcessor {
    String category, amount, length, table;

    public CategoryProcessor(String table, String category, String amount, String length) {
        this.category = category;
        this.amount = amount;
        this.length = length;
        this.table = table;
    }
    public String getTable() { return table; }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getLength() { return length; };
}
