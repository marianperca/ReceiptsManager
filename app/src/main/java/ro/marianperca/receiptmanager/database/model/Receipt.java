package ro.marianperca.receiptmanager.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "receipts")
public class Receipt {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;
    private double value;
    private String store;
    private Date createdAt;

    public Receipt(Date date, double value, String store) {
        this.date = date;
        this.value = value;
        this.store = store;
        this.createdAt = Calendar.getInstance().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
