package com.example.version;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DataPassing extends AppCompatActivity {

    EditText etName;
    EditText etAddress;
    Button btnSubmit;
    DatePicker dp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_passing);

        etName = findViewById(R.id.etxtName);
        etAddress = findViewById(R.id.etxtAddress);
        btnSubmit = findViewById(R.id.btnSubmit);
        dp = findViewById(R.id.dp);

        btnSubmit.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            int year = dp.getYear();
            int currentYear = 2025;
            int age = currentYear - year;

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences("MyData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", name);
            editor.putString("address", address);
            editor.putInt("Age", age);
            editor.apply();

            Intent intent = new Intent(DataPassing.this, DataResult.class);
            startActivity(intent);
        });
    }
}
