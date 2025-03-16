package com.bongoacademy.digitalmoneybag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView add_expenses, show_all_expenses, show_expenses, add_income, show_all_income, show_income,show_total_balance;
    SqlHelperClass sqlHelperClass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        add_expenses = findViewById(R.id.add_expense);
        show_expenses = findViewById(R.id.show_expenses);
        show_all_expenses = findViewById(R.id.show_all_expenses);
        add_income = findViewById(R.id.add_income);
        show_all_income = findViewById(R.id.show_all_income);
        show_income = findViewById(R.id.show_income);
        show_total_balance = findViewById(R.id.show_total_balance);


        sqlHelperClass = new SqlHelperClass(this);



        add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Add_data.Expense = false;

                startActivity(new Intent(getApplicationContext(), Add_data.class));
            }
        });



        add_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Add_data.Expense = true;

                startActivity(new Intent(getApplicationContext(), Add_data.class));

            }
        });


        show_all_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Show_data.all_expense = true;

                startActivity(new Intent(getApplicationContext(), Show_data.class));

            }
        });

        show_all_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Show_data.all_expense = false;

                startActivity(new Intent(getApplicationContext(), Show_data.class));

            }
        });



        updateUI();
    }


    public void updateUI (){

        Double income = sqlHelperClass.show_income();
        Double expense = sqlHelperClass.show_expense();

        Double total_balance = income - expense;

        show_expenses.setText("BDT "+expense);
        show_income.setText("BDT "+income);
        show_total_balance.setText("BDT "+total_balance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }


    //===================================================================
}