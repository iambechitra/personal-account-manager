package com.pipapps.bechitra.walleto.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pipapps.bechitra.walleto.room.entity.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("delete from `transaction` where wallet_id = :walletID")
    void deleteTransactionByID(long walletID);

    @Query("select * from `transaction` where wallet_id = :walletID")
    List<Transaction>getListOfTransactionOfActiveWallet(long walletID);

    @Query("select * from `transaction` where wallet_id = :walletID")
    LiveData<List<Transaction>>getAllTransactionOfActiveWallet(long walletID);
    //String query = "SELECT * FROM "+table+" WHERE (SUBSTR(DATE,7)||SUBSTR(DATE,4,2)||SUBSTR(DATE,1,2) BETWEEN '"
    //        +lower+"' AND '"+upper+"') AND WALLET_ID = "+getActivatedWalletID();

    @Query("select * from `transaction` where (substr(DATE, 7) || substr(DATE, 4, 2) || substr(DATE,1, 2) between :lowerBound and :upperBound and wallet_id = :walletID)")
    LiveData<List<Transaction>>getTransactionWithinRange(String lowerBound, String upperBound, long walletID);

    @Query("select * from `transaction` where (substr(DATE, 7) || substr(DATE, 4, 2) || substr(DATE,1, 2) between :lowerBound and :upperBound and wallet_id = :walletID)")
    List<Transaction>getRangeTransaction(String lowerBound, String upperBound, long walletID);

    @Query("delete from `transaction`")
    void reset();
}
