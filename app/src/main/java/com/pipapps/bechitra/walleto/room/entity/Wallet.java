package com.pipapps.bechitra.walleto.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallet")
public class Wallet {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "balance")
    private double balance;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Wallet(String name, double balance, boolean isActive) {
        this.name = name;
        this.balance = balance;
        this.isActive = isActive;
    }
}
/*
final static String WALLET_TABLE = "WALLET";
    final static String WT_COL1 = "WALLET_ID";
    final static String WT_COL2 = "NAME";
    final static String WT_COL3 = "ACTIVATED";
 */
