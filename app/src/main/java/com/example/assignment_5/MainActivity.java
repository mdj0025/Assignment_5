package com.example.assignment_5;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    SqLiteHandler dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new SqLiteHandler(this);
        Cursor cursor = getData(dbHelper);
        addDataToTable(cursor);
        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction(dbHelper);
            }
        });

    }

    public Cursor getData(SqLiteHandler dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String row[] = {"date", "amount", "reason"};
        Cursor cursor = db.query("moneyManager", row, null, null, null, null, null);
        //cursor.moveToFirst();
        return cursor;
    }

    public void insertData(SqLiteHandler dbHelper, Transaction transaction){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date", transaction.getDate());
        values.put("amount", transaction.getAmount());
        values.put("reason", transaction.getReason());

        db.insert("moneyManager", null, values);
    }

    public void addTransaction(SqLiteHandler dbHelper){
        EditText dateET = findViewById(R.id.date);
        EditText amountET = findViewById(R.id.amount);
        EditText reasonET = findViewById(R.id.reason);

        Transaction transaction = new Transaction();
        transaction.setDate(dateET.getText().toString());
        transaction.setAmount(Float.parseFloat(amountET.getText().toString()));
        transaction.setReason(reasonET.getText().toString());

        insertData(dbHelper, transaction);

        addTransactionToTable(transaction);

    }

    public void addTransactionToTable(Transaction transaction){
        TableLayout tl = findViewById(R.id.transactionTable);
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.FILL_PARENT

        ));
        TextView date = new TextView(this);
        TextView amount = new TextView(this);
        TextView reason = new TextView(this);
        date.setText(transaction.getDate());
        amount.setText(Float.toString(transaction.getAmount()));
        reason.setText(transaction.getReason());
        row.addView(date);
        row.addView(amount);
        row.addView(reason);
        tl.addView(row, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        EditText balanceET = findViewById(R.id.balanceAmount);
        Float balanceAmt = Float.parseFloat(balanceET.getText().toString());
        balanceAmt += transaction.amount;
        balanceET.setText(balanceAmt.toString());
    }


    public void addDataToTable(Cursor cursor){
        Double balance = 0.0;
        TableLayout tl = findViewById(R.id.transactionTable);

        cursor.moveToFirst();
        do{

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.FILL_PARENT

            ));

            TextView date = new TextView(this);
            TextView amount = new TextView(this);
            TextView reason = new TextView(this);

            date.setText(cursor.getString(0));
            amount.setText(Float.toString(cursor.getFloat(1)));
            reason.setText(cursor.getString(2));

            row.addView(date);
            row.addView(amount);
            row.addView(reason);

            tl.addView(row, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            balance += cursor.getFloat(1);
        }while (cursor.moveToNext());
        EditText balanceET = findViewById(R.id.balanceAmount);
        balanceET.setText(balance.toString());
    }


}
