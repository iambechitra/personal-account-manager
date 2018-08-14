package com.example.bechitra.walleto.table;

public class Schedule {
    private String ID, tableName, category, amount, note, date, repeat, walletID, active;

    public Schedule(String ID, String tableName, String category, String amount, String note,
                    String date, String repeat, String walletID, String active) {
        this.ID = ID;
        this.tableName = tableName;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.repeat = repeat;
        this.walletID = walletID;
        this.active = active;
    }

    public String getID() {
        return ID;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getWalletID() {
        return walletID;
    }

    public String getActive() { return active; }

    public boolean isActive() {
        if(Integer.parseInt(active) > 0)
            return true;

        return false;
    }
}
