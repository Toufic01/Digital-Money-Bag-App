package com.bongoacademy.digitalmoneybag;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Show_data extends AppCompatActivity {

    TextView tvtitle;
    ListView listView;

    SqlHelperClass sqlHelperClass;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    HashMap<String, String> hashMap;

    public static boolean all_expense = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        tvtitle = findViewById(R.id.tvtitle1);
        listView = findViewById(R.id.listview);
        sqlHelperClass = new SqlHelperClass(this);

        tvtitle.setText("");

        loadData();
    }

    private void loadData() {
        arrayList.clear();

        if (all_expense == true) {

            tvtitle.setText("Expense Details");

            Cursor cursor = sqlHelperClass.show_all_data_expense();

            if (cursor != null && cursor.getCount() > 0) {
                arrayList = new ArrayList<>(); // Initialize only once

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    double amount = cursor.getDouble(1);
                    String reason = cursor.getString(2);
                    String time = cursor.getString(3);

                    hashMap = new HashMap<>();
                    hashMap.put("id", "" + id);
                    hashMap.put("Amount", "" + amount);
                    hashMap.put("Reason", "" + reason);
                    hashMap.put("Time", "" + time);

                    arrayList.add(hashMap);  // Add to the list
                }

                listView.setAdapter(new MyAdapter());
            } else {
                tvtitle.setText("\nNO data found!!");
            }

        } else {
            tvtitle.setText("Income Details");
            Cursor cursor = sqlHelperClass.show_all_data_income();

            if (cursor != null && cursor.getCount() > 0) {
                arrayList = new ArrayList<>(); // Initialize only once

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    double amount = cursor.getDouble(1);
                    String reason = cursor.getString(2);
                    String time = cursor.getString(3);

                    hashMap = new HashMap<>();
                    hashMap.put("id", "" + id);
                    hashMap.put("Amount", "" + amount);
                    hashMap.put("Reason", "" + reason);
                    hashMap.put("Time", "" + time);

                    arrayList.add(hashMap);  // Add to the list
                }

                MyAdapter1 adapter = new MyAdapter1();
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged(); // Added here
            } else {
                tvtitle.setText("\nNO data found!!");
            }

        }
    }

    public class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            View myview = inflater.inflate(R.layout.item, parent,false);

            TextView tvReason = myview.findViewById(R.id.tv_reason);
            TextView tvAmount = myview.findViewById(R.id.tv_amount);
            TextView tvDelete = myview.findViewById(R.id.delete_item);
            TextView tvtime = myview.findViewById(R.id.time_expenses);


            hashMap = arrayList.get(i);

            String id = hashMap.get("id");
            String amount = hashMap.get("Amount");
            String reason = hashMap.get("Reason");
            String time = hashMap.get("Time");

            tvReason.setText(reason);
            tvAmount.setText(amount);
            tvtime.setText(time);


            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqlHelperClass.delete_expense(id);
                    loadData();
                    notifyDataSetChanged();
                }
            });



            return myview;


        }
    }


    public class MyAdapter1 extends BaseAdapter{


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            View myview = inflater.inflate(R.layout.item_income, parent,false);

            TextView tvReason = myview.findViewById(R.id.reason_of_income);
            TextView tvAmount = myview.findViewById(R.id.tv_income);
            TextView tvDelete = myview.findViewById(R.id.delete_income);
            TextView tvtime = myview.findViewById(R.id.time_income);


            hashMap = arrayList.get(i);

            String id = hashMap.get("id");
            String amount = hashMap.get("Amount");
            String reason = hashMap.get("Reason");
            String time = hashMap.get("Time");

            tvReason.setText(reason);
            tvAmount.setText(amount);
            tvtime.setText(time);


            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqlHelperClass.delete_Income(id);
                    loadData();
                    notifyDataSetChanged();
                }
            });

            return myview;
        }
    }

}