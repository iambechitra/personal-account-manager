package com.example.bechitra.walleto.utility;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.table.PrimeTable;

public class TransactionParcel implements Parcelable{
    private String tag,category, note, date;
    private long id, walletID;
    private double amount;
    private int flag;

    protected TransactionParcel(Parcel in) {
        tag = in.readString();
        id = in.readLong();
        category = in.readString();
        amount = in.readDouble();
        note = in.readString();
        date = in.readString();
        walletID = in.readLong();
        flag = in.readInt();
    }

    public TransactionParcel(String tag, Transaction data, int flag) {
        this.tag = tag;
        this.id = data.getId();
        this.category = data.getCategory();
        this.amount = data.getAmount();
        this.note = data.getNote();
        this.date = data.getDate();
        this.walletID = data.getWalletID();
        this.flag = flag;
    }

    public static final Creator<TransactionParcel> CREATOR = new Creator<TransactionParcel>() {
        @Override
        public TransactionParcel createFromParcel(Parcel in) {
            return new TransactionParcel(in);
        }

        @Override
        public TransactionParcel[] newArray(int size) {
            return new TransactionParcel[size];
        }
    };

    public long getID() {
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

    public long getWalletID(){ return walletID; }

    public String getTag() { return tag; }

    public int getFlag() { return flag; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeLong(id);
        dest.writeString(category);
        dest.writeDouble(amount);
        dest.writeString(note);
        dest.writeString(date);
        dest.writeLong(walletID);
        dest.writeInt(flag);
    }
}
