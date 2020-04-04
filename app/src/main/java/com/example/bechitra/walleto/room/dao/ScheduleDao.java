package com.example.bechitra.walleto.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bechitra.walleto.room.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Schedule schedule);

    @Delete
    void delete(Schedule schedule);

    @Update
    void update(Schedule schedule);

    @Query("select * from schedule")
    LiveData<List<Schedule>> getAllSchedule();

    @Query("select * from schedule where wallet_id = :walletID")
    LiveData<List<Schedule>>getAllScheduleFromActiveWallet(long walletID);

    @Query("select * from schedule where id = :id")
    Schedule getScheduleByID(long id);

    @Query("select * from schedule")
    List<Schedule>getAllScheduleList();


    @Query("select * from schedule where transaction_id = :transactionID")
    Schedule getScheduleByTransaction(long transactionID);

    @Query("delete from schedule")
    void reset();
}
