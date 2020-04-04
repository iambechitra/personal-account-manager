package com.pipapps.bechitra.walleto.sorting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BigSort {
    private List<String> bigData;
    private BigDecimal[] num;

    public BigSort(List<String> bigData) {
        this.bigData = bigData;
        runSortingAlgorithm();
    }

    public void runSortingAlgorithm() {
        num = new BigDecimal[bigData.size()];

        for(int i = 0; i<bigData.size(); i++)
            num[i] = new BigDecimal(bigData.get(i));

        boolean swap;

        for(int i = 0; i < num.length-1; i++) {
            swap = false;
            for(int j = 0; j < num.length-i-1; j++) {
                if(num[j].compareTo(num[j+1]) == 1) {
                    swapPosition(j, j+1);
                    swap = true;
                }
            }

            if(!swap)
                break;
        }

        List<String> sortedBig = new ArrayList<>();
        for(int i = 0; i < bigData.size(); i++)
            sortedBig.add(num[i].toString());
        bigData.clear();
        bigData = sortedBig;
    }

    public void swapPosition(int from, int to) {
        BigDecimal big = num[to];
        num[to] = num[from];
        num[from] = big;
    }

    public List<String> getBigData() {
        return bigData;
    }
}
