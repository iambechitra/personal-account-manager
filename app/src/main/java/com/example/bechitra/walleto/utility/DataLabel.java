package com.example.bechitra.walleto.utility;

public class DataLabel {
    String Date, amount;

    public DataLabel(String date, String amount) {
        Date = date;
        this.amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public String getAmount() {
        return amount;
    }
}
