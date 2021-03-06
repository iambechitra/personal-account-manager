package com.pipapps.bechitra.walleto.utility;

public class CategoryProcessor {
    private String category, amount, length, table, lowerBound, upperBound;

    public CategoryProcessor(MapHelper mapHelper, String tag) {
        this.category = mapHelper.getKey();
        this.amount = mapHelper.getAmount();
        this.length = mapHelper.getHit();
        this.table = tag;
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
