package com.pipapps.bechitra.walleto.utility;

import android.os.Parcel;
import android.os.Parcelable;

public class CategorisedDataParcel implements Parcelable {
    String category, table, length, lowerbound, upperbound;

    public CategorisedDataParcel(CategoryProcessor cat) {
        this.category = cat.getCategory();
        this.table = cat.getTable();
        this.length = cat.getLength();
        this.lowerbound = cat.getLowerBound();
        this.upperbound = cat.getUpperBound();
    }

    protected CategorisedDataParcel(Parcel in) {
        category = in.readString();
        table = in.readString();
        length = in.readString();
        lowerbound = in.readString();
        upperbound = in.readString();
    }

    public static final Creator<CategorisedDataParcel> CREATOR = new Creator<CategorisedDataParcel>() {
        @Override
        public CategorisedDataParcel createFromParcel(Parcel in) {
            return new CategorisedDataParcel(in);
        }

        @Override
        public CategorisedDataParcel[] newArray(int size) {
            return new CategorisedDataParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(table);
        dest.writeString(length);
        dest.writeString(lowerbound);
        dest.writeString(upperbound);
    }

    public String getCategory() {
        return category;
    }

    public String getTable() {
        return table;
    }

    public String getLength() {
        return length;
    }

    public String getLowerbound() {
        return lowerbound;
    }

    public String getUpperbound() {
        return upperbound;
    }
}
