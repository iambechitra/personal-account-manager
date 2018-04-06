package com.example.bechitra.walleto;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bechitra.walleto.graph.GraphData;
import com.example.bechitra.walleto.sorting.BigSort;
import com.example.bechitra.walleto.table.Earning;
import com.example.bechitra.walleto.table.Spending;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class DatabaseHelper extends SQLiteOpenHelper{
    final static String DB_NAME = "account.db";
    final static String SPENDING_TABLE = "SPENDING";
    final static String ST_COL1 = "TITLE";
    final static String ST_COL2 = "CATEGORY";
    final static String ST_COL3 = "AMOUNT";
    final static String ST_COL4 = "NOTE";
    final static String ST_COL5 = "DATE";

    final static String EARNING_TABLE = "EARNING";
    final static String ET_COL1 = "CATEGORY";
    final static String ET_COL2 = "AMOUNT";
    final static String ET_COL3 = "DATE";

    final static String USER_TABLE = "USER";
    final static String UT_COL1 = "TRANSACTION_ID";
    final static String UT_COL2 = "BALANCE";
    final static String UT_COL3 = "ALERT";

    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertAllTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean onInsertSpending(Spending spend) {
        ContentValues spending = new ContentValues();
        spending.put(ST_COL1, spend.getTitle());
        spending.put(ST_COL2, spend.getCategory());
        spending.put(ST_COL3, spend.getAmount());
        spending.put(ST_COL4, spend.getNote());
        spending.put(ST_COL5, spend.getDate());

        long result = db.insert(SPENDING_TABLE, null, spending);

        String balance = getBalanceForUser(db);
        String newBalance = "";

        if(balance != null) {
            newBalance = new BigDecimal(new BigDecimal(balance).subtract(new BigDecimal(spend.getAmount())).toString()).toString();
            updateUserTable(db, UT_COL2, balance, newBalance);
        }

        if(result != -1)
            return true;

        return false;
    }

    public String getBalanceForUser(SQLiteDatabase db) {
        Cursor cr = db.rawQuery("SELECT BALANCE FROM "+USER_TABLE, null);
        String balance = "0";
        while(cr.moveToNext())
            balance = cr.getString(0);

        return balance;
    }

    public List<String> getDistinctCategory(String tableName) {
        Cursor cr = db.rawQuery("SELECT DISTINCT CATEGORY FROM "+tableName, null);
        List<String> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(cr.getString(0));

        return list;
    }

    public List<String> getDistinctCategoryFromCurrentMonth(String tableName) {
        Cursor cr = db.rawQuery("SELECT DISTINCT CATEGORY FROM "+tableName+" WHERE DATE LIKE '%"
                +new StringPatternCreator().getCurrentMonthWithYear()+"'", null);
        List<String> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(cr.getString(0));

        return list;
    }

    public String getBalanceForUser() {
        Cursor cr = db.rawQuery("SELECT BALANCE FROM "+USER_TABLE, null);
        String balance = "0";

        while(cr.moveToNext())
            balance = cr.getString(0);

        return balance;
    }

    public String getAlertStatus(SQLiteDatabase db) {
        Cursor cr = db.rawQuery("SELECT ALERT FROM "+USER_TABLE, null);
        String alert = "NULL";
        while(cr.moveToNext())
            alert = cr.getString(0);

        return alert;
    }

    public String getTransactionID(SQLiteDatabase db) {
        Cursor cr = db.rawQuery("SELECT "+UT_COL1+" FROM "+USER_TABLE, null);
        String transactionID = "NULL";
        while(cr.moveToNext())
            transactionID = cr.getString(0);

        return transactionID;
    }

    public void updateUserTable(SQLiteDatabase db, String column, String oldValue, String newValue) {
        db.execSQL("UPDATE "+USER_TABLE+" SET "+column+" ='"+newValue+"' WHERE BALANCE ='"+oldValue+"'");
    }

    public String getCalculationOfCurrentMonth(String tableName) {

        String format = new StringPatternCreator().getCurrentMonthWithYear();

        Cursor cr = db.rawQuery("SELECT AMOUNT FROM "+tableName+" WHERE DATE LIKE '%"+format+"'", null);

        BigDecimal numb = BigDecimal.ZERO;

        while(cr.moveToNext())
            if(!cr.getString(0).equals(null))
                numb = numb.add(new BigDecimal(cr.getString(0)));


        Log.d("NEW BIG", numb.toString());

        return numb.toString();
    }

    public boolean OnInsertEarningTable(Earning earning) {
        ContentValues cv = new ContentValues();
        cv.put(ET_COL1, earning.getCategory());
        cv.put(ET_COL2, earning.getAmount());
        cv.put(ET_COL3, earning.getDate());
        long num = db.insert(EARNING_TABLE, null, cv);

        String balance = getBalanceForUser(db);

        BigDecimal newBalance = new BigDecimal(new BigDecimal(balance).add(new BigDecimal(earning.getAmount())).toString());
        updateUserTable(db, UT_COL2, balance, newBalance.toString());


        if(num != -1)
            return true;

        return false;
    }

    public boolean resetEveryThing() {
        db.execSQL("DROP TABLE IF EXISTS "+SPENDING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+EARNING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);

        insertAllTable(db);

        return true;
    }

    public List<Spending> getAllSpending() {
        Cursor cr = db.rawQuery("SELECT * FROM "+SPENDING_TABLE, null);
        List<Spending> spendingList = new ArrayList<>();
        while(cr.moveToNext())
            spendingList.add(new Spending(cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return spendingList;
    }

    private void insertAllTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+SPENDING_TABLE+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ST_COL1+" TEXT, "+ST_COL2+" TEXT, "+ST_COL3+ " TEXT, "+ST_COL4+" TEXT, "+ST_COL5+" TEXT)");
        db.execSQL("CREATE TABLE "+EARNING_TABLE+" ("+ET_COL1+" TEXT, "+ET_COL2+" TEXT, "+ET_COL3+ " TEXT)");
        db.execSQL("CREATE TABLE "+USER_TABLE+" ("+UT_COL1+" TEXT, "+UT_COL2+" TEXT, "+UT_COL3+ " TEXT)");
        db.execSQL("INSERT INTO "+USER_TABLE+" VALUES ('NULL', '0', '0')");
    }

    public List<Spending> getCurrentMonthSpending() {
        Cursor cr = db.rawQuery("SELECT * FROM "+SPENDING_TABLE+" WHERE DATE LIKE '%"
                                +new StringPatternCreator().getCurrentMonthWithYear()+"'", null);
        List<Spending> spendingList = new ArrayList<>();
        while(cr.moveToNext())
            spendingList.add(new Spending(cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return spendingList;
    }

    public List<Earning> getAllEarning() {
        Cursor cr = db.rawQuery("SELECT * FROM "+EARNING_TABLE,null);
        List<Earning> earningList = new ArrayList<>();
        while (cr.moveToNext())
            earningList.add(new Earning(cr.getString(0), cr.getString(1), cr.getString(2)));

        return earningList;
    }

    public List<Earning> getCurrentMonthEarning() {
        Cursor cr = db.rawQuery("SELECT * FROM "+EARNING_TABLE+" WHERE DATE LIKE '%"
                                +new StringPatternCreator().getCurrentMonthWithYear()+"'",null);
        List<Earning> earningList = new ArrayList<>();
        while (cr.moveToNext())
            earningList.add(new Earning(cr.getString(0), cr.getString(1), cr.getString(2)));

        return earningList;
    }

    public Spending getLastInsertedSpending() {
        Spending spending = null;
        Cursor cr = db.rawQuery("SELECT * FROM "+SPENDING_TABLE+" WHERE ID = (SELECT ID FROM "+SPENDING_TABLE+" ORDER BY ID DESC LIMIT 1)",null);

        while (cr.moveToNext()) {
            spending = new Spending(cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5));
        }

        return spending;
    }

    public String getBalanceToEarningRatio() {
        String str = "";

        String balance = getBalanceForUser();
        String earning = getCalculationOfCurrentMonth(EARNING_TABLE);
        double remainingBalance =  (Double.parseDouble(balance)/Double.parseDouble(earning) * 100.0);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        return df.format(remainingBalance).toString();
    }

    public void insertOnUserTable(String percentage) {
        String balance = getBalanceForUser();
        if(balance != null) {
            db.execSQL("UPDATE "+USER_TABLE+" SET "+UT_COL3+" = '"+percentage+"' WHERE "+UT_COL2+" = '"+balance+"'");
        }
    }

    public String getOnAlertPercentage() {
        String str = "0";
        Cursor cr = db.rawQuery("SELECT "+UT_COL3+" FROM "+USER_TABLE,null);
        while(cr.moveToNext())
            str = cr.getString(0);

        return str;
    }

    public List<GraphData> getPieChartData() {
        List<GraphData> graphData = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        List<String> sortedArray = null;
        List <GraphData> pieChartData = new ArrayList<>();

        List<String>category = getDistinctCategoryFromCurrentMonth(SPENDING_TABLE);
        boolean flag = false;
        if(category != null) {
            for(String str : category) {
                List<String> amount = getTotalAmountForACategoryForMonth(str, SPENDING_TABLE, new StringPatternCreator().getCurrentMonthWithYear());
                String total = getTotalAmountFromAList(amount);
                numbers.add(total);
                graphData.add(new GraphData(str, total));
                flag = true;
            }
        }

        String am = getCalculationOfCurrentMonth(SPENDING_TABLE);
        BigDecimal num = new BigDecimal(am);

        if(flag) {
            BigSort sort = new BigSort(numbers);
            sortedArray = sort.getBigData();

            for(int i = sortedArray.size()-1; i >= 0; i--) {
                for(GraphData graphData1 : graphData) {
                    if(graphData1.getData().equals(sortedArray.get(i)))
                        pieChartData.add(new GraphData(graphData1.getTitle(), new BigDecimal(graphData1.getData())
                                .divide(num, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).toString()));
                }

                if(sortedArray.size() - i == 4)
                    break;
            }
        } else
            pieChartData.add(new GraphData("Spending", "100"));

        return  pieChartData;
    }

    public List<String> getTotalAmountForACategoryForMonth(String category, String table, String dateFormat) {
        List<String> amount = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT AMOUNT FROM "+table+" WHERE CATEGORY = '"+category+"' AND DATE LIKE '%"+dateFormat+"'", null);
        while(cr.moveToNext())
            amount.add(cr.getString(0));

        return amount;
    }

    public String getTotalAmountFromAList(List<String> amount) {
        BigDecimal total = BigDecimal.ZERO;
        for(String str : amount)
            total = total.add(new BigDecimal(str));

        return total.toString();
    }

}
