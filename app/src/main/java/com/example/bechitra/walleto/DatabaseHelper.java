package com.example.bechitra.walleto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bechitra.walleto.table.Spending;

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
        db.execSQL("CREATE TABLE "+SPENDING_TABLE+" ("+ST_COL1+" TEXT, "+ST_COL2+" TEXT, "+ST_COL3+ " TEXT, "+ST_COL4+" TEXT, "+ST_COL5+" TEXT)");
        db.execSQL("CREATE TABLE "+EARNING_TABLE+" ("+ET_COL1+" TEXT, "+ET_COL2+" TEXT, "+ET_COL3+ " TEXT)");
        db.execSQL("CREATE TABLE "+USER_TABLE+" ("+UT_COL1+" TEXT, "+UT_COL2+" TEXT, "+UT_COL3+ " TEXT)");
        db.execSQL("INSERT INTO "+USER_TABLE+" VALUES ('NULL', '0', 'NULL')");
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

        double bal = 0;
        if(balance != null)
            bal = Double.parseDouble(balance) - Double.parseDouble(spend.getAmount());

        updateUserTable(db, UT_COL2, balance, Double.toString(bal));

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
}
