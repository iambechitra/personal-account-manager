package com.example.bechitra.walleto.utility;

public class CategoryProcessor {
    private String category, amount, length, table, lowerBound, upperBound;

    public CategoryProcessor(MapHelper mapHelper, String table) {
        this.category = mapHelper.getKey();
        this.amount = mapHelper.getAmount();
        this.length = mapHelper.getHit();
        this.table = table;
        this.lowerBound = mapHelper.getLowerBound();
        this.upperBound = mapHelper.getUpperBound();
    }


    public String getLowerBound() {
        return lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
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
