package com.pipapps.bechitra.walleto.utility;

import androidx.annotation.NonNull;
import java.util.List;

public class MapEntry {
    String key;
    List<MapEntry> mapValue;
    boolean flag = false;

    public List<MapEntry> getValues() {
        return mapValue;
    }

    public MapEntry(String key, List<MapEntry> mapValue, @NonNull boolean flag) {
        this.key = key;
        this.mapValue = mapValue;
        this.flag = flag;
    }

    public String getKey() {
        return key;
    }

    public boolean isFlag() {
        return flag;
    }
}
