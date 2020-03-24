package com.example.bechitra.walleto.utility;

import java.util.List;

public class EntrySet {
    String key;
    double value;

    public EntrySet(String key, double value) {
        this.key = key;
        this.value = value;
    }

    public EntrySet() {

    }

    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isExist(List<EntrySet> list, String category) {
        if(list!= null) {
            for(EntrySet set : list)
                if(set.getKey().equals(category))
                    return true;
        }

        return false;
    }
}
