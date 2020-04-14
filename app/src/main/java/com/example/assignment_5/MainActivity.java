package com.example.assignment_5;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SqLiteHandler dbHelper = new SqLiteHandler(this);
        Cursor cursor = getData(dbHelper);
        addDataToTable(cursor);
        Button addBtn = findViewById(R.id.addBtn);
        Button filterBtn = findViewById(R.id.filterBtn);
        final EditText minDateFilter = findViewById(R.id.minDateFilter);
        final EditText minAmountFilter = findViewById((R.id.minAmountFilter));
        final EditText maxDateFilter = findViewById(R.id.maxDateFilter);
        final EditText maxAmountFilter = findViewById(R.id.maxAmountFilter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction(dbHelper);
            }
        });
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTransactions(minDateFilter.getText().toString(), minAmountFilter.getText().toString(),
                        maxDateFilter.getText().toString(), maxAmountFilter.getText().toString(), dbHelper);
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

    public void filterTransactions(String minDate_in, String minAmount_in, String maxDate_in, String maxAmount_in, SqLiteHandler dbHelper){
        if(!minDate_in.isEmpty() || !minAmount_in.isEmpty() || !maxDate_in.isEmpty() || !maxAmount_in.isEmpty()) {
            Float minAmount = -Float.MAX_VALUE;
            Float maxAmount = Float.MAX_VALUE;
            int minMonth = 0;
            int minDay = 0;
            int minYear = 0;
            int maxMonth = 13;
            int maxDay = 32;
            int maxYear = Integer.MAX_VALUE;
            if(!minDate_in.isEmpty()){
                minMonth = Integer.parseInt(minDate_in.substring(0,2));
                minDay = Integer.parseInt(minDate_in.substring(3,5));
                minYear = Integer.parseInt(minDate_in.substring(6));
            }
            if(!maxDate_in.isEmpty()){
                maxMonth = Integer.parseInt(maxDate_in.substring(0,2));
                maxDay = Integer.parseInt(maxDate_in.substring(3,5));
                maxYear = Integer.parseInt(maxDate_in.substring(6));
            }
            Cursor cursor = getData(dbHelper);
            cursor.moveToFirst();
            ViewGroup tl = (ViewGroup)findViewById(R.id.transactionTable);
            int i = tl.getChildCount();
            while(i > 1) {
                TableRow tblRow = (TableRow) tl.getChildAt(1);
                tl.removeView(tblRow);
                i--;
            }
            do {
                int month = Integer.parseInt(cursor.getString(0).substring(0,2));
                int day = Integer.parseInt(cursor.getString(0).substring(3,5));
                int year = Integer.parseInt(cursor.getString(0).substring(6));

                if(!minAmount_in.isEmpty()){
                     minAmount = Float.parseFloat(minAmount_in);
                }
                if(!maxAmount_in.isEmpty()){
                    maxAmount = Float.parseFloat(maxAmount_in);
                }
                Float amountHT = cursor.getFloat(1);
                boolean minMonthBool = (month >= minMonth && day >= minDay && year >= minYear);
                boolean maxMonthBool = (month <= maxMonth && day <= maxDay && year <= maxYear);
                boolean minAmountBool = (minAmount <= cursor.getFloat(1));
                boolean maxAmountBool = amountHT <= maxAmount;
                if((month >= minMonth && day >= minDay && year >= minYear)&& (month <= maxMonth && day <= maxDay && year <= maxYear) && (minAmount <= cursor.getFloat(1) && cursor.getFloat(1) <= maxAmount)){
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

                }

            } while (cursor.moveToNext());
        }
        else{
            ViewGroup tl = (ViewGroup)findViewById(R.id.transactionTable);
            int i = tl.getChildCount();
            while(i > 1) {
                TableRow tblRow = (TableRow) tl.getChildAt(1);
                tl.removeView(tblRow);
                i--;
            }
            Cursor cursor = getData(dbHelper);
            addDataToTable(cursor);
        }
    }


}
