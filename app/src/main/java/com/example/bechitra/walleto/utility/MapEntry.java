package com.example.bechitra.walleto.utility;

import androidx.annotation.NonNull;
import com.example.bechitra.walleto.table.PrimeTable;
import java.util.List;

public class MapEntry {
    String key;
    List<PrimeTable> value;
    List<MapEntry> mapValue;
    boolean flag = false;

    public MapEntry(String key, List<PrimeTable> value) {
        this.key = key;
        this.value = value;
    }

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

    public List<PrimeTable> getValue() {
        return value;
    }
}
