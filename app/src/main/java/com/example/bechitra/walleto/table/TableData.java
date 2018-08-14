package com.example.bechitra.walleto.table;

public class TableData {
    String ID, category, amount, note, date, walletID;

    public TableData(String ID, String category, String amount, String note, String date, String walletID) {
        this.ID = ID;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.walletID = walletID;
    }

    public void setDate(String date) { this.date = date; }

    public String getID() {
        return ID;
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

    public String getWalletID(){ return walletID; }
}
