package com.pipapps.bechitra.walleto.utility;

import android.os.Parcel;
import android.os.Parcelable;
import com.pipapps.bechitra.walleto.room.entity.Schedule;

public class ScheduleParcel implements Parcelable {
    private String tag, category, note, date, repeat;
    boolean active;
    long id, walletID, transactionID;
    double amount;

    protected ScheduleParcel(Parcel in) {
        id = in.readLong();
        tag = in.readString();
        category = in.readString();
        amount = in.readDouble();
        note = in.readString();
        date = in.readString();
        repeat = in.readString();
        walletID = in.readLong();
        transactionID = in.readLong();
        active = in.readLong() == 1 ? true : false;
    }

    public ScheduleParcel(Schedule schedule) {
        this.id = schedule.getId();
        this.tag = schedule.getTag();
        this.category = schedule.getCategory();
        this.amount = schedule.getAmount();
        this.note = schedule.getNote();
        this.date = schedule.getDate();
        this.repeat = schedule.getRepeat();
        this.walletID = schedule.getWalletID();
        this.transactionID = schedule.getTransactionID();
        this.active = schedule.isActive();
    }

    public static final Creator<ScheduleParcel> CREATOR = new Creator<ScheduleParcel>() {
        @Override
        public ScheduleParcel createFromParcel(Parcel in) {
            return new ScheduleParcel(in);
        }

        @Override
        public ScheduleParcel[] newArray(int size) {
            return new ScheduleParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(tag);
        dest.writeString(category);
        dest.writeDouble(amount);
        dest.writeString(note);
        dest.writeString(date);
        dest.writeString(repeat);
        dest.writeLong(walletID);
        dest.writeLong(transactionID);
        dest.writeLong(active ? 1 : 0);
    }

    public long getID() {
        return id;
    }

    public String getTag() {
        return tag;
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

    public String getRepeat() {
        return repeat;
    }

    public long getWalletID() {
        return walletID;
    }

    public long getTransactionID() { return transactionID; }

    public boolean isActive() {
        return active;
    }
}
