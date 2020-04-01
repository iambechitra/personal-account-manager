package com.example.bechitra.walleto.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bechitra.walleto.room.dao.ScheduleDao;
import com.example.bechitra.walleto.room.dao.TransactionDao;
import com.example.bechitra.walleto.room.dao.WalletDao;
import com.example.bechitra.walleto.room.entity.Schedule;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.room.entity.Wallet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaction.class, Schedule.class, Wallet.class}, version = 2, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {
    private static volatile DatabaseRoom instance;
    private static final int threads = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(threads);

    public abstract TransactionDao transactionDao();
    public abstract ScheduleDao scheduleDao();
    public abstract WalletDao walletDao();

    private static DatabaseRoom.Callback databaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(()->{
                WalletDao dao = instance.walletDao();
                Log.d("exe", "i was run");
                dao.resetWallet();
                dao.insert(new Wallet("PRIMARY", 0, true));
                Log.d("data", ""+dao.getAllWallet().getValue());
            });
        }

    };

    public static DatabaseRoom getInstance(final Context context) {
        if(instance == null) {
            synchronized (DatabaseRoom.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseRoom.class, "store")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(databaseCallback)
                            .build();
                }
            }
        }

        return instance;
    }
}
