package com.example.bechitra.walleto.utility;

public class MapHelper {
    String key, hit, amount, lowerBound, upperBound;

    public MapHelper(String key, String hit, String amount, String lowerBound, String upperBound) {
        this.key = key;
        this.hit = hit;
        this.amount = amount;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public String getLowerBound() {
        return lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }

    public String getKey() {
        return key;
    }

    public String getHit() {
        return hit;
    }

    public String getAmount() {
        return amount;
    }

    public void incrementHit() {
        int h = Integer.parseInt(this.hit);
        this.hit = Integer.toString(h+1);
    }

    public void addAmount(String value) {
        double a = Double.parseDouble(this.amount);
        double b = Double.parseDouble(value);
        this.amount = Double.toString(a+b);
    }
}
