package com.example.bechitra.walleto.utility;

public class CategoryProcessor {
    String name, amount, num;

    public CategoryProcessor(String name, String amount, String num) {
        this.name = name;
        this.amount = amount;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getNum() { return num; };
}
