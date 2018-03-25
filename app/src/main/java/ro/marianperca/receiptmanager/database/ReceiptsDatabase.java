package ro.marianperca.receiptmanager.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ro.marianperca.receiptmanager.database.converter.DateConverter;
import ro.marianperca.receiptmanager.database.dao.ReceiptDao;
import ro.marianperca.receiptmanager.database.model.Receipt;

@Database(entities = {Receipt.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class ReceiptsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "receipts-app-db";

    private static ReceiptsDatabase database;

    public static ReceiptsDatabase getInstance(Context context) {
        if (null == database) {
            database = buildDatabaseInstance(context);
        }

        return database;
    }

    private static ReceiptsDatabase buildDatabaseInstance(Context appContext) {
        return Room.databaseBuilder(appContext, ReceiptsDatabase.class, DATABASE_NAME).build();
    }

    public abstract ReceiptDao receiptDao();
}
