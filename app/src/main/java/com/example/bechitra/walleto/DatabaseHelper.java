package com.example.bechitra.walleto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bechitra.walleto.dialog.listener.OnDeleteItem;
import com.example.bechitra.walleto.utility.EntrySet;
import com.example.bechitra.walleto.table.Schedule;
import com.example.bechitra.walleto.table.PrimeTable;
import com.example.bechitra.walleto.table.Wallet;
import com.example.bechitra.walleto.utility.DateManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper{
    final static String DB_NAME = "account.db";
    final static String SPENDING_TABLE = "SPENDING";
    final static String ST_COL1 = "ID";
    final static String ST_COL2 = "CATEGORY";
    final static String ST_COL3 = "AMOUNT";
    final static String ST_COL4 = "NOTE";
    final static String ST_COL5 = "DATE";
    final static String ST_COL6 = "WALLET_ID";

    final static String EARNING_TABLE = "EARNING";
    final static String ET_COL1 = "ID";
    final static String ET_COL2 = "CATEGORY";
    final static String ET_COL3 = "AMOUNT";
    final static String ET_COL4 = "NOTE";
    final static String ET_COL5 = "DATE";
    final static String ET_COL6 = "WALLET_ID";

    final static String WALLET_TABLE = "WALLET";
    final static String WT_COL1 = "WALLET_ID";
    final static String WT_COL2 = "NAME";
    final static String WT_COL3 = "ACTIVATED";

    final static String SCHEDULE_TABLE = "SCHEDULE";
    final static String SDL_COL1 = "ID";
    final static String SDL_COL2 = "TABLE_NAME";
    final static String SDL_COL3 = "CATEGORY";
    final static String SDL_COL4 = "AMOUNT";
    final static String SDL_COL5 = "NOTE";
    final static String SDL_COL6 = "DATE";
    final static String SDL_COL7 = "REPEAT";
    final static String SDL_COL8 = "WALLET_ID";
    final static String SDL_COL9 = "ACTIVE";

    final static String DEFAULT_WALLET = "PRIMARY";

    SQLiteDatabase db;
    OnDeleteItem listener;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    public String getSpendingTable() {
        return SPENDING_TABLE;
    }

    public String getEarningTable() {
        return EARNING_TABLE;
    }

    public String getWalletTable() {return WALLET_TABLE; }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertAllTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean insertOnTable(String table, PrimeTable data) {
        ContentValues cv = new ContentValues();
        cv.put(ST_COL2, data.getCategory());
        cv.put(ST_COL3, data.getAmount());
        cv.put(ST_COL4, data.getNote());
        cv.put(ST_COL5, data.getDate());
        cv.put(ST_COL6, data.getWalletID());

        if(table.equals(SPENDING_TABLE)) {
            long result = db.insert(table, null, cv);

            if (result != -1)
                return true;
        } else {
            long num = db.insert(table, null, cv);

            if(num != -1)
                return true;
        }

        return false;
    }

    public List<String> getDistinctCategory(String tableName) {
        Cursor cr = db.rawQuery("SELECT DISTINCT CATEGORY FROM "+tableName+" WHERE "+ST_COL6+" = "+getActivatedWalletID(), null);
        List<String> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(cr.getString(0));

        return list;
    }

    public List<String> getDistinctCategoryFromCurrentMonth(String tableName) {
        Cursor cr = db.rawQuery("SELECT DISTINCT CATEGORY FROM "+tableName+" WHERE DATE LIKE '%"
                +new DateManager().getCurrentMonthWithYear()+"' AND "+" "+ET_COL6+" = "+getActivatedWalletID(), null);
        List<String> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(cr.getString(0));

        return list;
    }

    public String getActivatedWalletID() {
        Cursor cr = db.rawQuery("SELECT "+WT_COL1+" FROM "+WALLET_TABLE+" WHERE "+WT_COL3+" = 1", null);
        String balance = "0";

        while(cr.moveToNext())
            balance = cr.getString(0);

        return balance;
    }

    public Wallet getActivatedWallet() {
        Cursor cr = db.rawQuery("SELECT * FROM "+WALLET_TABLE+" WHERE "+WT_COL3+" = 1", null);
        Wallet wallet = null;
        while(cr.moveToNext())
            wallet = new Wallet(cr.getString(0), cr.getString(1), cr.getString(2));

        return wallet;
    }

    public List<Wallet> getInactivateWallet() {
        Cursor cr = db.rawQuery("SELECT * FROM "+WALLET_TABLE+" WHERE "+WT_COL3+" = 0", null);
        List<Wallet> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(new Wallet(cr.getString(0), cr.getString(1), cr.getString(2)));

        return list;
    }

    public void updateWalletTable(String id, String column, String value) {
        db.execSQL("UPDATE "+WALLET_TABLE+" SET "+column+" ='"+value+"' WHERE "+WT_COL1+" ='"+id+"'");
    }

    public String getCalculationOfCurrentMonth(String tableName, String walletID) {

        String format = new DateManager().getCurrentMonthWithYear();

        Cursor cr = db.rawQuery("SELECT AMOUNT FROM "+tableName+" WHERE DATE LIKE '%"+format+"' AND WALLET_ID = "+walletID, null);

        BigDecimal numb = BigDecimal.ZERO;

        while(cr.moveToNext())
            if(!cr.getString(0).equals(null))
                numb = numb.add(new BigDecimal(cr.getString(0)));


        Log.d("NEW BIG", numb.toString());

        return numb.toString();
    }

    public boolean resetEveryThing() {
        db.execSQL("DROP TABLE IF EXISTS "+SPENDING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+EARNING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+WALLET_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SCHEDULE_TABLE);

        insertAllTable(db);

        return true;
    }

    public List<PrimeTable> getAllRow(String tableName) {
        Cursor cr = db.rawQuery("SELECT * FROM "+tableName, null);
        List<PrimeTable> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return list;
    }

    private void insertAllTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+SPENDING_TABLE+" ("+ST_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ST_COL2+" TEXT, "+ST_COL3+ " REAL, "+ST_COL4+" TEXT, "+ST_COL5+" TEXT, "+ST_COL6+" INTEGER)");
        db.execSQL("CREATE TABLE "+EARNING_TABLE+" ("+ET_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ET_COL2+" TEXT, "+ET_COL3+ " REAL, "+ET_COL4+" TEXT, "+ET_COL5+" TEXT, "+ET_COL6+" INTEGER)");
        db.execSQL("CREATE TABLE "+WALLET_TABLE+" ("+WT_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+WT_COL2+" TEXT, "+WT_COL3+ " INTEGER)");
        db.execSQL("CREATE TABLE "+SCHEDULE_TABLE+" ("+SDL_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SDL_COL2+" TEXT, "+SDL_COL3+" TEXT, "+SDL_COL4+" TEXT, "+SDL_COL5+" TEXT, " +
                                                SDL_COL6+" TEXT, "+SDL_COL7+" TEXT, "+SDL_COL8+" TEXT, "+SDL_COL9+" TEXT)");
        ContentValues cv = new ContentValues();
        cv.put(WT_COL2, DEFAULT_WALLET);
        cv.put(WT_COL3, 1);
        db.insert(WALLET_TABLE, null, cv);
    }

    public void insertNewWallet(String name, boolean activation) {
        ContentValues cv = new ContentValues();
        cv.put(WT_COL2, name);

        if(activation)
            cv.put(WT_COL3, 1);
        else
            cv.put(WT_COL3, 0);

        db.insert(WALLET_TABLE, null, cv);
    }

    public List<PrimeTable> getCurrentMonthData(String tableName) {
        Cursor cr = db.rawQuery("SELECT * FROM "+tableName+" WHERE DATE LIKE '%"
                                +new DateManager().getCurrentMonthWithYear()+"'", null);
        List<PrimeTable> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return list;
    }

    public String getCurrentBalance(String walletID) {

        String earning = getTotalAmount(EARNING_TABLE, walletID);
        String spending = getTotalAmount(SPENDING_TABLE, walletID);

        return Double.toString(Double.parseDouble(earning)-Double.parseDouble(spending));

    }

    public String getTotalAmount(String table, String walletID) {
        Cursor cr = db.rawQuery("SELECT TOTAL(AMOUNT) FROM "+table+" WHERE WALLET_ID = "+walletID, null);
        String balance = "0";
        while (cr.moveToNext())
            balance = cr.getString(0);

        cr.close();
        return balance;
    }


    public String getBalanceToEarningRatio() {
        String str = "";

        String balance = "0";
        String earning = getCalculationOfCurrentMonth(EARNING_TABLE, getActivatedWalletID());
        double remainingBalance =  (Double.parseDouble(balance)/Double.parseDouble(earning) * 100.0);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        return df.format(remainingBalance).toString();
    }

    //Need to recheck;
    public List<EntrySet> getPieChartData(int limit) {
        List<EntrySet> graphData = new ArrayList<>();
        List<String> numbers = new ArrayList<>();

        List<String>category = getDistinctCategoryFromCurrentMonth(SPENDING_TABLE);
        boolean flag = false;
        if(category != null) {
            for(String str : category) {
                List<String> amount = getTotalAmountForACategoryForMonth(str, SPENDING_TABLE, new DateManager().getCurrentMonthWithYear());
                double value = Double.parseDouble(getTotalAmountFromAList(amount));
                graphData.add(new EntrySet(str, value));
            }
            Collections.sort(graphData, new Comparator<EntrySet>() {
                @Override
                public int compare(EntrySet o1, EntrySet o2) {
                    return compareValue(o1.getValue(), o2.getValue());
                }
            });
        }
        else
            graphData.add(new EntrySet("Spending", 100.0));
        
        
        return graphData;
    }

    private int compareValue(double value, double value1) {
        if(value > value1)
            return 1;
        else if(value == value1)
            return 0;
        else
            return -1;
    }

    public List<String> getTotalAmountForACategoryForMonth(String category, String table, String dateFormat) {
        List<String> amount = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT AMOUNT FROM "+table+" WHERE CATEGORY = '"+category+"' AND DATE LIKE '%"+dateFormat+"'", null);
        while(cr.moveToNext())
            amount.add(cr.getString(0));

        return amount;
    }

    public String getTotalAmountForACategory(String category, String table) {
        Cursor cr = db.rawQuery("SELECT AMOUNT FROM "+table+" WHERE CATEGORY = '"+category+"' AND "+ET_COL6+" = "+getActivatedWalletID(), null);
        BigDecimal bigDecimal = BigDecimal.ZERO;

        while (cr.moveToNext())
            bigDecimal = bigDecimal.add(new BigDecimal(cr.getString(0)));

        return bigDecimal.toString();
    }

    public String getTotalAmountFromAList(List<String> amount) {
        BigDecimal total = BigDecimal.ZERO;
        for(String str : amount)
            total = total.add(new BigDecimal(str));

        return total.toString();
    }

    public List<PrimeTable> filterDataFromTable(String tableName, String pattern, String category) {
        Cursor cr = db.rawQuery("SELECT * FROM "+tableName+" WHERE DATE LIKE '"+pattern+"' AND CATEGORY LIKE '"+category+"' AND "+ET_COL6+" = "+getActivatedWalletID(),null);
        List<PrimeTable>list = new ArrayList<>();
        while (cr.moveToNext())
            list.add(new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return list;
    }

    public String getLastRowFromTable(String tableName) {
        Cursor cr = db.rawQuery("SELECT ID FROM "+tableName+" ORDER BY ID DESC LIMIT 1",null);
        String lastRow = null;
        while (cr.moveToNext())
            lastRow = cr.getString(0);

        return lastRow;
    }

    public List<String> getDistinctDate(String table) {
        Set<String> date = new HashSet<>();
        Cursor cr = db.rawQuery("SELECT DATE FROM "+table,null);
        while (cr.moveToNext())
            date.add(new DateManager().getYearFromDate(cr.getString(0)));

        List<String> list = new ArrayList<>();
        list.clear();
        list.addAll(date);

        return list;
    }

    public List<PrimeTable> getDataOnPattern(String tableName, String activeWallet, String pattern) {
        Cursor cr = db.rawQuery("SELECT * FROM "+tableName+" WHERE DATE LIKE '"+pattern+"' AND "+ST_COL6+" = "+activeWallet,null);
        List<PrimeTable>list = new ArrayList<>();
        while (cr.moveToNext())
            list.add(new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));
        cr.close();
        return list;
    }

    public List<PrimeTable> getAllRowOfACategory(String tableName, String category) {
        Cursor cr = db.rawQuery("SELECT * FROM "+tableName+" WHERE CATEGORY = '"+category+"' AND "+ET_COL6+" = "+getActivatedWalletID(),null);
        List<PrimeTable>list = new ArrayList<>();
        while (cr.moveToNext())
            list.add(new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return list;
    }

    public void deleteRowFromTable(String tableName, String rowID) {
        Log.d("entry", "I came here");
        db.execSQL("DELETE FROM "+tableName+" WHERE ID = "+rowID);
        if(tableName.equals(getWalletTable()))
            listener.onDelete(getWalletTable(), Integer.parseInt(rowID));
    }

    public void deleteRowFromTable(String tableName, String columnName, String regex) {
        db.execSQL("DELETE FROM "+tableName+" WHERE "+columnName+" = "+regex);
    }

    public String getScheduleTable() {
        return SCHEDULE_TABLE;
    }

    public void updateRowFromTable(String tableName, String rowID, String category, String amount, String note, String date) {
        db.execSQL("UPDATE "+tableName+" SET "+ST_COL2+" = '"+category+"', "+ST_COL3+" = "+amount+", "+ST_COL4+" = '"+note+"', "+
                        ST_COL5+" = '"+date+"' WHERE ID = "+rowID);
    }

    public void updateRowFromTable(String table, String ID, String column, String value) {
        db.execSQL("UPDATE "+table+" SET "+column+" = '"+value+"' WHERE ID = "+ID);
    }

    public void setOnDeleteItemListener(OnDeleteItem onDeleteItemListener) {
        this.listener = onDeleteItemListener;
    }

    public boolean insertNewSchedule(Schedule schedule) {
        ContentValues cv = new ContentValues();
        cv.put(SDL_COL2, schedule.getTableName());
        cv.put(SDL_COL3, schedule.getCategory());
        cv.put(SDL_COL4, schedule.getAmount());
        cv.put(SDL_COL5, schedule.getNote());
        cv.put(SDL_COL6, schedule.getDate());
        cv.put(SDL_COL7, schedule.getRepeat());
        cv.put(SDL_COL8, schedule.getWalletID());
        cv.put(SDL_COL9, schedule.getActive());

        long result = db.insert(SCHEDULE_TABLE, null, cv);

        if(result != -1)
            return true;

        return false;
    }

    public List<PrimeTable> getDataWithinARange(String table, String lowerBound, String upperBound) {
        List<PrimeTable> data = new ArrayList<>();

        String[] low = new DateManager().getSeparatedDateArray(lowerBound);
        String[] high = new DateManager().getSeparatedDateArray(upperBound);

        String lower = (low[2]+low[1]+low[0]); // convert date dd/MM/yyyy -> yyyyMMdd format;
        String upper = (high[2]+high[1]+high[0]);

        String query = "SELECT * FROM "+table+" WHERE (SUBSTR(DATE,7)||SUBSTR(DATE,4,2)||SUBSTR(DATE,1,2) BETWEEN '"
                                    +lower+"' AND '"+upper+"') AND WALLET_ID = "+getActivatedWalletID();

        Cursor cr = db.rawQuery(query,null);

        while (cr.moveToNext()) {
            data.add(new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2),
                    cr.getString(3), cr.getString(4), cr.getString(5)));
        }

        return data;
    }

    public List<Schedule> getScheduledData() {
        List<Schedule> schedules = new ArrayList<>();

        Cursor cr = db.rawQuery("SELECT * FROM "+SCHEDULE_TABLE,null);
        while (cr.moveToNext())
            schedules.add(new Schedule(cr.getString(0), cr.getString(1), cr.getString(2),
                    cr.getString(3), cr.getString(4), cr.getString(5),
                        cr.getString(6), cr.getString(7), cr.getString(8)));

        return schedules;
    }

    public Schedule getScheduledData(String tableName, String category, String amount, String note, String walletID) {
        Cursor cr = db.rawQuery("SELECT * FROM "+SCHEDULE_TABLE+" WHERE "+SDL_COL2+" = '"+tableName+"' AND "+SDL_COL3+
                                " = '"+category+"' AND "+SDL_COL4+" = '"+amount+"' AND "+SDL_COL5+" = '"+note+"' AND "+
                                        SDL_COL8+" = '"+walletID+"'",null);
        Schedule schedule = null;

        while (cr.moveToNext())
            schedule = new Schedule(cr.getString(0), cr.getString(1), cr.getString(2),
                            cr.getString(3), cr.getString(4), cr.getString(5),
                                cr.getString(6), cr.getString(7), cr.getString(8));

        return schedule;
    }

    public PrimeTable getDataFromRow(String id, String tableName) {
        PrimeTable data = null;
        Cursor cr = db.rawQuery("SELECT * FROM "+tableName+" WHERE ID = "+id,null);
        while (cr.moveToNext())
            data = new PrimeTable(cr.getString(0), cr.getString(1), cr.getString(2),
                    cr.getString(3), cr.getString(4), cr.getString(5));

        return data;
    }

}
