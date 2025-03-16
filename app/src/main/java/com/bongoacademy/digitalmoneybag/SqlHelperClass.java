package com.bongoacademy.digitalmoneybag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SqlHelperClass extends SQLiteOpenHelper {

    // ✅ Step 1: Updated database version to 2 for schema change
    public SqlHelperClass(@Nullable Context context) {
        super(context, "db_cost", null, 3);  // ← Updated version to 2
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ✅ Step 2: Changed 'Time' column type to TEXT (Correct Format for Date/Time)
        db.execSQL("create table Expense (id INTEGER PRIMARY KEY AUTOINCREMENT, Amount DOUBLE, Reason TEXT, Time TEXT)");
        db.execSQL("create table Income (id INTEGER PRIMARY KEY AUTOINCREMENT, Amount DOUBLE, Reason TEXT, Time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Expense"); // Correct deletion
        db.execSQL("DROP TABLE IF EXISTS Income");
        onCreate(db); // ✅ Step 3: Call onCreate() to recreate tables with new structure
    }

    // ✅ Step 4: Updated date format to include full date and time
    public void add_expense(Double amount, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());  // Correct date-time format

        contentvalues.put("Amount", amount);    // Correct key: "Amount"
        contentvalues.put("Reason", reason);    // Correct key: "Reason"
        contentvalues.put("Time", currentDateTime); // Correct key: "Time"

        db.insert("Expense", null, contentvalues);  // Correct insertion
    }


    public double show_expense() {
        double totalExpense = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Expense", null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                double expense = cursor.getDouble(1);
                totalExpense += expense;
            }
        }
        return totalExpense;
    }

    public void add_income(Double amount, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        contentvalues.put("Amount", amount);
        contentvalues.put("Reason", reason);
        contentvalues.put("Time", currentDateTime);

        db.insert("Income", null, contentvalues);
    }

    public double show_income() {
        double totalIncome = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Income", null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                double income = cursor.getDouble(1);
                totalIncome += income;
            }
        }
        return totalIncome;
    }

    public Cursor show_all_data_expense() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from Expense", null);
    }

    public Cursor show_all_data_income() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from Income", null);
    }

    public void delete_expense(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Expense where id = " + id); // Corrected delete logic
    }

    public void delete_Income(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Income where id = " + id); // Corrected delete logic
    }
}
