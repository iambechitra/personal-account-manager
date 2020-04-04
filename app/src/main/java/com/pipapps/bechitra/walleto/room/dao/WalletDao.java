package com.pipapps.bechitra.walleto.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pipapps.bechitra.walleto.room.entity.Wallet;

import java.util.List;

@Dao
public interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Wallet wallet);

    @Delete
    void delete(Wallet wallet);

    @Update
    void update(Wallet wallet);

    @Query("select id from `wallet` where is_active = 1")
    long getActivatedWalletID();

    @Query("select * from `wallet`")
    LiveData<List<Wallet>> getAllWallet();

    @Query("select * from `wallet` where is_active = 1")
    LiveData<Wallet> getActiveWallet();

    @Query("select * from `wallet` where is_active = 1")
    Wallet getActivedWallet();

    @Query("delete from `wallet`")
    void resetWallet();
}
