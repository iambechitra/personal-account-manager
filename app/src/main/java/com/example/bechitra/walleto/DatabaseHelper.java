package com.example.bechitra.walleto;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bechitra.walleto.table.Earning;
import com.example.bechitra.walleto.table.Spending;

import java.math.BigDecimal;
import java.text.DateFormat;
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

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
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

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(SPENDING_TABLE, null, spending);

        String balance = getBalanceForUser(db);
        String newBalance = "";

        if(balance != null && new StringPatternCreator().isCurrentMonth(spend.getDate())) {
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
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT DISTINCT CATEGORY FROM "+tableName, null);
        List<String> list = new ArrayList<>();
        while(cr.moveToNext())
            list.add(cr.getString(0));

        return list;
    }

    public String getBalanceForUser() {
        SQLiteDatabase db = this.getWritableDatabase();

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

        SQLiteDatabase db = this.getWritableDatabase();

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

        SQLiteDatabase db = this.getWritableDatabase();
        long num = db.insert(EARNING_TABLE, null, cv);

        String balance = getBalanceForUser(db);

        if(new StringPatternCreator().isCurrentMonth(earning.getDate())) {
            BigDecimal newBalance = new BigDecimal(new BigDecimal(balance).add(new BigDecimal(earning.getAmount())).toString());
            updateUserTable(db, UT_COL2, balance, newBalance.toString());
        }

        if(num != -1)
            return true;

        return false;
    }

    public boolean resetEveryThing() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+SPENDING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+EARNING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);

        insertAllTable(db);

        return true;
    }

    public List<Spending> getAllSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
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
        db.execSQL("INSERT INTO "+USER_TABLE+" VALUES ('NULL', '0', 'NULL')");
    }

    public List<Spending> getCurrentMonthSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM "+SPENDING_TABLE+" WHERE DATE LIKE '%"
                                +new StringPatternCreator().getCurrentMonthWithYear()+"'", null);
        List<Spending> spendingList = new ArrayList<>();
        while(cr.moveToNext())
            spendingList.add(new Spending(cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5)));

        return spendingList;
    }

    public List<Earning> getAllEarning() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cr = db.rawQuery("SELECT * FROM "+EARNING_TABLE,null);
        List<Earning> earningList = new ArrayList<>();
        while (cr.moveToNext())
            earningList.add(new Earning(cr.getString(0), cr.getString(1), cr.getString(2)));

        return earningList;
    }

    public List<Earning> getCurrentMonthEarning() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cr = db.rawQuery("SELECT * FROM "+EARNING_TABLE+" WHERE DATE LIKE '%"
                                +new StringPatternCreator().getCurrentMonthWithYear()+"'",null);
        List<Earning> earningList = new ArrayList<>();
        while (cr.moveToNext())
            earningList.add(new Earning(cr.getString(0), cr.getString(1), cr.getString(2)));

        return earningList;
    }

    public Spending getLastInsertedSpending() {
        Spending spending = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM "+SPENDING_TABLE+" WHERE ID = (SELECT ID FROM "+SPENDING_TABLE+" ORDER BY ID DESC LIMIT 1)",null);

        while (cr.moveToNext()) {
            spending = new Spending(cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5));
        }

        return spending;
    }

}
