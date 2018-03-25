package ro.marianperca.receiptmanager.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ro.marianperca.receiptmanager.database.model.Receipt;

@Dao
public interface ReceiptDao {
    @Query("SELECT * FROM receipts ORDER BY createdAt DESC")
    List<Receipt> getAll();

    @Insert
    void insert(Receipt receipt);

    @Update
    void update(Receipt receipt);

    @Delete
    void delete(Receipt receipt);
}
