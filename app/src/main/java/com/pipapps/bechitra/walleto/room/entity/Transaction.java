package com.pipapps.bechitra.walleto.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "wallet_id")
    private long walletID;

    public Transaction(String category, double amount, String note, String date, String tag, long walletID) {
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.tag = tag;
        this.walletID = walletID;
    }

    @Ignore
    public Transaction(long id, String category, double amount, String note, String date, String tag, long walletID) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.tag = tag;
        this.walletID = walletID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setWalletID(long walletID) {
        this.walletID = walletID;
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public String getTag() {
        return tag;
    }

    public long getWalletID() {
        return walletID;
    }
}