package com.example.bechitra.walleto.database;

public class QueryProcessor {
    final static String SPENDING_TABLE = "SPENDING";

    final static String ID = "ID";
    final static String CATEGORY = "CATEGORY";
    final static String AMOUNT = "AMOUNT";
    final static String NOTE = "NOTE";
    final static String DATE = "DATE";
    final static String WALLET_ID = "WALLET_ID";

    final static String EARNING_TABLE = "EARNING";

    final static String WALLET_TABLE = "WALLET";
    final static String WLT_ID = "WALLET_ID";
    final static String WLT_NAME = "NAME";
    final static String WLT_ACTIVATED = "ACTIVATED";

    final static String SCHEDULE_TABLE = "SCHEDULE";
    final static String SDL_ID = "ID";
    final static String SDL_ITEM_ID = "ITEM_ID";
    final static String SDL_TABLE_NAME = "TABLE_NAME";
    final static String SDL_TIME = "TIME";

    final static String DEFAULT_WALLET = "PRIMARY";

    final static String CREATE_EARNING_TABLE = "CREATE TABLE "+EARNING_TABLE+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    +CATEGORY+" TEXT, "+AMOUNT+ " REAL, "+NOTE+" TEXT, "+DATE+" TEXT, "+WALLET_ID+" INTEGER)";

    final static String CREATE_SPENDING_TABLE = "CREATE TABLE "+SPENDING_TABLE+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        +CATEGORY+" TEXT, "+AMOUNT+ " REAL, "+NOTE+" TEXT, "+DATE+" TEXT, "+WALLET_ID+" INTEGER)";

    final static String CREATE_WALLET_TABLE = "CREATE TABLE "+WALLET_TABLE+" ("+WLT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                +WLT_NAME+" TEXT, "+WLT_ACTIVATED+ " INTEGER)";

    final static String CREATE_SCHEDULE_TABLE = "CREATE TABLE "+SCHEDULE_TABLE+" ("+SDL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    +SDL_ITEM_ID+" INTEGER, "+SDL_TABLE_NAME+" TEXT, "+SDL_TIME+" TEXT)";

}
