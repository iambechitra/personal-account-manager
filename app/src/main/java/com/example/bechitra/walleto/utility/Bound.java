package com.example.bechitra.walleto.utility;

public class Bound {
    private String lowerBound, upperBound;

    public Bound(String lowerBound, String upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public String getLowerBound() {
        return lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }
}
