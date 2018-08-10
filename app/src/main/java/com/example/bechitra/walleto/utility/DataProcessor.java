package com.example.bechitra.walleto.utility;

import android.content.Context;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.StringPatternCreator;
import com.example.bechitra.walleto.table.TableData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    List<DataOrganizer> data;
    DatabaseHelper db;
    Context context;

    public DataProcessor(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        db = new DatabaseHelper(context);
    }

    public List<DataOrganizer> getProcessedData(String tableName) {
        StringPatternCreator spc = new StringPatternCreator();
        String today = spc.getCurrentDate();
        String[] date = spc.getSeparatedDateArray(today);

        int day = Integer.parseInt(date[0]);

        for(int i = day; i >= 1; i--) {
            String pattern = Integer.toString(i)+"/"+date[1]+"/"+date[2];
            List<TableData>  spendingList = db.getDataOnPattern(tableName,db.getActivatedWalletID(), pattern);

            if(spendingList.size() > 0) {
                BigDecimal big = BigDecimal.ZERO;
                for(TableData sp : spendingList)
                    big = big.add(new BigDecimal(sp.getAmount()));

                data.add(new DataOrganizer(new DataLabel(pattern, "$"+big.toString()), true));

                for(TableData sp : spendingList)
                    data.add(new DataOrganizer(sp, tableName, false));
            }
        }

        return data;
    }

}
