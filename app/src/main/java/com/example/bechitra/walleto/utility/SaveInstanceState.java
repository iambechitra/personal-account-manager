package com.example.bechitra.walleto.utility;

import android.os.Bundle;
import com.example.bechitra.walleto.R;

public class SaveInstanceState {
    String category = "null", amount = "null", note = "null", date = "null", repeatKey = "null", instanceOf = "null";
    boolean isChaked = false, isExist = false;

    public SaveInstanceState(Bundle instanceState) {
        if(instanceState != null) {
            this.category = instanceState.getString(String.valueOf(R.string.item_category));
            this.amount = instanceState.getString(String.valueOf(R.string.item_amount));
            this.note = instanceState.getString(String.valueOf(R.string.item_note));
            this.date = instanceState.getString(String.valueOf(R.string.item_date));
            this.repeatKey = instanceState.getString(String.valueOf(R.string.item_repeat_key));
            this.instanceOf = instanceState.getString("table");

            if(instanceState.getString(String.valueOf(R.string.item_repeat_status)).equals("true"))
                this.isChaked = true;

            this.isExist = true;
        }
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

    public String getRepeatKey() {
        return repeatKey;
    }

    public boolean isRepetitionChaked() {
        return isChaked;
    }

    public boolean isExist() {
        return isExist;
    }

    public boolean isInstanceOf(String instanceOf) {
        if(this.instanceOf.equals(instanceOf))
            return true;
        return false;
    }
}
