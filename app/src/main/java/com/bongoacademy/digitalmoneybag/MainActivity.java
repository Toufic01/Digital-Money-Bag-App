package com.bongoacademy.digitalmoneybag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView add_expenses, show_all_expenses, show_expenses, add_income, show_all_income, show_income,show_total_balance,
            add_assets, show_all_assets, show_assets, add_debt, show_all_debt, show_debt, show_total_balance2;

    LinearLayout layCircle, layCircle2;
    SqlHelperClass sqlHelperClass;

    private static final int REQUEST_STORAGE_PERMISSION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for using same theme for light and dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });


        add_expenses = findViewById(R.id.add_expense);
        show_expenses = findViewById(R.id.show_expenses);
        show_all_expenses = findViewById(R.id.show_all_expenses);
        add_income = findViewById(R.id.add_income);
        show_all_income = findViewById(R.id.show_all_income);
        show_income = findViewById(R.id.show_income);
        show_total_balance = findViewById(R.id.show_total_balance);
        add_assets = findViewById(R.id.add_assets);
        show_all_assets = findViewById(R.id.show_all_assets);
        show_assets = findViewById(R.id.show_assets);
        add_debt = findViewById(R.id.add_debt);
        show_all_debt = findViewById(R.id.show_all_debt);
        show_debt = findViewById(R.id.show_debt);
        show_total_balance2 = findViewById(R.id.show_total_balance2);
        layCircle = findViewById(R.id.layCircle);
        layCircle2 = findViewById(R.id.layCircle2);



        sqlHelperClass = new SqlHelperClass(this);



        add_assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Add_data.asset = true;
                Add_data.debt = false;

                startActivity(new Intent(getApplicationContext(), Add_data.class));
            }
        });


        add_debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Add_data.asset = false;
               Add_data.debt = true;

                startActivity(new Intent(getApplicationContext(), Add_data.class));

            }
        });


        add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Add_data.Expense = false;
               Add_data.asset = false;
               Add_data.debt = false;

                startActivity(new Intent(getApplicationContext(), Add_data.class));
            }
        });



        add_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Add_data.Expense = true;
                Add_data.asset = false;
                Add_data.debt = false;

                startActivity(new Intent(getApplicationContext(), Add_data.class));

            }
        });




        show_all_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Show_data.all_expense = true;
                Show_data.all_income = false;
                Show_data.all_asset = false;
                Show_data.all_debt = false;

                startActivity(new Intent(getApplicationContext(), Show_data.class));

            }
        });

        show_all_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Show_data.all_expense = false;
                Show_data.all_income = true;
                Show_data.all_asset = false;
                Show_data.all_debt = false;

                startActivity(new Intent(getApplicationContext(), Show_data.class));

            }
        });

        show_all_assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Show_data.all_expense = false;
                Show_data.all_income = false;
                Show_data.all_asset = true;
                Show_data.all_debt = false;

                startActivity(new Intent(getApplicationContext(), Show_data.class));

            }
        });

        show_all_debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Show_data.all_expense = false;
                Show_data.all_income = false;
                Show_data.all_asset = false;
                Show_data.all_debt = true;

                startActivity(new Intent(getApplicationContext(), Show_data.class));

            }
        });

        updateUI();
    }


    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with saving the file
                downloadBalanceSheet();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied to write to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
        // Check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            downloadBalanceSheet();
        }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadBalanceSheet() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        Double income = sqlHelperClass.show_income();
        Double expense = sqlHelperClass.show_expense();
        Double assets = sqlHelperClass.show_asset();
        Double debt = sqlHelperClass.show_debt();

        Double total_balance = income - expense;
        Double total_assets = assets - debt;

        StringBuilder balanceSheet = new StringBuilder();
        balanceSheet.append("Balance Sheet\n");
        balanceSheet.append("Date: ").append(currentDateTime).append("\n\n");

        balanceSheet.append("| Income                    | Amount |\n");
        balanceSheet.append("|---------------------------|--------|\n");

        // show all individual income details
        Cursor incomeCursor = sqlHelperClass.show_all_data_income();
        if (incomeCursor !=null && incomeCursor.getCount() > 0) {

            while (incomeCursor.moveToNext()){

                String reason = incomeCursor.getString(2); // Column index for Reason
                double amount = incomeCursor.getDouble(1); // Column index for Amount
                balanceSheet.append("| ").append(reason).append("                    | ").append(amount).append(" |\n");
            }

        }
        balanceSheet.append("|---------------------------|--------|\n");

        balanceSheet.append("| Total Income                    | ").append(income).append(" |\n");

        balanceSheet.append("|---------------------------|--------|\n");

        balanceSheet.append("| Expenses                  |        |\n");

        // show all individual expense details
        Cursor expenseCursor = sqlHelperClass.show_all_data_expense();
        if (expenseCursor !=null && expenseCursor.getCount() > 0) {

            while (expenseCursor.moveToNext()){

                String reason = expenseCursor.getString(2); // Column index for Reason
                double amount = expenseCursor.getDouble(1); // Column index for Amount
                balanceSheet.append("| ").append(reason).append("                    | ").append(amount).append(" |\n");
            }

        }
        balanceSheet.append("|---------------------------|--------|\n");

        balanceSheet.append("| Total Expenses            | ").append(expense).append(" |\n");
        balanceSheet.append("|---------------------------|--------|\n");

        balanceSheet.append("| Net Income                | ").append(total_balance).append(" |\n\n");
        balanceSheet.append("|---------------------------|--------|\n");
        balanceSheet.append("|---------------------------|--------|\n");




        balanceSheet.append("| Assets                    |\n");
        balanceSheet.append("|---------------------------|--------|\n");

        // show all individual asset details
        Cursor assetCursor = sqlHelperClass.show_all_data_asset();
        if (assetCursor !=null && assetCursor.getCount() > 0) {

            while (assetCursor.moveToNext()){

                String reason = assetCursor.getString(2); // Column index for Reason
                double amount = assetCursor.getDouble(1); // Column index for Amount
                balanceSheet.append("| ").append(reason).append("                           | ").append(amount).append(" |\n");
            }

            }
        balanceSheet.append("|---------------------------|--------|\n");
        balanceSheet.append("| Total Assets              | ").append(assets).append(" |\n\n");

        balanceSheet.append("|---------------------------|--------|\n");
        balanceSheet.append("|---------------------------|--------|\n");



        balanceSheet.append("| Liabilities               |\n");
        balanceSheet.append("|---------------------------|--------|\n");

        // show all individual debt details
        Cursor debtCursor = sqlHelperClass.show_all_data_debt();
        if (debtCursor !=null && debtCursor.getCount() > 0) {

            while (debtCursor.moveToNext()){

                String reason = debtCursor.getString(2); // Column index for Reason
                double amount = debtCursor.getDouble(1); // Column index for Amount
                balanceSheet.append("| ").append(reason).append("                           | ").append(amount).append(" |\n");
            }

        }
        balanceSheet.append("|---------------------------|--------|\n");

        balanceSheet.append("| Total Current Liabilities | ").append(debt).append(" |\n\n");

        balanceSheet.append("|---------------------------|--------|\n");
        balanceSheet.append("|---------------------------|--------|\n");
        balanceSheet.append("| Net Assets                | ").append(total_assets).append(" |\n\n");





        // Check permission for Android 10 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "BalanceSheet_" + currentDateTime + ".csv");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/BalanceSheets");

            // Insert the content
            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);

            try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                if (outputStream != null) {
                    outputStream.write(balanceSheet.toString().getBytes());
                    Toast.makeText(this, "Balance sheet saved to " + uri.getPath(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Failed to save balance sheet", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save balance sheet", Toast.LENGTH_SHORT).show();
            }
        } else {
            // For Android 9 and below, you can use the previous method.
            File documentsDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BalanceSheets");
            if (!documentsDir.exists()) {
                documentsDir.mkdirs();
            }

            String fileName = "BalanceSheet_" + currentDateTime + ".csv";
            File file = new File(documentsDir, fileName);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(balanceSheet.toString());
                Toast.makeText(this, "Balance sheet saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save balance sheet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateUI() {

        DecimalFormat formatter = new DecimalFormat("#,###");

        // Calculate totals
        Double income = sqlHelperClass.show_income();
        Double expense = sqlHelperClass.show_expense();
        Double assets = sqlHelperClass.show_asset();
        Double debt = sqlHelperClass.show_debt();

        Double total_balance = income - expense;
        Double total_assets = assets - debt;

        // Update UI with values
        show_expenses.setText("BDT " + formatter.format(expense));
        show_income.setText("BDT " + formatter.format(income));
        show_total_balance.setText("BDT " + formatter.format(total_balance));

        show_assets.setText("BDT " + formatter.format(assets));
        show_debt.setText("BDT " + formatter.format(debt));
        show_total_balance2.setText("BDT " + formatter.format(total_assets));

        // Change circle color based on total_balance
        if (total_balance < 0) {
            layCircle.setBackgroundResource(R.drawable.circle_negative); // Red circle
        } else if (total_balance > 0) {
            layCircle.setBackgroundResource(R.drawable.circle_positive); // Green circle
        } else {
            layCircle.setBackgroundResource(R.drawable.circle_colored); // Default circle (optional)
        }

        // Change circle color based on total_assets
        if (total_assets < 0) {
            layCircle2.setBackgroundResource(R.drawable.circle_negative); // Red circle
        } else if (total_assets > 0) {
            layCircle2.setBackgroundResource(R.drawable.circle_positive); // Green circle
        } else {
            layCircle2.setBackgroundResource(R.drawable.circle_colored); // Default circle (optional)
        }

        // Force UI refresh
        layCircle.invalidate();
        layCircle.requestLayout();
        layCircle2.invalidate();
        layCircle2.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }


    //===================================================================
}