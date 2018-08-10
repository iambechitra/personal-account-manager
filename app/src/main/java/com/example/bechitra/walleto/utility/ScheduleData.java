package com.example.bechitra.walleto.utility;

public class ScheduleData {
    String ID,category, amount, table, lastRepeat, repetitionInterval;

    public ScheduleData(String ID, String category, String amount, String table, String lastRepeat, String repetitionInterval) {
        this.ID = ID;
        this.category = category;
        this.amount = amount;
        this.table = table;
        this.lastRepeat = lastRepeat;
        this.repetitionInterval = repetitionInterval;
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

    public String getTable() {
        return table;
    }

    public String getLastRepeat() {
        return lastRepeat;
    }

    public String getRepetitionInterval() {
        return repetitionInterval;
    }
}
