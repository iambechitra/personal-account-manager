package com.example.bechitra.walleto.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "repeat")
    private String repeat;

    @ColumnInfo(name = "wallet_id")
    private long walletID;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public Schedule(String tag, String category, double amount, String note, String date, String repeat, long walletID, boolean isActive) {
        this.tag = tag;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.repeat = repeat;
        this.walletID = walletID;
        this.isActive = isActive;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public long getWalletID() {
        return walletID;
    }

    public void setWalletID(long walletID) {
        this.walletID = walletID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

/*
final static String SCHEDULE_TABLE = "SCHEDULE";
    final static String SDL_COL1 = "ID";
    final static String SDL_COL2 = "TABLE_NAME";
    final static String SDL_COL3 = "CATEGORY";
    final static String SDL_COL4 = "AMOUNT";
    final static String SDL_COL5 = "NOTE";
    final static String SDL_COL6 = "DATE";
    final static String SDL_COL7 = "REPEAT";
    final static String SDL_COL8 = "WALLET_ID";
    final static String SDL_COL9 = "ACTIVE";
 */
