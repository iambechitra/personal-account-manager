package com.example.bechitra.walleto.utility;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bechitra.walleto.table.TableData;

public class DataParser implements Parcelable{
    String tableName, ID, category, amount, note, date, walletID;
    int flag;

    protected DataParser(Parcel in) {
        tableName = in.readString();
        ID = in.readString();
        category = in.readString();
        amount = in.readString();
        note = in.readString();
        date = in.readString();
        walletID = in.readString();
        flag = in.readInt();
    }

    public DataParser(String tableName, TableData data, int flag) {
        this.tableName = tableName;
        this.ID = data.getID();
        this.category = data.getCategory();
        this.amount = data.getAmount();
        this.note = data.getNote();
        this.date = data.getDate();
        this.walletID = data.getWalletID();
        this.flag = flag;
    }

    public static final Creator<DataParser> CREATOR = new Creator<DataParser>() {
        @Override
        public DataParser createFromParcel(Parcel in) {
            return new DataParser(in);
        }

        @Override
        public DataParser[] newArray(int size) {
            return new DataParser[size];
        }
    };

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

    public String getTableName() { return tableName; }

    public int getFlag() { return flag; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tableName);
        dest.writeString(ID);
        dest.writeString(category);
        dest.writeString(amount);
        dest.writeString(note);
        dest.writeString(date);
        dest.writeString(walletID);
        dest.writeInt(flag);
    }
}
