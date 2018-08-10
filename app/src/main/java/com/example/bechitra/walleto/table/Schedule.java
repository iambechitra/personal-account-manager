package com.example.bechitra.walleto.table;

public class Schedule {
    private String ID, ItemID, tableName, repeat, time;

    public Schedule(String ID, String itemID, String tableName, String repeat, String time) {
        this.ID = ID;
        ItemID = itemID;
        this.tableName = tableName;
        this.repeat = repeat;
        this.time = time;
    }

    public String getID() {
        return ID;
    }

    public String getItemID() {
        return ItemID;
    }

    public String getTableName() {
        return tableName;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getTime() {
        return time;
    }

}
