package com.example.bechitra.walleto.utility;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.bechitra.walleto.table.Schedule;

public class ScheduleParser implements Parcelable {
    private String ID, tableName, category, amount, note, date, repeat, walletID, active;

    protected ScheduleParser(Parcel in) {
        ID = in.readString();
        tableName = in.readString();
        category = in.readString();
        amount = in.readString();
        note = in.readString();
        date = in.readString();
        repeat = in.readString();
        walletID = in.readString();
        active = in.readString();
    }

    public ScheduleParser(Schedule schedule) {
        this.ID = schedule.getID();
        this.tableName = schedule.getTableName();
        this.category = schedule.getCategory();
        this.amount = schedule.getAmount();
        this.note = schedule.getNote();
        this.date = schedule.getDate();
        this.repeat = schedule.getRepeat();
        this.walletID = schedule.getWalletID();
        this.active = schedule.getActive();
    }

    public static final Creator<ScheduleParser> CREATOR = new Creator<ScheduleParser>() {
        @Override
        public ScheduleParser createFromParcel(Parcel in) {
            return new ScheduleParser(in);
        }

        @Override
        public ScheduleParser[] newArray(int size) {
            return new ScheduleParser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(tableName);
        dest.writeString(category);
        dest.writeString(amount);
        dest.writeString(note);
        dest.writeString(date);
        dest.writeString(repeat);
        dest.writeString(walletID);
        dest.writeString(active);
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

    public String getActive() {
        return active;
    }
}
