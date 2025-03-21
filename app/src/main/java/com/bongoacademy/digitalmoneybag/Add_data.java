package com.bongoacademy.digitalmoneybag;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add_data extends AppCompatActivity {

    EditText add_amount, add_reason;
    Button submit;
    TextView tvtitle;

    SqlHelperClass sqlHelperClass;

    public static boolean Expense = true;
    public static boolean asset = true;
    public static boolean debt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        add_amount = findViewById(R.id.add_amount);
        add_reason = findViewById(R.id.add_reason);
        submit = findViewById(R.id.submit);
        tvtitle = findViewById(R.id.tvtitle);

        sqlHelperClass = new SqlHelperClass(this);

        // Set initial title based on the Expense flag
        if (Expense == true && asset == false && debt == false) {
            tvtitle.setText("Add Expense");
        } else if (Expense==false && asset == false && debt == false){
            tvtitle.setText("Add Income");
        } else if (asset == true && debt == false){
            tvtitle.setText("Add Asset");
        } else if (asset == false && debt == true){
            tvtitle.setText("Add Debt");
        }

        // Listener to reset title when user focuses on EditText
        add_amount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Reset title when user focuses on EditText to add new data
                if (Expense == true && asset == false && debt == false) {
                    tvtitle.setText("Add Expense");
                } else if (Expense==false && asset == false && debt == false){
                    tvtitle.setText("Add Income");
                } else if (asset == true && debt == false){
                    tvtitle.setText("Add Asset");
                } else if (asset == false && debt == true){
                    tvtitle.setText("Add Debt");
                }
            }
        });

        add_reason.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Reset title when user focuses on EditText to add new data
                if (Expense == true && asset == false && debt == false) {
                    tvtitle.setText("Add Expense");
                } else if (Expense==false && asset == false && debt == false){
                    tvtitle.setText("Add Income");
                } else if (asset == true && debt == false){
                    tvtitle.setText("Add Asset");
                } else if (asset == false && debt == true){
                    tvtitle.setText("Add Debt");
                }
            }
        });

        submit.setOnClickListener(v -> {
            String samount = add_amount.getText().toString();
            String reason = add_reason.getText().toString();

            // Validate the amount
            if (TextUtils.isEmpty(samount)) {
                Toast.makeText(Add_data.this, "Amount is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            Double amount;

            try {
                amount = Double.parseDouble(samount);
                if (amount <= 0) {
                    throw new NumberFormatException("Amount must be greater than zero");
                }
            } catch (NumberFormatException e) {
                Toast.makeText(Add_data.this, "Please enter a valid amount!", Toast.LENGTH_SHORT).show();
                add_amount.setText("");
                return;
            }

            // Validate the reason
            if (TextUtils.isEmpty(reason)) {
                Toast.makeText(Add_data.this, "Reason is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform add operation
            if (Expense == true && asset == false && debt == false) {
                sqlHelperClass.add_expense(amount, reason);
                tvtitle.setText("Expense Added!!");
                add_amount.setText("");
                add_reason.setText("");
            } else if (Expense==false && asset == false && debt == false){
                sqlHelperClass.add_income(amount, reason);
                tvtitle.setText("Income Added!!");
                add_amount.setText("");
                add_reason.setText("");
            } else if (asset == true && debt == false){
                sqlHelperClass.add_assets(amount, reason);
                tvtitle.setText("Asset Added!!");
                add_amount.setText("");
                add_reason.setText("");
            } else if (asset == false && debt == true){
                sqlHelperClass.add_debt(amount, reason);
                tvtitle.setText("Debt Added!!");
                add_amount.setText("");
                add_reason.setText("");
            }

            // Reset title to "Add Expense" or "Add Income" after the submit operation
            new Handler().postDelayed(() -> {
                if (Expense == true && asset == false && debt == false) {
                    tvtitle.setText("Add Expense");
                } else if (Expense==false && asset == false && debt == false){
                    tvtitle.setText("Add Income");
                }else if (asset == true && debt == false){
                    tvtitle.setText("Add Asset");
                } else if (asset == false && debt == true){
                    tvtitle.setText("Add Debt");
                }
            }, 1500);  // Delay to keep the success message visible for a short time
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
